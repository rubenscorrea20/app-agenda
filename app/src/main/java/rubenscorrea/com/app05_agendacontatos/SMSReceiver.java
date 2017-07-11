package rubenscorrea.com.app05_agendacontatos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * Created by Rubens on 07/07/2017.
 */

public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        Object[] mensagens = (Object[]) bundle.get("pdus");

        byte[] mensagem = (byte[]) mensagens[0];

        SmsMessage msg = SmsMessage.createFromPdu(mensagem);
        String telefone = msg.getDisplayOriginatingAddress();

        ContatosDAO dao = new ContatosDAO(context);

        if(dao.isContato(telefone)){

            MediaPlayer player = MediaPlayer.create(context, R.raw.hey);
            player.start();

        }

    }
}
