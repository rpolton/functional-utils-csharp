package me.shaftesbury.utils.functional;

import java.util.List;

/**
 * Created by Bob on 16/05/14.
 *
 * Common use patterns written here so that we don't have to wrestle continuously with the clunkiness and waffle that is the Java syntax.
 */
public final class FunctionalHelper
{
    /**
     * Given an input sequence of {@link me.shaftesbury.utils.functional.Option} types return a new lazily-evaluated sequence of those which are {@link me.shaftesbury.utils.functional.Option#Some()}
     * @param input the input sequence
     * @param <T> the type of the element underlying the {@link me.shaftesbury.utils.functional.Option}
     * @return a sequence of {@link me.shaftesbury.utils.functional.Option} which are {@link me.shaftesbury.utils.functional.Option#Some()}
     */
    public static <T>Iterable2<Option<T>> areSome(final Iterable<Option<T>> input)
    {
        return IterableHelper.create(input).filter(new Func<Option<T>, Boolean>() {
            @Override
            public Boolean apply(Option<T> tOption) {
                return tOption.isSome();
            }
        });
    }
    /**
     * Given an input sequence of {@link me.shaftesbury.utils.functional.Option} types return a new lazily-evaluated sequence of those which are {@link me.shaftesbury.utils.functional.Option#None()}
     * @param input the input sequence
     * @param <T> the type of the element underlying the {@link me.shaftesbury.utils.functional.Option}
     * @return a sequence of {@link me.shaftesbury.utils.functional.Option} which are {@link me.shaftesbury.utils.functional.Option#None()}
     */
    public static <T>Iterable2<Option<T>> areNone(final Iterable<Option<T>> input)
    {
        return IterableHelper.create(input).filter(new Func<Option<T>, Boolean>() {
            @Override
            public Boolean apply(Option<T> tOption) {
                return tOption.isNone();
            }
        });
    }
    /**
     * Given an input sequence of {@link me.shaftesbury.utils.functional.Option} types return true if all the elements are {@link me.shaftesbury.utils.functional.Option#Some()}
     * @param input the input sequence
     * @param <T> the type of the element underlying the {@link me.shaftesbury.utils.functional.Option}
     * @return true if all the elements are {@link me.shaftesbury.utils.functional.Option#Some()}, false otherwise
     */
    public static <T>boolean allSome(final Iterable<Option<T>> input)
    {
        return !IterableHelper.create(input).exists(new Func<Option<T>, Boolean>() {
            @Override
            public Boolean apply(Option<T> tOption) {
                return tOption.isNone();
            }
        });
    }
    /**
     * Given an input sequence of {@link me.shaftesbury.utils.functional.Option} types return true if all the elements are {@link me.shaftesbury.utils.functional.Option#None()}
     * @param input the input sequence
     * @param <T> the type of the element underlying the {@link me.shaftesbury.utils.functional.Option}
     * @return true if all the elements are {@link me.shaftesbury.utils.functional.Option#None()}}, false otherwise
     */
    public static <T>boolean allNone(final Iterable<Option<T>> input)
    {
        return !IterableHelper.create(input).exists(new Func<Option<T>, Boolean>() {
            @Override
            public Boolean apply(Option<T> tOption) {
                return tOption.isSome();
            }
        });
    }

    /**
     * Given an input sequence of {@link me.shaftesbury.utils.functional.Option} return a lazily-evaluated sequence containing
     * the underlying data elements. Note that the proper way to perform this action is to use
     * {@link me.shaftesbury.utils.functional.Option#bind(Func)}
     * @param input the input sequence
     * @param <T> the type of the underlying data
     * @return a lazily-evaluated sequence containing the underlying data
     * @throws me.shaftesbury.utils.functional.OptionNoValueAccessException if {@link Option#isNone()} is true for any element in <tt>input</tt>
     */
    public static <T>Iterable2<T> some(final Iterable<Option<T>> input)
    {
        return IterableHelper.create(input).map(new Func<Option<T>, T>() {
            @Override
            public T apply(Option<T> tOption) {
                return tOption.Some();
            }
        });
    }

    /**
     * The Range class holds an inclusive lower bound and an exclusive upper bound. That is lower <= pos < upper
     */
    public static class Range
    {
        private final int lowerBound;
        private final int upperExBound;

        /**
         * Create a new Range object
         * @param lower the inclusive lower bound of the Range
         * @param upperEx the exclusive upper bound of the Range
         */
        public Range(final int lower, final int upperEx)
        {
            this.lowerBound=lower;
            this.upperExBound=upperEx;
        }

        /**
         * Return the inclusive lower bound
         * @return the inclusive lower bound
         */
        public int from(){return lowerBound;}

        /**
         * return the exclusive upper bound
         * @return the exclusive upper bound
         */
        public int to(){return upperExBound;}
    }

    /**
     * This list generator returns a list of Range objects which split the interval [1-'howManyElements') into 'howManyPartitions' Range objects.
     * If the interval cannot be divided exactly then the remainder is allocated evenly across the first
     * 'howManyElements' % 'howManyPartitions' Range objects.
     * @param howManyElements defines the exclusive upper bound of the interval to be split
     * @param howManyPartitions defines the number of Range objects to generate to cover the interval
     * @return a list of Range objects
     */
    public static List<Range> partition(final int howManyElements, final int howManyPartitions)
    {
        final int size = howManyElements/howManyPartitions;
        final int remainder = howManyElements % howManyPartitions;

        assert size*howManyPartitions + remainder == howManyElements;

        return Functional.init(new Func<Integer, Range>() {
            @Override
            public Range apply(final Integer integer) {
                return new Range((integer-1)*size+(integer<=remainder+1?integer-1:remainder),
                        integer*size+(integer<=remainder?integer:remainder));
            }
        }, howManyPartitions);
    }

    private FunctionalHelper(){}
}
