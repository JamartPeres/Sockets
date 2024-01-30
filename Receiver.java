import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Receiver extends Observable implements Runnable {

    private int puerto;

    public Receiver(int puerto) {
        this.puerto = puerto;
    }

    @Override
    public void run() {
        ServerSocket servidor = null;
        Socket sc = null;
        DataInputStream in;

        try {
            //Creamos el socket del servidor
            servidor = new ServerSocket(puerto);

            //Siempre estara escuchando peticiones
            while (true) {
                //Espero a que un cliente se conecte
                sc = servidor.accept();
                //System.out.println("Cliente conectado");
                in = new DataInputStream(sc.getInputStream());
                //Leo el mensaje que me envia
                String mensaje = in.readUTF();

                this.setChanged();
                this.notifyObservers(mensaje);
                this.clearChanged();

                //Cierro el socket
                sc.close();

            }

        } catch (IOException ex) {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public int[] unmarshal(String votos){
        JSONObject jsonVotos = new JSONObject(votos);
        Object votos1 = jsonVotos.get("Votos1");
        Object votos2 = jsonVotos.get("Votos2");
        Object votos3 = jsonVotos.get("Votos3");
        int[] arrayVotos = {(int)votos1, (int)votos2, (int)votos3};
        return arrayVotos;
    }

}