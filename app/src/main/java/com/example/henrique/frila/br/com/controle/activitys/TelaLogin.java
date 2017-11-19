package com.example.henrique.frila.br.com.controle.activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.henrique.frila.R;
import com.example.henrique.frila.br.com.controle.help.Config;
import com.example.henrique.frila.br.com.controle.help.Mascara;
import com.example.henrique.frila.br.com.controle.help.PreferenciasUsuario;
import com.example.henrique.frila.br.com.controle.model.Conexao;

public class TelaLogin extends AppCompatActivity {

    private EditText editLogin,editSenha;
    private CheckBox cbPermanecer;

    private TextWatcher cpfMask;

    private ProgressDialog prgDialog;

    private String cpf_usuario,senha_usuario,email_usuario,nome_usuario,url = "",paramentros ="",NOME_PREFERENCE = "INFORMACOES_LOGIN_AUTOMATICO";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);

        prgDialog = new ProgressDialog(this);

        prgDialog.setMessage("Logando...");

        prgDialog.setCancelable(false);

        editLogin = (EditText) findViewById(R.id.editLogin);
        editSenha= (EditText) findViewById(R.id.editSenha);

        cbPermanecer = (CheckBox) findViewById(R.id.cbPermanece);

        mascara();
        estaLogado();

    }

    private void mascara(){
        cpfMask = Mascara.insert("###.###.###-##",editLogin);
        editLogin.addTextChangedListener(cpfMask);
    }

    public void efetuarLogin(){
        try {
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()){
                url = Config.Url+"logar.php";
                paramentros="cpf_usuario="+ cpf_usuario +"&senha_usuario=" + senha_usuario ;
                new SolicitarDados().execute(url);
            }else {
                prgDialog.hide();
                Toast.makeText(TelaLogin.this,"Você não possue conexão com a internet!",Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            prgDialog.hide();
            Toast.makeText(this, "Ouve um erro!" , Toast.LENGTH_SHORT).show();
        }

    }

    public class SolicitarDados extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... urls){
                return Conexao.postDados(urls[0],paramentros);
            }
            @Override
                    protected void onPostExecute(String resultado){
                try {
                    if (resultado.contains("login_ok,")){
                        String[] dados = resultado.split(",");
                        email_usuario = dados[1];
                        nome_usuario = dados[2];
                        preferencia();
                        telaPrincipal();
                        permancerLogado();
                    }else if(resultado.contains("login_erro")){
                        prgDialog.hide();
                        Toast.makeText(TelaLogin.this,"Email ou senha estão incorretos!",Toast.LENGTH_LONG).show();
                    }else {
                        prgDialog.hide();
                        Toast.makeText(TelaLogin.this,"Sem resposta do servidor!",Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    prgDialog.hide();
                    Toast.makeText(TelaLogin.this,"deu ruim",Toast.LENGTH_LONG).show();
                }

            }
        }

    public void login(View view){
        cpf_usuario = String.valueOf(editLogin.getText());
        senha_usuario = String.valueOf(editSenha.getText());
        String contar = String.valueOf(cpf_usuario.length());
       if (contar != "14") {
            if (cpf_usuario.isEmpty() || senha_usuario.isEmpty()) {
                Toast.makeText(TelaLogin.this, "Nenhum campo deve estar vazio!", Toast.LENGTH_LONG).show();
            } else {
                prgDialog.show();
                efetuarLogin();
            }
        }else {
            Toast.makeText(TelaLogin.this, "CPF invalido!", Toast.LENGTH_LONG).show();
        }
    }

    private void preferencia(){
        PreferenciasUsuario preferencias = new PreferenciasUsuario(TelaLogin.this);
        preferencias.salvarDados(cpf_usuario,nome_usuario,email_usuario);
    }

    public void Registro(View view) {
        Intent activivyRegistro = new Intent(this, TelaRegistro.class);
        startActivity(activivyRegistro);
        finish();
    }

    private void telaPrincipal(){
        Intent verificado = new Intent(this,TelaPrincipal.class);
        startActivity(verificado);
        finish();
    }

    private void estaLogado(){
        SharedPreferences prefs = getSharedPreferences(NOME_PREFERENCE, MODE_PRIVATE);
        String login= prefs.getString("login", null);
        if (login!= null) {
            telaPrincipal();
        } else {
            // não existe configuração salvar
        }
    }
    private void permancerLogado(){

        if(cbPermanecer.isChecked()){
            SharedPreferences.Editor editor = getSharedPreferences(NOME_PREFERENCE, MODE_PRIVATE).edit();
            editor.putString("login", editLogin.getText().toString());
            editor.putString("senha", editSenha.getText().toString());
            editor.commit();
        };

    }

}
