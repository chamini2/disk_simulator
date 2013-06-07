import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

// Imports para lectura facil de XML
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class Simulador {

    private Disco disco;
    private static int tamano_bloque = 4096;    //  Tamano de bloques de ext4

    private PriorityQueue<Peticion> peticiones; //
    private ArrayList<Accion> bloques;               //  Bloques a leer

    /**
      *
      */
    public Simulador(String file_petitions, Disco disco) {

        this.disco = disco;
        this.peticiones = leerPeticiones(file_petitions);
        this.bloques = new ArrayList<Accion>();
    }

    public void algoritmo() {

        Accion acc;
        long tiempo = 0;
        int min;

        while ((!peticiones.isEmpty()) && (!bloques.isEmpty())) {

            //Si hay bloques que leer/escribir
            if (!bloques.isEmpty()) {

                min = this.getClosestBlock();
                acc = bloques.remove(min);
                tiempo += this.procesarBloque(acc.getBloque(), acc.getTipo());

            }
        }
    }

    public int getClosestBlock() {

        int track = disco.getCilindroActual();
        int distancia = disco.getNumCilindros();
        int minimo = track;
        int act;
        int sector;
        Accion este;

        // Busco el bloque en el track mas cercano
        for (int i = 0; i < bloques.size(); i++) {
            este = bloques.get(i);
            sector = disco.buscarSectorParaBloque(este.getBloque());
            act = disco.buscarCilindroParaSector(sector);

            act = Math.abs(act - track);

            if (act < distancia) {
                distancia = act;
                minimo = i;
            }
        }

        return minimo;
    }

    /*
     * Parsea la informacion de un disco duro definida en un archivo
     * de configuracion
     */
    private Disco leerDisco(String archivo_config) {

        return null;
    }

    /*
     * Modifica la variable 'peticiones'
     */
    public PriorityQueue<Peticion> leerPeticiones(String xml) {
        PriorityQueue<Peticion> lista = new PriorityQueue<Peticion>();

        try {
            File file = new File(xml);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            NodeList petitions, blocks;
            Element petE, blE;
            Node pet, bl;


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

                    lista.add(in);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
      * Dado un numero de bloque del sistema de archivos devuelve el tiempo
      * tomado por el disco duro para leer o escribir dicho bloque
      */
    private long procesarBloque(int bloque, char tipo) {
        int sector = disco.buscarSectorParaBloque(bloque);
        return disco.procesarSector(sector, tipo);
    }

    // private long getHandleTime(Peticion p) {
    //     int total = 0;

    //     List<Integer> dispatchQueue = new LinkedList<Integer>();
    //     Integer sector;
    //     for (Integer b: p.getBloques()) {
    //         sector = buscarSectorParaBloque(b);
    //         dispatchQueue.add(sector);
    //     }

    //     Collections.sort(dispatchQueue);

    //     for (int sectorAux: dispatchQueue) {
    //         total += d.procesarSector(sectorAux, p.getTipo());
    //     }
    //     return total;
    // }
}
