import java.lang.Thread;
public class Main{
    public static void main(String args[]){
        Disco disco = new Disco(1073741824, 2, 512, 1000, 5, 1000, 500, 7200, 1000);
        GUI interfaz = new GUI(disco);
   }
}
