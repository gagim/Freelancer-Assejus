package com.example.henrique.frila.br.com.controle.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BancoSQLite extends SQLiteOpenHelper{

    private static final String NOME_BANCO = "dbreserva";

    //Tabela itens
    static final String TABELA_ITENS = "item_reserva";
    public static final String ID_ITENS_RESERVA = "_id";
    public static final String CATEGORIA_ITENS = "categoria_item";
    public static final String NOME_ITENS_RESERVA = "nome_item_reserva";
    public static final String VALOR_LOCACAO_ITENS = "valor_locacao";
    public static final String PERIODO_RESERVA_ITENS = "periodo_reserva";
    public static final String DATA_ITENS_RESERVA = "data_item_reserva";
    public static final String TIME_ITENS_INICIO = "time_item_inicio";
    public static final String TIME_ITENS_TERMINO = "time_item_termino";
    public static final String FOTO_RESERVA = "foto_reserva";
    public static final String SITUACAO_ITEM = "situacao_item";

    //tabela reservas
    static final String TABELA = "reservas";
    public static final String ID_RESERVA = "_id";
    public static final String ID_USUARIO = "id_Usuario";
    public static final String ID_ITEM_RESERVA = "id_Reserva";
    public static final String CATEGORIA_ITEM_RESERVA = "item_Reserva";
    public static final String NOME_ITEM_RESERVA = "nome_Reserva";
    public static final String VALOR_LOCACAO = "valor_Locacao";
    public static final String PERIODO_RESERVA = "periodo_Reserva";
    public static final String DATA_ITEM_RESERVA = "data_Reserva";
    public static final String TIME_ITEM_INICIO = "tempo_Inicial";
    public static final String TIME_ITEM_TERMINO = "tempo_Final";
    public static final String SITUACAO_RESERVA = "situacao_Reserva";

    //versão
    public static final int VERSAO = 1;

    public BancoSQLite(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String table = "CREATE TABLE IF NOT EXISTS "+ TABELA + "("
                + ID_RESERVA + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ID_USUARIO + " VARCHAR  not null, "
                + ID_ITEM_RESERVA + " INTEGER not null, "
                + CATEGORIA_ITEM_RESERVA + " VARCHAR(20) not null, "
                + NOME_ITEM_RESERVA + " VARCHAR(30) not null, "
                + VALOR_LOCACAO + " VARCHAR not null, "
                + PERIODO_RESERVA + " VARCHAR not null, "
                + DATA_ITEM_RESERVA + " VARCHAR not null, "
                + TIME_ITEM_INICIO + " VARCHAR not null, "
                + TIME_ITEM_TERMINO + " VARCHAR not null, "
                + SITUACAO_RESERVA + " VARCHAR(15) not null "
                + ")";

        String tableItem = "CREATE TABLE IF NOT EXISTS "+ TABELA_ITENS + "("
                + ID_ITENS_RESERVA + " INTEGER not null, "
                + CATEGORIA_ITENS  + " VARCHAR(20) not null, "
                + NOME_ITENS_RESERVA + " VARCHAR(30) not null, "
                + VALOR_LOCACAO_ITENS + " VARCHAR not null, "
                + PERIODO_RESERVA_ITENS + " VARCHAR not null, "
                + DATA_ITENS_RESERVA + " VARCHAR not null, "
                + TIME_ITENS_INICIO + " VARCHAR not null, "
                + TIME_ITENS_TERMINO + " varchar not null, "
                + FOTO_RESERVA +  " VARCHAR(100) not null, "
                + SITUACAO_ITEM + " boolean not null"
                + ")";

        db.execSQL(tableItem);
        db.execSQL(table);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABELA);
        onCreate(db);
    }

}
