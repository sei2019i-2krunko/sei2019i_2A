package co.edu.unal.krunko.sitespins.dataAccess.models;

import com.google.firebase.firestore.GeoPoint;

public abstract class Pin {

	private String name;
	private String owner;
	private String autoId;
	private String comment;
	private GeoPoint point;

	Pin(String owner, String name, String autoId, String comment, GeoPoint point) {
		this.owner = owner;
		this.name = name;
		this.autoId = autoId;
		this.comment = comment;
		this.point = point;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getAutoId() {
		return autoId;
	}

	public void setAutoId(String autoId) {
		this.autoId = autoId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public GeoPoint getPoint() {
		return point;
	}

	public void setPoint(GeoPoint point) {
		this.point = point;
	}
}
