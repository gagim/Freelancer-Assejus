package com.example.henrique.frila.br.com.controle.activitys;

import android.app.AlarmManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.henrique.frila.R;
import com.example.henrique.frila.br.com.controle.help.Config;
import com.example.henrique.frila.br.com.controle.help.MySigle;
import com.example.henrique.frila.br.com.controle.help.PreferenciasUsuario;
import com.example.henrique.frila.br.com.controle.model.Conexao;
import com.example.henrique.frila.br.com.controle.model.ControladorSQLite;
import com.example.henrique.frila.br.com.controle.model.SampleBC;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sun.mail.iap.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import okhttp3.Request;

public class TelaPrincipal extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private String NOME_PREFERENCE = "INFORMACOES_LOGIN_AUTOMATICO",id_usuario,id_reserva,identificadorUsuario,url,paramentros;
    private Toolbar toolbar;
    private ListView listView;
    private SQLiteDatabase db;
    private Cursor cursor;
    private SimpleCursorAdapter ap;
    private String[] nomeCampos;
    private ProgressDialog prgDialog;
    private ControladorSQLite controller = new ControladorSQLite(this);
    private StringRequest stringRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        prgDialog = new ProgressDialog(this);

        prgDialog.setMessage("Apagando...");

        prgDialog.setCancelable(false);

        Intent alarmIntent = new Intent(getApplicationContext(), SampleBC.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 5000, 10 * 1000, pendingIntent);


        PreferenciasUsuario preferencias = new PreferenciasUsuario(this);
        identificadorUsuario = preferencias.getIdentificador();
        ToolBar();
        buscarDados();
        mostrarDados();
    }

    private void buscarDados(){
        try {
            db = openOrCreateDatabase("dbreserva", Context.MODE_PRIVATE,null);
            cursor = db.rawQuery("SELECT * FROM reservas",null);
        }catch (Exception e){
        }
    }

    private void mostrarDados(){
        listView = (ListView) findViewById(R.id.lista_mostrar);
        if (cursor != null) {

            nomeCampos = new String[]{"_id", "id_Reserva", "nome_Reserva", "data_Reserva",
                    "tempo_Inicial", "tempo_Final", "situacao_Reserva"};
            int[] idViews = new int[]{R.id.id, R.id.id_reserva, R.id.txt_item_escolhido, R.id.txtData, R.id.txtInicio, R.id.txtFinal, R.id.txt_status};

            ap = new SimpleCursorAdapter(this, R.layout.item_reserva, cursor, nomeCampos, idViews);

            listView.setAdapter(ap);
            listView.setOnItemClickListener(this);
        }else {
        }

    }

    private void ToolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Bem Vindo(a) Assejus!");
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ( item.getItemId() ){
            case R.id.sair :
                deslogarUsuario();
                return true;
            case R.id.refresh :
                syncSQLiteMySQLDB();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void syncSQLiteMySQLDB() {

        prgDialog = new ProgressDialog(this);

        prgDialog.setMessage("Sincronizando...");

        prgDialog.setCancelable(true);

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();

        prgDialog.show();



        client.post(Config.Url+"pegarUsuario.php", params, new AsyncHttpResponseHandler() {


           @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                prgDialog.hide();
                   String obj =(String.valueOf(responseBody));
                   updateSQLite(obj);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Não há atualizções", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
                            Toast.LENGTH_LONG).show();

                }

        }

        });

    }

    private void go(){
        String url = Config.Url+"UrlpegarUsuario.php";
        stringRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            String Response = jsonObject.getString("response");
                            Toast.makeText(TelaPrincipal.this, jsonObject.toString(), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                return params;
            }
        };
    }

    private void sync(){
        String url = Config.Url+"UrlpegarUsuario.php";
        try {
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()){
                new TelaPrincipal.SolicitarDadosSync().execute(url);
            }else {
                dialogSalvar();
            }
        }catch (Exception e){
            Toast.makeText(this, "Ouve um erro!" , Toast.LENGTH_SHORT).show();
        }
    }

    public class SolicitarDadosSync extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... urls){
            return Conexao.postDados(urls[0],null);
        }
        @Override
        protected void onPostExecute(String resultado){
            Toast.makeText(TelaPrincipal.this,resultado,Toast.LENGTH_SHORT).show();
            //updateSQLite(resultado);
        }
    }

    public void updateSQLite(String response){
        ArrayList<HashMap<String, String>> usersynclist;
        usersynclist = new ArrayList<HashMap<String, String>>();

        try {

            JSONArray arr = new JSONArray(response);

            System.out.println(arr.length());

            Gson gson = new GsonBuilder().create();

            if(arr.length() != 0){

                for (int i = 0; i < arr.length(); i++) {

                    JSONObject obj = (JSONObject) arr.get(i);

                    System.out.println(obj.get("id_item_reserva"));
                    System.out.println(obj.get("categoria_item"));
                    System.out.println(obj.get("nome_item_reserva"));
                    System.out.println(obj.get("valor_locacao"));
                    System.out.println(obj.get("periodo_reserva"));
                    System.out.println(obj.get("data_item_reserva"));
                    System.out.println(obj.get("time_item_inicio"));
                    System.out.println(obj.get("time_item_termino"));

                    HashMap<String, String> queryValues = new HashMap<String, String>();

                    queryValues.put("id_item_reserva", obj.get("id_item_reserva").toString());
                    queryValues.put("categoria_item", obj.get("categoria_item").toString());
                    queryValues.put("nome_item_reserva", obj.get("nome_item_reserva").toString());
                    queryValues.put("valor_locacao", obj.get("valor_locacao").toString());
                    queryValues.put("periodo_reserva", obj.get("periodo_reserva").toString());
                    queryValues.put("data_item_reserva", obj.get("data_item_reserva").toString());
                    queryValues.put("time_item_inicio", obj.get("time_item_inicio").toString());
                    queryValues.put("time_item_termino", obj.get("time_item_termino").toString());


                    controller.insertUser(queryValues);

                    HashMap<String, String> map = new HashMap<String, String>();


                    map.put("id_item_reserva", obj.get("id_item_reserva").toString());

                    map.put("status", "1");

                    usersynclist.add(map);

                }
                updateMySQLSyncSts(gson.toJson(usersynclist));
                reloadActivity();

            }

        } catch (JSONException e) {

            e.printStackTrace();

        }

    }

    public void updateMySQLSyncSts(String json) {

        System.out.println(json);

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();

        params.put("syncsts", json);

        client.post(Config.Url+"sync.php", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Toast.makeText(getApplicationContext(), "O MySQL DB foi informado sobre a atividade de sincronização", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "Ocorreu um erro", Toast.LENGTH_LONG).show();
            }

        });

    }

    public void addNewNoteFunction(View view){
            Intent novoLayout = new Intent(this, TelaDeReserva.class);
            startActivity(novoLayout);
    }

    public void reloadActivity() {
        Intent objIntent = new Intent(getApplicationContext(), TelaPrincipal.class);
        startActivity(objIntent);
    }

    private void apagarReserva(){
        TextView textView = (TextView)findViewById(R.id.id);
        id_usuario = textView.getText().toString();
        try {
            db = openOrCreateDatabase("dbreserva",Context.MODE_PRIVATE,null);
            db.delete("reservas","_id=? AND id_Reserva=?",new String[]{ id_usuario,id_reserva });
            db.close();
            prgDialog.show();
            Toast.makeText(this,"Removido com sucesso!",Toast.LENGTH_SHORT).show();
            reloadActivity();
        }catch (Exception e){
            Toast.makeText(this,"Não foi possivel apagar a reserva!",Toast.LENGTH_SHORT).show();
            prgDialog.hide();
        }
    }

    private void apagarReservaSQL(){
        TextView id = (TextView)findViewById(R.id.id_reserva);
        id_reserva = id.getText().toString();
        try {
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()){
                url = Config.Url+"apagar.php";
                paramentros = "cpf_usuario=" + identificadorUsuario + "&id_item_reserva=" + id_reserva;
                new TelaPrincipal.SolicitarDados().execute(url);
            }else {
                dialogSalvar();
            }
        }catch (Exception e){
            Toast.makeText(this, "Ouve um erro!" , Toast.LENGTH_SHORT).show();
        }

    }

    private void dialogSalvar(){
        AlertDialog.Builder salvar = new AlertDialog.Builder(this);
        salvar.setTitle("Sem conexão com a internet");
        salvar.setMessage("Você não possue conexão com a internet!");
        salvar.setCancelable(false);
        salvar.setPositiveButton("Tentar mais tarde", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        salvar.create();
        salvar.show();
    }

    public class SolicitarDados extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... urls){
            return Conexao.postDados(urls[0],paramentros);
        }
        @Override
        protected void onPostExecute(String resultado){
            if (resultado.contains("id_erro")) {
                Toast.makeText(TelaPrincipal.this, "id invalido!", Toast.LENGTH_LONG).show();
            } else if (resultado.contains("sucesso,")) {
                    apagarReserva();
                }else if(resultado.contains("error")) {
                    Toast.makeText(TelaPrincipal.this, "Algo de errado não está certo!", Toast.LENGTH_LONG).show();
                }
            }
        }

    @Override
    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
        AlertDialog.Builder salvar = new AlertDialog.Builder(this);
        salvar.setTitle("Deseja apagar está reserva?");
        salvar.setCancelable(false);
        salvar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                apagarReservaSQL();
            }
        });
        salvar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        salvar.create();
        salvar.show();

    }

    private void deslogarUsuario(){
        SharedPreferences.Editor editor = getSharedPreferences(NOME_PREFERENCE, MODE_PRIVATE).edit();
        editor.putString("login",null);
        editor.putString("senha",null);
        editor.commit();
        Intent sair = new Intent(this,TelaLogin.class);
        startActivity(sair);
        finish();
    }

}
