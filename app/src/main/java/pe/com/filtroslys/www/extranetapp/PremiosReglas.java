package pe.com.filtroslys.www.extranetapp;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import Util.Constantes;

public class PremiosReglas extends AppCompatActivity {

    SharedPreferences preferences;
    String  pagina = "";
    WebView webViewPremiosReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premios_reglas);
        preferences = PreferenceManager.getDefaultSharedPreferences(PremiosReglas.this);
        String dni  =preferences.getString("DNI",null);
        pagina = getIntent().getExtras().getString("Pagina");
        webViewPremiosReg = (WebView)findViewById(R.id.webPremioReg);
        LoadPage(dni,pagina);
    }

    public  void LoadPage (String dni , String pagina){

        String url = "";
        if (pagina.equals("Premios")){
             setTitle("Premios Disponibles");
             url = Constantes.UrlWS + "/" + Constantes.PremiosWeb +"?dni="+ dni;
        }
        else  if (pagina.equals("Reglas")){
            url = Constantes.UrlWS + "/" + Constantes.ReglasWeb +"?dni="+ dni;
            setTitle("Reglas de Promociones.");
        }

        webViewPremiosReg.loadUrl(url);
        webViewPremiosReg.getSettings().setJavaScriptEnabled(true);
        webViewPremiosReg.getSettings().setSaveFormData(true);
        webViewPremiosReg.getSettings().setBuiltInZoomControls(true);
        webViewPremiosReg.setWebViewClient(new WebViewClient());


    }
}
