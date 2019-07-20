package co.edu.unal.krunko.sitespins.dataAccess.models;

import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseUser;

public class User {

	private String uid;
	private String displayName;
	private Uri photoUrl;
	private String email;
	private String providerId;
	private boolean admin;

	public User(String uid, String displayName, Uri photoUrl, String email, String providerId) {
		this.uid = uid;
		this.displayName = displayName;
		this.photoUrl = photoUrl;
		this.email = email;
		this.providerId = providerId;
		this.admin = false;
	}

	public User(String uid, String displayName, Uri photoUrl, String email, String providerId, boolean admin) {
		this.uid = uid;
		this.displayName = displayName;
		this.photoUrl = photoUrl;
		this.email = email;
		this.providerId = providerId;
		this.admin = admin;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Uri getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(Uri photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public static User fromFirebaseUser(FirebaseUser firebaseUser) {
		return firebaseUser == null ? null : new User(
				firebaseUser.getUid(),
				firebaseUser.getDisplayName(),
				firebaseUser.getPhotoUrl(),
				firebaseUser.getEmail(),
				firebaseUser.getProviderId()
		);
	}

	public static User fromGoogleUser(GoogleSignInAccount account) {
		return account == null ? null : new User(
				account.getId(),
				account.getDisplayName(),
				account.getPhotoUrl(),
				account.getEmail(),
				account.getIdToken()
		);
	}
}
