import org.json.JSONObject;


import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Forwarder implements Runnable {
    private int puerto;
    private String mensaje;
    private String host;

    public Forwarder(int puerto, String host) {
        this.puerto = puerto;
        this.host = host;
    }

    public void marshal(int[] votos){
        JSONObject sampleObject = new JSONObject();
        sampleObject.put("Votos1", votos[0]);
        sampleObject.put("Votos2", votos[1]);
        sampleObject.put("Votos3", votos[2]);
        this.mensaje = sampleObject.toString();
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public void run() {
        DataOutputStream out;
        try {
            //Creo el socket para conectarme con el cliente
            Socket sc = new Socket(host, puerto);
            out = new DataOutputStream(sc.getOutputStream());
            //Envio un mensaje al cliente
            out.writeUTF((String)mensaje);
            sc.close();

        } catch (IOException ex) {
            Logger.getLogger(Forwarder.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}