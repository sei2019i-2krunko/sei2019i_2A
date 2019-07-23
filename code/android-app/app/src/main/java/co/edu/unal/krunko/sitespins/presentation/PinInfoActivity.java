package co.edu.unal.krunko.sitespins.presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.concurrent.ExecutionException;

import co.edu.unal.krunko.sitespins.R;
import co.edu.unal.krunko.sitespins.businessLogic.MapController;
import co.edu.unal.krunko.sitespins.dataAccess.models.Pin;

public class PinInfoActivity extends AppCompatActivity {

	private Button _entry;
	EditText _name;
	EditText _comment;

	LatLng point;
	LatLngBounds boundaries;
	Float zoom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pin_info);
		initViews();


		//recupera el marker y los bounds
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			LatLng point = new LatLng(extras.getDouble("PointLat"), extras.getDouble("PointLong"));

			LatLngBounds boundaries = new LatLngBounds(
					new LatLng(
							extras.getDouble("SWBoundLat"),
							extras.getDouble("SWBoundLong")
					)
					, new LatLng(
					extras.getDouble("NEBoundLat"),
					extras.getDouble("NEBoundLong"))
			);

			//The key argument here must match that used in the other activity
			this.point = point;
			this.boundaries = boundaries;

			this.zoom = extras.getFloat("zoom");

		} else {
			finish();
		}

		_entry.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new AddNewPinTask().execute(
						point,
						boundaries,
						_name.getText().toString(),
						_comment.getText().toString(),
						zoom
				);
			}
		});

	}

	private void initViews() {

		// Inicialización de Botones y Campos de Texto (desde Layout)
		_name = findViewById(R.id.editText);
		_comment = findViewById(R.id.commentn);
		_entry = findViewById(R.id._entry);
	}

	@SuppressLint("StaticFieldLeak")
	private class AddNewPinTask extends AsyncTask<Object, Void, Pin> {

		@Override
		protected synchronized Pin doInBackground(Object... objects) {
			MapController mapController = new MapController();

			Pin pin = null;

			try {
				float _zoom = (Float) objects[4];

				pin = mapController.createNewPin(
						(LatLng) objects[0],  // point
						(LatLngBounds) objects[1], // Boundaries
						(String) objects[2], // name
						(String) objects[3], // comment
						_zoom //zoom
				);
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			return pin;
		}

		protected void onPostExecute(Pin pin) {
			Intent intent = new Intent(getBaseContext(), MapsActivity.class);
			intent.putExtra("point", new double[]{pin.getPoint().getLatitude(), pin.getPoint().getLongitude()});
			intent.putExtra("name", pin.getName());
			intent.putExtra("owner", pin.getOwner());
			intent.putExtra("id", pin.getAutoId());
			intent.putExtra("comment", pin.getComment());
			intent.putExtra("isAdmin", getIntent().getExtras().getBoolean("isAdmin", false));
			intent.putExtra("zoom", getIntent().getExtras().getFloat("zoom"));
			startActivity(intent);
			finish();
		}
	}
}
