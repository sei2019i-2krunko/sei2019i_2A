package co.edu.unal.krunko.sitespins.presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import co.edu.unal.krunko.sitespins.R;

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

        // Inicialización de Botones y Campos de Texto (desde Layout)
            _nameText = findViewById(R.id.input_name);
            _emailText = findViewById(R.id.input_email);
            _passwordText = findViewById(R.id.input_password);
            _reEnterPasswordText = findViewById(R.id.input_reEnterPassword);
            _signupButton = findViewById(R.id.btn_signup);
            _loginLink = findViewById(R.id.link_login);

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // Finalización del Activity de Sign-Up y regreso al Activity de Login
                Intent backLogin = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(backLogin);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        // TODO: Completar el Activity <:

    }

}
