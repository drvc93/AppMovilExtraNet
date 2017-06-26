package pe.com.filtroslys.www.extranetapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Model.ItemFact;
import Task.GetItemsFactorTask;
import Util.Constantes;

public class LectorQRActivity extends AppCompatActivity {


     public  static TextView txtresultCode ;
     private Button btnScan , btnNext  ;
    String dniUserApp = "";
    SharedPreferences preferences;
    long startTime = 0;
    String []  stringvalues;
    ArrayList<ItemFact> listItemsGlobal = new ArrayList<ItemFact>();
   // ListView  lv_ItemsFac;
    ArrayAdapter<String>  arrayAdapter;
    WebView wbItemsFact ;

    TextView  txtTimer ;
   /* Handler timerHandler = new Handler();
    Runnable  timerRunable = new Runnable() {
        @Override
        public void run() {
            int SegundosTranscurridos;
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            SegundosTranscurridos = seconds;
            int minutes = seconds / 60;
            seconds = seconds % 60;

            //txtTimer.setText(String.format("%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 500);
            RefreshContadores(SegundosTranscurridos);
        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lector_qr);
        txtresultCode = (TextView)  findViewById(R.id.txtCodeQR);
        btnScan = (Button)findViewById(R.id.btnScanQr);
        btnNext = (Button)findViewById(R.id.btnSiguienteP);
        preferences = PreferenceManager.getDefaultSharedPreferences(LectorQRActivity.this);
        dniUserApp = preferences.getString("DNI",null);
        wbItemsFact = (WebView)findViewById(R.id.webItemsFact);
        LoadPageItems(dniUserApp);
        //lv_ItemsFac =  (ListView) findViewById(R.id.lvItemFactor);
        // txtTimer = (TextView) findViewById(R.id.txtTimer);

        /*** Cargando Items con Facto  Promocion ***/
        /**
       // LoadItemsFacore();
        /**********************************************/

        setTitle("Escanear codigo QR.");

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent    = new Intent(LectorQRActivity.this , BarCodeVision.class);
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
        /*startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunable, 0);*/
    }

    public  void  SiguientePaso (){


        String cod = txtresultCode.getText().toString();
        //cod = "LF1A-649";

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

    public  void  LoadPageItems (String dni){
        wbItemsFact.loadUrl(Constantes.UrlWS + "/" + Constantes.FactorItemsPage +"?dni="+dni);
        wbItemsFact.getSettings().setJavaScriptEnabled(true);
        wbItemsFact.getSettings().setSaveFormData(true);
        wbItemsFact.getSettings().setBuiltInZoomControls(true);
        wbItemsFact.setWebViewClient(new WebViewClient());
        //  GuardarHtml();

    }

    public  void  RefreshContadores (int SegTransucrridos){

        if (listItemsGlobal.size()>0){

            for (int  i =  0 ;  i < listItemsGlobal.size(); i++)
            {
                String  item  , propaganda , flag ,descPromo;
                int  segundodur ;
                item = listItemsGlobal.get(i).getItem();
                propaganda   = listItemsGlobal.get(i).getPropaganda();
                flag =  listItemsGlobal.get(i).getFlagcontador();
                segundodur =  listItemsGlobal.get(i).getSegundos();
                descPromo = listItemsGlobal.get(i).getDescPromo();
                descPromo = descPromo.toUpperCase();
                segundodur  = segundodur - SegTransucrridos;
                if (flag.equals("S")){

                    stringvalues[i] = item + " : " + propaganda + " " +  FormatHHMMSS(segundodur) +" (Aplica a promocion  "+ descPromo.toUpperCase() +")";
                    Log.i("Segundos item"  + String.valueOf(i) , String.valueOf(segundodur));

                }
                else {
                    stringvalues[i] = item + " : " + propaganda +" (Aplica a promocion  "+ descPromo.toUpperCase() +")" ;

                }

            }

            arrayAdapter.notifyDataSetChanged();

        }

    }

    public  void LoadItemsFacore (){

        AsyncTask<String,String,ArrayList<ItemFact>> asyncTaskItemFact;
        GetItemsFactorTask getItemsFactorTask = new GetItemsFactorTask();
        try {
            asyncTaskItemFact = getItemsFactorTask.execute(dniUserApp);
            listItemsGlobal  = (ArrayList<ItemFact>) asyncTaskItemFact.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (listItemsGlobal.size()>0){

            stringvalues  =new String[listItemsGlobal.size()];
            for (int  i =  0 ;  i < listItemsGlobal.size(); i++)
            {
                String  item  , propaganda , flag, descPromo ;
                int  segundodur ;
                 item = listItemsGlobal.get(i).getItem();
                 propaganda   = listItemsGlobal.get(i).getPropaganda();
                flag =  listItemsGlobal.get(i).getFlagcontador();
                segundodur =  listItemsGlobal.get(i).getSegundos();
                descPromo = listItemsGlobal.get(i).getDescPromo();
                //Log.i("Desc Promo" , descPromo);
                descPromo = descPromo.toUpperCase();

                if (flag.equals("S")){

                    stringvalues[i] = item + " : " + propaganda + " " +  FormatHHMMSS(segundodur)+" (Aplica a promocion  "+ descPromo.toUpperCase() +")";
                    Log.i("Segundos item"  + String.valueOf(i) , String.valueOf(segundodur));

                }
                else {
                    stringvalues[i] = item + " : " + propaganda +" (Aplica a promocion  "+ descPromo +")";

                }

            }


           arrayAdapter = new ArrayAdapter<String>(LectorQRActivity.this,android.R.layout.simple_list_item_1,android.R.id.text1,stringvalues);
            //lv_ItemsFac.setAdapter(arrayAdapter);
        }


    }

    public  String FormatHHMMSS(int Segundos){
        int horas  =  0 ;
        int minutos  = 0 ;
        int segundos  = 0 ;
        String format = "%1$02d";
        horas  = (Segundos / 3600) ;
        minutos  = ((Segundos - (horas*3600))/60);
        segundos = ((Segundos - (horas*3600))%60);
        String  result  = "";
        Log.i("Horas",String.valueOf(horas));
        Log.i("Minutos",String.valueOf(minutos));
        Log.i("Segundos",String.valueOf(segundos));
        if (segundos>0) {
            result = " - Vence en " + String.format(format, horas) + ":" + String.format(format, minutos) + ":" + String.format(format, segundos) + " horas.";
        }
        else  if (segundos<=0){
            result = "";
        }
        return  result;

    }
}
