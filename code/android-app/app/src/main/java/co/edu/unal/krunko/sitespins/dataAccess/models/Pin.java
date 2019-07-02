package co.edu.unal.krunko.sitespins.dataAccess.models;

public class Pin {

	private String uid;
	private int autoNum;
	private float latitude;
	private float longitude;
	private boolean visited;

	public Pin(String uid, int autoNum, float latitude, float longitude, boolean visited) {
		this.uid = uid;
		this.autoNum = autoNum;
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

	public int getAutoNum() {
		return autoNum;
	}

	public void setAutoNum(int autoNum) {
		this.autoNum = autoNum;
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
