package ifpb.edu.br.myapplication.bruno.util;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Rhavy on 14/12/2015.
 * <p/>
 * Contribuições: Gustavo Ribeiro, Rerisson Daniel.
 */
public class HttpService {

    // IP da máquina onde se encontra o servidor. response
    private static final String URL_CONTEXT = "http://ladoss.com.br:8773/pesquisa-simpif-service/services/";

    // private static final String URL_CONTEXT =
    // "192.168.25.4:8080/resteasy-servlet/";
    public static HttpURLConnection sendGetRequest(String service) throws MalformedURLException, IOException {

        HttpURLConnection connection = null;

        URL url = new URL(URL_CONTEXT + service);

        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        return connection;
    }

    public static Response sendJSONPostResquest(JSONObject jsonObject, String service) throws IOException {

        HttpURLConnection connection = null;

        URL url = new URL(URL_CONTEXT + service);

        connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");

        connection.connect();

        DataOutputStream stream = new DataOutputStream(connection.getOutputStream());

        stream.writeBytes(jsonObject.toString());

        stream.flush();
        stream.close();

        int httpCode = connection.getResponseCode();
        String content = getHttpContent(connection);

        connection.disconnect();

        Response response = new Response(httpCode, content);

        return response;
    }

    public static String getHttpContent(HttpURLConnection connection) {

        StringBuilder builder = new StringBuilder();

        try {

            InputStream content = null;

            if (connection.getResponseCode() <= HttpURLConnection.HTTP_BAD_REQUEST) {
                content = connection.getInputStream();
            } else {
                content = connection.getErrorStream();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(content, "UTF-8"), 8);

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            content.close();

        } catch (IOException e) {
            Log.e("NotificationWearApp", "IOException: " + e);
        }

        return builder.toString();
    }
}
