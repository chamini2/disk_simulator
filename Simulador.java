import java.util.List;
import java.util.LinkedList;
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
    Disco d;
    static int tamano_bloque = 4096;    //  Tamano de bloques de ext4
    List<Peticion> peticiones; //

    public Simulador(String file_disk, String file_petitions) {
        this.d = d;
        this.peticiones = null;

    }

    private Disco leerDisco(String archivo_config) {
        //  Matteo
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

    /**
      * Dado un numero de bloque devuelve el tiempo tomado por el disco duro
      * para leer o escribir dicho bloque
      */
    private long procesarBloque(int bloque, char tipo) {
        int sector = d.buscarSectorParaBloque(bloque);
        return d.procesarSector(sector, tipo);
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
