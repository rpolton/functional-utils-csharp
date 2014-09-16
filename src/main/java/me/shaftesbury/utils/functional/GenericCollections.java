package me.shaftesbury.utils.functional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Bob on 10/12/13.
 */
public final class GenericCollections
{
    public interface Generator<A>
    {
        Collection<A> initialiseEmptyContainer();
        Collection<A> createUnmodifiableContainer(Collection<A> c);
    }

    public static final <A>Generator<A> createArrayListGenerator() { return new GenericCollections().new ArrayListGenerator<A>(); }

    public class ArrayListGenerator<A> implements Generator<A>
    {
        @Override
        public Collection<A> initialiseEmptyContainer() {
            return new ArrayList<A>();
        }

        @Override
        public Collection<A> createUnmodifiableContainer(Collection<A> c) {
            return java.util.Collections.unmodifiableList(Functional.toList(c));
        }
    }

    public static final <A>Generator<A> createHashSetGenerator() { return new GenericCollections().new HashSetGenerator<A>(); }

    public class HashSetGenerator<A> implements Generator<A>
    {
        @Override
        public Collection<A> initialiseEmptyContainer() {
            return new HashSet<A>();
        }

        @Override
        public Collection<A> createUnmodifiableContainer(Collection<A> c) {
            return java.util.Collections.unmodifiableSet(Functional.toSet(c));
        }
    }

    public final static <A>Iterable<A> filter(final Func<A,Boolean> pred, final Generator<A> generator, final Iterable<A> input)
    {
        final Collection<A> output = generator.initialiseEmptyContainer();
        for(final A element : input)
            if(pred.apply(element))
                output.add(element);

        return generator.createUnmodifiableContainer(output);
    }
}
