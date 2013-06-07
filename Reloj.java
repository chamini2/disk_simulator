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

    public int getTicker(){
        return this.ticker;
    }

    public void run() {
        try{
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            System.out.println("Interrumpido");
        }

        while (true) {
            try {
                Thread.sleep(this.ticker);
            } catch (InterruptedException e){
                System.out.println("Sleep interrumpido.");
            }
            ++this.tiempo;
        }
    }
}
