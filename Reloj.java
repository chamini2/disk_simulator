import java.lang.Thread;

public class Reloj implements Runnable {

    private int ticker;     // Cada cuanto pasa una unidad de tiempo, en milisegundos
    private int tiempo;

    public Reloj(int ticker) {
        this.ticker = ticker;
        this.tiempo = 0;
    }


    
    /*getter*/
    public int getTiempo() {
        return this.tiempo;
    }

    public void run() {

        while (true) {
            try {
                Thread.sleep(this.ticker);
            } catch (InterruptedException e){
                System.out.println("Sleep interrumpido.");
            }
            ++this.tiempo;
            System.out.println("Tiempo: "+this.tiempo);
        }
    }
}
