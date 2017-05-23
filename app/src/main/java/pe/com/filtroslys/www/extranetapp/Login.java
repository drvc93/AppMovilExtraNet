package pe.com.filtroslys.www.extranetapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import junit.framework.TestResult;

public class Login extends AppCompatActivity {

    TextView   txtTegistrar  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Registrar Usuario");
        txtTegistrar = (TextView)  findViewById(R.id.txtRegistrate);

        txtTegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this , RegistrarUsuario.class);
                startActivity(intent);
            }
        });
    }
}
