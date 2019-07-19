package co.edu.unal.krunko.sitespins.dataAccess.models;

import com.google.firebase.firestore.GeoPoint;

public class PinUser extends Pin {

	private boolean visited;

	public PinUser(String owner, String name, String autoId, String comment, GeoPoint point) {
		super(owner, name, autoId, comment, point);
		this.visited = false;
	}

	public PinUser(String owner, String name, String autoId, String comment, GeoPoint point, boolean visited) {
		super(owner, name, autoId, comment, point);
		this.visited = visited;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}
}
