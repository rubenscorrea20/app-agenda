package rubenscorrea.com.app05_agendacontatos;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by Rubens on 01/07/2017.
 */

public class FormularioHelper {
    private Contato contato;

    private EditText nome;
    private EditText email;
    private EditText site;
    private EditText telefone;
    private EditText endereco;

    private ImageView imagemContato;

    private Button botaoFoto;

    public FormularioHelper(Formulario activity) {
        this.contato = new Contato();

        this.nome = (EditText) activity.findViewById(R.id.nomeFormulario);
        this.email = (EditText) activity.findViewById(R.id.emailFormulario);
        this.site = (EditText) activity.findViewById(R.id.siteFormulario);
        this.telefone = (EditText) activity.findViewById(R.id.telefoneFormulario);
        this.endereco = (EditText) activity.findViewById(R.id.enderecoFormulario);

        this.imagemContato = (ImageView) activity.findViewById(R.id.imagemFormulario);

        this.botaoFoto = (Button) activity.findViewById(R.id.botaoFormulario);
    }

    public Button getBotaoFoto() {
        return botaoFoto;
    }

    public Contato pegaContatoFormulario(){

        contato.setNome(nome.getText().toString());
        contato.setEmail(email.getText().toString());
        contato.setSite(site.getText().toString());
        contato.setTelefone(telefone.getText().toString());
        contato.setEndereco(endereco.getText().toString());

        contato.setFoto((String) imagemContato.getTag());

        return contato;
    }

    public void colocaNoFormulario(Contato contato){

        nome.setText(contato.getNome());
        email.setText(contato.getEmail());
        site.setText(contato.getSite());
        telefone.setText(contato.getTelefone());
        endereco.setText(contato.getEndereco());

        imagemContato.setTag(contato.getFoto());
        carregaImagem(contato.getFoto());

        this.contato = contato;

    }

    public void carregaImagem(String localDaFoto){
        if (localDaFoto != null){
            Bitmap imagemFoto = BitmapFactory.decodeFile(localDaFoto);
            imagemContato.setImageBitmap(imagemFoto);
            imagemContato.setTag(localDaFoto);
        }
    }
}
