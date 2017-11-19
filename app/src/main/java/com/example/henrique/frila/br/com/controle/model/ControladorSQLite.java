package com.example.henrique.frila.br.com.controle.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.henrique.frila.br.com.controle.model.BancoSQLite.TABELA_ITENS;


public class ControladorSQLite {

    private SQLiteDatabase db;
    private BancoSQLite banco;


    public ControladorSQLite(Context context){
        banco = new BancoSQLite(context);

    }

    public String insereDado(String idUsuario,int idItemReserva,String categoraItemReserva, String nomeItem,String valor,
                             String periodoReserva,String data,String time_inicio, String time_final, String sitacaoReserva){
        ContentValues valores;
        long resultado;

        db = banco.getWritableDatabase();
        valores = new ContentValues();
        valores.put(BancoSQLite.ID_USUARIO, idUsuario);
        valores.put(BancoSQLite.ID_ITEM_RESERVA, idItemReserva);
        valores.put(BancoSQLite.CATEGORIA_ITEM_RESERVA, categoraItemReserva);
        valores.put(BancoSQLite.NOME_ITEM_RESERVA, nomeItem);
        valores.put(BancoSQLite.VALOR_LOCACAO, valor);
        valores.put(BancoSQLite.PERIODO_RESERVA, periodoReserva);
        valores.put(BancoSQLite.DATA_ITEM_RESERVA,data);
        valores.put(BancoSQLite.TIME_ITEM_INICIO, time_inicio);
        valores.put(BancoSQLite.TIME_ITEM_TERMINO, time_final);
        valores.put(BancoSQLite.SITUACAO_RESERVA, sitacaoReserva);

        resultado = db.insert(BancoSQLite.TABELA, null, valores);
        db.close();

        if (resultado ==-1) {
            return "Erro ao inserir registro";
        }else
            return "Registro inserido com sucesso";

    }

    /*public String insereDadoItens(int id_item,String categoria_itens,String nome_itens,String valor_itens,
                             String periodo_itens,String data_itens,String time_inicio_itens, String time_final_itens, String situacao_item){
        ContentValues valores;
        long resultado;

        db = banco.getWritableDatabase();
        valores = new ContentValues();
        valores.put(BancoSQLite.ID_ITENS_RESERVA, id_item);
        valores.put(BancoSQLite.CATEGORIA_ITENS, categoria_itens);
        valores.put(BancoSQLite.NOME_ITENS_RESERVA, nome_itens);
        valores.put(BancoSQLite.VALOR_LOCACAO_ITENS, valor_itens);
        valores.put(BancoSQLite.PERIODO_RESERVA_ITENS, periodo_itens);
        valores.put(BancoSQLite.DATA_ITENS_RESERVA,data_itens);
        valores.put(BancoSQLite.TIME_ITENS_INICIO, time_inicio_itens);
        valores.put(BancoSQLite.TIME_ITENS_TERMINO, time_final_itens);
        valores.put(BancoSQLite.SITUACAO_ITEM, situacao_item);


        resultado = db.insert(TABELA_ITENS, null, valores);
        db.close();

        if (resultado ==-1) {
            return "Erro ao inserir registro";
        }else
            return "Registro inserido com sucesso";

    }*/
    public void insertUser(HashMap<String, String> queryValues) {

        db = banco.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("_id", queryValues.get("_id"));
        values.put("categoria_item", queryValues.get("categoria_item"));
        values.put("nome_item_reserva", queryValues.get("nome_item_reserva"));
        values.put("valor_locacao", queryValues.get("valor_locacao"));
        values.put("periodo_reserva", queryValues.get("periodo_reserva"));
        values.put("data_item_reserva", queryValues.get("data_item_reserva"));
        values.put("time_item_inicio", queryValues.get("time_item_inicio"));
        values.put("time_item_termino", queryValues.get("time_item_termino"));
        values.put("situacao_item", queryValues.get("situacao_item"));

        db.insert("item_reserva", null, values);

        db.close();

    }


    /*public ArrayList<HashMap<String, String>> getAllUsers() {

        ArrayList<HashMap<String, String>> usersList;

        usersList = new ArrayList<HashMap <String,String>>();

        String selectQuery = "SELECT * FROM item_reserva";

        db = banco.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            do {

                HashMap<String, String> map = new HashMap<String, String>();

                map.put("_id", cursor.getString(0));
                map.put("categoria_item", cursor.getString(1));
                map.put("nome_item_reserva", cursor.getString(2));
                map.put("valor_locacao", cursor.getString(3));
                map.put("periodo_reserva", cursor.getString(4));
                map.put("data_item_reserva", cursor.getString(5));
                map.put("time_item_inicio", cursor.getString(6));
                map.put("time_item_termino", cursor.getString(7));
                map.put("situacao_item", cursor.getString(8));

                usersList.add(map);

            } while (cursor.moveToNext());

        }

        db.close();

        return usersList;

    }*/



}
