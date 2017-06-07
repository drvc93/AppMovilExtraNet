package pe.com.filtroslys.www.extranetapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import Task.VerificarCatalogoTask;
import Util.Constantes;

public class BuscarFiltro extends AppCompatActivity {

    public static EditText txtFiltroCat;
    public static Button btnVerCatalogo ;
    Button btnScan ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_filtro);
        setTitle("Escanear Filtro");
        btnScan = (Button)findViewById(R.id.btnEscanFiltro);
        btnVerCatalogo = (Button)findViewById(R.id.btnVerCatalogo);
        txtFiltroCat = (EditText) findViewById(R.id.txtFiltroCatalago);
        txtFiltroCat.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent    = new Intent(BuscarFiltro.this , ReadScanQR.class);
                intent.putExtra("Activ" , "Catalogo");
                startActivity(intent);
            }
        });

        btnVerCatalogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(txtFiltroCat.getText().toString())) {
                    CreateCustomToast("Debe Ingresar  un codigo de filtro en la casilla.",Constantes.icon_warning , Constantes.layot_warning);
                }

                else {

                    if (VerificarFiltroCat(txtFiltroCat.getText().toString()).equals("S")){

                        Intent intent = new Intent(BuscarFiltro.this, VisorCatalago.class);
                        intent.putExtra("codFiltro",txtFiltroCat.getText().toString());
                        startActivity(intent);

                    }
                    else {

                        CreateCustomToast("No existe este codigo de filtro en el catalogo." , Constantes.icon_warning,Constantes.layot_warning);
                    }


                }
            }
        });

    }


    public  String VerificarFiltroCat (  String filtro){
        String sResult = "" ;
        AsyncTask <String,String,String> asyncTaskVerificar ;
        VerificarCatalogoTask  verificarCatalogoTask  = new VerificarCatalogoTask();

        try {
            asyncTaskVerificar = verificarCatalogoTask.execute(filtro);
            sResult = (String)asyncTaskVerificar.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return  sResult;
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
        Toast toast = new Toast(BuscarFiltro.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();


    }


}
