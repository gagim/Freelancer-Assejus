package com.example.henrique.frila.br.com.controle.activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.henrique.frila.R;
import com.example.henrique.frila.br.com.controle.help.Config;
import com.example.henrique.frila.br.com.controle.help.Mascara;
import com.example.henrique.frila.br.com.controle.help.PreferenciasUsuario;
import com.example.henrique.frila.br.com.controle.model.Conexao;

public class TelaRegistro extends AppCompatActivity {

    private EditText editCPF,editSenha,editNome,editEmail;

    private TextWatcher cpfMask;

    private String cpf,senha,nome,email,url = "",paramentros = "",NOME_PREFERENCE = "INFORMACOES_LOGIN_AUTOMATICO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_registro);

        editCPF = (EditText) findViewById(R.id.editCPFRegistro);
        editSenha = (EditText) findViewById(R.id.editSenhaRegistro);
        editNome = (EditText) findViewById(R.id.editNomeRegistro);
        editEmail = (EditText) findViewById(R.id.editEmailRegistro);

        mascara();
    }

    private void mascara(){
        cpfMask = Mascara.insert("###.###.###-##",editCPF);
        editCPF.addTextChangedListener(cpfMask);
    }

    public void Salvar(View view){
        AlertDialog.Builder salvar = new AlertDialog.Builder(this);
        salvar.setTitle("Salvar?");
        salvar.setMessage("Deseja salvar e criar uma conta?");
        salvar.setCancelable(false);
        salvar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                capturarDaTela();
            }
        });
        salvar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(TelaRegistro.this, "Ok =(" , Toast.LENGTH_SHORT).show();
            }
        });
        salvar.create();
        salvar.show();
    }

    public void Cancelar(View view){
        AlertDialog.Builder cancelar = new AlertDialog.Builder(this);
        cancelar.setTitle("Cancelar?");
        cancelar.setMessage("Deseja cancelar o cadastro?");
        cancelar.setCancelable(false);
        cancelar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelarCadastro();
                                 
            }
        });
        cancelar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        cancelar.create();
        cancelar.show();
    }

    private void cancelarCadastro(){
        Intent cancelarCadastro = new Intent(TelaRegistro.this,TelaLogin.class);
        startActivity(cancelarCadastro);
        finish();
    }

    private class SolicitarDados extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... urls){
            return Conexao.postDados(urls[0],paramentros);
        }
        @Override
        protected void onPostExecute(String resultado) {
            if (resultado.contains("cpf_erro")) {
                Toast.makeText(TelaRegistro.this, "CPF invalido!", Toast.LENGTH_LONG).show();
            } else if (resultado.contains("registro_ok")) {
                    preferencia();
                    Intent verificado = new Intent(TelaRegistro.this, TelaPrincipal.class);
                    startActivity(verificado);
                Toast.makeText(TelaRegistro.this, "Salvo!", Toast.LENGTH_LONG).show();
                    finish();
                } else if(resultado.contains("registro_erro")) {
                    Toast.makeText(TelaRegistro.this, "Algo de errado não está certo!", Toast.LENGTH_LONG).show();
                }
            }

    }

    private void capturarDados(){
        cpf = editCPF.getText().toString();
        senha = editSenha.getText().toString() ;
        nome = editNome.getText().toString();
        email = editEmail.getText().toString();
    }

    public void efetuarLogin(){
        capturarDados();
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            url = Config.Url+"registrar.php";
            paramentros= "cpf_usuario=" + cpf + "&email_usuario=" + email + "&nome_usuario=" + nome +"&senha_usuario=" + senha;
            new SolicitarDados().execute(url);
        }else {
            Toast.makeText(TelaRegistro.this,"Você não possue conexão com a internet!",Toast.LENGTH_LONG).show();
        }
    }

    private void capturarDaTela(){
        capturarDados();
        if (cpf.isEmpty() || senha.isEmpty() || nome.isEmpty() || email.isEmpty()){
            Toast.makeText(TelaRegistro.this,"Nenhum campo deve estar vazio!",Toast.LENGTH_LONG).show();
        }else{
            efetuarLogin();
        }
    }

    private void preferencia(){
        PreferenciasUsuario preferencias = new PreferenciasUsuario(this);
        preferencias.salvarDados(cpf,nome,email);
        SharedPreferences.Editor editor = getSharedPreferences(NOME_PREFERENCE, MODE_PRIVATE).edit();
        editor.putString("login", cpf);
        editor.putString("senha", senha);
        editor.commit();
    }
}
