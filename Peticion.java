import java.

public class Peticion {
    private int tiempo;                 //  Momento en que se hizo la peticion
    private LinkedList<int> bloques;    //  bloque inicial
    private int tipo;                   //  tipo de peticion (Lectura o escritura)
    private Prio prioridad;             //  tipo de prioridad 

    private enum Prio {
        RT, BE, IDLE
    }
}

