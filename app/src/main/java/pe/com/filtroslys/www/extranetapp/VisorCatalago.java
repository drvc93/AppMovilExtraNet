package pe.com.filtroslys.www.extranetapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import Util.Constantes;

public class VisorCatalago extends AppCompatActivity {

    String codFiltro ;
    WebView WCatalgo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visor_catalago);
        codFiltro = getIntent().getExtras().getString("codFiltro") ;
        setTitle("INFORMACION FILTRO : " + codFiltro);
        WCatalgo = (WebView)findViewById(R.id.webCatalogo);
        LoadCatalogo();
    }

    public  void  LoadCatalogo (){
        WCatalgo.loadUrl(Constantes.UrlWS + "/" + Constantes.CatalogoWebPage +"?item="+codFiltro);
        WCatalgo.getSettings().setJavaScriptEnabled(true);
        WCatalgo.getSettings().setSaveFormData(true);
        WCatalgo.getSettings().setBuiltInZoomControls(true);
        WCatalgo.setWebViewClient(new WebViewClient());
        //  GuardarHtml();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_act_scanqr, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.back) {
            onBackPressed();
        }
        return true;
    }
}
