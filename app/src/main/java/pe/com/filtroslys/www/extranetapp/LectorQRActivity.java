package pe.com.filtroslys.www.extranetapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LectorQRActivity extends AppCompatActivity {


     public  static TextView txtresultCode ;
     private Button btnScan  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lector_qr);
        txtresultCode = (TextView)  findViewById(R.id.txtCodeQR);
        btnScan = (Button)findViewById(R.id.btnScanQr);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent    = new Intent(LectorQRActivity.this , ReadScanQR.class);
                startActivity(intent);
            }
        });
    }
}
