package co.edu.unal.krunko.sitespins.dataAccess.models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.GeoPoint;

public class PinAdmin extends Pin {

	private GeoPoint NEBound;
	private GeoPoint SWBound;
	private float zoom;

	/**
	 * @param owner   User's id.
	 * @param name    Pin's name.
	 * @param autoId  Pin's id in firebase.
	 * @param comment Pin's comment
	 * @param point   Pin's location
	 * @param NEBound North-East boundary location (it cannot be null).
	 * @param SWBound North-West boundary location (it cannot be null).
	 * @param zoom Maximum zoom in map.
	 */
	public PinAdmin(String owner, String name, String autoId, String comment, GeoPoint point, @NonNull GeoPoint NEBound, @NonNull GeoPoint SWBound, float zoom) {
		super(owner, name, autoId, comment, point);
		this.NEBound = NEBound;
		this.SWBound = SWBound;
		this.zoom = zoom;
	}

	public GeoPoint getNEBound() {
		return NEBound;
	}

	public void setNEBound(GeoPoint NEBound) {
		this.NEBound = NEBound;
	}

	public GeoPoint getSWBound() {
		return SWBound;
	}

	public void setSWBound(GeoPoint SWBound) {
		this.SWBound = SWBound;
	}

	public double getZoom() {
		return zoom;
	}

	public void setZoom(float zoom) {
		this.zoom = zoom;
	}

	public String toString() {
		String string = "ID: " + this.getAutoId();
		string += "\nOwner: " + this.getOwner();
		string += "\nName: " + this.getName();
		string += "\nComment: " + this.getComment();
		string += "\nPoint: " + this.getPoint().toString();
		string += "\nNEBound: " + this.getNEBound().toString();
		string += "\nSWBound: " + this.getSWBound().toString();
		string += "\n";
		return string;
	}
}
