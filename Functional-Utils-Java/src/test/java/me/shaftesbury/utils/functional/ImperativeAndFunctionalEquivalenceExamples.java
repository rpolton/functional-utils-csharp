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
}
