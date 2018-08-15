package me.shaftesbury.utils.functional;

import io.vavr.control.Either;
import org.junit.Test;

import java.util.OptionalInt;
import java.util.function.Function;
import java.util.function.IntConsumer;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class AnyMTest {

    private final Function<String, MyEither<Exception, Integer>> intValue = s -> {
        try {
            return MyEither.right(Integer.valueOf(s));
        } catch (final NumberFormatException e) {
            return MyEither.left(e);
        }
    };

    private final Function<Integer, OptionalInt> add20IfLessThan20 = i -> i == null || i > 20 ? OptionalInt.empty() : OptionalInt.of(i + 20);

    @Test
    public void add20ToSmaller() {
        final OptionalIntT<Either<Exception, Integer>> result = intValue.apply("10").flatMapT(add20IfLessThan20::apply);

        assertTrue(result.isPresent());
        assertEquals(30, result.getAsInt());
    }

    @Test
    public void add20ToLarger() {
        final OptionalIntT<Either<Exception, Integer>> result = intValue.apply("100").flatMapT(add20IfLessThan20::apply);

        result.ifPresent(i -> fail("Expected empty but received " + i));
    }

    @Test
    public void dontAdd() {
        final OptionalIntT<Either<Exception, Integer>> result = intValue.apply("string").flatMapT(add20IfLessThan20::apply);

        assertFalse(result.isPresent());
        assertTrue(result.liftM().isLeft());
        assertTrue(result.liftM().left().get() instanceof NumberFormatException);
    }
}

class OptionalIntT<M /*extends Monad*/> {
    private final OptionalInt optionalInt;
    private final M underlyingMonad;

    OptionalIntT(final OptionalInt op, final M underlyingMonad) {
        this.optionalInt = requireNonNull(op, "op must not be null");
        this.underlyingMonad = requireNonNull(underlyingMonad, "underlyingMonad must not be null");
    }

    boolean isPresent() {
        return optionalInt.isPresent();
    }

    int getAsInt() {
        return optionalInt.getAsInt();
    }

    void ifPresent(final IntConsumer consumer) {
        optionalInt.ifPresent(consumer);
    }

    M liftM() {
        return underlyingMonad;
    }
}

class MyEither<L, R> {
    private final Either<L, R> either;

    private MyEither(final Either<L, R> either) {
        this.either = either;
    }

    static <L, R> MyEither<L, R> left(final L l) {
        return new MyEither<>(Either.left(l));
    }

    static <L, R> MyEither<L, R> right(final R r) {
        return new MyEither<>(Either.right(r));
    }

    <X> MyEither<L, X> flatMap(final Function<? super R, Either<L, X>> f) {
        final Either<L, X> lxEither = either.flatMap(f);
        return new MyEither<>(lxEither);
    }

    OptionalIntT<Either<L, R>> flatMapT(final Function<? super R, OptionalInt> f) {
        if (either.isLeft()) {
            return new OptionalIntT<>(OptionalInt.empty(), either);
        } else {
            final OptionalInt result = f.apply(either.right().get());
            return new OptionalIntT<>(result, either);
        }
    }

}

