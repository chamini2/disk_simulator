import java.lang.Thread;
public class Main{


    public static void main(String args[]){

        Reloj reloj = new Reloj(1000);
        Thread hilo = new Thread(reloj, "hilo");
        hilo.start();
    }

    

}
