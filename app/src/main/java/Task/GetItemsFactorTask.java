package Task;

import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import Model.CAccesos;
import Model.ItemFact;
import Util.Constantes;

/**
 * Created by dvillanueva on 26/06/2017.
 */

public class GetItemsFactorTask extends AsyncTask<String ,String , ArrayList<ItemFact>> {
    @Override
    protected ArrayList<ItemFact> doInBackground(String... strings) {
        ArrayList<ItemFact> result = new ArrayList<ItemFact>();

        final String NAMESPACE = Constantes.UrlWS;
        final String URL=NAMESPACE+Constantes.NameSpaceWS;
        final String METHOD_NAME = "GetItemsFactor";
        final String SOAP_ACTION = NAMESPACE+METHOD_NAME;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("Dni", strings[0]);



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
                ItemFact  a =  new ItemFact( );
                a.setItem( ic.getProperty(0).toString());
                a.setPropaganda(ic.getProperty(1).toString());
                a.setFlagcontador(ic.getProperty(2).toString());
                a.setSegundos(Integer.valueOf(ic.getProperty(3).toString()));
                a.setDescPromo(ic.getProperty(4).toString());


                result.add(a);

            }



        }
        catch (Exception e){

            String B = e.getMessage();
            Log.i("Lista ItemsFact==>" , e.getMessage());

        }

//        Log.i("Size result array ==>" ,String.valueOf(result.length) );

        return result;
    }
}
