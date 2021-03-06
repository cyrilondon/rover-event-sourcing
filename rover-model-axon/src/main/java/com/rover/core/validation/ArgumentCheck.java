package com.rover.core.validation;

import com.rover.core.util.Utils;
import com.rover.domain.command.model.exception.GameExceptionLabels;
import com.rover.domain.command.model.exception.IllegalArgumentGameException;

/**
 * Utility validation class used to check the method arguments nullity or emptiness.
 * Useful to give consistent error code and messages for all the application.
 * As this utility class could be called by every layer of the application (not only the model but the adapter as well)
 * it is located in a core package.
 *
 */
public class ArgumentCheck {
	
	public static <T> T preNotNull(final T object, final String message) {
		return requiresNotNull(object, String.format(GameExceptionLabels.PRE_CHECK_ERROR_MESSAGE, message));
	}
	
	private static <T> T requiresNotNull(final T object, final String message) {
		if (object == null) throw new IllegalArgumentGameException(message);
		return object;
	}
	
	public static void requiresTrue(final Boolean object, final String message) {
		if (object == Boolean.FALSE) throw new IllegalArgumentGameException(message);
	}
	
	public static void requiresFalse(final Boolean object, final String message) {
		if (object == Boolean.TRUE) throw new IllegalArgumentGameException(message);
	}
	
	public static String preNotEmpty(final String string, final String message) {
		return requiresNotEmpty(string, String.format(GameExceptionLabels.PRE_CHECK_ERROR_MESSAGE, message));
	}
	
	private static String requiresNotEmpty(final String string, final String message) {
		if (!Utils.hasText(string)) throw new IllegalArgumentGameException(message);
		return string;
	}

}
