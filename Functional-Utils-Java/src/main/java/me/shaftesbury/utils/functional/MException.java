package me.shaftesbury.utils.functional;


import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by Bob on 09/12/13.
 */
public class MException<U>
{
    private Func0<U> fn;
    private U state = null;
    private RuntimeException exception = null;
    private StackTraceElement[] stacktrace = null;

    // this is 'return'
    public static <B>MException<B> toMException(final Func0<B> f)
    {
        return new MException<B>(f);
    }

    public final <B>MException<B> bind(final Func<U,MException<B>> f)
    {
        if(f==null) throw new IllegalArgumentException("f");
        if(!hasException())
            try {
                return f.apply(state);
            } catch(final RuntimeException ex) {
                return new MException<B>(ex, new Throwable().getStackTrace());
            }
        else
            return new MException<B>(exception, stacktrace);
    }

    private static <C>MException<C> toMException(final Pair<RuntimeException,StackTraceElement[]> exc)
    {
        return new MException(exc.getLeft(),exc.getRight());
    }

    public static <A,B,C>MException<C> lift(final Func2<A,B,C> f, final MException<A> a, final MException<B> b)
    {
        if(f==null) throw new IllegalArgumentException("f");
        if(a==null) throw new IllegalArgumentException("a");
        if(b==null) throw new IllegalArgumentException("b");

        if(a.hasException()) return toMException(a.getExceptionWithStackTrace());
        if(b.hasException()) return toMException(b.getExceptionWithStackTrace());
        return new MException(BinaryFunction.delay(f, a.read(), b.read()));
    }

    private MException(final Func0<U> f)
    {
        if(f==null) throw new IllegalArgumentException("f");
        fn = f;
    }

    private MException(final RuntimeException ex, final StackTraceElement[] stack)
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
            } catch(final RuntimeException ex) {
                exception = ex;
                stacktrace = new Throwable().getStackTrace();
            }
        }
        return exception!=null;
    }

    public RuntimeException getException()
    {
        return exception; // what happens if exception==null?
    }

    public Pair<RuntimeException,StackTraceElement[]> getExceptionWithStackTrace()
    {
        return Pair.of(exception, stacktrace); // what happens if exception==null?
    }

    public final U read()
    {
        if(!hasException()) return state;
        throw exception;
    }
}
