package co.edu.unal.krunko.sitespins.presentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import co.edu.unal.krunko.sitespins.R;
import co.edu.unal.krunko.sitespins.businessLogic.LoginController;

public class LoginActivity extends AppCompatActivity {

	private static final int REQUEST_SIGNUP = 0;

	EditText _emailText;
	EditText _passwordText;

	Button _loginButton;
	Button _anonimoButton;
	LoginButton _facebookButton;

	TextView _signupLink;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

				if (firebaseUser != null) {
					goToMainActivity();

				} else {
					Log.d("Activity", "In SignedinFirebaseMethod");
				}
			}
		};

		FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
		initViews();
		isLoggedIn();

		_signupLink.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Iniciar el Activity de Sign-Up
				Intent goSignup = new Intent(getApplicationContext(), SignupActivity.class);
				startActivityForResult(goSignup, REQUEST_SIGNUP);
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

		_facebookButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fbAuth();
				//LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
			}
		});

	}

	private void fbAuth(){
		LoginController loginController = new LoginController(this);
		_facebookButton.registerCallback(loginController.getCallbackManager(), new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				Log.d("FBLogin_Success","facebook:onSuccess: " + loginResult);
				new LoginController().handleFacebookAccessToken(loginResult.getAccessToken());
			}

			@Override
			public void onCancel() {
				Log.d("FBLogin_Cancel","facebook:onCancel");
				// Show message cancel

			}

			@Override
			public void onError(FacebookException error) {
				Log.d("FBLogin_Error","facebook:onError: ", error);
				// Show message error

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		new LoginController().getCallbackManager().onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
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

	private void isLoggedIn(){
		//_facebookButton.setReadPermissions("email","public_profile");
		if(new LoginController(this).isLoggedIn()){
			goToMainActivity();
		}
	}

	private void goToMainActivity() {
		Intent mainAct = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(mainAct);
		finish();
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
		_facebookButton = (LoginButton) findViewById(R.id.btn_login_FB);

	}
}
