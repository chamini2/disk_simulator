import java.lang.Thread;

public class Reloj implements Runnable {

    int ticker;     // Cada cuanto pasa una unidad de tiempo, en milisegundos

    Reloj(int ticker) {
        this.ticker = ticker;
    }

    public void tick() {

        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e){
                System.out.println("Sleep interrumpido.");
            }
            notifyAll()
        }
    }
}
