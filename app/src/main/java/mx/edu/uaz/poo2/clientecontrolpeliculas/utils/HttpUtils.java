package mx.edu.uaz.poo2.clientecontrolpeliculas.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Clase utilitaria para comunicarse con servicios via HTTP
 */
public class HttpUtils {
    /**
     * Metodo para llamar a un servicio usando el metodo GET de HTTP
     * @param url   URL del servicio a contactar
     * @param acceptType  Especificacion del formato de datos a solicitar al servicio (application/json o application/xml)
     * @return   Regresa el texto devuelto por el servidor
     * @throws IOException  En caso de no poder contactar al servicio
     */
    public static String httpGet(String url,
                                 String acceptType)
            throws IOException {
        URL urlservicio;
        HttpURLConnection conn = null;
        InputStream is;
        String respuesta = null;

        urlservicio = new URL(url);
        conn = (HttpURLConnection) urlservicio.openConnection();
        if (acceptType != null)
            conn.setRequestProperty("Accept", acceptType);
        int codigo = conn.getResponseCode();
        if (codigo == HttpURLConnection.HTTP_OK) {
            is = conn.getInputStream();
            BufferedReader entrada = new BufferedReader(new InputStreamReader(is));
            respuesta = entrada.readLine();
        }
        else {
            throw new ServidorException(codigo);
        }
        return respuesta;
    }

    /**
     * Metodo para llamar a un servicio usando el metodo POST, PUT o DELETE de HTTP
     * @param url   URL del servicio a contactar
     * @param datos  String con los datos a enviar al servicios (en el formato adecuado)
     * @param contentType  Indicacion del formato de los de datos a enviar (application/json o application/xml)
     * @param method  Indicacion del metodo HTTP a usar  (PUT, DELETE o POST)
     * @return  Regresa el texto devuelto por el servidor
     * @throws IOException  En caso de no poder contactar al servicio
     */
    public static String httpPostPutDelete(String url,
                                           String datos,
                                           String contentType,
                                           String method)
            throws IOException {
        URL urlservicio;
        HttpURLConnection conn = null;
        OutputStream os;
        InputStream is;
        String respuesta = "false";

        urlservicio = new URL(url);
        conn = (HttpURLConnection) urlservicio.openConnection();
        conn.setRequestMethod(method);
        if (!method.equals("DELETE")) {
            conn.setDoOutput(true);
            byte info[] = datos.getBytes();
            if (contentType != null) {
                conn.setRequestProperty("Content-Type", contentType);
            }
            conn.setRequestProperty("Content-Length" ,
                    Integer.toString(info.length));
            os = conn.getOutputStream();
            os.write(info);
        }

        int codigo = conn.getResponseCode();
        if (codigo == 204) {  // Codigo 204 indica una respuesta vacia pero exitosa
            respuesta = "true";
        }
        else if (codigo==200) {
            is = conn.getInputStream();
            BufferedReader entrada = new BufferedReader(new InputStreamReader(is));
            respuesta = entrada.readLine();
        }
        else {
            throw new ServidorException(codigo);
        }
        return respuesta;
    }
}
