package me.shaftesbury.utils.functional;

/**
 * Created with IntelliJ IDEA.
 * User: Bob
 * Date: 26/11/13
 * Time: 20:57
 * To change this template use File | Settings | File Templates.
 */
public final class LispList
{
    public static interface List<T>
    {
        T head();
        List<T> tail();

        boolean isEmpty();
    }

    private static final <T>List<T> reverse(final List<T> input, final List<T> accumulator)
    {
        return input.isEmpty() ? accumulator : reverse(input.tail(),list(input.head(),accumulator));
    }

    public static final <T>List<T> reverse(final List<T> input)
    {
        return reverse(input,(List<T>)nil());
    }

    private static final <T>List<T> filter(final Func<T,Boolean> f, final List<T> input, final List<T> accumulator)
    {
        return input.isEmpty()
                ? accumulator
                : filter(f,input.tail(),f.apply(input.head())?list(input.head(), accumulator):accumulator);
    }

    public static final <T>List<T> filter(final Func<T,Boolean> f, final List<T> input)
    {
        return reverse(filter(f, input, (List<T>) nil()));
    }

    public static final <T,R>List<R> map(final Func<T,R> f, final List<T> input)
    {
        return input.isEmpty()
               ? (List<R>)nil()
               : list(f.apply(input.head()),map(f,input.tail()));
    }

    public static final <T,R>R fold(final Func2<R,T,R> f, final R initialValue, final List<T> input)
    {
        return input.isEmpty()
                ? initialValue
                : fold(f,f.apply(initialValue,input.head()),input.tail());
    }
    public static final <T,R>R foldRight(final Func2<T,R,R> f, final R initialValue, final List<T> input)
    {
        return input.isEmpty()
                ? initialValue
                : f.apply(input.head(),foldRight(f,initialValue,input.tail()));
    }

    public static final <T>List<T> cons(final T t, final List<T> l) { return list(t,l); }
    public static final <T>T car(final List<T> l) { return l.head(); }
    public static final <T>List<T> cdr(final List<T> l) { return l.tail(); }
    public static final <T>T cadr(final List<T> l) { return car(cdr(l)); }
    public static final <T>List<T> compose(final T t1, final T t2) { return list(t1, list(t2, (List<T>)nil())); }

    public static final class EmptyListHasNoHead extends RuntimeException {}
    public static final class EmptyListHasNoTail extends RuntimeException {}

    private static final LispList ll = new LispList();

    public final class NonEmptyList<T> implements List<T>
    {
        final private T _head;
        final private List<T> _tail;

        NonEmptyList(final T head, final List<T> tail)
        {
            _head = head;
            _tail = tail;
        }

        @Override
        public T head() {
            return _head;
        }

        @Override
        public List<T> tail() {
            return _tail;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        public boolean equals(Object o)
        {
            if(o==null) return false;
            if(o instanceof NonEmptyList<?>)
                return ((NonEmptyList<?>)o).head().equals(head()) && ((NonEmptyList<?>)o).tail().equals(tail());
            else return false;
        }

        public String toString()
        {
            return "( "+head()+", "+tail().toString()+" )";
        }
    }

    public static final <T>List<T> nil()
    {
        return new List<T>(){
            @Override
            public T head() {
                throw new EmptyListHasNoHead();
            }

            @Override
            public List<T> tail() {
                throw new EmptyListHasNoTail();
            }

            @Override
            public boolean isEmpty() {
                return true;
            }

            public boolean equals(Object o)
            {
                if(o==null) return false;
                return o instanceof List<?> && ((List<?>)o).isEmpty();
            }

            public String toString() { return "( )";}
        };
    }

    public static final <T>List<T> list(final T head, final List<T> tail)
    {
        return ll.new NonEmptyList<T>(head,tail);
    }
}
