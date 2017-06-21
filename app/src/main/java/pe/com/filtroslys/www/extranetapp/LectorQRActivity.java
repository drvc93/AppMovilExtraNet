package pe.com.filtroslys.www.extranetapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import Util.Constantes;

public class LectorQRActivity extends AppCompatActivity {


     public  static TextView txtresultCode ;
     private Button btnScan , btnNext  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lector_qr);
        txtresultCode = (TextView)  findViewById(R.id.txtCodeQR);
        btnScan = (Button)findViewById(R.id.btnScanQr);
        btnNext = (Button)findViewById(R.id.btnSiguienteP);
        setTitle("Escanear codigo QR.");

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent    = new Intent(LectorQRActivity.this , ReadScanQR.class);
                intent.putExtra("Activ" , "Escaner");
                startActivity(intent);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SiguientePaso();

            }
        });
    }

    public  void  SiguientePaso (){


        String cod = txtresultCode.getText().toString();
        cod = "LF1A-649";

        if (cod.equals("Codigo QR")|| TextUtils.isEmpty(cod) ==true){

            CreateCustomToast("Codigo QR invalido, intente de nuevo por favor.", Constantes.icon_warning,Constantes.layot_warning);
        }
        else {
            Intent intent = new Intent(LectorQRActivity.this, RegistroCodQr.class);
            intent.putExtra("Codigo", cod);
            startActivity(intent);
        }
    }

    public void CreateCustomToast(String msj, int icon, int backgroundLayout)
    {

        LayoutInflater infator = getLayoutInflater();
        View layout = infator.inflate(R.layout.toast_alarm_success, (ViewGroup) findViewById(R.id.toastlayout));
        TextView toastText = (TextView) layout.findViewById(R.id.txtDisplayToast);
        ImageView imgIcon = (ImageView) layout.findViewById(R.id.imgToastSucc);
        LinearLayout parentLayout = (LinearLayout) layout.findViewById(R.id.toastlayout);
        imgIcon.setImageResource(icon);
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            parentLayout.setBackgroundDrawable(getResources().getDrawable(backgroundLayout));
        } else {
            parentLayout.setBackground(getResources().getDrawable(backgroundLayout));
        }


        toastText.setText(msj);
        Toast toast = new Toast(LectorQRActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();


    }
}
