package Task;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import Util.Constantes;
import pe.com.filtroslys.www.extranetapp.RegistrarUsuario;
import pe.com.filtroslys.www.extranetapp.RegistroCodQr;

/**
 * Created by dvillanueva on 23/05/2017.
 */

public class TransferirUsuarioTask extends AsyncTask<Void,String,Void>  {
    public Context context ;
  //  public  MyApplication myapp;
    public ProgressDialog pd;
    public  String tipo , correlativo;

    public String varReusl = "";
    public Resources res ;
    public Toast  ToastSuccess;
    public RegistrarUsuario ActiviRegUser ;
    public RegistroCodQr ActiviRegCodQr;

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        pd.dismiss();
        if (varReusl.equals("timeout")){
            varReusl = "OK";
        }
        if ( varReusl.equals("OK")){
              ToastSuccess.show();
        }
        else {
            Toast.makeText(context,"Error : "+ varReusl,Toast.LENGTH_LONG).show();
        }

        if (tipo.equals("RU") && varReusl.equals("OK")){
            ActiviRegUser.Notif();
            ActiviRegUser.finish();
        }
        else if (tipo.equals("EQ") && varReusl.equals("OK")){
            ActiviRegUser.Notif();
            ActiviRegCodQr.finish();
        }
    }


    @Override
    protected Void doInBackground(Void... params) {

        String result ="";
        // String urlserver = params[2];
        final String NAMESPACE = Constantes.UrlWS;
        final String URL=NAMESPACE+ Constantes.NameSpaceWS;
        final String METHOD_NAME = "TransferirUsuario";
        final String SOAP_ACTION = NAMESPACE+METHOD_NAME;

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("tipo", tipo);
        request.addProperty("correlativo", correlativo);
        Log.i("Tipo transferencia => ",tipo);
        Log.i("Correlativo  Inside Task =>" , correlativo);

        SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transporte = new HttpTransportSE(URL);

        try
        {
            transporte.call(SOAP_ACTION, envelope);

            SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
            String res = resultado_xml.toString();
            result = res;
           // myapp.setVarTransf(res);
            Log.i("Trasnferir usuario app var => ", tipo+ " - "  + result);
            varReusl = res;

            /*
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("msjTrans", res);
            editor.commit();
            Log.i("msj transf" , res);
            result=res;
            */

        }
        catch (Exception e)
        {
            result = e.getMessage();
        }


        Log.i("Trasnferir usuario  => ", tipo+ " - "  + result);
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //pd = new ProgressDialog(context);
        pd.setIndeterminate(true);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);

        pd.show();
    }




}
