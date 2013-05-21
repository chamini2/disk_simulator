import java.util.LinkedList;
import java.util.Collections;

public class Simulador {
    Disco d;
    static int tamano_bloque = 4096;    //  Tamano de bloques de ext4

    public Simulador(Disco d) {
        this.d = d;
    }

    private Disco parseDisco(String archivo_config) {
        //  Matteo
        return null;
    }

    private int buscarSectorParaBloque(int bloque) {
        return (bloque * 8) + 1;
    }

    private long getHandleTime(Peticion p) {
        int total = 0;

        LinkedList<Integer> dispatch_queue = new LinkedList<Integer>();
        Integer sector;
        for (Integer b: p.getBloques()) {
            sector = buscarSectorParaBloque(b);
            dispatch_queue.add(sector);
        }

        Collections.sort(dispatch_queue);

        int trackAux;
        for (int sectorAux: dispatch_queue) {
            Cabezal c = d.buscarCabezal(sectorAux);
            trackAux = buscarTrackParaSector(sectorAux);
            total += d.moverBrazo(trackAux);
            total += d.procesarSector(sectorAux, p.getTipo());    
        }
        return total;
    }

    private int buscarTrackParaSector(int sector) {
        //  PENDIENTE
        return 0;
    }
}
