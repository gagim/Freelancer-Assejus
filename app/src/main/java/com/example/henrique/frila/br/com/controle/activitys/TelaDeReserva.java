package com.example.henrique.frila.br.com.controle.activitys;


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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.henrique.frila.R;
import com.example.henrique.frila.br.com.controle.help.Config;
import com.example.henrique.frila.br.com.controle.help.PreferenciasUsuario;
import com.example.henrique.frila.br.com.controle.help.Scroll;
import com.example.henrique.frila.br.com.controle.help.SendMail;
import com.example.henrique.frila.br.com.controle.model.BancoSQLite;
import com.example.henrique.frila.br.com.controle.model.Conexao;
import com.example.henrique.frila.br.com.controle.model.ControladorSQLite;
import static com.example.henrique.frila.R.id.sp_reserva_de;


public class TelaDeReserva extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner mesInterrese,reserva,time_inicial,time_final,reserva_de;

    private ImageView image;

    private TextView txtLocacao;

    private Toolbar toolbar;

    private SQLiteDatabase db;

    private Cursor cursor;

    private String[] nomeCampos;

    private SimpleCursorAdapter ap,ap2;


    //private ControladorSQLite controller = new ControladorSQLite(this);

    private String identificadorUsuario,dataReserva,time1Reserva,time2Reserva,periodoReserva,reservaDe,
    salvarString = "",situacao,valorLocacao,url,paramentros,nome_item,NOME_PREFERENCE = "INFORMACOES_LOGIN_AUTOMATICO";

    private int id_item_reserva;

    Long id_nome,id_item,id_reserva;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        switch (parent.getId()) {
            case sp_reserva_de:
                nome_item = parent.getSelectedItem().toString();
                if (nome_item.equals("nada") && nome_item != null) {
                    adapterReserva(0);
                }
                /*id_reserva = parent.getSelectedItemId();
                item_reserva_escolha(id_reserva);
                id_item = parent.getSelectedItemId();*/
                break;
            case R.id.sp_reserva:
                nome_item = parent.getSelectedItem().toString();
                if (nome_item.equals("nada") && nome_item != null) {
                    adapterReserva(1);
                }
                /*nome_item = parent.getSelectedItem().toString();
                item_reservar(nome_item);
                id_nome = parent.getSelectedItemId();*/
                break;
        }
    }

    private void adapterReserva(int reservaId){
        switch (reservaId){
            case 0:
                buscarDados("SELECT DISTINCT _id,categoria_item FROM item_reserva");
                if (cursor != null) {
                    nomeCampos = new String[]{"categoria_item"};
                    int[] idViews = new int[]{R.id.txtVisu};
                    ap2 = new SimpleCursorAdapter(this, R.layout.listar_spinner, cursor, nomeCampos, idViews);
                    reserva_de.setAdapter(ap2);
        }
            case 1:
                buscarDados("SELECT DISTINCT _id,nome_item_reserva,valor_locacao FROM item_reserva");
                if (cursor != null) {
                    nomeCampos = new String[]{"nome_item_reserva","valor_locacao"};
                    int[] idViews = new int[]{R.id.txtVisu2,R.id.txtValor};
                    ap2 = new SimpleCursorAdapter(this, R.layout.listar_spinner_novo, cursor, nomeCampos, idViews);
                    reserva.setAdapter(ap2);
                }
        }
    }

    public void item_reserva_escolha(/*Long item_reserva*/){
        /*if (item_reserva == 0){

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.item_para_reserva_churrasqueira, android.R.layout.simple_spinner_item);
            reserva.setAdapter(adapter);

            ArrayAdapter<CharSequence> adapter_horario_inicio = ArrayAdapter.createFromResource(this,
                    R.array.horario_churrasqueira_inicial, android.R.layout.simple_spinner_item);
            time_inicial.setAdapter(adapter_horario_inicio);

            ArrayAdapter<CharSequence> adapter_horario_final = ArrayAdapter.createFromResource(this,
                    R.array.horario_churrasqueira_final, android.R.layout.simple_spinner_item);
            time_final.setAdapter(adapter_horario_final);

        }else if(item_reserva == 1){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.item_para_reserva_quadra, android.R.layout.simple_spinner_item);
            reserva.setAdapter(adapter);

            ArrayAdapter<CharSequence> adapter_horario = ArrayAdapter.createFromResource(this,
                    R.array.horario_quadra, android.R.layout.simple_spinner_item);

            time_inicial.setAdapter(adapter_horario);
            time_final.setAdapter(adapter_horario);

        }else if(item_reserva == 2){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.item_para_reserva_salao, android.R.layout.simple_spinner_item);
            reserva.setAdapter(adapter);

            ArrayAdapter<CharSequence> adapter_horario_inicial = ArrayAdapter.createFromResource(this,
                    R.array.horario_salao_inicial, android.R.layout.simple_spinner_item);
            time_inicial.setAdapter(adapter_horario_inicial);

            ArrayAdapter<CharSequence> adapter_horario_final = ArrayAdapter.createFromResource(this,
                    R.array.horario_salao_final, android.R.layout.simple_spinner_item);
            time_final.setAdapter(adapter_horario_final);

        }*/
    }

    public void item_reservar(String nome_item){
        if(nome_item.contains("Coletiva")) {
            txtLocacao.setText(R.string.coletiva);
        }else if(nome_item.contains("Comunitária")) {
            txtLocacao.setText(R.string.comunitaria);
        }else if(nome_item.contains("Futebol")) {
            txtLocacao.setText(R.string.futebol);
        }else if(nome_item.contains("Areia") || nome_item.contains("Tênis") ) {
            txtLocacao.setText(R.string.mult);
        }else if (nome_item.contains("Futsal") || nome_item.contains("Basquete") ){
            txtLocacao.setText(R.string.mult2);
        }else if (nome_item.contains("Ricota")){
            txtLocacao.setText(R.string.ricota);
        }else if (nome_item.contains("Externo")){
            txtLocacao.setText(R.string.buffet);
        }else if (nome_item.contains("Aluguel")){
            txtLocacao.setText(R.string.fixo);
        }else {
            txtLocacao.setText("");
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(TelaDeReserva.this,"Nada foi selecionado",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_de_reserva);

        txtLocacao = (TextView) findViewById(R.id.txtLocacao);

        reserva_de = (Spinner) findViewById(R.id.sp_reserva_de);
        reserva = (Spinner) findViewById(R.id.sp_reserva);
        mesInterrese = (Spinner) findViewById(R.id.sp_mes);
        time_inicial = (Spinner) findViewById(R.id.time_inicial);
        time_final = (Spinner) findViewById(R.id.time_final);
        image = (ImageView) findViewById(R.id.image);

        reserva_de.setOnItemSelectedListener(this);
        reserva.setOnItemSelectedListener(this);
        mesInterrese.setOnItemSelectedListener(this);

        ToolBar();
    }

    public void pesquisar(View view){
       //item();
        //db.execSQL("INSERT INTO item_reserva (_id, categoria_item, nome_item_reserva, valor_locacao, periodo_reserva, data_item_reserva, " +
               //"time_item_inicio, time_item_termino,foto_reserva, situacao_item ) VALUES ('1', 'Quadras Poliesportivas', 'Coletiva para 50 convidados', '100.00', 'Jan', '13/10/2017', '17:00', '19:00', '', 'true' ) ");
        mostrarDados();
        /*ArrayList<HashMap<String, String>> userList = controller.getAllUsers();

        if (userList.size() != 0) {


            ListAdapter adapter = new SimpleAdapter(this, userList, R.layout.listar_itens, new String[]{
                    "_id","data_item_reserva"}, new int[]{
                    R.id.txtIdListar,R.id.txtData});

            Scroll non_scroll_list = (Scroll) findViewById(R.id.lv_nonscroll_list);


            non_scroll_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    salvar();
                }
            });

            non_scroll_list.setAdapter(adapter);

        }else {
            Toast.makeText(getApplicationContext(), "Não possue nenhum registro", Toast.LENGTH_SHORT).show();
        }*/
    }

    private void lerBanco(){
        BancoSQLite banco = new BancoSQLite(this);
        db = banco.getReadableDatabase();
    }

    private void buscarDados(String comando){
        try {
            db = openOrCreateDatabase("dbreserva", Context.MODE_PRIVATE,null);
            cursor = db.rawQuery(comando,null);
            lerBanco();
        }catch (Exception e){
             }
    }

    private void mostrarDados(){
        Scroll non_scroll_list = (Scroll) findViewById(R.id.lv_nonscroll_list);
        buscarDados("SELECT * FROM item_reserva");
        if (cursor != null) {
                nomeCampos = new String[]{"_id","data_item_reserva"};
                int[] idViews = new int[]{R.id.txtIdListar, R.id.txtData};

                ap = new SimpleCursorAdapter(this, R.layout.listar_itens, cursor, nomeCampos, idViews);

                non_scroll_list.setAdapter(ap);
                non_scroll_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        salvar();
                    }
                });
        }else {
            Toast.makeText(getApplicationContext(), "Não possue nenhum registro", Toast.LENGTH_SHORT).show();
        }

    }

    private void ToolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Nova Reserva");
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_reserva,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ( item.getItemId() ){
            case R.id.sair :
                deslogarUsuario();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void dadosReserva(){
        TextView data_item = (TextView) findViewById(R.id.txtData);
        TextView lista_item = (TextView) findViewById(R.id.txtIdListar);
        PreferenciasUsuario preferencias = new PreferenciasUsuario(this);
        identificadorUsuario = preferencias.getIdentificador();
        periodoReserva = mesInterrese.getSelectedItem().toString();
        reservaDe = reserva_de.getSelectedItem().toString();
        nome_item = reserva.getSelectedItem().toString();
        time1Reserva = time_inicial.getSelectedItem().toString();
        time2Reserva = time_final.getSelectedItem().toString();
        valorLocacao = txtLocacao.getText().toString();
        dataReserva = data_item.getText().toString();
        situacao = "Aguardando";
        id_item_reserva = Integer.parseInt(lista_item.getText().toString());
    }

    private void salvarDadosSQLeSQLite(){
        dadosReserva();
            try {
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()){
                    url = Config.Url +"registrarReserva.php";
                    paramentros = "cpf_usuario=" + identificadorUsuario + "&id_item_reserva=" + id_item_reserva + "&categoria_item=" + reservaDe
                    + "&nome_item_reserva=" + nome_item + "&valor_locacao=" + valorLocacao + "&periodo_reserva=" + periodoReserva
                    + "&data_item_reserva=" + dataReserva + "&time_item_inicio=" + time1Reserva + "&time_item_termino=" + time2Reserva
                    + "&situacao_reserva=" + situacao;
                    new TelaDeReserva.SolicitarDados().execute(url);
                }else {
                    dialogSalvar();
                }
            }catch (Exception e){
                Toast.makeText(this, "Ouve um erro!" , Toast.LENGTH_SHORT).show();
            }

        }

    public class SolicitarDados extends AsyncTask<String,Void,String> {
            @Override
            protected String doInBackground(String... urls){
                return Conexao.postDados(urls[0],paramentros);
            }
            @Override
            protected void onPostExecute(String resultado){
                    Long idTempoInicial = time_inicial.getSelectedItemId();
                    Long idTempoFinal = time_final.getSelectedItemId();
                    if (id_item == 1 && idTempoInicial >= idTempoFinal) {
                        Toast.makeText(TelaDeReserva.this, "Campo inicio não deve ser maior que o termino", Toast.LENGTH_LONG).show();
                    } else {
                        if (resultado.contains("item_erro,")) {
                            Toast.makeText(TelaDeReserva.this, "Essa reserva ja existe!", Toast.LENGTH_LONG).show();
                        } else if (resultado.contains("registro_ok")) {
                            salvarDados();
                        }else if(resultado.contains("registro_erro")) {
                            Toast.makeText(TelaDeReserva.this, "Erro,ao criar a reserva!", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(TelaDeReserva.this, "Sem resposta do servidor!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        }

    private void dialogSalvar(){
        AlertDialog.Builder salvar = new AlertDialog.Builder(this);
        salvar.setTitle("Sem conexão com a internet!");
        salvar.setMessage("Você não possue conexão com a internet,porem,deseja salvar mesmo assim?");
        salvar.setCancelable(false);
        salvar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Long idTempoInicial = time_inicial.getSelectedItemId();
                Long idTempoFinal = time_final.getSelectedItemId();
                if (id_item == 1 && idTempoInicial >= idTempoFinal) {
                    Toast.makeText(TelaDeReserva.this, "Campo inicio não deve ser maior que o termino", Toast.LENGTH_LONG).show();
                } else {
                salvarDados();}
            }
        });
        salvar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        salvar.create();
        salvar.show();
    }

    private void salvarDados(){
            try {
                if (identificadorUsuario != null && nome_item != null && time1Reserva != null && time2Reserva != null && periodoReserva != null) {
                    ControladorSQLite crud = new ControladorSQLite(getBaseContext());
                    String resultado = crud.insereDado(identificadorUsuario,id_item_reserva,reservaDe,nome_item,valorLocacao,periodoReserva,dataReserva,time1Reserva, time2Reserva, situacao);
                    Toast.makeText(TelaDeReserva.this, resultado, Toast.LENGTH_SHORT).show();
                    if (resultado == "Registro inserido com sucesso"){
                        enviarEmail();;
                    }
                } else {
                    Toast.makeText(TelaDeReserva.this, "Nenhum campo pode estar vazio!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(TelaDeReserva.this, "Ouve um erro aqui!", Toast.LENGTH_SHORT).show();
            }
    }

    public void chamarTelaPrincipal(){
        Intent jaFoi = new Intent(TelaDeReserva.this,TelaPrincipal.class);
        startActivity(jaFoi);
        finish();
    }

    private void textDialog(Long id_reserva) {
        if (id_reserva == null && id_nome == null){
            salvarString = "";
        }else
        if (id_reserva == 0 && id_nome == 0) {
            salvarString = "Cheque caução de R$ 1.000,00 como garantia.\n" +
                    "Prezado(a) Associado(a), informamos que a falta injustificada no dia e horário reservado para a utilização da churrasqueira, implicará em MULTA no valor de UMA LOCAÇÃO.";
        }else if(id_reserva==0 && id_nome != 0){
            salvarString = "Cheque caução de R$ 500,00 como garantia.\n" +
                    "Prezado(a) Associado(a), informamos que a falta injustificada no dia e horário reservado para a utilização da churrasqueira, implicará em MULTA no valor de UMA LOCAÇÃO.";
        }else if(id_reserva == 1){
            salvarString = "Ligar antes da vinda ao Clube da Assejus.";
        }else if (id_reserva == 2) {
            salvarString = "Área de cozinha com fogão industrial de 6 bocas.\n" +
                    "Dois banheiros.\n" +
                    "40 mesas redondas.\n" +
                    "399 cadeiras acolchoadas.\n" +
                    "Dois frezers horizontais com capacidade para 419 litros.\n" +
                    "Duração do Evento: 6 horas.\n" +
                    "Entrada de 20% no ato de assinatura do contrato, juntamente a uma taxa do ECAD no valor de 15%, e mais dois cheques de 40% cada para débito aos três e seis meses antes da data de utilização, além de um cheque caução de R$ 4.500,00 como garantia da devolução dos itens locados em bom estado de conservação (só aceitamos cheques do contratante).\n" +
                    "Visitas ao salão somente em dias em que não houver evento.\n" +
                    "Fechamento de contrato de Terça a Domingo, entre 09:00h e 15:00h.\n" +
                    "LIGAR ANTES DA VINDA AO CLUBE DA ASSEJUS.";
        }
    }

    private void salvar(){
        textDialog(id_reserva);
        AlertDialog.Builder salvar = new AlertDialog.Builder(this);
        salvar.setTitle("Deseja realizar uma nova reserva?");
        salvar.setMessage(salvarString);
        salvar.setCancelable(false);
        salvar.setPositiveButton("Aceitar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                salvarDadosSQLeSQLite();
            }
        });
        salvar.setNegativeButton("Recusar", new DialogInterface.OnClickListener() {
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

    private void enviarEmail(){
        PreferenciasUsuario preferenciasUsuario =new PreferenciasUsuario(this);
        String emailUsuario = preferenciasUsuario.getEmail();
        String nomeUsuario = preferenciasUsuario.getNome();
        String motivo = "Reserva Assejus";
        String mensagem = "Olá senhor(a) "+ nomeUsuario+ ",Nós da Assejus vimos por meio deste confirmar sua reserva em nossa dependencia,está é uma mensagem automatica,por favor NÃO RETORNE!";
        SendMail sm = new SendMail(this, emailUsuario, motivo, mensagem);

        sm.execute();
        chamarTelaPrincipal();
    }

}