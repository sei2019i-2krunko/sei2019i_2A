package co.edu.unal.krunko.sitespins.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import co.edu.unal.krunko.sitespins.R;

public class MainActivity extends AppCompatActivity {

	private Button _mapsButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this._mapsButton = findViewById(R.id.btn_maps);
		TextView _welcome = findViewById(R.id.welcome_text);

		if (FirebaseAuth.getInstance().getCurrentUser() != null) {
			_welcome.append(" " + FirebaseAuth.getInstance().getUid());
		}

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
