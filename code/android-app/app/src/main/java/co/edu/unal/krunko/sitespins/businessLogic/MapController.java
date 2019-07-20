package co.edu.unal.krunko.sitespins.businessLogic;

import android.app.Activity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import co.edu.unal.krunko.sitespins.dataAccess.repositories.PinRepository;


public class MapController {

	private Activity activity;
	private PinRepository pinRepository;


	public MapController() {
		this(null);
	}

	public MapController(Activity activity) {
		this.activity = activity;
		this.pinRepository = new PinRepository();
	}

	public void markerOfTheDay(GoogleMap mMap) {
		//TODO:
		/*//link en los repositorios como es
		LatLng markerOfTheDay = GlobalPinsRepository.getGeolocation();
		mMap.addMarker(new MarkerOptions().position(markerOfTheDay).title("Marker of the day"));
		mMap.moveCamera(CameraUpdateFactory.newLatLng(markerOfTheDay));*/
	}

	public LatLngBounds markerOfTheDayBounds() {

		/*//TODO:
		//link en los repositorios como es
		return globalPointsRepository.getBounds();*/
		return null;
	}

	public void otherPines(GoogleMap mMap) {
		//TODO:
		/*//link en los repositorios como es
		pins[] pines = userRepository.getallPins;
		for (int i = 0; i < pins.lenght; i++) {
			dropPin(pins.geopoint, pins.name, pins.comment, mMap);
		}*/

	}

	public void addMarker(LatLng point, LatLngBounds bounds, String title, String message) {

	}

	public void dropPin(LatLng ll, String title, String message, GoogleMap mMap) {
		MarkerOptions options = new MarkerOptions()
				.position(ll)
				.title(title)
				.snippet(message)
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

		mMap.addMarker(options);
	}


}
