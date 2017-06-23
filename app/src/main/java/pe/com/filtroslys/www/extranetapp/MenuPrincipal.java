package pe.com.filtroslys.www.extranetapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Model.CAccesos;
import Task.HtmlPortalTask;
import Task.ListaAccesosTask;
import Util.Constantes;

public class MenuPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //int ResultCode = 1 ;
   // String  nomb;
    String DniUserApp;
    SharedPreferences preferences;
    WebView webPortal;
    ImageView  imgProfile ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        setTitle("Menú de opciones");
        preferences = PreferenceManager.getDefaultSharedPreferences(MenuPrincipal.this);
        String txtNom  =preferences.getString("NOMBRE",null);
        String Correo = preferences.getString("MAIL",null);
         DniUserApp = preferences.getString("DNI",null);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        webPortal = (WebView) findViewById(R.id.webviewPortal);
        LoadPortal();
        //WebView mWebView=(WebView)findViewById(R.id.mWebView);

       /* webPortal.loadUrl("http://www.google.com");
        webPortal.getSettings().setJavaScriptEnabled(true);
        webPortal.getSettings().setSaveFormData(true);
        webPortal.getSettings().setBuiltInZoomControls(true);
        webPortal.setWebViewClient(new WebViewClient()); */

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent  = new Intent(MenuPrincipal.this , LectorQRActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu m = navigationView.getMenu();
        LoadNavDrawMenu(m);
        CreateCustomToast("BIENVENIDO "+ txtNom+".", Constantes.icon_succes,Constantes.layout_success);
        View header=navigationView.getHeaderView(0);
/*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        TextView name = (TextView)header.findViewById(R.id.txtUserNav);
        TextView  email = (TextView)header.findViewById(R.id.txtMailNav);
        imgProfile=  (ImageView) header.findViewById(R.id.imgProfile);
        name.setText(txtNom);
        email.setText(Correo);
        ObtenerPermisios();

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // ObtenerImagenPerfil();
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id==11000){ //  registro qr
            Intent  intent  = new Intent(MenuPrincipal.this , LectorQRActivity.class);
            startActivity(intent);
        }
        else if (id==12000){ //puntos acumulados
            Intent  intent  = new Intent(MenuPrincipal.this , VisorPuntosAcumulados.class);
            startActivity(intent);
        }
        else if (id==13000){
            Intent  intent  = new Intent(MenuPrincipal.this , PremiosReglas.class);
            intent.putExtra("Pagina", "Premios");
            startActivity(intent);
        }
        else if (id==14000){
            Intent  intent  = new Intent(MenuPrincipal.this , PremiosReglas.class);
            intent.putExtra("Pagina", "Reglas");
            startActivity(intent);
        }
        else if (id==15000){// catalago filtros
            Intent  intent  = new Intent(MenuPrincipal.this , BuscarFiltro.class);
            startActivity(intent);
        }
        else if (id==999998){ // salir
            Intent intent = new Intent(MenuPrincipal.this,Login.class);
            startActivity(intent);
        }




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public   void  LoadNavDrawMenu (Menu menu){
        ArrayList<CAccesos> listAcc = new ArrayList<>();
        AsyncTask<String,String,ArrayList<CAccesos>> asyncTaskAccesos;
        ListaAccesosTask listaAccesosTask = new ListaAccesosTask();


        try {
            asyncTaskAccesos = listaAccesosTask.execute(DniUserApp);
            listAcc = (ArrayList<CAccesos>) asyncTaskAccesos.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (listAcc != null  && listAcc.size()>0)
        {

            ArrayList<CAccesos> ListNivel1 = new ArrayList<>();
            ArrayList<CAccesos> ListNivel2 = new ArrayList<>();

            for (int i = 0 ; i < listAcc.size();i++){
                CAccesos item  = listAcc.get(i);
                if (item.getNivel2()== 0 && item.getNivel3() == 0 && item.getNivel4() == 0 && item.getNivel5()==0){

                    ListNivel1.add(item);
                }
                else    if ( item.getNivel3() == 0 && item.getNivel4() == 0 && item.getNivel5()==0){

                    ListNivel2.add(item);
                }


            }

            if (ListNivel1.size()>0){

               /* Menu cM = m.addSubMenu(1,1,1,"Gana Con Lys");
                cM.add(1,2,2,"Escanear Codigo").setIcon(R.drawable.ic_search_black_24dp);
                cM.add(1,3,3,"Escanear Codigo").setIcon(R.drawable.ic_search_black_24dp);*/


                for (int  j  = 0  ;   j < ListNivel1.size() ; j++){
                    CAccesos n1 =  ListNivel1.get(j);

                    Menu  m  = menu.addSubMenu(n1.getNivelGN(),n1.getNivelGN(),j,n1.getDescripcion());

                    for (int k  = 0 ;  k < ListNivel2.size()  ; k++){

                        CAccesos n2 = ListNivel2.get(k);
                        m.add(n1.getNivelGN(),n2.getNivelGN(),k,n2.getDescripcion()).setIcon(SelectIconMenu(n2.getNivelGN()));

                    }

                }

            }

            Menu mn = menu.addSubMenu(999999,999999,99,"Atajos");
            menu.add(999999,999998,99,"SALIR").setIcon(SelectIconMenu(999998));


        }



    }



    public  int SelectIconMenu  (int NivelGN){
        int icon =  R.drawable.icn_list24;
        switch (NivelGN){

            case  11000 :
                icon  = R.drawable.ic_search_black_24dp;
                break;
            case 12000:
                  icon = R.drawable.icn_acumulado24;
                break;
            case 13000:
                icon = R.drawable.icn_premio24;
                break;
            case 14000:
                icon = R.drawable.icn_reglas24;
                break;
            case  15000:
                icon = R.drawable.icn_catalogo24_2;
                break;
            case  999998 :
                icon = R.drawable.icn_apagar24;


        }
        Log.i("Id Item Menu" , String.valueOf(NivelGN));

        return  icon;
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
        Toast toast = new Toast(MenuPrincipal.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();


    }

    public  void  LoadPortal (){
        webPortal.loadUrl(Constantes.UrlWS + "/" + Constantes.PortalWebPage+"?dni="+DniUserApp);
        webPortal.getSettings().setJavaScriptEnabled(true);
        webPortal.getSettings().setSaveFormData(true);
        webPortal.getSettings().setBuiltInZoomControls(true);
        webPortal.setWebViewClient(new WebViewClient());
        //  GuardarHtml();

    }


    public void   GuardarHtml (){
        String resultHtml   = null ;
        AsyncTask<String,String,String> asyncTaskHtml ;
        HtmlPortalTask htmlPortalTask = new HtmlPortalTask();



        try {
            asyncTaskHtml = htmlPortalTask.execute();
            resultHtml = (String)  asyncTaskHtml.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (resultHtml!=null) {

            File path = new File(Environment.getExternalStorageDirectory() +File.separator+ Constantes.FolderApp);
            path.mkdirs();
            File file = new File(path, "portal.html");
            Log.i("Diirectorio file " , file.toString());
            FileOutputStream stream = null;
            try {
                 stream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                stream.write(resultHtml.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //***//*** Load HTML //****//***
            webPortal.loadUrl("file:///"+Environment.getExternalStorageDirectory().toString()+File.separator+ Constantes.FolderApp+File.separator+"portal.html");
            webPortal.getSettings().setJavaScriptEnabled(true);
            webPortal.getSettings().setSaveFormData(true);
            webPortal.getSettings().setBuiltInZoomControls(true);
            webPortal.setWebViewClient(new WebViewClient());

        }

    }

    public  void    ObtenerPermisios ()
    {

        if (ContextCompat.checkSelfPermission(MenuPrincipal.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED);
        {

            ActivityCompat.requestPermissions(MenuPrincipal.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        }
    }

    private void ObtenerImagenPerfil() {


         /*
     * WebView is created programatically here.
     *
     * @Here are the list of items to be shown in the list
     */
        final CharSequence[] items = { "Galeria", "Tomar Foto" };

        AlertDialog.Builder builder = new AlertDialog.Builder(MenuPrincipal.this);
        builder.setTitle("Seleccionar o tomar foto");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                if  (item == 0) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);//one can be replaced with any action code

                }

                if (item==1){

                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);//zero can be replaced with any act
                }
                // will toast your selection
               // showToast("Name: " + items[item]);


            }
        }).show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Uri foto",data.getDataString());
        switch(requestCode) {

            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    imgProfile.setImageURI(selectedImage);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    imgProfile.setImageURI(selectedImage);
                }
                break;


        }

    }
}
