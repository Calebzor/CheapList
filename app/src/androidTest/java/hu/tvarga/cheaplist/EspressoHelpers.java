package hu.tvarga.cheaplist;

import android.support.test.espresso.ViewInteraction;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

public class EspressoHelpers {

	public static Matcher<View> childAtPosition(final Matcher<View> parentMatcher,
			final int position) {

		return new TypeSafeMatcher<View>() {
			@Override
			public void describeTo(Description description) {
				description.appendText("Child at position " + position + " in parent ");
				parentMatcher.describeTo(description);
			}

			@Override
			public boolean matchesSafely(View view) {
				ViewParent parent = view.getParent();
				return parent instanceof ViewGroup && parentMatcher.matches(parent) && view.equals(
						((ViewGroup) parent).getChildAt(position));
			}
		};
	}

	public static void clickNavigationButton() {
		ViewInteraction appCompatImageButton = onView(allOf(childAtPosition(
				allOf(withId(R.id.toolbar), childAtPosition(withId(R.id.app_bar), 0)), 0),
				isDisplayed()));
		appCompatImageButton.perform(click());
	}
}
