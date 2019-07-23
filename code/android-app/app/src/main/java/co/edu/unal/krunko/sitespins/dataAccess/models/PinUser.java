package co.edu.unal.krunko.sitespins.dataAccess.models;

import com.google.firebase.firestore.GeoPoint;

public class PinUser extends Pin {

	private boolean visited;


	/**
	 * This constructor set by default visited to false.
	 *
	 * @param owner   User's id.
	 * @param name    Pin's name.
	 * @param autoId  Pin's id in Firebase.
	 * @param comment Pin's comment.
	 * @param point   Pin's location.
	 */
	public PinUser(String owner, String name, String autoId, String comment, GeoPoint point) {
		this(owner, name, autoId, comment, point, false);
	}

	/**
	 * @param owner   User's id.
	 * @param name    Pin's name.
	 * @param autoId  Pin's id in Firebase.
	 * @param comment Pin's comment.
	 * @param point   Pin's location.
	 * @param visited Pin's visited state.
	 */
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
