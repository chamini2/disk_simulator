import java.lang.Thread;

public class Pintor implements Runnable {
    private GUI lienzo;
    private Disco disco;


    public Pintor(GUI lienzo, Disco disco) {
        this.lienzo = lienzo;
        this.disco = disco;
    }

    public void run() {
        int anterior[];
        int values[];
        boolean b;
        values = disco.getValues();
        anterior = values;
        while (true) {
            values = disco.getValues();
            b = true;
            for (int i = 0; i < values.length; ++i) {
                b = b && (anterior[i] == values[i]);
            }
            if (b) {
                continue;
            }
            for (int i = 0; i < values.length; ++i) {
                System.out.println("[ " + i + " ] = " + values[i]);
            }
            anterior = values;
            if (!this.lienzo.refreshInterface(values)) {
                break;
            }
        }
    }
}

