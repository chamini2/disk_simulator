import java.util.List;
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

    private long procesarBloque(int bloque, char tipo) {
        int sector = d.buscarSectorParaBloque(bloque);
        return d.procesarSector(sector, tipo);
    }

    //private long getHandleTime(Peticion p) {
        //int total = 0;

        //List<Integer> dispatchQueue = new LinkedList<Integer>();
        //Integer sector;
        //for (Integer b: p.getBloques()) {
            //sector = buscarSectorParaBloque(b);
            //dispatchQueue.add(sector);
        //}

        //Collections.sort(dispatchQueue);

        //for (int sectorAux: dispatchQueue) {
            //total += d.procesarSector(sectorAux, p.getTipo());
        //}
        //return total;
    //}
}
