import java.util.List;
 
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
