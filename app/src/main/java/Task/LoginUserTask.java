package Task;

import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import Model.CUsuario;
import Util.Constantes;

/**
 * Created by dvillanueva on 24/05/2017.
 */

public class LoginUserTask extends AsyncTask<String,String , ArrayList<CUsuario>> {
    @Override
    protected ArrayList<CUsuario> doInBackground(String... strings) {
        ArrayList<CUsuario> result = new ArrayList<CUsuario>();

        final String NAMESPACE = Constantes.UrlWS;
        final String URL=NAMESPACE+Constantes.NameSpaceWS;
        final String METHOD_NAME = "AutenticarLogin";
        final String SOAP_ACTION = NAMESPACE+METHOD_NAME;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("user", strings[0]);
        request.addProperty("clave", strings[1]);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transporte = new HttpTransportSE(URL);
        transporte.debug = true;
        try
        {
            transporte.call(SOAP_ACTION, envelope);

            SoapObject resSoap =(SoapObject)envelope.getResponse();
            Log.i("Result Soap  LoginTaks ==>",resSoap.toString());
            int cont = resSoap.getPropertyCount();
            for (int i = 0 ; i<cont; i++){
                SoapObject ic=(SoapObject)resSoap.getProperty(i);
                CUsuario  u =  new CUsuario( );
                u.setDni( ic.getProperty(0).toString());
                u.setClave( ic.getProperty(1).toString());
                u.setNombre( ic.getProperty(2).toString());
                u.setApellido( ic.getProperty(3).toString());
                u.setFechaNacimiento( ic.getProperty(4).toString());
                u.setMail( ic.getProperty(5).toString());
                u.setTelefono( ic.getProperty(6).toString());
                u.setEmpruc( ic.getProperty(7).toString());
                u.setEmpnom( ic.getProperty(8).toString());

                result.add(u);

            }



        }
        catch (Exception e){

            String B = e.getMessage();
            Log.i("Error LoginTask ==>" , e.getMessage());

        }

//        Log.i("Size result array ==>" ,String.valueOf(result.length) );

        return result;
    }
}
