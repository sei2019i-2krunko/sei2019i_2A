package co.edu.unal.krunko.sitespins.dataAccess.models;

public class Pin {

	private String uid;
	private String autoId;
	private float latitude;
	private float longitude;
	private boolean visited;

	public Pin(String uid, String autoId, float latitude, float longitude, boolean visited) {
		this.uid = uid;
		this.autoId = autoId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.visited = visited;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getAutoId() {
		return autoId;
	}

	public void setAutoId(String autoId) {
		this.autoId = autoId;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}
}
