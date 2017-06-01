package pe.com.filtroslys.www.extranetapp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.SyncStateContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import Task.InsertUserBDTempTask;
import Task.TransferirUsuarioTask;
import Task.VerificarExisteUsuarioTask;
import Util.Constantes;

public class RegistrarUsuario extends AppCompatActivity {

    EditText txtFecha, txtDni;
    EditText txtNom , txtApellidos, txtCorreo, txtCelular, txtRucEmp , txtNomEmp;
    String FechaFinal;
    Button btnRegUser ;
    int currentapiVersion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        txtFecha = (EditText)findViewById(R.id.txtFechaNacimiento);
        txtDni = (EditText)findViewById(R.id.txtDniUs);
        txtNom =  (EditText)findViewById(R.id.txtNombreUs);
        txtApellidos  =(EditText) findViewById(R.id.txtApellidos);
        txtCorreo = (EditText) findViewById(R.id.txtMailUs);
        txtCelular = (EditText) findViewById(R.id.txtCelUs);
        txtRucEmp = (EditText) findViewById(R.id.txtRucEmp);
        txtNomEmp = (EditText)findViewById(R.id.txtNomEmp) ;
        btnRegUser = (Button)findViewById(R.id.btnRegUser);
        setTitle("Registrar Usuario");

        currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            ActionBar actionBar = getSupportActionBar();
            actionBar = getSupportActionBar();
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.hide();

        }

        txtFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelecFecha();
            }
        });

        btnRegUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sVerUser = VerificarUsuario();
                if (sVerUser.equals("NO")){
                   CreateCustomToast("Usuario ya existe." , Constantes.icon_warning,Constantes.layot_warning);
                }
                else {

                    AlertDialog();
                    //ValidarDatosIngresados();
                }
            }
        });
    }


    public String  VerificarUsuario (){
        String sRes = "";
        AsyncTask<String,String,String> asyncTask;
        VerificarExisteUsuarioTask verificarExisteUsuarioTask = new VerificarExisteUsuarioTask();


        try {
            asyncTask = verificarExisteUsuarioTask.execute(txtDni.getText().toString());
            sRes = asyncTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return  sRes;

    }

    public  void ValidarDatosIngresados (){
        String msn = "OK";

        String   dni ,  nombre , apellido , correo , telefono , empruc , empnom , fechanacimiento ;

        dni = txtDni.getText().toString();
        nombre = txtNom.getText().toString();
        apellido = txtApellidos.getText().toString();
        correo = txtCorreo.getText().toString();
        telefono =  txtCelular.getText().toString();
        empruc = txtRucEmp.getText().toString();
        empnom  = txtNomEmp.getText().toString();
        fechanacimiento  = txtFecha.getText().toString();

        if (TextUtils.isEmpty(dni)){

            msn = "Debe ingresar un DNI valido." ;
        }
        else if (dni.length()<8){

            msn = "El DNI debe contener 8 números." ;
        }
        else if (TextUtils.isEmpty(nombre)){

            msn = "Debe ingresar un nombre valido." ;
        }
        else if (TextUtils.isEmpty(apellido)){

            msn = "Debe ingresar un apellido valido.";
        }

        else if (TextUtils.isEmpty(correo)){

            msn = "Debe ingresar un correo valido.";
        }

        else if (TextUtils.isEmpty(telefono)){

            msn = "Debe ingresar un telefono valido.";
        }

        else if (TextUtils.isEmpty(empruc)){

            msn = "Debe ingresar un RUC de empresa  valido.";
        }

        else if (TextUtils.isEmpty(empnom)){

            msn = "Debe ingresar un nombre de empresa  valido.";
        }

        else if (empruc.length()<11){
            msn = "El ruc de la empresa debe tener al menos 11 caracteres";
        }

        else if (TextUtils.isEmpty(fechanacimiento)){

            msn = "Debe ingresar una fecha de nacimiento  valida.";
        }

        else if (CompararFechas(fechanacimiento).equals("ERROR")){

            msn = "La fecha de nacimiento  es mayor a la fecha actual.";
        }

        if (msn.equals("OK")){

             RegistrarUsuario(dni,nombre,apellido,correo,telefono,empruc,empnom,fechanacimiento);
        }
        else {

            CreateCustomToast(msn,Constantes.icon_warning,Constantes.layot_warning);
        }

    }

    public  void  RegistrarUsuario ( String   dni , String nombre ,String apellido ,String correo , String telefono ,
                                      String empruc , String empnom , String fechanacimiento ){

        String resul = "" ;
        AsyncTask<String,String,String> asyncTask ;
        InsertUserBDTempTask insertUserBDTempTask =  new InsertUserBDTempTask();

        Log.i("dni",dni);
        Log.i("nombre",nombre);
        Log.i("apellido",apellido);
        Log.i("fechanac",fechanacimiento);
        Log.i("correo",correo);
        Log.i("empruc",empruc);
        Log.i("empnom",empnom);
        Log.i("telfono",telefono);

        try {
            asyncTask =  insertUserBDTempTask.execute(dni , nombre , apellido ,fechanacimiento , correo , telefono , empruc , empnom);
            resul  = (String) asyncTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Log.i("Correlativo" , resul);
        if (resul.equals("-1")){

            CreateCustomToast("No se puedo registrar al usuario." , Constantes.icon_error,Constantes.layout_error);

        }
        else if (Integer.valueOf(resul)>0){
            String resTrans  = "";
            AsyncTask<String,String,String> AsyncTrans   ;
            TransferirUsuarioTask   transferirUsuarioTask = new TransferirUsuarioTask();



            try {
                AsyncTrans = transferirUsuarioTask.execute("RU",resul);
                resTrans = (String) AsyncTrans.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if (resTrans.equals("OK")){

                CreateCustomToast("Se registro correctamente el usuario ,  le enviaremos sus datos de acceso a su correo por favor verifique.", Constantes.icon_succes,Constantes.layout_success);
                try {
                    Thread.sleep(3000);
                    onBackPressed();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            else {
                CreateCustomToast("Hubo un error al momentro de registrar : " + resTrans,Constantes.icon_error,Constantes.layout_error);
            }

        }




    }

    public void SelecFecha() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.date_picker_layout, null, false);
        final DatePicker myDatePicker = (DatePicker) view.findViewById(R.id.myDatePicker);
        new AlertDialog.Builder(RegistrarUsuario.this).setView(view)
                .setTitle("Seleccionar Fecha")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    // @TargetApi(11)
                    public void onClick(DialogInterface dialog, int id) {

                        int month = myDatePicker.getMonth() + 1;
                        int day = myDatePicker.getDayOfMonth();
                        int year = myDatePicker.getYear();
                        String mes = String.format("%02d", month);

                        String dia = String.format("%02d", day);
                        txtFecha.setText(dia + "/" + mes + "/" + String.valueOf(year));

                        FechaFinal = mes + "/" + dia + "/" + String.valueOf(year);
                        dialog.cancel();

                    }

                }).show();
    }


    public  void  AlertDialog (){


        new AlertDialog.Builder(RegistrarUsuario.this)
                    .setTitle("Aviso")
                    .setMessage("¿Desea continuar el registro?")
                    .setIcon(R.drawable.icn_alert24)
                    .setPositiveButton("SI",
                            new DialogInterface.OnClickListener() {
                              //  @TargetApi(11)
                                public void onClick(DialogInterface dialog, int id) {
                                    ValidarDatosIngresados();
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

    public void CreateCustomToast(String msj, int icon, int backgroundLayout) {

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
        Toast toast = new Toast(RegistrarUsuario.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();


    }

    public  String CompararFechas (String fechaNacimiento) {
        String res = "" ;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String getCurrentDateTime = sdf.format(c.getTime());

        if ((getCurrentDateTime.compareTo(fechaNacimiento))<0){
            res  = "ERROR";

        }
        else {

            res  = "OK";
        }

        return  res ;
    }

}
