package rubenscorrea.com.app05_agendacontatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rubens on 01/07/2017.
 */

public class ContatosDAO extends SQLiteOpenHelper {

    private static final int VERSAO = 1;
    private static final String TABELA = "Contatos";
    private static final String DATABASE = "MinhaAgenda";

    public ContatosDAO(Context context) {
        super(context, DATABASE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String ddl = "CREATE TABLE " + TABELA
                + "(id INTEGER PRIMARY KEY,"
                + "nome TEXT NOT NULL,"
                + "email TEXT,"
                + "site TEXT NOT NULL,"
                + "telefone TEXT,"
                + "endereco TEXT, "
                + "caminhoFoto TEXT);";

        db.execSQL(ddl);




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(oldVersion == 1){
            String sql = "ALTER TABLE " + TABELA + " ADD COLUMN caminhoFoto TEXT;";
            db.execSQL(sql);
        }
    }

    public void inserirContato(Contato contato){

        ContentValues values = new ContentValues();
        values.put("nome", contato.getNome());
        values.put("email", contato.getEmail());
        values.put("site", contato.getSite());
        values.put("telefone", contato.getTelefone());
        values.put("endereco", contato.getEndereco());
        values.put("caminhoFoto", contato.getFoto());


        getWritableDatabase().insert(TABELA, null, values);


    }
    public void apagarContato(Contato contato){

        SQLiteDatabase db = getWritableDatabase();
        String[] args = {contato.getId().toString()};
        db.delete("contatos", "id=?", args);

    }
    public void alteraContato(Contato contato){

        ContentValues values = new ContentValues();

        values.put("nome", contato.getNome());
        values.put("email", contato.getEmail());
        values.put("site", contato.getSite());
        values.put("telefone", contato.getTelefone());
        values.put("endereco", contato.getEndereco());
        values.put("caminhoFoto", contato.getFoto());

        String[] idParaAlterar = {
                contato.getId().toString()
        };

        getWritableDatabase().update(TABELA, values, "id=?", idParaAlterar);
    }


    public boolean isContato(String telefone){

        String[] parametros = {telefone};
        Cursor cursor = getReadableDatabase().rawQuery("SELECT telefone FROM "+ TABELA + " WHERE telefone=?", parametros);
        int total = cursor.getCount();
        // Se total retornar maior que zero, retorna verdadeiro que o contato já existe.
        return total > 0;
    }

    public List<Contato> getLista(){
        List<Contato> contatos = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM "+ TABELA + ";", null);

        while (cursor.moveToNext()){

            Contato contato = new Contato();
            contato.setId(cursor.getLong(cursor.getColumnIndex("id")));
            contato.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            contato.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            contato.setSite(cursor.getString(cursor.getColumnIndex("site")));
            contato.setTelefone(cursor.getString(cursor.getColumnIndex("telefone")));
            contato.setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
            contato.setFoto(cursor.getString(cursor.getColumnIndex("caminhoFoto")));

            contatos.add(contato);


        }

        cursor.close();
        return contatos;
    }

}
