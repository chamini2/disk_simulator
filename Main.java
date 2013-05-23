import java.lang.Thread;
public class Main implements Runnable{
    private GUI interfaz;

    public static void main (String[] args) {
    
        Main m = new Main();
        Thread hilo = new Thread (m, "hilo");
        hilo.start();
        m.interfaz = new GUI(50);
        for (int i = 1; ; i = (i+1) % 100){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e){
                System.out.println("No durmio");
            }
            m.interfaz.agregarRegistro("Registro"+i);
            if (i % 2 == 0)
                m.interfaz.pintarPeticion(150);
            else
                m.interfaz.pintarPeticion(i);
        }
    }

    public void run(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e){
            System.out.println("No durmio");
        }

        for (int i = 1, j = 1, k = 1; ; i = (i+1) % 5, j = (j + 1) % 51, k = (k + 1) % 3){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e){
                System.out.println("No durmio");
            }
            if (i != 0 && j != 0 && k != 0){
                interfaz.cambiarCabezal(i);
                interfaz.pintarCilindro(j);
                interfaz.cambiarDisco(k);

            }
        }
    }
}
