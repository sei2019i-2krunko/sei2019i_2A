package co.edu.unal.krunko.sitespins.businessLogic;

import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;

public class MapControllers {

	private Activity activity;

	public MapControllers() {
		this.activity = null;
	}

	public LatLng markerOfTheDay() {
		LatLng markerOfTheDay = new LatLng(4.6625352, -74.0616287);
		return markerOfTheDay;
	}


}
