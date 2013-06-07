import java.lang.Thread;

public class Pintor implements Runnable {
    private GUI lienzo;
    private Disco disco;


    public Pintor(GUI lienzo, Disco disco) {
        this.lienzo = lienzo;
        this.disco = disco; 
    }

    public void run() {
        int values[];
        while (true) {
            values = disco.getValues();
            if (!this.lienzo.refreshInterface(values)) {
                break;
            }
        }
    }
}

