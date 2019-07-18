package co.edu.unal.krunko.sitespins.dataAccess.models;

import com.google.firebase.firestore.GeoPoint;

public class Pin {

	private String uid;
	private String name;
	private String autoId;
	private GeoPoint point;
	private boolean visited;

	// TODO: 17/07/19 add visited to firestore model

	public Pin(String uid, String name, String autoId, GeoPoint point, boolean visited) {
		this.uid = uid;
		this.name = name;
		this.point = point;
		this.autoId = autoId;
		this.visited = visited;
	}

	public Pin(String uid, String name, String autoId, double latitude, double longitude, boolean visited) {
		this.uid = uid;
		this.name = name;
		this.point = new GeoPoint(latitude, longitude);
		this.autoId = autoId;
		this.visited = visited;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAutoId() {
		return autoId;
	}

	public void setAutoId(String autoId) {
		this.autoId = autoId;
	}

	public GeoPoint getPoint() {
		return point;
	}

	public void setPoint(GeoPoint point) {
		this.point = point;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

}
