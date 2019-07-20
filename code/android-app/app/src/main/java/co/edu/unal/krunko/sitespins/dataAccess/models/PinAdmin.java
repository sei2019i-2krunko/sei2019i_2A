package co.edu.unal.krunko.sitespins.dataAccess.models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.GeoPoint;

public class PinAdmin extends Pin {

	private GeoPoint NEBound;
	private GeoPoint SWBound;

	/**
	 * @param owner   User's id.
	 * @param name    Pin's name.
	 * @param autoId  Pin's id in firebase.
	 * @param comment Pin's comment
	 * @param point   Pin's location
	 * @param NEBound North-East boundary location (it cannot be null).
	 * @param SWBound North-West boundary location (it cannot be null).
	 */
	public PinAdmin(String owner, String name, String autoId, String comment, GeoPoint point, @NonNull GeoPoint NEBound, @NonNull GeoPoint SWBound) {
		super(owner, name, autoId, comment, point);
		this.NEBound = NEBound;
		this.SWBound = SWBound;
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
}
