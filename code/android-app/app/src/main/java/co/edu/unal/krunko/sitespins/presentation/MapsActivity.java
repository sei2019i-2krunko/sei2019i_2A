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

import co.edu.unal.krunko.sitespins.R;
import co.edu.unal.krunko.sitespins.businessLogic.MapControllers;

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

		MapControllers mo = new MapControllers();
		//pone todos los markers
		mo.markerOfTheDay(mMap);
		mo.otherPines(mMap);
		//fronteriza el mapa de acuerdo a los bounds
		LatLngBounds ne = mo.markerOfTheDayBounds();
		mMap.setLatLngBoundsForCameraTarget(ne);
		//si se mantiene oprimido lo lleva a la otra actividad
		mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
			@Override
			public void onMapLongClick(LatLng point) {
				// save boundary
				LatLngBounds value = mMap.getProjection().getVisibleRegion().latLngBounds;
				;
				Intent i = new Intent(getBaseContext(), PinInfoActivity.class);
				//send bounds
				i.putExtra("aLatLngPbLat", value.southwest.latitude);
				i.putExtra("aLatLngPbLong", value.southwest.longitude);
				i.putExtra("bLatLngPbLat", value.northeast.latitude);
				i.putExtra("bLatLngPbLong", value.northeast.longitude);
				//send point
				i.putExtra("LatLngPLat", point.latitude);
				i.putExtra("LatLngPLong", point.longitude);

				startActivity(i);
			}
		});
	}

}
