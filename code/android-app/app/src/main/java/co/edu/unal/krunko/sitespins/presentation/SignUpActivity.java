package co.edu.unal.krunko.sitespins.presentation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.ExecutionException;

import co.edu.unal.krunko.sitespins.R;
import co.edu.unal.krunko.sitespins.businessLogic.RegisterController;

public class SignUpActivity extends AppCompatActivity {

	EditText _nameText;
	EditText _emailText;
	EditText _passwordText;

	EditText _reEnterPasswordText;
	Button _signUpButton;
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


		_signUpButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				createAccount(
						_nameText.getText().toString(),
						_emailText.getText().toString(),
						_passwordText.getText().toString(),
						_reEnterPasswordText.getText().toString()
				);
			}

		});


	}

	private void createAccount(String name, String email, String password, String verify_password) {
		new RegisterEmailPasswordTask().execute(
				this,
				name,
				email,
				password,
				verify_password
		);
	}


	private void initViews() {
		// Inicialización de Botones y Campos de Texto (desde Layout)
		this._nameText = findViewById(R.id.input_name);
		this._emailText = findViewById(R.id.input_email);
		this._passwordText = findViewById(R.id.input_password);
		this._reEnterPasswordText = findViewById(R.id.input_reEnterPassword);
		this._signUpButton = findViewById(R.id.btn_signup);
		this._loginLink = findViewById(R.id.link_login);
	}

	@SuppressLint("StaticFieldLeak")
	private class RegisterEmailPasswordTask extends AsyncTask<Object, Void, RegisterController.RegisterStatus> {

		@Override
		protected synchronized RegisterController.RegisterStatus doInBackground(Object... objects) {

			RegisterController.RegisterStatus registerStatus = null;

			try {
				registerStatus = new RegisterController((Activity) (objects[0])).registerWithEmailAndPassword(
						(String) objects[1], //name
						(String) objects[2], //email
						(String) objects[3], //password
						(String) objects[4]  //verification password
				);

			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

			if (user != null) {

				return user.getDisplayName() != null && user.getDisplayName().equals(objects[1]) ?
						RegisterController.RegisterStatus.REGISTER_SUCCESSFUL :
						RegisterController.RegisterStatus.NAME_NOT_UPDATED;

			} else if (registerStatus != RegisterController.RegisterStatus.PENDING) {
				return RegisterController.RegisterStatus.REGISTER_UNSUCCESSFUL;
			}

			return registerStatus;
		}
	}

	protected void onPostExecute(RegisterController.RegisterStatus registerStatus) {
		switch (registerStatus) {
			case REGISTER_UNSUCCESSFUL:
				// we must display an error :u
				break;

			case REGISTER_SUCCESSFUL:
				// do nothing (there is an auth listener we do not have to worry at this point)
				break;

			case INVALID_EMAIL:
				this._emailText.setError("Debes escribir un correo válido");
				break;

			case INVALID_PASSWORD:
				this._passwordText.setError(getResources().getString(R.string.PASSWORD_ERROR));
				break;

			case INVALID_NAME:
				this._nameText.setError("Debes asignar un nombre válido");
				break;

			case NAME_NOT_UPDATED:
				Toast.makeText(this, "User's name has not been updated", Toast.LENGTH_SHORT).show();
				break;

			case PASSWORDS_DO_NOT_MATCH:
				_reEnterPasswordText.setError("Las contraseñas no coinciden");
				break;

			default:
				Log.d("RegisterEmailPassword", "Unexpected status " + registerStatus.name());
				break;
		}
	}

}
