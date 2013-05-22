import java.util.List;
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
    int capacidadDisco;         //  Tamano en bytes del disco duro
    int capacidadPlatos;        //  Tamano en bytes de cada plato
    int tamanoSector;           //  Tamano en bytes de cada sector del dd
    int trackActual;            //  Track actual en la que esta posicionado el brazo
    int densidad;
    List<Cabezal> cabezales;

    int rpm;                    //  Revoluciones por minuto de los platos
    int averageSeekTime;        //  Tiempo promedio de mover el cabezal de un track al siguiente
    float latenciaRotacional;   //  Tiempo promedio de espera para que leer o escribir un sector
    int tasaLectura;            //  MB / milisegundos
    int tasaEscritura;          //  MB / milisegundos
	ArrayList<Peticion> peticiones;

    int diametroPlato;
    int sectoresPorTrack;

    public Disco(int cs, int cd, int cp, int ts, int ta, int d,
                 ArrayList<Cabezal> cbs, int rpm, int ast, float lr,
                 int tl, int te, int dp) {
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
        this.diametroPlato = dp;
        //this.sectoresPorTrack
		this.peticiones = null;
    }

    public int getTrackActual() {
        return this.trackActual;
    }

    public long procesarSector(int sector, char tipoAccion) {
        long total = 0;
        Cabezal c = buscarCabezal(sectorAux);
        int trackAux = buscarTrackParaSector(sectorAux);

        total += moverBrazo(trackAux);
        total += efectuarAccion(sectorAux, tipoAccion);
        return total;
    }

    private long moverBrazo(int trackDestino) {
        this.trackActual = trackDestino;
        return Math.abs(trackDestino - trackActual) * averageSeekTime;
    }

    private Cabezal buscarCabezal(int sector) {
        for (Cabezal c: this.cabezales) {
            if (c.min <= sector && sector < c.max)
                return c;
        }
        return null;
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

    private long efectuarAccion(int sector, char tipo) {
        long total = 0;
        if (tipo == 'r')
            total = sector / tasaLectura;
        else if (tipo == 'w')
            total = sector / tasaEscritura;
        return total;
    }

    private int buscarTrackParaSector(int sector) {
        //  PENDIENTE
        return 0;
    }

    private int buscarSectorParaBloque(int bloque) {
        return (bloque * 8) + 1;
    }
}
