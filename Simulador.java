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

public class Simulador implements Runnable {

    private Disco disco;
    private Reloj reloj;
    private static int tamano_bloque = 4096;    //  Tamano de bloques de ext4

    private PriorityQueue<Peticion> peticiones; //
    private ArrayList<Accion> bloques;               //  Bloques a leer

    /**
      *
      */
    public Simulador(String file_petitions, Disco disco, Reloj reloj) {

        this.disco = disco;
        this.reloj = reloj;
        this.peticiones = leerPeticiones(file_petitions);
        this.bloques = new ArrayList<Accion>();
    }

    public void run() {

        try{
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            System.out.println("Interrumpido");
        }
        Accion acc;
        Peticion p = null;
        long tiempo = 0;
        int min;
        int estado[];
        int reads = 0;
        int writes = 0; 
        int tiempoWrites = 0;
        int tiempoReads = 0;
        int tiempoTotal = 0;
        long tiempoAnterior = 0;
        boolean b;

        while (!peticiones.isEmpty()) {

            // Reviso si la peticion esta en tiempo
            p = peticiones.peek();
            if (p == null) {
                break;
            }

            if (this.reloj.getTiempo() >= p.getTiempo()) {
                peticiones.poll();
                /*Se agregan acciones a la lista de bloques*/
                for (int bloq : p.getBloques()) {
                    acc = new Accion(bloq, p.getTipo());
                    bloques.add(acc);
                }
            }

            b = false;
            if (bloques.isEmpty()){
                tiempoAnterior = this.reloj.getTiempo();
                b = true;
            }
            //Si hay bloques que leer/escribir
            while (!bloques.isEmpty()) {
                
                min = this.getClosestBlock();
                acc = bloques.remove(min);
                p.setTiempo(this.reloj.getTiempo());
                tiempo = this.procesarBloque(acc.getBloque(), acc.getTipo(),p);
                tiempo *= this.reloj.getTicker();
                System.out.println("---------------------------------------");
                //System.out.println("DUrmiendo " + tiempo);
                //System.out.println("ticker " + reloj.getTicker());

                if (acc.getTipo() == 'R') {
                    ++reads;
                    tiempoReads += tiempo;
                    tiempoTotal += tiempo;
                } else {
                    ++writes;
                    tiempoWrites += tiempo;
                    tiempoTotal += tiempo;
                }

                System.out.println("Peticion atendida en ");
                System.out.println("p.sector" + (acc.getBloque() * 8));
                System.out.println("cilindro" + disco.getCilindroActual());

               //Se duerme para simular tiempo
                try {
                    Thread.sleep(tiempo);
                } catch (InterruptedException e){
                    System.out.println("Sleep interrumpido.");
                }
            }
            if (b)
                tiempoTotal += this.reloj.getTiempo() - tiempoAnterior;
        }

        //  Estadisticas
        p.setTiempo(-1);
        disco.setValues(-1,-1,-1,p);

        disco.setEstadisticas(reads, writes, tiempoReads, tiempoWrites, tiempoTotal);
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
    private long procesarBloque(int bloque, char tipo, Peticion p) {
        int sector = disco.buscarSectorParaBloque(bloque);
        return disco.procesarSector(sector, tipo, p);
    }

}
