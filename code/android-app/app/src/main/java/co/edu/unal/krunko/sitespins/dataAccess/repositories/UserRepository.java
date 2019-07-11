package co.edu.unal.krunko.sitespins.dataAccess.repositories;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;
import java.util.concurrent.Executor;

import co.edu.unal.krunko.sitespins.dataAccess.models.User;


public class UserRepository {

	private FirebaseAuth auth;
	private User user;
	private Activity activity;
	private AccessToken fbLoggedIn;


	public UserRepository(Activity activity) {
		this.auth = FirebaseAuth.getInstance();
		this.user = User.fromFirebaseUser(this.auth.getCurrentUser());
		this.activity = activity;
		this.fbLoggedIn = AccessToken.getCurrentAccessToken();
	}

	public UserRepository(Activity activity, AccessToken token){
		this.auth = FirebaseAuth.getInstance();
		this.user = User.fromFirebaseUser(this.auth.getCurrentUser());
		this.activity = activity;
		this.fbLoggedIn = token;
	}

	public User getUser() {
		this.user = User.fromFirebaseUser(this.auth.getCurrentUser());
		return this.user;
	}

	public User getFacebookUser(AccessToken token){
		Log.d("FacebookToken", "handleFacebookAccessToken:" + token.getToken());
		AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
		auth.signInWithCredential(credential)
				.addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							Log.d("Login_Success", "signInWithCredential:success");
							user = User.fromFirebaseUser(auth.getCurrentUser());
						} else {
							// If sign in fails, display a message to the user.
							Log.w("Login_Fail", "signInWithCredential:failure", task.getException());
							user = null;
						}
					}
				});

		return user;
	}

	public User updateCurrentUserName(String displayName) {

		if (this.getUser() == null) {
			return null;
		}

		Objects.requireNonNull(this.auth.getCurrentUser()).updateProfile(
				new UserProfileChangeRequest.Builder().setDisplayName(displayName).build()
		).addOnCompleteListener(activity, new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task) {
				if (task.isSuccessful()) {
					Log.d("UserUpdate", "UsernameUpdate:success");
				} else {
					Log.w("UserUpdate", "UsernameUpdate:failure", task.getException());
				}
			}
		});

		try {
			wait(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return this.getUser();
	}

	public User getUserByEmailAndPassword(String email, String password) {

		this.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if (task.isSuccessful()) {
					Log.d("EmailPassword", "signInWithEmail:success");
				} else {
					Log.w("EmailPassword", "signInWithEmail:failure", task.getException());
				}
			}
		});

		Log.d("EmailPassword", "actual user " + (this.getUser() != null ? this.getUser().getUid() : "null"));

		return this.getUser();
	}


	public User createUserWithEmailAndPassword(String _email, String _password) {
		auth.createUserWithEmailAndPassword(_email, _password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
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
		});


		return this.getUser();
	}

	public boolean fbTokenExist(){
		return this.fbLoggedIn != null && !this.fbLoggedIn.isExpired();
	}

	public AccessToken getFbLoggedIn() {
		return fbLoggedIn;
	}
}
