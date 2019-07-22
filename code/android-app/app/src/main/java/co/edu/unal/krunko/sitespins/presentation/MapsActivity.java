package co.edu.unal.krunko.sitespins.presentation;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

	private GoogleMap googleMap;
	private boolean isAdmin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			isAdmin = extras.getBoolean("isAdmin", false);
			Log.d("Activity", "In MapsActivity");
			Log.d("MapsActivity", "Is an admin? " + isAdmin);
		}

	}


	@Override
	public void onMapReady(GoogleMap googleMap) {
		this.googleMap = googleMap;
		this.googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
		UiSettings uiSettings = this.googleMap.getUiSettings();
		uiSettings.setZoomControlsEnabled(true);

		uiSettings.setMapToolbarEnabled(true);

		MapController mapController;

		try {
			mapController = new MapController();

			//pone todos los markers
			mapController.showPins(this.googleMap, this.isAdmin);
			mapController.otherPines(this.googleMap);
			Bundle extras = getIntent().getExtras();

			LatLngBounds ne = mapController.markerOfTheDayBounds();
			this.googleMap.setLatLngBoundsForCameraTarget(ne);


			// if we receive a pin from another activity
			if (extras != null && !isAdmin) {
				double[] point = extras.getDoubleArray("point");
				String name = extras.getString("name");
				String comment = extras.getString("comment");
				String owner = extras.getString("owner");
				String autoId = extras.getString("id");

				mapController.showPinInMaps(new PinUser(owner, name, autoId, comment, new GeoPoint(point[0], point[1])), googleMap);
				//fronteriza el mapa de acuerdo a los bounds
			}

			//si se mantiene oprimido lo lleva a la otra actividad
			this.googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
				@Override
				public void onMapLongClick(LatLng point) {
					// save boundary
					LatLngBounds value = MapsActivity.this.googleMap.getProjection().getVisibleRegion().latLngBounds;
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
		} catch (NullPointerException n) {
			Toast.makeText(this, "There was an error. Probably it is your user.", Toast.LENGTH_SHORT).show();
			finish();
		}


	}

}
