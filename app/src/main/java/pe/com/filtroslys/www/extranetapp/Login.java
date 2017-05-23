package pe.com.filtroslys.www.extranetapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import junit.framework.TestResult;

public class Login extends AppCompatActivity {

    TextView   txtTegistrar  ;
    int currentapiVersion;
    EditText txtUser ,  txtPass ;
    ActionBar actionBar;
    Button btnLogin ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        txtTegistrar = (TextView)  findViewById(R.id.txtRegistrate);
        txtUser  =  (EditText) findViewById(R.id.txtUserName);
        txtPass = (EditText)findViewById(R.id.txtPass);
        btnLogin  = (Button)findViewById(R.id.btnLogin);

        txtTegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this , RegistrarUsuario.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this , MenuPrincipal.class);
                startActivity(intent);
            }
        });

        currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
            actionBar = getSupportActionBar();
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);

        }

        txtUser.addTextChangedListener(new TextWatcher() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (currentapiVersion>=20){
                    actionBar.hide();
                    getWindow().setStatusBarColor(Color.parseColor("#fc0101"));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
