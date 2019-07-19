package co.edu.unal.krunko.sitespins.dataAccess.models;

import com.google.firebase.firestore.GeoPoint;

public class PinAdmin extends Pin {

	private GeoPoint NEBound;
	private GeoPoint SWBound;

	public PinAdmin(String owner, String name, String autoId, String comment, GeoPoint point, GeoPoint NEBound, GeoPoint SWBound) {
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
