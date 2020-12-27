package com.rover.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

public class Utils {

	public static boolean hasLength(String str) {
		return (str != null && !str.isEmpty());
	}

	public static boolean containsText(CharSequence str) {
		int strlen = str.length();
		for (int i = 0; i < strlen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasText(String str) {
		return hasLength(str) && containsText(str);
	}

	public static <T> List<List<T>> nPartition(List<T> listToSplit, final int partitionSize) {
		return new ArrayList<>(listToSplit.stream()
				.collect(Collectors.groupingBy(e1 -> listToSplit.indexOf(e1) / partitionSize)).values());
	}
	
	public static <T> T singleResult(@Nullable Collection<T> results) throws IncorrectResultSizeDataAccessException {
		if (CollectionUtils.isEmpty(results)) {
			return null;
		}
		if (results.size() > 1) {
			throw new IncorrectResultSizeDataAccessException(1, results.size());
		}
		return results.iterator().next();
	}

}
