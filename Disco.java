import java.util.List;
import java.util.ArrayList;

public class Disco {

    int capacidadDisco;             //  Tamano en bytes del disco duro
    int numPlatos;                  //  Numero de platos del disco duro
    int tamanoSector;               //  Tamano en bytes de cada sector del dd
    int numCilindros;               //  Numero de cilindros 
    List<Cabezal> cabezales;        //  Lista de cabezales del disco duro
    int trackActual;                //  Track actual en la que esta posicionado el brazo
    int diametroPlato;              //  Diametro de cada plato del disco duro, medido en centimetros

    //  Variables de performance del disco
    int averageSeekTime;            //  Tiempo promedio de mover el cabezal de un track al siguiente
    int tasaLectura;                //  tasa promedio de lectura del disco duro MB / milisegundos 
    int tasaEscritura;              //  tasa promedio de escritura del disco duro MB / milisegundos

    int sectoresPorTrack;           //  Cantidad de sectores existentes en cada track del disco duro

    int rpm;                        //  Revoluciones por minuto de los platos
    float latenciaRotacional;       //  Tiempo promedio de espera para que leer o escribir un sector

    /**
      * Constructor
      */
    public Disco(int cd, int np, int ts, int ast, 
                 int tl, int te, int dp, int rpm) {
        this.capacidadDisco = cd;
        this.numPlatos = np;
        this.tamanoSector = ts;
        this.averageSeekTime = ast;
        this.tasaLectura = tl / 1000;
        this.tasaEscritura = te / 1000;
        this.diametroPlato = dp;
        this.rpm = rpm;
        this.latenciaRotacional = 60 / this.rpm;
    }

    /**
      * Devuelve el numero de pista sobre la cual se encuentra el brazo del 
      * disco actualmente
      */
    public int getTrackActual() {
        return this.trackActual;
    }

    /**
      * Toma el numero de un sector del disco duro y consigue el tiempo
      * tomado por el disco duro para leer o escribir sobre el
      */
    public long procesarSector(int sector, char tipoAccion) {
        long total = 0;
        Cabezal c = buscarCabezal(sector);
        int trackAux = buscarTrackParaSector(sector);

        total += moverBrazo(trackAux);
        total += efectuarAccion(tamanoSector, tipoAccion);
        return total;
    }

    /**
      *
      */
    private long moverBrazo(int trackDestino) {
        long distancia = Math.abs(trackDestino - trackActual);
        this.trackActual = trackDestino;
        return distancia * averageSeekTime;
    }

    /**
      *
      */
    private Cabezal buscarCabezal(int sector) {
        for (Cabezal c: this.cabezales) {
            if (c.min <= sector && sector < c.max)
                return c;
        }
        return null;
    }

    /**
      *
      */
    private long efectuarAccion(int tamanoSector, char tipo) {
        long total = 0;
        if (tipo == 'r')
            //  Calculo del tiempo de lectura del sector
            //  Es relativo a la posicion del cabezal
            total = tamanoSector / tasaLectura;
        else if (tipo == 'w')
            //  Calculo del tiempo de escritura del sector
            //  Es relativo a la posicion del cabezal
            total = tamanoSector / tasaEscritura;
        else
            System.out.println("Disco.efectuarAccion Error peticion mal definida");
        return total;
    }

    /**
      *
      */
    private int buscarTrackParaSector(int sector) {
        //  PENDIENTE

        return 0;
    }

    /**
      *
      */
    private int buscarSectorParaBloque(int bloque) {
        return (bloque * 8) + 1;
    }
}
