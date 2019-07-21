package co.edu.unal.krunko.sitespins.businessLogic;

import android.app.Activity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import java.util.concurrent.ExecutionException;

import co.edu.unal.krunko.sitespins.dataAccess.models.Pin;
import co.edu.unal.krunko.sitespins.dataAccess.models.PinUser;
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
		mMap.addNewPin(new MarkerOptions().position(markerOfTheDay).title("Marker of the day"));
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

	public Pin addNewPin(LatLng point, LatLngBounds bounds, String title, String message) throws ExecutionException, InterruptedException {
		GeoPoint geoPoint = PinRepository.toGeoPoint(point);

		GeoPoint NEBound = PinRepository.toGeoPoint(bounds.northeast);
		GeoPoint SWBound = PinRepository.toGeoPoint(bounds.southwest);

		return this.pinRepository.createNewPin(title, geoPoint, message, NEBound, SWBound);
	}

	public void dropPin(PinUser pin, GoogleMap map) {

		MarkerOptions options = new MarkerOptions()
				.position(PinRepository.toLatLong(pin.getPoint()))
				.title(pin.getName())
				.snippet(pin.getComment())
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

		map.addMarker(options);
	}


}
