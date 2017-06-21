package pe.com.filtroslys.www.extranetapp;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import Util.Constantes;

public class VisorPuntosAcumulados extends AppCompatActivity {
    String dni = "" ;
    WebView  webVisorPuntos ;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visor_puntos_acumulados);
        webVisorPuntos  =  (WebView)findViewById(R.id.webVisorPuntos);
        preferences = PreferenceManager.getDefaultSharedPreferences(VisorPuntosAcumulados.this);
         dni  =preferences.getString("DNI",null);
        setTitle("Reporte de puntos acumulados.");
        LoadVisor();
       // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }


    public  void  LoadVisor (){
        webVisorPuntos.loadUrl(Constantes.UrlWS + "/" + Constantes.ReportePuntosWebpage +"?dni="+ dni);
        webVisorPuntos.getSettings().setJavaScriptEnabled(true);
        webVisorPuntos.getSettings().setSaveFormData(true);
        webVisorPuntos.getSettings().setBuiltInZoomControls(true);
        webVisorPuntos.setWebViewClient(new WebViewClient());
        //  GuardarHtml();

    }

}
