package hu.tvarga.capstone.cheaplist.utility;

import timber.log.Timber;

public class StringUtils {

	public static String setTextToPlaceholder(String textWithPlaceholder, Object... textToInsert) {
		String result;
		try {
			result = String.format(textWithPlaceholder, (Object[]) textToInsert);
		}
		catch (Exception ex) {
			result = textWithPlaceholder;
			Timber.e(ex);
		}
		return result;
	}
}