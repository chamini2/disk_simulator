import java.lang.Comparable;

import java.util.List;
import java.util.LinkedList;

public class Peticion implements Comparable<Peticion>{
    private int tiempo;                 //  Momento en que se hizo la peticion
    private List<Integer> bloques;      //  bloque inicial
    private char tipo;                  //  tipo de peticion (Lectura o escritura)
    private Prio prioridad;             //  tipo de prioridad

    private enum Prio {
        RT, BE, IDLE
    }

    public Peticion(int tiempo, String tipo, String prioridad) {
        this.tiempo = tiempo;

        if (tipo.equals("R"))
            this.tipo = 'R';
        else
            this.tipo = 'W';

        if (prioridad.equals("RT"))
            this.prioridad = Prio.RT;
        else if (prioridad.equals("BE"))
            this.prioridad = Prio.BE;
        else
            this.prioridad = Prio.IDLE;

        this.bloques = new LinkedList<Integer>();
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

    public void addBloque(int bloque) {
        this.bloques.add(bloque);
    }

    @Override
    public String toString() {
        String str = "(";

        str += tiempo + ", ";
        str += prioridad + ", ";

        if (tipo == 'R')
            str += "R, ";
        else
            str += "W, ";

        str += bloques.toString() + ")";

        return str;
    }

    @Override
    public int compareTo(Peticion p) {

        if (this.tiempo > p.tiempo) {
            return 1;
        } else if (this.tiempo < p.tiempo) {
            return -1;
        } else {
            return 0;
        }
    }

}

