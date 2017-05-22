package pe.com.filtroslys.www.extranetapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static me.dm7.barcodescanner.core.CameraUtils.getCameraInstance;

/**
 * Created by dvillanueva on 22/05/2017.
 */

public class ScanQRActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private  int  RESULTOK = 1;
    android.hardware.Camera mCamera;
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();

        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
         mCamera = getCameraInstance();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();


    }

    @Override
    public void handleResult(Result rawResult) {

        Log.i("QRCode", rawResult.getText());

        mScannerView.resumeCameraPreview(this);
         mCamera.release();
      //  mPreview.getHolder().removeCallback(mPreview);

        Bundle conData = new Bundle();
        conData.putString("QRCode", rawResult.getText());
        Intent intent = new Intent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        finish();


    }
}
