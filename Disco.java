import java.util.ArrayList;
 
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
    int tasaLectura;            // MB / milisegundos
    int tasaEscritura;          // MD / milisegundos

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
