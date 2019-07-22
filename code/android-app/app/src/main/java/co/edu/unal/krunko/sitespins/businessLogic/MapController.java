package co.edu.unal.krunko.sitespins.businessLogic;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import co.edu.unal.krunko.sitespins.dataAccess.models.Pin;
import co.edu.unal.krunko.sitespins.dataAccess.models.PinUser;
import co.edu.unal.krunko.sitespins.dataAccess.repositories.PinRepository;


public class MapController {

	private PinRepository pinRepository;

	public MapController() {
		this.pinRepository = new PinRepository();
	}

	public void showPins(final GoogleMap googleMap, boolean isAdmin) {
		// TODO: 22/07/19 implement for admin and non-admin users
		/*mMap.createNewPin(new MarkerOptions().position(markerOfTheDay).title("Marker of the day"));
		mMap.moveCamera(CameraUpdateFactory.newLatLng(markerOfTheDay));*/
		if (!isAdmin) {
			// TODO: 22/07/19  Non-admin:
			//  get global pin and then lock the map scrolling
			new ShowPinsTask().execute(this.pinRepository, googleMap);

		} else {
			// TODO: 22/07/19  Admin:
			//  get global pin without lock the map scrolling
		}
	}

	public LatLngBounds markerOfTheDayBounds() {

		/*//TODO:
		//link en los repositorios como es
		return globalPointsRepository.getBounds();*/
		return null;
	}

	public void otherPines(GoogleMap googleMap) {
		//TODO:
		/*//link en los repositorios como es
		pins[] pines = userRepository.getallPins;
		for (int i = 0; i < pins.lenght; i++) {
			showPinInMaps(pins.geopoint, pins.name, pins.comment, googleMap);
		}*/

	}

	public Pin createNewPin(LatLng point, LatLngBounds bounds, String title, String message) throws ExecutionException, InterruptedException {
		GeoPoint geoPoint = PinRepository.toGeoPoint(point);

		GeoPoint NEBound = PinRepository.toGeoPoint(bounds.northeast);
		GeoPoint SWBound = PinRepository.toGeoPoint(bounds.southwest);

		return this.pinRepository.createNewPin(title, geoPoint, message, NEBound, SWBound);
	}

	public void showPinInMaps(PinUser pin, GoogleMap map) {

		MarkerOptions options = new MarkerOptions()
				.position(PinRepository.toLatLong(pin.getPoint()))
				.title(pin.getName())
				.snippet(pin.getComment())
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

		map.addMarker(options);
	}


	@SuppressLint("StaticFieldLeak")
	private class ShowPinsTask extends AsyncTask<Object, Void, List<Object>> {

		@Override
		protected synchronized List<Object> doInBackground(Object... objects) {
			List<Object> pins = new ArrayList<>(2);
			try {
				PinRepository repository = (PinRepository)objects[0];
				pins.add(repository.getPins());//List<PinUser>
				pins.add(objects[1]); //GoogleMap
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return pins;
		}

		@Override
		protected void onPostExecute(List<Object> objects) {
			List<PinUser> pins = (List<PinUser>) objects.get(0);
			GoogleMap googleMap = (GoogleMap) objects.get(1);

			for (int i = 0; i < pins.size(); i++) {
				showPinInMaps(pins.get(i), googleMap);
			}
		}
	}

}
