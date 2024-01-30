import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

public class Peer2 extends JFrame implements Observer {
    private Receiver receiver;
    private Forwarder forwarder;
    private JFrame ventanaPastel;
    private JFrame ventanaBarras;
    private String host;

    private DefaultPieDataset dataset;
    private DefaultCategoryDataset dataset2;
    public Peer2(){
        instanciarComponentes();
        host = JOptionPane.showInputDialog("IP del otro Peer: ");
        //this.getRootPane().setDefaultButton(this.btnEnviar);
        receiver = new Receiver(6000);
        receiver.addObserver(this);
        Thread t = new Thread(receiver);
        t.start();
    }
    public void instanciarComponentes(){
        ventanaPastel = new JFrame();
        ventanaBarras = new JFrame();
        dataset = new DefaultPieDataset();
        dataset2 = new DefaultCategoryDataset();
    }

    @SuppressWarnings("removal")
    public void crearVistaPastel(int votosChip, int votosMack, int votosKalvo){
        dataset.setValue("Chip Wright", new Double(votosChip));
        dataset.setValue("Mack SF", new Double(votosMack));
        dataset.setValue("Brad Kalvo", new Double(votosKalvo));

        JFreeChart graficaPastel = ChartFactory.createPieChart("Presidential Election Process", dataset, true, true, false);
        ChartPanel panel = new ChartPanel(graficaPastel);

        ventanaPastel.setVisible(true);
        ventanaPastel.setSize(700, 500);
        ventanaPastel.add(panel);
    }
    public void crearVistaBarras(int votosChip, int votosMack, int votosBrad){
        dataset2.setValue(votosChip, "Chip Wright", "Chip Wright");
        dataset2.setValue(votosMack, "MackSF", "Mack SF");
        dataset2.setValue(votosBrad, "Brad Kalvo", "Brad Kalvo");

        JFreeChart graficaBarras = ChartFactory.createBarChart3D("Candidatura a la presidencia", "Candidatos", "Votos",dataset2,  PlotOrientation.VERTICAL, true, true, false);
        ChartPanel panel2 = new ChartPanel(graficaBarras);

        ventanaBarras.setLocation(0,500);
        ventanaBarras.setVisible(true);
        ventanaBarras.setSize(700, 500);
        ventanaBarras.add(panel2);
    }

    @Override
    public void update(Observable o, Object arg) {
        //String host = "127.0.0.1";
        int[] arrayVotos = receiver.unmarshal((String)arg);

        int votosChip = arrayVotos[0];
        int votosMack = arrayVotos[1];
        int votosBrad = arrayVotos[2];
        this.crearVistaPastel(votosChip, votosMack, votosBrad);
        this.crearVistaBarras(votosChip, votosMack, votosBrad);

        forwarder = new Forwarder(5000, host);
        forwarder.setMensaje("Voto capturado correctamente");
        Thread t = new Thread(forwarder);
        t.start();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Peer2();
            }
        });
    }
}
