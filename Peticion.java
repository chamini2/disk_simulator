public class Peticion {
    //  nt tiempo;         //  Momento en que se hizo la peticion
    private int inicial;        //  bloque inicial
    private int cantidad;       //  bloques a leer o escribir
    private int tipo;           //  tipo de peticion (Lectura o escritura)
    private Prio prioridad;      //  tipo de prioridad 

    private enum Prio {
        RT, BE, IDLE
    }
}

