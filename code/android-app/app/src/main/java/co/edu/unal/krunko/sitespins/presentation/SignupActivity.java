package co.edu.unal.krunko.sitespins.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import co.edu.unal.krunko.sitespins.R;
import co.edu.unal.krunko.sitespins.businessLogic.RegisterController;

public class SignupActivity extends AppCompatActivity {

	EditText _nameText;
	EditText _emailText;
	EditText _passwordText;

	EditText _reEnterPasswordText;
	Button _signupButton;
	TextView _loginLink;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);

		initViews();

		_loginLink.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Finalización del Activity de Sign-Up y regreso al Activity de Login
				Intent backLogin = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(backLogin);
				finish();
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});

		final String password = getResources().getString(R.string.verify_password);

		_signupButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (passwordsMatch()) {
					createAccount(
							_nameText.getText().toString(),
							_emailText.getText().toString(),
							_passwordText.getText().toString()
					);
				} else {
					Toast.makeText(
							getApplicationContext(),
							password,
							Toast.LENGTH_SHORT).show();
				}
			}

		});


	}

	private boolean passwordsMatch() {
		return _passwordText.getText().toString().equals(_reEnterPasswordText.getText().toString());

	}

	private void createAccount(String name, String email, String password) {
		RegisterController.RegisterStatus registerStatus = new RegisterController(this).
				registerWithEmailAndPassword(name, email, password);

		switch (registerStatus) {
			case INVALID_NAME:
				break;
			case INVALID_EMAIL:
				break;
			case INVALID_PASSWORD:
				break;
			case NAME_NOT_UPDATED:
				break;
			case REGISTER_SUCCESSFUL:
				break;
			case REGISTER_UNSUCCESSFUL:
				break;
			default:
				break;
		}
	}


	private void initViews() {
		// Inicialización de Botones y Campos de Texto (desde Layout)
		this._nameText = findViewById(R.id.input_name);
		this._emailText = findViewById(R.id.input_email);
		this._passwordText = findViewById(R.id.input_password);
		this._reEnterPasswordText = findViewById(R.id.input_reEnterPassword);
		this._signupButton = findViewById(R.id.btn_signup);
		this._loginLink = findViewById(R.id.link_login);
	}

}
