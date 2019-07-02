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
                LocalLogin(_emailText.getText().toString(),_passwordText.getText().toString());
            }
        });

        _anonimoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMapsActivity();

            }
        });

        // TODO: Completar el Activity <:

    }


    private LoginController loginController;
    //public static User logedUser;

    private void LocalLogin(String email, String password){
        loginController = new LoginController();

        String msg = loginController.loginWithEmailAndPassword(email, password);

        if(msg.equalsIgnoreCase("Ingreso Exitoso")){
            //logedUser = loginController.getUser();
            goToMainActivity();

        }else if(msg.equalsIgnoreCase("Ingrese e-mail")){
            _emailText.setError(msg);

        }else if(msg.equalsIgnoreCase("Ingrese contraseña")){
            _passwordText.setError(msg);

        }else if(msg.equalsIgnoreCase("El e-mail no está registrado")){
            _emailText.setError(msg);

        }else if(msg.equalsIgnoreCase("Contraseña incorrecta")){
            _passwordText.setError(msg);
        }else{
            System.out.println("Error en Login Actiyvity -> Local Login");
        }
    }

    private void goToMainActivity(){
        Intent mainAct = new Intent (getApplicationContext(), MainActivity.class);
        startActivity(mainAct);
    }
    private void goToMapsActivity(){
        Intent mapsAct = new Intent (getApplicationContext(), MapsActivity.class);
        startActivity(mapsAct);
    }

    private void initViews(){

        // Inicialización de Botones y Campos de Texto (desde Layout)
        _emailText = findViewById(R.id.input_email);
        _passwordText = findViewById(R.id.input_password);

        _loginButton = findViewById(R.id.btn_login);
        _anonimoButton= findViewById(R.id.btn_login_A);

        _signupLink = findViewById(R.id.link_signup);
    }
}
