package me.shaftesbury.utils.functional;

/**
 * Created by Bob on 20/12/13.
 */

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static me.shaftesbury.utils.functional.Functional.*;
import static me.shaftesbury.utils.functional.MException.*;
import static me.shaftesbury.utils.functional.UnaryFunction.*;
import static me.shaftesbury.utils.functional.Option.*;

public final class ImperativeAndFunctionalEquivalenceExamples
{
    public static final class CalculateSumOfFirstNPositiveIntegers
    {
        private static final int imperative(final int n)
        {
            int sum = 0;
            for(int i=1;i<=n;++i)
                sum+=i;
            return sum;
        }

        private static final int functional(final int n)
        {
            return fold(sum, 0, init(Functional.<Integer>identity(), n));
        }

        @Test
        public void test()
        {
            Assert.assertEquals(imperative(10),functional(10));
        }
    }

    public static final class CalculateSumOfFirstNOddPositiveIntegers
    {
        private static final int imperative(final int n)
        {
            int sum = 0;
            for(int i=0;i<n;++i)
            {
                final int oddElem = 2*i + 1;
                sum += oddElem;
            }
            return sum;
        }

        private static final int functional(final int n)
        {
            Func<Integer,Integer> oddGenerator = new Func<Integer, Integer>() {
                @Override
                public Integer apply(Integer integer) {
                    return 2*(integer-1) + 1;            // because init starts counting from 1
                }
            };
            return fold(sum, 0, init(oddGenerator, n));
        }

        @Test
        public void test()
        {
            Assert.assertEquals(imperative(10),functional(10));
        }
    }

    public static final class GivenASeqOfIntsFindThoseDivisibleBy3
    {
        private static final Iterable<Integer> imperative(final Iterable<Integer> ys)
        {
            List<Integer> result = new ArrayList<Integer>();
            for(Integer y : ys)
            {
                if(y%3==0)
                  result.add(y);
            }
            return result;
        }

        public static final Func<Integer,Boolean> isDivisibleBy(final int denominator)
        {
            return new Func<Integer,Boolean>(){
                public Boolean apply(final Integer numerator) {
                    return numerator % denominator == 0;
                }
            };
        }

        private static final Iterable<Integer> functional(final Iterable<Integer> ys)
        {
            return filter(isDivisibleBy(3),ys);
        }

        @Test
        public void test()
        {
            List<Integer> ys = Arrays.asList(1,4,5,6,8,9,12,14,15,18,21,22);
            Assert.assertEquals(imperative(ys),functional(ys));
        }
    }

    public static final class FindAllBooksWhoseAuthorsNamesStartWith
    {
        public class Person
        {
            public Person(final String name) { this.name = name; }
            public String name;
        }

        public class Book
        {
            public Book(final Date publicationDate, final String title, final List<Person> authors)
            {
                this.publicationDate=publicationDate;
                this.title=title;
                this.authors=authors;
            }

            public Date publicationDate;
            public String title;
            public List<Person> authors;
        }

        private static final List<Book> imperative(final String s, final List<Book> books)
        {
            List<Book> results = new ArrayList<Book>();
            for(final Book b : books)
            {
                final List<Person> authors = b.authors;
                boolean authorsFound = false;
                for(final Person p : authors)
                {
                    if(p.name.startsWith(s)) { authorsFound=true; break; }
                }
                if(authorsFound) results.add(b);
            }
            return results;
        }

        private static final List<Book> functional(final String s, final List<Book> books)
        {
            return filter(new Func<Book, Boolean>() {
                @Override
                public Boolean apply(final Book book) {
                    return IterableHelper.create(book.authors).exists(new Func<Person, Boolean>() {
                        @Override
                        public Boolean apply(final Person person) {
                            return person.name.startsWith(s);
                        }
                    });
                }
            }, books);
        }

        @Test
        public void test()
        {
            List<Book> ys = Arrays.asList(
                    new Book(new Date(),"title",Arrays.asList(new Person("Bob"))),
                    new Book(new Date(),"title",Arrays.asList(new Person("John"),new Person("Julie"))),
                    new Book(new Date(),"title",Arrays.asList(new Person("Sid"),new Person("Nancy"))),
                    new Book(new Date(),"title",Arrays.asList(new Person("Nancy"))),
                    new Book(new Date(),"title",Arrays.asList(new Person("Marge"))),
                    new Book(new Date(),"title",Arrays.asList(new Person("John")))
            );
            Assert.assertEquals(imperative("N",ys),functional("N",ys));
        }
    }

    public static final class DivideFirstIntByEachSubsequentUsingMException
    {
        private final List<Integer> imperative(final List<Integer> l)
        {
            List<Integer> results = new ArrayList<Integer>();
            try {
                final Integer first = l.get(0);
                try {
                    for(int i=1;i<l.size();++i)
                        results.add(first/l.get(i));
                }
                catch(IndexOutOfBoundsException i2) {    // in the event that there is only one element in the input list

                }
                catch(ArithmeticException d) {      // if we divide by zero

                }
            } catch(IndexOutOfBoundsException i)    // if the input list is empty
            {

            }

            // Now calculate the square of each number in the resulting list

            List<Integer> squares = new ArrayList<Integer>();
            for(final Integer i : results)
                squares.add(i*i);

            return squares;
        }

        private final List<Integer> functional(final List<Integer> l)
        {
            final MException<Integer> firstElement = toMException(delay(new Func<List<Integer>, Integer>() {
                @Override
                public Integer apply(List<Integer> o) {
                    return o.get(0);
                }
            }, l));
            final Iterable<MException<Integer>> results =
                    IterableHelper.create(Functional.skip(1,l)).map(new Func<Integer, MException<Integer>>() {
                        @Override
                        public MException<Integer> apply(final Integer integer) {
                            return firstElement.bind(new Func<Integer, MException<Integer>>() {
                                @Override
                                public MException<Integer> apply(final Integer underlyingInteger) {
                                    return toMException(delay(new Func<Integer, Integer>() {
                                        @Override
                                        public Integer apply(Integer o) {
                                            return underlyingInteger / o;
                                        }
                                    }, integer));
                                }
                            });
                        }
                    });
            final Iterable<MException<Integer>> squares = Functional.map(new Func<MException<Integer>, MException<Integer>>() {
                @Override
                public MException<Integer> apply(MException<Integer> o) {
                    return o.bind(new Func<Integer,MException<Integer>>() {
                        @Override
                        public MException<Integer> apply(final Integer i) {
                            return toMException(new Func0<Integer>() {
                                @Override
                                public Integer apply() {
                                    return i*i;
                                }
                            });
                        }
                    });
                }}, results);

            return toList(choose(new Func<MException<Integer>, Option<Integer>>() {
                @Override
                public Option<Integer> apply(MException<Integer> i) {
                    return i.hasException() ? Option.<Integer>None() : toOption(i.read());
                }
            }, squares));
        }

        @Test
        public void test()
        {
            List<Integer> testVals = Arrays.asList(0,1,2,3,4,5,6);
            Assert.assertEquals(imperative(testVals),functional(testVals));
        }
    }
}
