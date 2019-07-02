package co.edu.unal.krunko.sitespins.dataAccess.models;

public class User {

	private String uid;
	private String displayName;
	private String photoUrl;
	private String email;
	private String providerId;

	public User(String uid, String displayName, String photoUrl, String email, String providerId) {
		this.uid = uid;
		this.displayName = displayName;
		this.photoUrl = photoUrl;
		this.email = email;
		this.providerId = providerId;
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

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
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
}
