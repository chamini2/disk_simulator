import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.JPanel;

public class Board extends JPanel {

    private static final long serialVersionUID = 2L;
    private int disco, sector, cabezal;
    private Graphics2D g2;
    private AffineTransform at;
    private int track;
    private int TBP;
    private int sectores;
    
    public Board(int TBP, int sectores){
        super(null);
        setBackground(Color.WHITE);
        this.track    = 3; //Pista pintada por default
        this.disco    = 1;
        this.sector   = 1;
        this.cabezal  = 1;
        this.TBP      = TBP;
        this.sectores = sectores; 
    }

    public int getTrack(){
        return this.track;
    }

    public void setTrack(int track){
        this.track = track;
    }

    public void setDisco(int disco){
        this.disco = disco;
    }
    public void setSector(int sector){
        this.sector = sector;
    }
    public void setCabezal(int cabezal){
        this.cabezal = cabezal;
    }


    /*Metodos publicos para manipular el board*/

    /*Pinta el panel*/
    public void paint(Graphics g)
    {
        double w,h,r,x,y, densidad;
        int i;
        Arc2D arc;
        Line2D line;
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


        g2.setColor(Color.gray);
        densidad = 350.0 / this.TBP;
        /*Ciclo que crea el plato del disco duro*/
        for(r = 350, x=-400, y=-300; 
                r > 0; r-=densidad, x+= densidad / 2, y+= densidad / 2){
            arc = new Arc2D.Double(x,y,r,r,0,360,1);
            g2.draw(at.createTransformedShape(arc)); //Dibuja la forma transformada con precision double

        }


        
        pintarCilindro(this.track);

        /*Identificadores de disco, sector y cabezal en la interfaz*/
        g2.drawString("Disco:", 350, 30);
        g2.drawString(""+disco, 410, 30);
        g2.drawString("Sector:", 350, 50);
        g2.drawString(""+sector, 410, 50);
        g2.drawString("Cabezal:", 350, 70);
        g2.drawString(""+cabezal, 410, 70);

        /*Caja para log*/
        g2.drawString("Registro", 490, 395);

        /*Leyenda de discos y brazos*/
        for (i = 100; i < 150; i += 25){
            arc = new Arc2D.Double(-300,i,250,50,0,360,1);
            g2.draw(at.createTransformedShape(arc)); //Dibuja la forma transformada con precision double

        }

        /*Linea para el grafo*/
        g2.drawString("0", 492, 40);
        g2.drawString(""+this.sectores, 794, 40);
        line = new Line2D.Double(45, -300, 45, -305);
        g2.draw(at.createTransformedShape(line));
        line = new Line2D.Double(350, -300, 350, -305);
        g2.draw(at.createTransformedShape(line));
        line = new Line2D.Double(45, -300, 350, -300);
        g2.draw(at.createTransformedShape(line));

        /*Se pinta el disco actual en la leyenda*/
        this.pintarDisco(this.disco, Color.blue);
        /*Para seleccionar el segundo disco, se pinta el primero de blanco*/
        if (this.disco == 2)
            this.pintarDisco(1, Color.white);

        /*Dibuja los brazos */
        this.dibujarBrazo(1);
        this.dibujarBrazo(2);
        this.dibujarBrazo(3);

        /*Pinta el brazo actual*/
        this.pintarBrazo(this.cabezal);

        /*Guia para sector actual*/
        g2.setColor(Color.orange);
        g2.setComposite(AlphaComposite.getInstance(
                                AlphaComposite.SRC_OVER, 0.3f));
        /*Area leida en el disco (sector actual)*/
        for (i = -250; i <= -200; ++i){
            line = new Line2D.Double(-225, -125, i, -300);
            g2.draw(at.createTransformedShape(line));

        }

    }

    /*Dibuja un brazo sobre la leyenda*/
    private void dibujarBrazo(int brazo){
        Line2D line;
        int y0, y1;

        y0 = 83 + (27 * brazo);
        y1 = y0 + 20;
        line = new Line2D.Double(-330, y0 , -150, y1);
        g2.draw(at.createTransformedShape(line));
        line = new Line2D.Double(-330, y0 + 10, -150, y1);
        g2.draw(at.createTransformedShape(line));
        line = new Line2D.Double(-330, y0, -330, y0 + 10);
        g2.draw(at.createTransformedShape(line));

    }

    /*Pinta el brazo actual de la leyenda dado el cabezal*/
    public void pintarBrazo(int cabezal){
        Line2D line;
        int brazo, y0, y1, limite;

        /*Transformacion de cabezal a brazo*/
        switch (cabezal){
            case 0:
                brazo = 1;
                break;

            case 1:
                brazo = 2;
                break;

            case 2:
                brazo = 2;
                break;

            default:
                brazo = 3;
                break;
        }

        y0 = 83 + (27 * brazo);
        limite = y0 + 10;
        y1 = y0 + 20;

        /*Pinta el brazo*/
        for ( ; y0 <= limite; ++y0){
            line = new Line2D.Double(-330, y0 , -150, y1);
            this.g2.draw(at.createTransformedShape(line));
        }


    }


    /*Pinta un cilindro de un color*/
    private void pintarCilindro(int track){
        double r,x,y,densidad;

        densidad = 350.0 / this.TBP;
        Arc2D arc;

        /*Se setea el color de relleno*/
        this.g2.setColor(Color.yellow);
        /*Ciclo que llena entre un track*/
        for(r = track*densidad, 
                x=-400 + (this.TBP - track)*(densidad / 2), 
                y=-300 + (this.TBP - track)*(densidad / 2); 
                r > track*densidad - densidad + 0.1;
                r-=1, x+= 0.5, y+=0.5){
            arc = new Arc2D.Double(x,y,r,r,0,360,1);
            this.g2.draw(at.createTransformedShape(arc)); //Dibuja la forma transformada con precision double
                }
        this.g2.setColor(Color.gray);
    }

    private void pintarDisco(int disco, Color color){
        double w, h, x, y;

        Arc2D arc;

        this.g2.setColor(color);
        /*Ciclo que pinta el disco 'disco'*/
        for (w = 250, h = 50, x = -300, y = 75 + 25 * disco;
                w > 0; w -= 1, h -= 1, x += 0.5, y += 0.5){
            arc = new Arc2D.Double(x,y,w,h,0,360,1);
            this.g2.draw(at.createTransformedShape(arc)); //Dibuja la forma transformada con precision double
                }
        this.g2.setColor(Color.gray);
    }



}
