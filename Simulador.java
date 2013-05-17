public class Simulador {
    Disco d;
    static int tamano_bloque = 4096;

    public Simulador(Disco d) {
        this.d = d;
    }

    private Disco parseDisco(String archivo_config) {
        //  Matteo
    }

    private int buscarSectorParaBloque(int bloque) {
        return (bloque * 8) + 1;
    }

    private Cabezal buscarCabezal(int sector) {
        for (Cabezal c: d.cabezales) {
            if (c.min <= sector && sector < c.max)
                return c;
        }
    }

    private long getLatency(Peticion p) {
        int total;

        LinkedList<int> sectores_pendientes = new LinkedList<int>();
        int num_sector;
        for (int b: p.bloques) {
            num_sector = buscarSectorParaBloque(p.inicial);
            sectores_pendientes.add(num_sector);
        }

        Collections.sort(sectores_pendientes);

        long tiempo_por_sector = 0;
        for (int sector: sectores_pendientes) {
            tiempo_por_sector += moveCabezal(buscarCabezal(sector));    
            tiempo_por_sector += procesarSector(sector);    
            total += tiempo_por_sector;
        }
        return aux1 + aux2;
    }

    private int sectorToTrack(int sector) {
    }

    private long moveCabezal(Cabezal c, int track) {
        
    }

    private long getTiempo (int tipo) {
    }
}
