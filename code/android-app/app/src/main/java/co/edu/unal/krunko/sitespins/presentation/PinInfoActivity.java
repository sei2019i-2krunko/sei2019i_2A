package co.edu.unal.krunko.sitespins.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import co.edu.unal.krunko.sitespins.R;
import co.edu.unal.krunko.sitespins.businessLogic.MapControllers;

public class PinInfoActivity extends AppCompatActivity {

	private Button _entry;
	EditText _name;
	EditText _comment;
	LatLng n1;
	LatLngBounds n2;
	MapControllers mo = new MapControllers();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pin_info);
		initViews();
		//recupera el marker y los bounds
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			LatLng value = new LatLng(extras.getDouble("LatLngPLat"), extras.getDouble("LatLngPLong"));
			LatLngBounds value2 = new LatLngBounds(new LatLng(
					extras.getDouble("aLatLngPbLat"), extras.getDouble("aLatLngPbLong"))
					, new LatLng(extras.getDouble("bLatLngPbLat"), extras.getDouble("bLatLngPbLong"))
			);
			//The key argument here must match that used in the other activity
			n1 = value;
			n2 = value2;
		}

		_entry.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// devuelve lo que obtuvo
				Intent intent = new Intent(getBaseContext(), MapsActivity.class);
				mo.addMarker(n1, n2, _name.getText().toString(), _comment.getText().toString());
				startActivity(intent);
			}
		});

	}

	private void initViews() {

		// Inicialización de Botones y Campos de Texto (desde Layout)
		_name = findViewById(R.id.editText);
		_comment = findViewById(R.id.commentn);
		_entry = findViewById(R.id._entry);
	}
}
