import java.util.List;
import java.util.ArrayList;

public class Disco {

    //  Variables constantes de un disco duro
    final int capacidadDisco;             //  Tamano en bytes del disco duro
    final int numPlatos;                  //  Numero de platos del disco duro
    final int tamanoSector;               //  Tamano en bytes de cada sector del dd
    final int numCilindros;               //  Numero de cilindros

    final int sectoresPorCara;            //  Numero
    final int sectoresPorTrack;
    final int rpm;                        //  Revoluciones por minuto de los platos
    //  Variables de performance del disco
    int averageSeekTime;                  //  Tiempo promedio de mover el cabezal de un track al siguiente
    int tasaLectura;                      //  tasa promedio de lectura del disco duro MB / milisegundos
    int tasaEscritura;                    //  tasa promedio de escritura del disco duro MB / milisegundos

    //  Variables de estado del disco duro
    int trackActual;
    int cilindroActual;             //  Track actual en la que esta posicionado el brazo
    int platoActual;                //  Plato actual para pintar en interfaz
    int cabezalActual;              // Cabezal actual para pintar en interfaz
    Peticion actual;
    int esta[];

    //  Temporal
    //List<Cabezal> cabezales;        //  Lista de cabezales del disco duro
    float latenciaRotacional;       //  Tiempo promedio de espera para que leer o escribir un sector
    /**
      * Constructor
      */
    public Disco(int cd, int np, int ts, int nc,
                 int ast, int tl, int te, int rpm, int ticker) {
        this.capacidadDisco     = cd;
        this.numPlatos          = np;
        this.tamanoSector       = ts;
        this.numCilindros       = nc;
        this.rpm                = rpm;
        this.averageSeekTime    = ast;
        this.tasaLectura        = tl * 1000 / ticker;
        this.tasaEscritura      = te * 1000 / ticker;

        int numSectores         = capacidadDisco / tamanoSector;
        this.sectoresPorCara    = numSectores / (numPlatos * 2);
        this.sectoresPorTrack   = sectoresPorCara / numCilindros;

        this.latenciaRotacional = 60 / this.rpm;
        this.trackActual        = 0;
        this.cilindroActual     = 0;
        this.platoActual        = 0;
        this.cabezalActual      = 0;
        this.esta               = new int[5];
    }

    public String toString() {
        String s = "Disco"
            + "capacidad: "+this.capacidadDisco
            + "sectoresPorCara "+this.sectoresPorCara
            + "tamanoSector "+this.tamanoSector
            + "sectoresPorTrack "+this.sectoresPorTrack;
        return s;
    }

    public int getSectoresPorTrack() {
        return this.sectoresPorTrack;
    }

    /**
      * Devuelve el numero de pista sobre la cual se encuentra el brazo del
      * disco actualmente
      */
    public int getCilindroActual() {
        return this.cilindroActual;
    }


    /**
      * Devuelve el numero de pista sobre la cual se encuentra el brazo del
      * disco actualmente
      */
    public int getPlatoActual() {
        return this.platoActual;
    }

    /**
      * Devuelve el numero de pista sobre la cual se encuentra el brazo del
      * disco actualmente
      */
    public void setPlatoActual(int platoActual) {
        this.platoActual = platoActual;
    }

    /**/
    public int getCabezalActual() {
        return this.cabezalActual;
    }

    /**/
    public void setCabezalActual(int cabezal) {
        this.cabezalActual = cabezal;
    }

    /**
     * Devuelve el numero de cilindros que tiene el disco
     */
    public int getNumCilindros() {
        return this.numCilindros;
    }

    /**
      * Toma el numero de un sector del disco duro y consigue el tiempo
      * tomado por el disco duro para leer o escribir sobre el
      */
    public long procesarSector(int numSector, char tipoAccion, Peticion p) {
        long total = 0;
        int cil, plat, cab;
        int trackAux = buscarCilindroParaSector(numSector);

        total += moverBrazo(trackAux);

        cil = this.getCilindroActual();
        plat = (int) Math.floor(numSector / (sectoresPorCara * 2));
        cab = (int) Math.floor(numSector / sectoresPorCara);

        setValues(cil, plat, cab, p);
        total += efectuarAccion(numSector, tipoAccion);
        return total;
    }

    /**
      *
      */
    private long moverBrazo(int cilindroDestino) {
        long distancia = Math.abs(cilindroDestino - cilindroActual);
        this.cilindroActual = cilindroDestino;
        return distancia * averageSeekTime;
    }

    /**
      *
      */
    private long efectuarAccion(int sector, char tipo) {
        long total = 0;
        if (tipo == 'R')
            //  Calculo del tiempo de lectura del sector
            //  Es relativo a la posicion del cabezal
            total = calcularLecturaSector(sector);
        else if (tipo == 'W')
            //  Calculo del tiempo de escritura del sector
            //  Es relativo a la posicion del cabezal
            total = calcularEscrituraSector(sector);
        else
            System.out.println("Disco.efectuarAccion: Error peticion mal definida");
        return total;
    }

    /**
      * Para un numero de sector especifico consigue en que cilindro esta situado dicho sector
      */
    public int buscarCilindroParaSector(int sector) {
        this.trackActual = (int) Math.floor(sector / sectoresPorTrack);
        return (int) Math.floor(trackActual % numCilindros);
    }

    /**
      *
      */
    public int buscarSectorParaBloque(int bloque) {
        return (bloque * 8);
    }

    /*Monitor*/
    public synchronized void setValues(int cilindroActual, int plato, int cabezal, Peticion p) {
        this.cilindroActual = cilindroActual;
        this.platoActual    = plato;
        this.cabezalActual  = cabezal;
        this.actual         = p;

        System.out.println("[ cilindroActual ] = " + cilindroActual);
        System.out.println("[ plato          ] = " + plato);
        System.out.println("[ cabezal        ] = " + cabezal);
        System.out.println("[ p              ] = " + p);

    }

    public synchronized int[] getValues() {
        int values[] = new int[4];

        if (this.actual == null) {
            values[3] = -1;
        } else if (this.actual.getTiempo() == -1){
            values[3] = -2;
        } else {
            values[3] = this.actual.getTiempo();
        }
        values[0] = this.cilindroActual;
        values[1] = this.platoActual;
        values[2] = this.cabezalActual;

        return values;
    }

    public synchronized int[] getEstadisticas() {
        return this.esta;
    }

    public synchronized void setEstadisticas(int w, int r, int tw, int tr, int t) {
        this.esta[0] = w;
        this.esta[1] = r;
        this.esta[2] = tw;
        this.esta[3] = tr;
        this.esta[4] = t - (tw + tr);
    }

    /**
      *
      */
    private long calcularLecturaSector(int sector) {
        return (this.tamanoSector * 8) * 8 / tasaLectura;
    }

    private long calcularEscrituraSector(int sector){
        return (this.tamanoSector * 8) * 8 / tasaEscritura;
    }

}
