import javax.swing.*;
import java.awt.*;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.JTextArea;
import javax.swing.BoxLayout;
import javax.swing.text.DefaultCaret;
public class GUI extends JFrame {
    private Board contentPane;
    private Grafico grafo;
    private JPanel log;
    private JTextArea textArea;
    private JScrollPane scrollPaneGr;

    public GUI(int TBP){
        initUI(TBP);
    }

    /*Inicializa la interfaz*/
    private final void initUI(int TBP){
        JScrollPane scrollPaneLog;
        DefaultCaret caret;

        /*Panel general*/
        contentPane = new Board(TBP);
        contentPane.setPreferredSize(new Dimension(900, 700));
        setContentPane(contentPane);

        /*Scrolling panel para log*/
        textArea = new JTextArea();
        textArea.setEditable(false);
        caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        scrollPaneLog = new JScrollPane(textArea, 
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneLog.setBounds(490, 400, 350, 270);


        /*Scrolling para grafico de requests*/
        this.grafo = new Grafico(150);
        this.grafo.setPreferredSize(new Dimension(340, 260));
        this.grafo.setBackground(Color.WHITE);
        scrollPaneGr = new JScrollPane(grafo, 
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneGr.setBounds(490, 100, 360, 270);

        contentPane.add(scrollPaneLog);
        contentPane.add(scrollPaneGr);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    
    /*Aumenta el tamaño del panel del grafo para el scroll*/
    private void aumentarGrafo(){
        int w, h, y;
        Dimension d;
        JViewport vista;
        Rectangle nVista;

        d = this.grafo.getPreferredSize();
        w = (int) d.getWidth();
        h = (int) d.getHeight();

        this.grafo.setPreferredSize(new Dimension(w, h+10));

        this.grafo.revalidate();
        this.revalidate();
    }


/*Metodos publicos para manipular interfaz*/

/*Pinta un punto en el grafo de peticiones, y si hay alguna anterior, los une con un vector*/
public void pintarPeticion(int sector){
    this.grafo.agregarPunto(sector);
    if (this.grafo.getPunto() != null) {
        if (this.grafo.getPunto().getY() >= 250){
            this.aumentarGrafo();
        }
    }
    this.grafo.repaint();
}

/*Pinta un track del disco que se ve*/
public void pintarCilindro(int track){
    this.contentPane.setTrack(track);
    this.contentPane.repaint();
}

/*Agrega un registro a la caja del log*/
public void agregarRegistro(String registro){
    this.textArea.append(registro+"\n");
    this.revalidate();
}

/*Cambia el disco actual de la interfaz*/
public void cambiarDisco(int disco){
    this.contentPane.setDisco(disco);
    this.contentPane.repaint();
}

/*Cambia el sector actual de la interfaz*/
public void cambiarSector(int sector){
    this.contentPane.setSector(sector);
    this.contentPane.repaint();
}

/*Cambia el sector actual de la interfaz*/
public void cambiarCabezal(int cabezal){
    this.contentPane.setCabezal(cabezal);
    this.contentPane.repaint();
}
}
