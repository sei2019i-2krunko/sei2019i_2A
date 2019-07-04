package co.edu.unal.krunko.sitespins.presentation;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import co.edu.unal.krunko.sitespins.R;

public class MainActivity extends AppCompatActivity {

	Button _mapsButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this._mapsButton = findViewById(R.id.btn_maps);

		this._mapsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goToMapsActivity();
			}
		});
	}

	public void goToMapsActivity() {
		Intent mapsAct = new Intent(getApplicationContext(), MapsActivity.class);
		startActivity(mapsAct);
	}
}
