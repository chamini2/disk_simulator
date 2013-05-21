import java.util.ArrayList;
// Imports para lectura facil de XML
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class Disco {
    int cilindros;          // Cilindros del disco
    int capacidadDisco;     //
    int capacidadPlatos;
    int tamanoSector;
    int trackActual;
    int densidad;
    ArrayList<Cabezal> cabezales;
    int rpm;
    int averageSeekTime;
    float latenciaRotacional;
    // int tfreno, tarranque;      //
    int tasaLectura;            // MB / milisegundos
    int tasaEscritura;          // MD / milisegundos
    ArrayList<Peticion> peticiones;

    public Disco(int cs, int cd, int cp, int ts, int ta, int d,
                 ArrayList<Cabezal> cbs, int rpm, int ast, float lr,
                 int tl, int te) {
        this.cilindros = cs;
        this.capacidadDisco = cd;
        this.capacidadPlatos = cp;
        this.tamanoSector = ts;
        this.trackActual = ta;
        this.densidad = d;
        this.cabezales = cbs;
        this.rpm = rpm;
        this.averageSeekTime = ast;
        this.latenciaRotacional = 60 / this.rpm;
        this.tasaLectura = tasaLectura / 1000;
        this.tasaEscritura = tasaEscritura / 1000;
    }

    public long moverBrazo(int trackDestino) {
        this.trackActual = trackDestino;
        return Math.abs(trackDestino - trackActual) * averageSeekTime;
    }

    /*
     * Modifica la variable 'peticiones'
     */
    public void leerPeticiones(String xml) {

        try {
            File file = new File(xml);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            NodeList petitions, blocks;
            Element petE, blE;
            Node pet, bl;

            this.peticiones = new ArrayList<Peticion>();
            Peticion in;
            String prioridad, tipo;
            int tiempo, bloque;

            doc.getDocumentElement().normalize();

            petitions = doc.getElementsByTagName("petition");

            for (int i = 0; i < petitions.getLength(); i++) {

                pet = petitions.item(i);
                if (pet.getNodeType() == Node.ELEMENT_NODE) {

                    petE = (Element) pet;

                    prioridad = petE.getElementsByTagName("priority")
                                .item(0).getTextContent();
                    tiempo = Integer.parseInt(petE.getElementsByTagName("time")
                                              .item(0).getTextContent());
                    tipo = petE.getElementsByTagName("type")
                           .item(0).getTextContent();

                    in = new Peticion(tiempo, tipo, prioridad);

                    blocks = petE.getElementsByTagName("block");

                    for (int j = 0; j < blocks.getLength(); j++) {

                        bl = blocks.item(j);
                        bloque = Integer.parseInt(bl.getTextContent());

                        in.addBloque(bloque);
                    }

                    peticiones.add(in);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Cabezal buscarCabezal(int sector) {
        for (Cabezal c: this.cabezales) {
            if (c.min <= sector && sector < c.max)
                return c;
        }
        return null;
    }

    public long procesarSector(int sector, char tipo) {
        long total = 0;
        if (tipo == 'r')
            total = sector / tasaLectura;
        else if (tipo == 'w')
            total = sector / tasaEscritura;

        return total;
    }
}
