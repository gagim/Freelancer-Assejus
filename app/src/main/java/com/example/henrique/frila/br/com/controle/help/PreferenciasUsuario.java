package com.example.henrique.frila.br.com.controle.help;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenciasUsuario {

    private Context contexto;
    private SharedPreferences sharedPreferences;
    private String NOME_ARQUIVO = "frila.preferencias";
    private int MODE = 0;
    private SharedPreferences.Editor editor;

    private String CHAVE_IDENTIFICADOR = "identificadorUsuarioLogado";
    private String CHAVE_NOME = "nomeUsuarioLogado";
    private String CHAVE_EMAIL = "emailUsuarioLogado";

    public PreferenciasUsuario(Context contextoParametro) {

        contexto = contextoParametro;
        sharedPreferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = sharedPreferences.edit();

    }

    public void salvarDados(String identificadorUsuario ,String nomeUsuarioLogado, String emailUsuarioLogado) {

        editor.putString(CHAVE_IDENTIFICADOR, identificadorUsuario);
        editor.putString(CHAVE_NOME, nomeUsuarioLogado);
        editor.putString(CHAVE_EMAIL, emailUsuarioLogado);
        editor.commit();

    }

    public String getIdentificador(){
        return sharedPreferences.getString(CHAVE_IDENTIFICADOR,null);

    }
    public String getNome(){
        return sharedPreferences.getString(CHAVE_NOME,null);
    }
    public String getEmail(){
        return sharedPreferences.getString(CHAVE_EMAIL,null);
    }

}
