package pe.com.filtroslys.www.extranetapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Bienvenido");
        Intent  intent  = new Intent(MainActivity.this , MenuPrincipal.class);
        startActivity(intent);
    }
}
