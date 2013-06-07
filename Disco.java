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
    final int diametroPlato;              //  Diametro de cada plato del disco duro, medido en centimetros
    //  Variables de performance del disco
    int averageSeekTime;                  //  Tiempo promedio de mover el cabezal de un track al siguiente
    int tasaLectura;                      //  tasa promedio de lectura del disco duro MB / milisegundos
    int tasaEscritura;                    //  tasa promedio de escritura del disco duro MB / milisegundos

    //  Variables de estado del disco duro
    int cilindroActual;             //  Track actual en la que esta posicionado el brazo
    int platoActual;                //  Plato actual para pintar en interfaz
    int cabezalActual;              // Cabezal actual para pintar en interfaz

    //  Temporal
    //List<Cabezal> cabezales;        //  Lista de cabezales del disco duro
    float latenciaRotacional;       //  Tiempo promedio de espera para que leer o escribir un sector

    /**
      * Constructor
      */
    public Disco(int cd, int np, int ts, int nc,
                 int ast, int tl, int te, int dp, int rpm) {
        this.capacidadDisco     = cd;
        this.numPlatos          = np;
        this.tamanoSector       = ts;
        this.numCilindros       = nc;
        this.rpm                = rpm;
        this.diametroPlato      = dp;
        this.averageSeekTime    = ast;
        this.tasaLectura        = tl / 1000;
        this.tasaEscritura      = te / 1000;

        int numSectores         = capacidadDisco / tamanoSector;
        this.sectoresPorCara    = numSectores / (numPlatos * 2);
        this.sectoresPorTrack   = sectoresPorCara / numCilindros;

        this.latenciaRotacional = 60 / this.rpm;
        this.cilindroActual     = 0;
        this.platoActual        = 0;
        //this.cabezalActual      = 1;
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
    public long procesarSector(int numSector, char tipoAccion) {
        long total = 0;
        int trackAux = buscarCilindroParaSector(numSector);

        setPlatoActual((int) Math.floor(numSector / sectoresPorCara));
        total += moverBrazo(trackAux);
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
        if (tipo == 'r')
            //  Calculo del tiempo de lectura del sector
            //  Es relativo a la posicion del cabezal
            total = calcularLecturaSector(sector);
        else if (tipo == 'w')
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
        return (int) Math.floor(sector / sectoresPorTrack);
    }

    /**
      *
      */
    public int buscarSectorParaBloque(int bloque) {
        return (bloque * 8) + 1;
    }

    /*Monitor*/
    public synchronized void setValues(int cilindroActual, int plato, int cabezal) {
        this.cilindroActual = cilindroActual;
        this.platoActual    = plato;
        this.cabezalActual  = cabezal;
    }

    public synchronized int[] getValues() {
        int values[] = new int[3];

        values[0] = this.cilindroActual;
        values[1] = this.platoActual;
        values[2] = this.cabezalActual;

        return values;
    }

    /**
      *
      */
    private long calcularLecturaSector(int sector) {
        // TODO
        return sector / tasaLectura;
    }

    private long calcularEscrituraSector(int sector){
        // TODO
        return sector / tasaEscritura;
    }
}
