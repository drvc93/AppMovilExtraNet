package pe.com.filtroslys.www.extranetapp;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;

import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture;
import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever;

public class BarCodeVision extends AppCompatActivity implements BarcodeRetriever {

    private static final String TAG = "BarcodeMain";

    CheckBox fromXMl;
    SwitchCompat drawRect, autoFocus, supportMultiple, touchBack, drawText;
    String Act;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_barcode);

        final BarcodeCapture barcodeCapture = (BarcodeCapture) getSupportFragmentManager().findFragmentById(R.id.barcode);
        barcodeCapture.setRetrieval(this);
        barcodeCapture.setShowDrawRect(true);
        Act = getIntent().getExtras().getString("Activ").toString();
        fromXMl = (CheckBox) findViewById(R.id.from_xml);
        drawRect = (SwitchCompat) findViewById(R.id.draw_rect);
        autoFocus = (SwitchCompat) findViewById(R.id.focus);
        supportMultiple = (SwitchCompat) findViewById(R.id.support_multiple);
        touchBack = (SwitchCompat) findViewById(R.id.touch_callback);
        drawText = (SwitchCompat) findViewById(R.id.draw_text);

        findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fromXMl.isChecked()) {

                } else {
                    barcodeCapture.setShowDrawRect(drawRect.isChecked());
                    barcodeCapture.setSupportMultipleScan(supportMultiple.isChecked());
                    barcodeCapture.setTouchAsCallback(touchBack.isChecked());
                    barcodeCapture.shouldAutoFocus(autoFocus.isChecked());
                    barcodeCapture.setShouldShowText(drawText.isChecked());
                    barcodeCapture.refresh();
                }
            }
        });
    }

    @Override
    public void onRetrieved(final Barcode barcode) {
        Log.d(TAG, "Barcode read: " + barcode.displayValue);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /*AlertDialog.Builder builder = new AlertDialog.Builder(BarCodeVision.this)
                        .setTitle("code retrieved")
                        .setMessage(barcode.displayValue);
                builder.show();*/
                if (Act.equals("Catalogo")) {
                    int  index = 0;
                    String   codFinal;
                    String   cod_Entery  = barcode.displayValue;
                    index = cod_Entery.indexOf("-");

                    if (index>0){

                        codFinal = cod_Entery.substring(0,index);
                    }
                    else {

                        codFinal  = "No se encontro codigo.";
                    }
                    BuscarFiltro.txtFiltroCat.setText(codFinal);
                    // BuscarFiltro.btnVerCatalogo.setEnabled(true);
                    //onBackPressed();
                }
                else  if (Act.equals("Escaner")){

                    LectorQRActivity.txtresultCode.setText(barcode.displayValue);
                }
                onBackPressed();
            }
        });
    }

    @Override
    public void onRetrievedMultiple(Barcode barcode, List<BarcodeGraphic> list) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onRetrievedFailed(String s) {

    }
}
