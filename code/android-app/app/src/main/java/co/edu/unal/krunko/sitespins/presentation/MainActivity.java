package co.edu.unal.krunko.sitespins.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import co.edu.unal.krunko.sitespins.R;

public class MainActivity extends AppCompatActivity {

	private Button _mapsButton;
	private Button _signOutButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

				if (firebaseUser == null) {
					goToMainActivity();

				} else {
					Log.d("Activity", "In MainActivity");
				}
			}
		};

		FirebaseAuth.getInstance().addAuthStateListener(authStateListener);

		this._mapsButton = findViewById(R.id.btn_maps);
		this._signOutButton = findViewById(R.id.btn_sign_out);
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
		this._signOutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FirebaseAuth.getInstance().signOut();
			}
		});

	}

	public void goToMapsActivity() {
		Intent mapsAct = new Intent(getApplicationContext(), MapsActivity.class);
		startActivity(mapsAct);
	}

	public void goToMainActivity(){
		Intent backLogin = new Intent(getApplicationContext(), LoginActivity.class);
		startActivity(backLogin);
		finish();
	}
}
