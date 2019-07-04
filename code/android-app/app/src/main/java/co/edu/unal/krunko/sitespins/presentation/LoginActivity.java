package co.edu.unal.krunko.sitespins.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import co.edu.unal.krunko.sitespins.R;
import co.edu.unal.krunko.sitespins.businessLogic.LoginController;

public class LoginActivity extends AppCompatActivity {

	private static final int REQUEST_SIGNUP = 0;

	EditText _emailText;
	EditText _passwordText;

	Button _loginButton;
	Button _anonimoButton;

	TextView _signupLink;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		//TODO: check if the user is logged in already

		initViews();

		_signupLink.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Iniciar el Activity de Sign-Up
				Intent goSignup = new Intent(getApplicationContext(), SignupActivity.class);
				startActivityForResult(goSignup, REQUEST_SIGNUP);
				finish();
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});

		_loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Login
				login(_emailText.getText().toString(), _passwordText.getText().toString());
			}
		});

		_anonimoButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goToMapsActivity();

			}
		});

	}


	private void login(String email, String password) {

		LoginController.LoginStatus loginStatus = new LoginController(this).loginWithEmailAndPassword(email, password);

		switch (loginStatus) {
			case WRONG_CREDENTIALS:
				_emailText.setError(getResources().getString(R.string.WRONG_CREDENTIALS));
				_passwordText.setError(getResources().getString(R.string.WRONG_CREDENTIALS));
				break;
			case SUCCESSFUL_LOGIN:
				goToMainActivity();
				break;
			case EMAIL_IS_REQUIRED_OR_INVALID:
				_emailText.setError(getResources().getString(R.string.EMAIL_ERROR));
				break;
			case PASSWORD_IS_REQUIRED:
				_passwordText.setError(getResources().getString(R.string.PASSWORD_ERROR));
				break;
			default:
				break;

		}
	}

	private LoginController loginController;

	private void goToMainActivity() {
		Intent mainAct = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(mainAct);
	}

	private void goToMapsActivity() {
		Intent mapsAct = new Intent(getApplicationContext(), MapsActivity.class);
		startActivity(mapsAct);
	}

	private void initViews() {

		// Inicialización de Botones y Campos de Texto (desde Layout)
		_emailText = findViewById(R.id.input_email);
		_passwordText = findViewById(R.id.input_password);

		_loginButton = findViewById(R.id.btn_login);
		_anonimoButton = findViewById(R.id.btn_login_A);

		_signupLink = findViewById(R.id.link_signup);
	}
}
