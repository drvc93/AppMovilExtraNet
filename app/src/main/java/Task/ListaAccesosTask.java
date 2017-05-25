package Task;

import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import Model.CAccesos;
import Model.CUsuario;
import Util.Constantes;

/**
 * Created by dvillanueva on 24/05/2017.
 */

public class ListaAccesosTask extends AsyncTask<String,String,ArrayList<CAccesos>> {
    @Override
    protected ArrayList<CAccesos> doInBackground(String... strings) {

        ArrayList<CAccesos> result = new ArrayList<CAccesos>();

        final String NAMESPACE = Constantes.UrlWS;
        final String URL=NAMESPACE+Constantes.NameSpaceWS;
        final String METHOD_NAME = "ListaAccesos";
        final String SOAP_ACTION = NAMESPACE+METHOD_NAME;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("dni", strings[0]);



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
                CAccesos  a =  new CAccesos( );
                a.setNivel1( Integer.valueOf(ic.getProperty(0).toString()));
                a.setNivel2( Integer.valueOf(ic.getProperty(1).toString()));
                a.setNivel3( Integer.valueOf(ic.getProperty(2).toString()));
                a.setNivel4( Integer.valueOf(ic.getProperty(3).toString()));
                a.setNivel5( Integer.valueOf(ic.getProperty(4).toString()));
                a.setDescripcion( ic.getProperty(5).toString());
                a.setNivelGN( Integer.valueOf(ic.getProperty(6).toString()));


                result.add(a);

            }



        }
        catch (Exception e){

            String B = e.getMessage();
            Log.i("Error Lista Accesos ==>" , e.getMessage());

        }

//        Log.i("Size result array ==>" ,String.valueOf(result.length) );

        return result;
    }
}
