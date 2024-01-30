import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

public class Peer1 extends JFrame implements Observer{
    private JFrame ventanaVotos;
    private Receiver receiver;
    private Forwarder forwarder;
    private JLabel textoServidor;
    private String host;// = "127.0.0.1";
    private int[] votos = {0,0,0};
    public Peer1(){
        crearVentanaVotos();
        host = JOptionPane.showInputDialog("IP del host: ");
        receiver  = new Receiver(5000);
        receiver.addObserver(this);
        Thread t = new Thread(receiver);
        t.start();
    }

    private void crearVentanaVotos(){

        ventanaVotos = new JFrame();
        ventanaVotos.setSize(400,436);
        ventanaVotos.setTitle("Presidential Election Process");
        ventanaVotos.setLocationRelativeTo(null);
        ventanaVotos.setDefaultCloseOperation(EXIT_ON_CLOSE);
        ventanaVotos.setResizable(false);
        ventanaVotos.setLayout(null);
        ventanaVotos.setVisible(true);

        ////////////////////SECCIÓN DE DISEÑO DE LA VENTANA DE VOTOS
        Color colorComponentes = new Color(191, 216, 254);
        Color colorComponentes2 = new Color(142, 183, 247);
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 400, 400);
        panel.setBackground(colorComponentes);
        panel.setLayout(null);
        ventanaVotos.getContentPane().add(panel);

        JButton botonChip = new JButton("Candidato1");
        botonChip.setBackground(colorComponentes2);
        botonChip.setBounds(70, 70, 260, 40);
        botonChip.setForeground(Color.white);
        botonChip.setFont(new Font("Cousine", 1, 16));
        panel.add(botonChip);

        JButton botonMack = new JButton("Candidato2");
        botonMack.setBackground(colorComponentes2);
        botonMack.setBounds(70, 170, 260, 40);
        botonMack.setForeground(Color.white);
        botonMack.setFont(new Font("Cousine", 1, 16));
        panel.add(botonMack);

        JButton botonBrad = new JButton("Candidato3");
        botonBrad.setBackground(colorComponentes2);
        botonBrad.setBounds(70, 270, 260, 40);
        botonBrad.setForeground(Color.white);
        botonBrad.setFont(new Font("Cousine", 1, 16));
        panel.add(botonBrad);

        textoServidor = new JLabel();
        textoServidor.setBounds(110, 365, 260, 30);
        panel.add(textoServidor);

        //lISTENER DE LOS TRES BOTONES
        ActionListener cargarVotoChip = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                textoServidor.setText("");
                votos[0] = votos[0] + 1;

                forwarder = new Forwarder(6000, host);
                forwarder.marshal(votos);
                Thread t = new Thread(forwarder);
                t.start();
            }
        };
        botonChip.addActionListener(cargarVotoChip);




        ActionListener cargarVotoMack = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {

                votos[1] = votos[1] + 1;
                forwarder = new Forwarder(6000, host);
                forwarder.marshal(votos);
                Thread t = new Thread(forwarder);
                t.start();
            }
        };
        botonMack.addActionListener(cargarVotoMack);

        ActionListener cargarVotoBrad = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                textoServidor.setText("");
                votos[2] = votos[2] + 1;
                forwarder = new Forwarder(6000, host);
                forwarder.marshal(votos);
                Thread t = new Thread(forwarder);
                t.start();

            }
        };
        botonBrad.addActionListener(cargarVotoBrad);

        //Este mouse listener sirve para que el texto que envia el servidor se borre
        //Y sea más evidente cuando el siguiente voto se envíe correctamente
        MouseListener borrarTexto = new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mousePressed(MouseEvent e) {
                textoServidor.setText("");
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                textoServidor.setText("");
            }
            @Override
            public void mouseExited(MouseEvent e) {
                textoServidor.setText("");
            }

        };
        botonChip.addMouseListener(borrarTexto);
        botonMack.addMouseListener(borrarTexto);
        botonBrad.addMouseListener(borrarTexto);
    }



    @Override
    public void update(Observable o, Object arg) {
        textoServidor.setText((String)arg);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Peer1();
            }
        });
    }
}
