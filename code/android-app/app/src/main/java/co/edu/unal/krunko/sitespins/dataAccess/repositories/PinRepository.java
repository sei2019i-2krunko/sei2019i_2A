package co.edu.unal.krunko.sitespins.dataAccess.repositories;

import android.app.Activity;

import com.google.firebase.firestore.GeoPoint;

import co.edu.unal.krunko.sitespins.dataAccess.models.Pin;

public class PinRepository {

	private Activity activity;

	public PinRepository(Activity activity) {
		this.activity = activity;
	}

	public Pin createNewPin(GeoPoint point) {
		return null;
	}

	public Pin createNewPin(String name, GeoPoint point) {
		return null;
	}

	public Pin createNewPin(double latitude, double longitude) {
		return null;
	}

	public Pin createNewPin(String name, double latitude, double longitude) {
		return null;
	}

}
