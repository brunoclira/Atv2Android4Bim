package ifpb.edu.br.myapplication.bruno;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.ifpb.myapplication.R;
import ifpb.edu.br.myapplication.bruno.interfaces.NomesInterface;

public class MainActivity extends Activity implements TextWatcher, NomesInterface {

    // Define o tamanho mínimo do texto para consulta no servidor.
    private static int TAMANHO_MINIMO_TEXTO = 4;

    private EditText nomeEditText;
    List<String> nomes;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nomeEditText = (EditText) findViewById(R.id.editTextWatch);
        nomeEditText.addTextChangedListener(this);

        ListView nomesListView = (ListView) findViewById(R.id.listView1);
        nomes = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nomes);

        nomesListView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.i("EditTextListener", "beforeTextChanged: " + s);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.i("EditTextListener", "onTextChanged: " + s);
        String nome = s.toString();

        try {

            if (nome.length() >= TAMANHO_MINIMO_TEXTO) {
                JSONObject json = new JSONObject();
                json.put("fullName", nome);
                BuscaAsyncTask buscarNomeAsyncTask = new BuscaAsyncTask(this);
                buscarNomeAsyncTask.execute(json);
                nomes.add(nome);
                adapter.notifyDataSetChanged();
            } else {
                nomes.clear();
            }

        } catch (JSONException e) {

            Log.e("EditTextListener", e.getMessage());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.i("EditTextListener", "afterTextChanged: " + s);
    }

    @Override
    public void BuscaNome(List<String> nome) {
        nomes.clear();
        nomes.addAll(nome);
        adapter.notifyDataSetChanged();
    }

}
