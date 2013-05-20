import java.util.LinkedList;

public class Peticion {
    private int tiempo;                     //  Momento en que se hizo la peticion
    private LinkedList<Integer> bloques;    //  bloques a leer
    private boolean tipo;                   //  tipo de peticion (lectura => true, escritura => false)
    private Prio prioridad;                 //  tipo de priorida

    private enum Prio {
        RT, BE, IDLE
    }

    public Peticion(int tiempo, String tipo, String prioridad) {
      this.tiempo = tiempo;

      if (tipo.equals("R"))
        this.tipo = true;
      else
        this.tipo = false;

      if (prioridad.equals("RT")) {
        this.prioridad = Prio.RT;
      } else if (prioridad.equals("BE")) {
        this.prioridad = Prio.BE;
      } else {
        this.prioridad = Prio.IDLE;
      }

      this.bloques = new LinkedList<Integer>();
    }

    public void addBloque(int bloque) {

      this.bloques.add(bloque);
    }

    @Override
    public String toString() {
      String str = "(";

      str += tiempo + ", ";
      str += prioridad + ", ";

      if (tipo)
        str += "R, ";
      else
        str += "W, ";

      str += bloques.toString() + ")";

      return str;
    }
}

