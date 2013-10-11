package org.quadrifacet.common;

import com.google.common.base.Function;
import com.google.common.base.Optional;

import java.util.*;

/**
 * User: rhowe
 * Date: 10/11/13
 * Time: 1:08 PM
 */
public class ArgumentFinder {

    public ArgumentFinder(String[] args) {
        this.args = args;
    }

    public ArgumentFinder(String spaceDelimitedArgs) {
        this(spaceDelimitedArgs.split(" "));
    }

    private String[] args;

    public <T> Optional<T> findFirstArgument(Function<String, Optional<T>> finder) {
        for (String arg : args) {
            Optional<T> query = finder.apply(arg);
            if (query != null && query.isPresent())
                return query;
        }
        return Optional.absent();
    }

    public <T> List<T> findAllArguments(Function<String, Optional<T>> finder) {
        List<T> foundArguments = new ArrayList<>();
        for (String arg : args) {
            Optional<T> query = finder.apply(arg);
            if (query != null && query.isPresent())
                foundArguments.add(query.get());
        }
        return foundArguments;
    }

    public void test() {
        Function<String, Optional<Integer>> getInt = new Function<String, Optional<Integer>>() {
            @Override public Optional<Integer> apply(String arg) {
                boolean allDigit = true;
                for (char ch : arg.toCharArray())
                    allDigit &= Character.isDigit(ch);
                if (allDigit)
                    return Optional.fromNullable(Integer.parseInt(arg));
                else
                    return Optional.absent();
            }
        };

        String args = "1 and 2 and 3 and 4";
        int shouldBeOne = new ArgumentFinder(args).findFirstArgument(getInt).or(1);
        List<Integer> shouldHaveOneTwoThreeAndFour = new ArgumentFinder(args).findAllArguments(getInt);
    }

}
