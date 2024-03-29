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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.ExecutionException;

import co.edu.unal.krunko.sitespins.R;
import co.edu.unal.krunko.sitespins.businessLogic.LoginController;


public class LoginActivity extends AppCompatActivity {

	private static final int REQUEST_SIGN_UP = 0;
	private static final int RC_SIGN_IN = 0;
	private CallbackManager callbackManager;

	EditText _emailText;
	EditText _passwordText;

	Button _loginButton;
	Button _anonymousButton;
	LoginButton _facebookButton;
	SignInButton _signInGoogle;

	TextView _signUpLink;

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
					Log.d("Activity", "In SignedInFirebaseMethod");
				}
			}
		};

		FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
		initViews();

		_signUpLink.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Iniciar el Activity de Sign-Up
				Intent goSignup = new Intent(getApplicationContext(), SignUpActivity.class);
				startActivityForResult(goSignup, REQUEST_SIGN_UP);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});

		_loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				login(_emailText.getText().toString(), _passwordText.getText().toString());
			}
		});

		_signInGoogle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ggAuth();
			}
		});

		_anonymousButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				anonymous();
			}
		});

		_facebookButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fbAuth();
			}
		});

		callbackManager = CallbackManager.Factory.create();
	}

	private void ggAuth() {
		LoginController loginController = new LoginController(this);
		startActivityForResult(loginController.handleGoogleSignUp(getString(R.string.default_web_client_id)), RC_SIGN_IN);
	}

	private void fbAuth() {
		final Activity activity = this;
		_facebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				Log.d("FBLogin_Success", "facebook:onSuccess: " + loginResult);
				new LoginController(activity).handleFacebookAccessToken(loginResult.getAccessToken());
			}

			@Override
			public void onCancel() {
				Log.d("FBLogin_Cancel", "facebook:onCancel");
				// Show message cancel
			}

			@Override
			public void onError(FacebookException error) {
				Log.d("FBLogin_Error", "facebook:onError: ", error);
				// Show message error
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		callbackManager.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RC_SIGN_IN) {
			new LoginController(this).handleGoogleSignInResult(data);
		}
	}

	private void anonymous(){
		new AnonymousAuth().execute(this);
	}

	private void login(final String email, final String password) {
		new LoginEmailPasswordTask().execute(this, email, password);
	}

	private void goToMainActivity() {
		Intent mainAct = new Intent(getApplicationContext(), MainActivity.class);
		mainAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
		_anonymousButton = findViewById(R.id.btn_login_A);

		_signUpLink = findViewById(R.id.link_signup);
		_facebookButton = findViewById(R.id.btn_login_FB);
		_signInGoogle = findViewById(R.id.btn_login_G);

	}

	@SuppressLint("StaticFieldLeak")
	private class LoginEmailPasswordTask extends AsyncTask<Object, Void, LoginController.LoginStatus> {

		@Override
		protected synchronized LoginController.LoginStatus doInBackground(Object... objects) {

			LoginController.LoginStatus loginStatus = null;
			try {
				loginStatus = new LoginController((Activity) objects[0]).loginWithEmailAndPassword(
						(String) objects[1], //email
						(String) objects[2]  //password
				);
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			return loginStatus != null ? loginStatus : LoginController.LoginStatus.WRONG_CREDENTIALS;
		}


		protected void onPostExecute(LoginController.LoginStatus loginStatus) {
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
	}

	@SuppressLint("StaticFieldLeak")
	private class AnonymousAuth extends AsyncTask<Object, Void, LoginController.LoginStatus> {

		@Override
		protected synchronized LoginController.LoginStatus doInBackground(Object... objects) {

			LoginController.LoginStatus loginStatus = null;
			try {
				loginStatus = new LoginController((Activity) objects[0]).handleAnonymous();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			return loginStatus != null ? loginStatus : LoginController.LoginStatus.WRONG_CREDENTIALS;
		}


		protected void onPostExecute(LoginController.LoginStatus loginStatus) {
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

				case ANONYMOUS_FAILED:
					_emailText.setError(getResources().getString(R.string.ANONYMOUS_ERROR));
					break;

				default:
					break;
			}
		}
	}

}
