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
            this.lienzo.cambiarSector((this.lienzo.getSector() + 1) % this.disco.getSectoresPorTrack());
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

        //  Imprimir estadisticas
        try {
            Thread.sleep(499);       
        } catch(Exception e) {
        }
        values = disco.getEstadisticas();
        String s;
        s = "Peticiones:    " + (values[0] + values[1]) + "\n" +
            "Escrituras:    " + values[0] + "\n" + 
            "Lecturas:      " + values[1] + "\n" +
            "CPU escritura: " + ((values[2] * 100) / (values[2] + values[3] + values[4])) + "%\n" +
            "CPU lectura:   " + ((values[3] * 100) / (values[3] + values[2] + values[4])) + "%\n"; 
            lienzo.agregarRegistro(s);
    }
}

