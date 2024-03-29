package co.edu.unal.krunko.sitespins.dataAccess.repositories;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import co.edu.unal.krunko.sitespins.dataAccess.models.User;

import static com.google.android.gms.tasks.Tasks.await;


public class UserRepository {

	private FirebaseAuth auth;
	private User user;
	private Activity activity;
	private AccessToken fbLoggedIn;
	private GoogleSignInOptions ggOptions;
	private GoogleSignInClient ggLoggedIn;


	public UserRepository() {
		this.auth = FirebaseAuth.getInstance();
		this.user = User.fromFirebaseUser(auth.getCurrentUser());
	}

	public UserRepository(Activity activity) {
		this.auth = FirebaseAuth.getInstance();
		this.user = User.fromFirebaseUser(this.auth.getCurrentUser());
		this.activity = activity;
		this.fbLoggedIn = AccessToken.getCurrentAccessToken();
	}

	public UserRepository(Activity activity, AccessToken token) {
		this.auth = FirebaseAuth.getInstance();
		this.activity = activity;
		this.fbLoggedIn = token;
	}

	public UserRepository(Activity activity, GoogleSignInOptions ggOptions) {
		this.auth = FirebaseAuth.getInstance();
		this.activity = activity;
		this.ggOptions = ggOptions;
		this.ggLoggedIn = GoogleSignIn.getClient(this.activity, ggOptions);
		this.user = User.fromFirebaseUser(this.auth.getCurrentUser());
	}

	public User getUser() {
		this.user = User.fromFirebaseUser(this.auth.getCurrentUser());
		return this.user;
	}

	public static User getCurrentUser() {
		return User.fromFirebaseUser(FirebaseAuth.getInstance().getCurrentUser());
	}

	public static Boolean isAdminFirestore(@NonNull String uid) throws ExecutionException, InterruptedException {
		DocumentReference user_doc = FirebaseFirestore.getInstance().document("/users/" + uid);
		return (Boolean) await(user_doc.get().continueWith(new Continuation<DocumentSnapshot, Object>() {
			@Override
			public Object then(@NonNull Task<DocumentSnapshot> task) throws Exception {
				DocumentSnapshot result = task.getResult();
				if (result != null) {
					Log.d("UserRepository", "Info from firebase was got.");
					return result.get("admin");
				}
				Log.e("UserRepository", "Info from firebase could not be got.");
				return null;
			}
		}));
	}

	public User getGoogleUser(Task<GoogleSignInAccount> completedTask) {
		try {
			final GoogleSignInAccount account = completedTask.getResult(ApiException.class);

			Log.d("Google firebase access", "firebaseAuthWithGoogle:" + account.getId());
			AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
			auth.signInWithCredential(credential)
					.addOnCompleteListener(this.activity, new OnCompleteListener<AuthResult>() {
						@Override
						public void onComplete(@NonNull Task<AuthResult> task) {
							if (task.isSuccessful()) {
								// Sign in success, update UI with the signed-in user's information
								Log.d("Login success", "signInWithCredential:success");
								user = User.fromFirebaseUser(auth.getCurrentUser());
							} else {
								// If sign in fails, display a message to the user.
								Log.w("Login failed", "signInWithCredential:failure", task.getException());
								user = null;
							}
						}
					});

		} catch (ApiException e) {
			Log.w("Google sign in error ", "signInResult:failed code=" + e.getStatusCode());
			user = null;
		}
		return user;
	}

	public User getFacebookUser() {
		Log.d("FacebookToken", "handleFacebookAccessToken:" + this.fbLoggedIn.getToken());
		AuthCredential credential = FacebookAuthProvider.getCredential(this.fbLoggedIn.getToken());
		this.auth.signInWithCredential(credential)
				.addOnCompleteListener(this.activity, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							Log.d("Login_Success", "signInWithCredential:success");
							user = getUser();
							LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile"));
						} else {
							Log.w("Login_Fail", "signInWithCredential:failure", task.getException());
							user = null;
						}
					}
				});

		return user;
	}

	public User updateCurrentUserName(String displayName) throws ExecutionException, InterruptedException {

		if (this.getUser() == null) {
			throw new NullPointerException();
		}

		await(Objects.requireNonNull(this.auth.getCurrentUser()).updateProfile(
				new UserProfileChangeRequest.Builder().setDisplayName(displayName).build()
		).addOnCompleteListener(activity, new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task) {
				if (task.isSuccessful()) {
					Log.d("UserUpdate", "UsernameUpdate:success - User's name: " + getUser().getDisplayName());
				} else {
					Log.w("UserUpdate", "UsernameUpdate:failure", task.getException());
				}
			}
		}));

		return this.getUser();
	}

	public User anonymousLogin() throws ExecutionException, InterruptedException{
		await(this.auth.signInAnonymously()
				.addOnCompleteListener(this.activity, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							// Sign in success, update UI with the signed-in user's information
							Log.d("Anonymous login success", "signInAnonymously:success");
							user = getUser();
						} else {
							// If sign in fails, display a message to the user.
							Log.w("Anonymous login failed", "signInAnonymously:failure", task.getException());
							user = null;
						}
					}
				}));
		return user;
	}

	public User getUserByEmailAndPassword(String email, String password) throws ExecutionException, InterruptedException {

		await(this.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if (task.isSuccessful()) {
					Log.d("EmailPassword", "signInWithEmail:success");
				} else {
					Log.w("EmailPassword", "signInWithEmail:failure", task.getException());
				}
			}
		}));

		Log.d("EmailPassword", "actual user " + (this.getUser() != null ? this.getUser().getUid() : "null"));

		return this.getUser();
	}

	public User createUserWithEmailAndPassword(String _email, String _password) throws ExecutionException, InterruptedException {
		await(auth.createUserWithEmailAndPassword(_email, _password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if (task.isSuccessful()) {
					user = User.fromFirebaseUser(auth.getCurrentUser());
					Log.d("EmailPassword", "signUpWithEmail:success");
				} else {
					user = null;
					Log.w("EmailPassword", "signUpWithEmail:failure", task.getException());
				}
			}
		}));


		return this.getUser();
	}

	public boolean fbTokenExist() {
		return this.fbLoggedIn != null && !this.fbLoggedIn.isExpired();
	}

	public AccessToken getFbLoggedIn() {
		return fbLoggedIn;
	}

	public GoogleSignInOptions getGgOptions() {
		return ggOptions;
	}

	public GoogleSignInClient getGgLoggedIn() {
		return ggLoggedIn;
	}
}
