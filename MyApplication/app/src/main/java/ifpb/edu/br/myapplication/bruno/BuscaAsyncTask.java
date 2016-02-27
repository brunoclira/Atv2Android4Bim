package ifpb.edu.br.myapplication.bruno;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ifpb.edu.br.myapplication.bruno.interfaces.NomesInterface;
import ifpb.edu.br.myapplication.bruno.util.HttpService;
import ifpb.edu.br.myapplication.bruno.util.Response;

public class BuscaAsyncTask extends AsyncTask<JSONObject, Void, Response> {
    public static final String TAG = "BuscaAsyncTask";

    NomesInterface nome;

    public BuscaAsyncTask(MainActivity nome) {
        this.nome = nome;
    }

    @Override
    protected Response doInBackground(JSONObject... vetor) {
        Response response = null;
        JSONObject json = vetor[0];

        try {
            new HttpService();
            response = HttpService.sendJSONPostResquest(json, "get-byname");
        } catch (IOException e) {
            Log.e("EditTextListener", e.getMessage());
        }

        return response;

    }

    @Override
    protected void onPostExecute(Response reponse) {
        try {
            JSONArray jsonArray = new JSONArray(reponse.getContentValue());
            List<String> nomes = new ArrayList<>();
            for (int i = 0;i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("fullName");
                nomes.add(name);
            }

            nome.BuscaNome(nomes);

        } catch (JSONException e) {

        }
    }

}