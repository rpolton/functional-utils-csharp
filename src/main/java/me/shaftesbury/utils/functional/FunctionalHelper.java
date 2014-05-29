package me.shaftesbury.utils.functional;

/**
 * Created by Bob on 16/05/14.
 *
 * Common use patterns written here so that we don't have to wrestle continuously with the clunkiness and waffle that is the Java syntax.
 */
public final class FunctionalHelper
{
    public static <T>Iterable2<Option<T>> areSome(final Iterable<Option<T>> input)
    {
        return IterableHelper.create(input).filter(new Func<Option<T>, Boolean>() {
            @Override
            public Boolean apply(Option<T> tOption) {
                return tOption.isSome();
            }
        });
    }
    public static <T>Iterable2<Option<T>> areNone(final Iterable<Option<T>> input)
    {
        return IterableHelper.create(input).filter(new Func<Option<T>, Boolean>() {
            @Override
            public Boolean apply(Option<T> tOption) {
                return tOption.isNone();
            }
        });
    }
    public static <T>boolean allSome(final Iterable<Option<T>> input)
    {
        return !IterableHelper.create(input).exists(new Func<Option<T>, Boolean>() {
            @Override
            public Boolean apply(Option<T> tOption) {
                return tOption.isNone();
            }
        });
    }
    public static <T>boolean allNone(final Iterable<Option<T>> input)
    {
        return !IterableHelper.create(input).exists(new Func<Option<T>, Boolean>() {
            @Override
            public Boolean apply(Option<T> tOption) {
                return tOption.isSome();
            }
        });
    }

    private FunctionalHelper(){}
}
