import java.util.List;

public class Peticion {
    private int tiempo;                 //  Momento en que se hizo la peticion
    private List<Integer> bloques;      //  bloque inicial
    private char tipo;                  //  tipo de peticion (Lectura o escritura)
    private Prio prioridad;             //  tipo de prioridad 

    private enum Prio {
        RT, BE, IDLE
    }

    public int getTiempo() {
        return this.tiempo;
    }

    public List<Integer> getBloques() {
        return this.bloques;
    }

    public char getTipo() {
        return this.tipo;
    }
}

