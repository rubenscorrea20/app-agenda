package rubenscorrea.com.app05_agendacontatos;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {

    ListView minhaLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Formulario.class);
                startActivity(intent);
            }
        });

        minhaLista = (ListView) findViewById(R.id.minhaLista);
        registerForContextMenu(minhaLista);

        minhaLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Contato contato;
                contato = (Contato) parent.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, Formulario.class);
                intent.putExtra("contatoSelecionado", contato);
                startActivity(intent);

            }
        });


        permissaoSMS();
        permissaoLigacao();

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        //Pega a posição que foi clicada na lista
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Contato contatoSelecionado = (Contato) minhaLista.getAdapter().getItem(info.position);

        final MenuItem itemLigar = menu.add("Ligar para contato");
        final MenuItem itemSMS = menu.add("Enviar SMS");
        final MenuItem itemSite = menu.add("Visitar o Site");
        final MenuItem itemApagar = menu.add("Apagar Contato");

        itemApagar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Apagar Contato?")
                        .setMessage("Deseja realmente apagar esse contato?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ContatosDAO dao = new ContatosDAO(MainActivity.this);
                                dao.apagarContato(contatoSelecionado);
                                dao.close();
                                carregaLista();
                            }
                        })
                        .setNegativeButton("Não", null).show();


                return false;
            }
        });

        itemSMS.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent intentSms = new Intent(Intent.ACTION_VIEW);
                intentSms.setData(Uri.parse("sms:" + contatoSelecionado.getTelefone()));

                startActivity(intentSms);

                return false;
            }
        });

        itemSite.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent intentSite = new Intent(Intent.ACTION_VIEW);
                intentSite.setData(Uri.parse("http://" + contatoSelecionado.getSite()));
                startActivity(intentSite);

                return false;
            }
        });

        itemLigar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                ligarParaContato(contatoSelecionado);

                return false;
            }
        });


        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    protected void onResume() {
        carregaLista();
        super.onResume();
    }

    private void carregaLista() {

        ContatosDAO dao = new ContatosDAO(this);
        List<Contato> contatos = dao.getLista();
        dao.close();

        ContatoAdaptador adaptador = new ContatoAdaptador(this, contatos);

        this.minhaLista.setAdapter(adaptador);
    }

    private void permissaoSMS() {

        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.RECEIVE_SMS)) {

            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.RECEIVE_SMS}, 0);
            }

        } else {

        }

    }

    private void permissaoLigacao() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.CALL_PHONE)) {

            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.CALL_PHONE}, 0);
            }

        } else {


        }
    }


    private void ligarParaContato(Contato contato) {

        if (contato != null) {

            Intent intentLigar = new Intent(Intent.ACTION_CALL);
            intentLigar.setData(Uri.parse("tel:" + contato.getTelefone()));
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(intentLigar);

        }

    }
}
