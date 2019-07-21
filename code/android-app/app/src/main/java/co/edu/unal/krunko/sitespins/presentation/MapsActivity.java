package co.edu.unal.krunko.sitespins.presentation;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.firestore.GeoPoint;

import co.edu.unal.krunko.sitespins.R;
import co.edu.unal.krunko.sitespins.businessLogic.MapController;
import co.edu.unal.krunko.sitespins.dataAccess.models.PinUser;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

	private GoogleMap mMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

	}


	/**
	 * Manipulates the map once available.
	 * This callback is triggered when the map is ready to be used.
	 * This is where we can add markers or lines, add listeners or move the camera. In this case,
	 * we just add a marker near Sydney, Australia.
	 * If Google Play services is not installed on the device, the user will be prompted to install
	 * it inside the SupportMapFragment. This method will only be triggered once the user has
	 * installed Google Play services and returned to the app.
	 */
	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
		UiSettings uiSettings = mMap.getUiSettings();
		uiSettings.setZoomControlsEnabled(true);

		uiSettings.setMapToolbarEnabled(true);

		MapController mapController = new MapController();

		//pone todos los markers
		mapController.markerOfTheDay(mMap);
		mapController.otherPines(mMap);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			double [] point = extras.getDoubleArray("point");
			String name = extras.getString("name");
			String comment = extras.getString("comment");
			String owner = extras.getString("owner");
			String autoId = extras.getString("id");

			mapController.dropPin(new PinUser(owner, name, autoId, comment, new GeoPoint(point[0], point[1])), googleMap);
		}


		//fronteriza el mapa de acuerdo a los bounds
		LatLngBounds ne = mapController.markerOfTheDayBounds();
		mMap.setLatLngBoundsForCameraTarget(ne);

		//si se mantiene oprimido lo lleva a la otra actividad
		mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
			@Override
			public void onMapLongClick(LatLng point) {
				// save boundary
				LatLngBounds value = mMap.getProjection().getVisibleRegion().latLngBounds;
				Intent intent = new Intent(getBaseContext(), PinInfoActivity.class);

				//send bounds
				intent.putExtra("SWBoundLat", value.southwest.latitude);
				intent.putExtra("SWBoundLong", value.southwest.longitude);
				intent.putExtra("NEBoundLat", value.northeast.latitude);
				intent.putExtra("NEBoundLong", value.northeast.longitude);

				//send point
				intent.putExtra("PointLat", point.latitude);
				intent.putExtra("PointLong", point.longitude);

				startActivity(intent);
			}
		});
	}

}
