package pe.com.filtroslys.www.extranetapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.TestResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import Model.CUsuario;
import Task.LoginUserTask;
import Util.Constantes;

public class Login extends AppCompatActivity {

    TextView   txtTegistrar  ;
    int currentapiVersion;
    EditText txtUser ,  txtPass ;
    ActionBar actionBar;
    Button btnLogin ;
    SharedPreferences preferences;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");

        txtTegistrar = (TextView)  findViewById(R.id.txtRegistrate);
        txtUser  =  (EditText) findViewById(R.id.txtUserName);
        txtPass = (EditText)findViewById(R.id.txtPass);
        btnLogin  = (Button)findViewById(R.id.btnLogin);
        preferences = PreferenceManager.getDefaultSharedPreferences(Login.this);

        txtTegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this , RegistrarUsuario.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               AutenticarUsuarioLogin(txtUser.getText().toString(),txtPass.getText().toString());
            }
        });

        ObtenerPermisios();
        currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
            actionBar = getSupportActionBar();
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);

        }

        txtUser.addTextChangedListener(new TextWatcher() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (currentapiVersion>=20){
                    actionBar.hide();
                    getWindow().setStatusBarColor(Color.parseColor("#fc0101"));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public  void  AutenticarUsuarioLogin (String user , String clave){
        ArrayList<CUsuario>  listUs = null;
        AsyncTask<String,String,ArrayList<CUsuario>> asyncTask;
        LoginUserTask   loginUserTask = new LoginUserTask();



        try {
            asyncTask = loginUserTask.execute(user,clave);
            listUs = (ArrayList<CUsuario>) asyncTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (listUs==null || listUs.size()<= 0){

            CreateCustomToast("Usuario o clave incorrecto. por favor intente nuevamente.", Constantes.icon_error,Constantes.layout_error);
        }
        else if (listUs.size()>0){
            CUsuario u = listUs.get(0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("DNI", u.getDni());
            editor.putString("EMPRUC",u.getEmpruc());
            editor.putString("NOMBRE",u.getNombre());
            editor.putString("APELLIDO",u.getApellido());
            editor.putString("MAIL",u.getMail());
            editor.putString("TELEFONO",u.getTelefono());
            editor.commit();


            Intent intent = new Intent(Login.this , MenuPrincipal.class);
            intent.putExtra("nombre",u.getNombre());
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
        Toast toast = new Toast(Login.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();


    }

    public  void    ObtenerPermisios ()
    {

        if (ContextCompat.checkSelfPermission(Login.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED);
        {

            ActivityCompat.requestPermissions(Login.this, new String[] {Manifest.permission.CAMERA}, 1);

          }
    }
}
