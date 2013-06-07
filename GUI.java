import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.JTextArea;
import javax.swing.BoxLayout;
import javax.swing.text.DefaultCaret;
public class GUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private Board contentPane;
    private Grafico grafo;
    private JTextArea textArea;
    private JScrollPane scrollPaneGr;
    private Menu menu;
    private Simulador simulador;
    private Disco disco;

    public GUI(Disco disco){
        this.disco = disco;
        initMenu();
    }

    /*Genera el Menu*/
    private final void initMenu(){
        menu = new Menu();
        Dimension tam;
        JButton start;
        menu.setPreferredSize(new Dimension(900,700));
        menu.setLayout(null);
        setContentPane(menu);
        start = new JButton("Continuar");
        start.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e){
                initUI();
            }
        });

        menu.add(start);
        tam = start.getPreferredSize();
        start.setBounds(450 - (tam.width / 2),600,tam.width,tam.height);


        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

    }

   
    /*Inicializa la interfaz*/
    private final void initUI(){
        JScrollPane scrollPaneLog;
        DefaultCaret caret;
        Dimension tam;
        final GUI gui = this;
        JButton start = new JButton("Comenzar Simulación");
        start.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e){
                int values[];
                int tick      = menu.getReloj();
                String dif    = menu.getDiferencia();
                int den       = menu.getDensidad();

                //Aqui se genera el xml con densidad y diferencia

                Reloj reloj   = new Reloj(tick);
                Simulador s   = new Simulador("./archivo.xml", disco, reloj);
                Pintor pintor = new Pintor(gui, disco);
                Thread hReloj = new Thread(reloj, "Hilo de reloj");
                Thread hSim   = new Thread(s, "Hilo de simulador");
                Thread h      = new Thread(pintor, "hilo que pinta");
                hReloj.start();
                hSim.start();
                h.start();

            }
        });


        /*Panel general*/
        contentPane = new Board(50, this.disco.getNumCilindros());
        contentPane.setPreferredSize(new Dimension(900, 700));
        setContentPane(contentPane);

        contentPane.add(start);
        tam = start.getPreferredSize();
        start.setBounds(100 + (tam.width / 2),600,tam.width,tam.height);
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

    /*Refresca la interfaz*/
    public boolean refreshInterface(int values[]) {


        if (values[3] == -2) {
            agregarRegistro("Estadisticas: ");
            return false;
        }
        pintarCilindro(values[0]);
        cambiarDisco(values[1]);
        cambiarCabezal(values[2]);
        if (values[3] != -1) {
            agregarRegistro("Peticion atendida en tiempo "+values[3]);
        }
        pintarPeticion(values[0]);
        return true;
    }
}
