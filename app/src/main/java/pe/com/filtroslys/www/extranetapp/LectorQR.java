package pe.com.filtroslys.www.extranetapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.android.gms.drive.internal.StringListResponse;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class LectorQR extends AppCompatActivity {

    // creo el detector qr
    BarcodeDetector  barcodeDetector;
    // creo la camara fuente
    CameraSource cameraSource;
    SurfaceView cameraView;
    String coderes = "";
    int ResultCode ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lector_qr);
        setTitle("Lector QR");
        Intent intent = new Intent(getApplicationContext(), ScanQRActivity.class);
       //  ResultCode = 4545; // Esto puede ser cualquier código.
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(ResultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("QRCode");
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult





}

