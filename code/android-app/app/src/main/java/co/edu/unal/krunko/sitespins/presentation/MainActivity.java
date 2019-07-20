package co.edu.unal.krunko.sitespins.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import co.edu.unal.krunko.sitespins.R;
import co.edu.unal.krunko.sitespins.dataAccess.models.User;
import co.edu.unal.krunko.sitespins.dataAccess.repositories.UserRepository;

public class MainActivity extends AppCompatActivity {

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

		Button _mapsButton = findViewById(R.id.btn_maps);
		Button _signOutButton = findViewById(R.id.btn_sign_out);
		TextView _welcome = findViewById(R.id.welcome_text);

		User user = UserRepository.getCurrentUser();

		if (user != null) {
			if (user.getDisplayName() == null) {
				_welcome.append(" " + user.getUid());
			} else {
				_welcome.append(" " + user.getDisplayName());
			}
		}

		_mapsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goToMapsActivity();
			}
		});
		_signOutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FirebaseAuth.getInstance().signOut();
				LoginManager.getInstance().logOut();
			}
		});

	}

	public void goToMapsActivity() {
		Intent mapsAct = new Intent(getApplicationContext(), MapsActivity.class);
		startActivity(mapsAct);
	}

	public void goToMainActivity() {
		Intent backLogin = new Intent(getApplicationContext(), LoginActivity.class);
		startActivity(backLogin);
		finish();
	}
}
