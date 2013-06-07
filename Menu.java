import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.*;

public class Menu extends JPanel{
    private static final long serialVersionUID = 4L;
    private int reloj;          //Tic del reloj en milisegundos
    private int densidad;       //Cantidad de peticiones por unidad de tiempo
    private String diferencia;  //Diferencia entre peticiones read y write

    public Menu(){
        this.reloj = 1;
        this.densidad = 0;
        this.diferencia = null;
        initMenu();
    }

    /*Getters*/

    public int getReloj(){
        return this.reloj;
    }

    public int getDensidad(){
        return this.densidad;
    }

    public String getDiferencia(){
        return this.diferencia;
    }

    /*Inicializa interfaz de menu*/
    public final void initMenu(){
        JLabel descTic, descDen, descDif, title; // Labels de descripcion
        JComboBox<String> ticReloj;              // Select box para el tic del reloj
        JComboBox<String> densidadPeticion;      // Select box para densidad de peticiones
        JComboBox<String> diferenciaWR;          // Select box para diferencia entre escritura y lectura
        String[] tics = { "1 milisegundo","10 milisegundos","100 milisegundos","1 segundo"};    //Opciones para el tic del reloj
        String[] den  = { "Regular", "Muchas", "Pocas"};
        String[] dif  = { "Escritura == Lectura", "Escritura > Lectura", "Escritura < Lectura"};
        Rectangle rec;
        Dimension tam;
        ActionListener action;

        /*Selects*/
        ticReloj = new JComboBox<String>(tics);
        densidadPeticion = new JComboBox<String>(den);
        diferenciaWR = new JComboBox<String>(dif);

        /*Labels*/
        title   = new JLabel("Simulador de disco");
        title.setFont(new Font("Serif", Font.PLAIN, 30));
        descTic = new JLabel("Frecuencia del reloj");
        descDen = new JLabel("Densidad de peticiones");
        descDif = new JLabel("Relación Escritura/Lectura");

        /*Se agregan al Panel*/
        add(title);
        add(descTic);
        add(ticReloj);
        add(descDen);
        add(diferenciaWR);
        add(descDif);
        add(densidadPeticion);

        /*Se ubican en la inerfaz*/
        tam = title.getPreferredSize();
        title.setBounds(450 - (tam.width / 2) , 50, tam.width, tam.height);

        locateComboBox(ticReloj, descTic, 50, 200);
        locateComboBox(diferenciaWR, descDif, 50, 300);
        locateComboBox(densidadPeticion, descDen, 50, 400);
        
        /*Action listeners*/

        action = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> cb  = (JComboBox<String>) e.getSource();
                String ticR   = (String)cb.getSelectedItem();
                switch (ticR) {
                    case "1 milisegundo":
                        reloj = 1;
                        break;
                    case "10 milisegundos":
                        reloj = 10;
                        break;
                    case "100 milisegundos":
                        reloj = 100;
                        break;
                    case "1 segundo":
                        reloj = 1000;
                        break;
                }

            }
        };
        ticReloj.addActionListener(action);

        action = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> cb  = (JComboBox<String>) e.getSource();
                String s   = (String)cb.getSelectedItem();
                switch (s) {
                    case "Regular":
                        densidad = 0;
                        break;
                    case "Muchas":
                        densidad = 1;
                        break;
                    case "Pocas":
                        densidad = -1;
                        break;
                }

            }
        };
        densidadPeticion.addActionListener(action);

        action = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> cb  = (JComboBox<String>) e.getSource();
                String s   = (String)cb.getSelectedItem();
                switch (s) {
                    case "Escritura > Lectura":
                        diferencia = "R";
                        break;
                    case "Escritura == Lectura":
                        diferencia = "W";
                        break;
                    case "Escritura < Lectura":
                        diferencia = null;
                        break;
                }

            }
        };
        diferenciaWR.addActionListener(action);

    }

    /*Ubica el select con su label*/
    private void locateComboBox(JComboBox<String> box, JLabel label, int x, int y){
        Rectangle rec;
        Dimension tam;

        tam = box.getPreferredSize();
        box.setBounds(x, y, tam.width, tam.height);
        rec = box.getBounds();
        tam = label.getPreferredSize();
        label.setBounds((int)rec.getX(),(int)(rec.getY() - 30), tam.width, tam.height);
    }

    /*Metodo para pintar el panel con un fondo*/
    @Override 
    protected void paintComponent(Graphics g){
       /* Image bg;*/
        //try{
            //bg = ImageIO.read(new File("./fondo1toekoms.jpg"));
            //g.drawImage(bg, 0, 0, null);
        //} catch (IOException e) {
            //System.out.println("No exite el archivo");
        /*}*/
    }
}
