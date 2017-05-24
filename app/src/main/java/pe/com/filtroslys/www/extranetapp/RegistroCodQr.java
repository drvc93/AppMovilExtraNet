package pe.com.filtroslys.www.extranetapp;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import Task.InsertarCodQRBDTempTasl;
import Task.TransferirUsuarioTask;
import Util.Constantes;

public class RegistroCodQr extends AppCompatActivity {

    EditText txtNom , txtCodigo , txtDniRef, txtMailRef , txtCelularRef;
    Button btnEnviar;
    String DniUserApp ;

    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_cod_qr);
        txtNom = (EditText)findViewById(R.id.txtNomQr);
        txtCodigo = (EditText) findViewById(R.id.txtCodQrItem);
        btnEnviar = (Button)findViewById(R.id.btnEnviar);
        txtDniRef = (EditText) findViewById(R.id.txtDniRef);
        txtMailRef = (EditText) findViewById(R.id.txtMailQR);
        txtCelularRef = (EditText)findViewById(R.id.txtCelQR);
        setTitle("Registro Codigo QR.");

        preferences = PreferenceManager.getDefaultSharedPreferences(RegistroCodQr.this);
        String nombre  =preferences.getString("NOMBRE",null);
        String apellido =  preferences.getString("APELLIDO",null);
        DniUserApp = preferences.getString("DNI",null);
        String Cod = getIntent().getExtras().getString("Codigo");

        txtNom.setText(nombre  + " " +  apellido);
        txtCodigo.setText(Cod);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog();

            }
        });
    }


    public  void  AlertDialog ()
    {


        new AlertDialog.Builder(RegistroCodQr.this)
                .setTitle("Aviso")
                .setMessage("¿Esta seguro que desea regitrar el codigo QR?")
                .setIcon(R.drawable.icn_alert24)
                .setPositiveButton("SI",
                        new DialogInterface.OnClickListener() {
                            //  @TargetApi(11)
                            public void onClick(DialogInterface dialog, int id) {
                                ValidarDatos();
                            }
                        })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    // @TargetApi(11)
                    public void onClick(DialogInterface dialog, int id) {
                        //showToast("Mike is not awesome for you. :(");
                        dialog.cancel();
                    }
                }).show();

    }

    public  void  ValidarDatos (){
        String sMsj = "OK";
        String dniref = txtDniRef.getText().toString();
        String correoref = txtMailRef.getText().toString();
        String celRef = txtCelularRef.getText().toString();

        if (TextUtils.isEmpty(dniref) == true) {
           sMsj = "Ingrese un DNI de referencia valido.";
        }
        else  if (TextUtils.isEmpty(correoref) == true) {
            sMsj = "Ingrese un correo valido.";
        }

        else  if (dniref.length()<8) {
            sMsj = "El DNI de referencia debe contener 8 digitos.";
        }

        else if (TextUtils.isEmpty(celRef) == true) {

            sMsj= "Ingrese un  número de celalar valido";
        }

        if (sMsj.equals("OK")){

            InsertarCodigoQRTemp(dniref,correoref,celRef);
        }
        else {

            CreateCustomToast("No se puedo compleatr el registro intentelo de nuevo", Constantes.icon_warning,Constantes.layot_warning);
        }


    }

    public  void  InsertarCodigoQRTemp(String DniRef , String CorreoRef,String TelefonoRef){
        String result = "";
        String codFiltroQR =  txtCodigo.getText().toString();
        AsyncTask<String,String,String> asyncTaskQR ;
        InsertarCodQRBDTempTasl  insertarCodQRBD = new InsertarCodQRBDTempTasl();


        try {
            asyncTaskQR = insertarCodQRBD.execute(DniUserApp, codFiltroQR,DniRef,TelefonoRef,CorreoRef);
            result = (String) asyncTaskQR.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (result.equals("-1")){
            CreateCustomToast("No se pudo regitrar el codigo QR.",Constantes.icon_error,Constantes.layout_error);
        }
        else if (Integer.valueOf(result)>0) {
            String resTrasnf = "";
            AsyncTask<String,String,String> asyncTaskTransQR ;
            TransferirUsuarioTask transferirUsuarioTask = new TransferirUsuarioTask();


            try {
                asyncTaskTransQR = transferirUsuarioTask.execute("EQ",result);
                resTrasnf = (String) asyncTaskTransQR.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if (resTrasnf.equals("OK")){

                CreateCustomToast("Se regitro correctamente el codigo QR, en  unos minutos se le estara enviando un correo.",Constantes.icon_succes,Constantes.layout_success);
                try {
                    Thread.sleep(3000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                onBackPressed();

            }
            else {

                CreateCustomToast(resTrasnf,Constantes.icon_error, Constantes.layot_warning);
            }
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
        Toast toast = new Toast(RegistroCodQr.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();


    }
}
