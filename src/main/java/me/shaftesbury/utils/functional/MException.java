package me.shaftesbury.utils.functional;

import org.javatuples.Pair;

/**
 * Created by Bob on 09/12/13.
 */
public class MException<U>
{
    private Func0<U> fn;
    private U state = null;
    private Exception exception = null;
    private StackTraceElement[] stacktrace = null;

    // this is 'return'
    public static final <B>MException<B> toMException(final Func0<B> f)
    {
        return new MException<B>(f);
    }

    public final <B>MException<B> bind(final Func<U,MException<B>> f)
    {
        if(!hasException())
            try {
                return f.apply(state);
            } catch(Exception ex) {
                return new MException<B>(ex, new Throwable().getStackTrace());
            }
        else
            return new MException<B>(exception, stacktrace);
    }

    private MException(final Func0<U> f)
    {
        fn = f;
    }

    private MException(final Exception ex, final StackTraceElement[] stack)
    {
        exception = ex;
        stacktrace = stack;
    }

    public boolean hasException()
    {
        if(exception!=null) return true;
        if(state==null)
        {
            try {
                state = fn.apply();
            } catch(Exception ex) {
                exception = ex;
                stacktrace = new Throwable().getStackTrace();
            }
        }
        return exception!=null;
    }

    public Exception getException()
    {
        return exception; // what happens if exception==null?
    }

    public Pair<Exception,StackTraceElement[]> getExceptionWithStackTrace()
    {
        return Pair.with(exception, stacktrace); // what happens if exception==null?
    }

    public final U read() throws Exception
    {
        if(!hasException()) return state;
        throw exception;
    }
}
