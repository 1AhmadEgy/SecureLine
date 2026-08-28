package com.secureline.secureline.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionUtils {

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static <T> List<T> filter(Collection<T> collection, Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        if (collection != null) {
            for (T item : collection) {
                if (predicate.test(item)) {
                    result.add(item);
                }
            }
        }
        return result;
    }

    public interface Predicate<T> {
        boolean test(T item);
    }
}
