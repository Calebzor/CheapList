package hu.tvarga.capstone.cheaplist.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import dagger.android.support.DaggerAppCompatActivity;
import hu.tvarga.capstone.cheaplist.R;
import hu.tvarga.capstone.cheaplist.ui.compare.CompareActivity;
import hu.tvarga.capstone.cheaplist.ui.shoppinglist.ShoppingListActivity;

public abstract class AuthBaseActivity extends DaggerAppCompatActivity {

	public static final String ANONYMOUS = "anonymous";
	public static final int RC_SIGN_IN = 1;

	private String userName;
	private FirebaseAuth firebaseAuth;
	private FirebaseAuth.AuthStateListener authStateListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		firebaseAuth = FirebaseAuth.getInstance();

		userName = ANONYMOUS;

		authStateListener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				FirebaseUser user = firebaseAuth.getCurrentUser();
				if (user != null) {
					onSignedInInitialize(user.getDisplayName());
				}
				else {
					onSignedOutCleanUp();
					startActivityForResult(
							AuthUI.getInstance().createSignInIntentBuilder().setIsSmartLockEnabled(
									false).setAvailableProviders(Arrays.asList(
									new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
									new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
									.build(), RC_SIGN_IN);
				}
			}
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		if (this instanceof ShoppingListActivity) {
			menu.findItem(R.id.shoppingListMenuItem).setVisible(false);
		}
		else if (this instanceof CompareActivity) {
			menu.findItem(R.id.compareMenuItem).setVisible(false);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.sign_out_menu:
				AuthUI.getInstance().signOut(this);
				return true;
			case R.id.shoppingListMenuItem:
				// TODO 29-Oct-2017/vatam: put intent
				startActivity(new Intent(this, MainActivity.class));
				return true;
			case R.id.compareMenuItem:
				// TODO 29-Oct-2017/vatam: put intent
				startActivity(new Intent(this, MainActivity.class));
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		firebaseAuth.addAuthStateListener(authStateListener);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (authStateListener != null) {
			firebaseAuth.removeAuthStateListener(authStateListener);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (RC_SIGN_IN == requestCode) {
			if (RESULT_OK == resultCode) {
				Toast.makeText(this, getString(R.string.signed_in), Toast.LENGTH_SHORT).show();
			}
			else if (RESULT_CANCELED == resultCode) {
				Toast.makeText(this, getString(R.string.sign_in_canceled), Toast.LENGTH_SHORT)
						.show();
				finish();
			}
		}
	}

	private void onSignedInInitialize(String displayName) {
		userName = displayName;
	}

	private void onSignedOutCleanUp() {
		userName = ANONYMOUS;
	}
}
