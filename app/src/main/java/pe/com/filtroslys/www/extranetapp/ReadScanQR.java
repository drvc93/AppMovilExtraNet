package pe.com.filtroslys.www.extranetapp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ReadScanQR extends AppCompatActivity  implements  ZXingScannerView.ResultHandler  {

    private ZXingScannerView mScannerView;
    String Act;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
        setTitle("Escanear codigo QR.");
        Act = getIntent().getExtras().getString("Activ").toString();
       // ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();
        // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        // Log.v("tag", rawResult.getText()); // Prints scan results
        // Log.v("tag", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
        Log.i("From Activity", Act);
        if (Act.equals("Escaner"))
        {
            LectorQRActivity.txtresultCode.setText(rawResult.getText());
            onBackPressed();
        }

        else  if (Act.equals("Catalogo"))
        {
            int  index = 0;
            String   codFinal;
            String   cod_Entery  = rawResult.getText().toString();
            index = cod_Entery.indexOf("-");

            if (index>0){

                codFinal = cod_Entery.substring(0,index);
            }
            else {

                codFinal  = "No se encontro codigo.";
            }
            BuscarFiltro.txtFiltroCat.setText(codFinal);
            // BuscarFiltro.btnVerCatalogo.setEnabled(true);
            onBackPressed();
        }
        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
    }
}
