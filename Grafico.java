import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.JPanel;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;

public class Grafico extends JPanel {
    private static final long serialVersionUID = 3L;
    private Punto punto;                //Posicion del ultimo punto dibujado (si es que existe)
    private int sectores;               //Cantidad de sectores para posicionar los puntos
    private int length;                 //Largo del eje de referencia para el grafo
    private AffineTransform at;         //Transformador para dibujos 2D
    private Graphics2D g2;              //Grafico a pintar
    private ArrayList<Punto> puntos;    //ArrayList de puntos a pintar 
    private ArrayList<Punto> puntosR;    //ArrayList de puntos a pintar 


    public Grafico(int sectores){
        this.punto = null;
        this.puntos = new ArrayList<Punto>();
        this.length = 300;
        this.sectores = this.length / sectores;


    }

    /*Getter punto*/
    public Punto getPunto(){
        return this.punto;
    }


    
    
    /*Metodo que pinta los puntos del arraylist*/
    private void pintarPuntos(){
        int x, y, i, length;
        Punto anterior = null;

        length = this.puntos.size();

        for (i = 0; i < length; ++i) {
            x = this.puntos.get(i).getX();
            y = 15 + (length - (i + 1)) * 15;
            this.puntos.get(i).setY(y);
            g2.drawString(""+this.puntos.get(i).getSector(), x, y-5);
            g2.setColor(Color.blue);
            g2.fillOval(x,y,5,5);
            if (anterior != null){

                this.contectarPuntos(anterior, this.puntos.get(i));
            }
            anterior = this.puntos.get(i);

        }
        g2.setColor(Color.gray);
    }

    private void contectarPuntos(Punto a, Punto b){
        Line2D line;
        Dimension d;
        int x1, y1, x2, y2, w, h;

        d = this.getPreferredSize();
        w = (int)d.getWidth() / 2;
        h = (int)d.getHeight() / 2;
        x1 = a.getX() - w;
        x2 = b.getX() - w;
        y1 = a.getY() - h;
        y2 = b.getY() - h;
        line = new Line2D.Double(x1, y1, x2, y2);
        g2.draw(this.at.createTransformedShape(line));
    }

    /*Metodos publicos para manipular el grafo*/

    /*Metodo para pintar*/
    public void paint(Graphics g){
        double w,h,r,x,y;
        Line2D line;
        JScrollPane ta;
        super.paint(g);

        g2 = (Graphics2D) g;

        /*Se crean los Rendering hints para evitar aliasing en los valores*/
        RenderingHints rh =
            new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2.setRenderingHints(rh);

        /*Se obtienen las dimensiones y se setea el trazo y el color*/
        Dimension size = getSize();
        w = size.getWidth();
        h = size.getHeight();
        g2.setStroke(new BasicStroke(1));
        g2.setColor(Color.gray);

        /*Se crea un transformador de formas para tener mas precision sobre ellas*/
        this.at = AffineTransform.getTranslateInstance(w / 2, h / 2);
        
        /*Se pintan los puntos del arrayList*/
        this.pintarPuntos();
    }

    /*Agrega punto de peticion de sector al arraylist de puntos*/
    public void agregarPunto(int sector){
        Punto p;
        int x, y;

        /*Se calcula la posicion del sector*/
        x = sector * this.sectores; 
        y = this.puntos.size();     //El indice en el cual fue agregado el punto
        p = new Punto(x,y,sector);
        if (y == 0)
            this.punto = p;
        this.puntos.add(p);
    }

}
