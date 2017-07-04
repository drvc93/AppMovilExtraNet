package pe.com.filtroslys.www.extranetapp;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
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

import com.github.florent37.viewtooltip.ViewTooltip;

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
    SharedPreferences preferences;
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
        txtNom.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        txtApellidos.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        txtNomEmp.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        preferences = PreferenceManager.getDefaultSharedPreferences(RegistrarUsuario.this);
        setTitle("Registrar Usuario");

        currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            ActionBar actionBar = getSupportActionBar();
            //actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayUseLogoEnabled(true);
                actionBar.setDisplayShowCustomEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.hide();
            }


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
        } catch (InterruptedException | ExecutionException e) {
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
           // ed = txtDni;
        }
        else if (dni.length()<8){

            msn = "El DNI debe contener 8 números." ;
           // ed = txtDni;
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


        if (msn.equals("OK")){

             RegistrarUsuario(dni,nombre,apellido,correo,telefono,empruc,empnom,fechanacimiento);
        }
        else {

            CreateCustomToast(msn,Constantes.icon_warning,Constantes.layot_warning);
          //  SetToolTip(ed);
        }

    }

    public  void  RegistrarUsuario ( String   dni , String nombre ,String apellido ,String correo , String telefono ,
                                      String empruc , String empnom , String fechanacimiento ){

        String resul = "" ;
        AsyncTask<String,String,String> asyncTask ;
        InsertUserBDTempTask insertUserBDTempTask =  new InsertUserBDTempTask();
        ProgressDialog progressDialog= new ProgressDialog(RegistrarUsuario.this);
        progressDialog.setTitle("Registrando...");
        progressDialog.setMessage("Transfiriendo datos espere por favor...");
        progressDialog.setIcon(R.drawable.sinc_24);

        insertUserBDTempTask.context = RegistrarUsuario.this;
        insertUserBDTempTask.pd = progressDialog;
       //  insertUserBDTempTask.ShowDialog();

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
            resul  = asyncTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        Log.i("Correlativo" , resul);
        if (resul.equals("-1")){

            CreateCustomToast("No se puedo registrar al usuario." , Constantes.icon_error,Constantes.layout_error);

        }
        else if (Integer.valueOf(resul)>0){
            String resTrans  = "";
            //MyApplication appc   = (MyApplication)this.getApplication();
            AsyncTask<Void,String,Void> AsyncTrans   ;
            TransferirUsuarioTask   transferirUsuarioTask = new TransferirUsuarioTask();
            transferirUsuarioTask.context = RegistrarUsuario.this;

            transferirUsuarioTask.correlativo=resul;
            transferirUsuarioTask.tipo = "RU";
           // transferirUsuarioTask.preferences   = preferences;

            transferirUsuarioTask.pd =  progressDialog;
          //  transferirUsuarioTask.myapp = appc  ;
            transferirUsuarioTask.ToastSuccess = CreateAndGetToast("Se  registro correctamente", Constantes.icon_succes,Constantes.layout_success);
            transferirUsuarioTask.ActiviRegUser = this;

            try {
                AsyncTrans = transferirUsuarioTask.execute();

                //Log.i("MYapp Var" , resTrans);
            } catch (Exception e) {
                e.printStackTrace();
            }

           /* if (resTrans.equals("OK")){

                CreateCustomToast("Se registro correctamente el usuario ,  le enviaremos sus datos de acceso a su correo por favor verifique.", Constantes.icon_succes,Constantes.layout_success);
                Notif();

            }

            else {
                CreateCustomToast("Hubo un error al momentro de registrar : " + resTrans,Constantes.icon_error,Constantes.layout_error);
            }*/

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
                                    dialog.cancel();
                                   // ShowProgressDialog();
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

     public  Toast CreateAndGetToast (String msj, int icon, int backgroundLayout){
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
         return toast;
        // toast.show();
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


    public  void  SetToolTip (EditText editText) {
        ViewTooltip
                .on(editText)
                .autoHide(true, 1500)
                .position(ViewTooltip.Position.RIGHT)
                .text("Ingrese el dato nuevamente")
                .show();
    }

    public  void  Notif (){
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] pattern = {500,500,500,500,500,500,500,500,500};
        // NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_EMAIL);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
        Notification n  = new Notification.Builder(this)
                .setContentTitle("Datos de acceso.")
                .setContentText("Revise sus datos de acceso en su correo.")
                .setSmallIcon(android.R.drawable.sym_action_email)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setVibrate(pattern)

               // .addAction(android.R.drawable.sym_action_email, "Call", pIntent)
               // .addAction(R.drawable.icn_success, "More", pIntent)
                .addAction(android.R.drawable.sym_action_email, "Revisar correo.", pIntent).build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);

    }

    public void ShowProgressDialog  (){

        final ProgressDialog myPd_ring=ProgressDialog.show(RegistrarUsuario.this, "Please wait", "Loading please wait..", true);
        myPd_ring.setCancelable(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try
                {
                    Thread.sleep(5000);
                }catch(Exception e){}
                myPd_ring.dismiss();
            }
        }).start();

    }

}
