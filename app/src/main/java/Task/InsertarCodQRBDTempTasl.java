package Task;

import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import Util.Constantes;

/**
 * Created by dvillanueva on 24/05/2017.
 */

public class InsertarCodQRBDTempTasl extends AsyncTask<String,String,String> {
    @Override
    protected String doInBackground(String... strings) {
        String result ="-1";
        // String urlserver = params[2];
        final String NAMESPACE = Constantes.UrlWS;
        final String URL=NAMESPACE+ Constantes.NameSpaceWS;
        final String METHOD_NAME = "InsertarCodigoQR";
        final String SOAP_ACTION = NAMESPACE+METHOD_NAME;

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("dniQR", strings[0]);
        request.addProperty("codigoQR", strings[1]);
        request.addProperty("dniUF", strings[2]);
        request.addProperty("telefonoUF", strings[3]);
        request.addProperty("correoUF", strings[4]);



        SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transporte = new HttpTransportSE(URL);

        try
        {
            transporte.call(SOAP_ACTION, envelope);

            SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
            String res = resultado_xml.toString();

            result=res;


        }
        catch (Exception e)
        {
            result = "-1";
        }
        Log.i("Codigo QR Registrado  => ", result);
        return result;
    }
}
