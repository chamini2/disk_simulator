public class Punto{
    private double x;      //Coordenada X
    private double  y;      //Coordenada Y
    private int sector; //Sector al que corresponde

    public Punto(double x, double y, int sector){
        this.x      = x;
        this.y      = y;
        this.sector = sector;
    }


    /*Getters*/
    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }


    public int getSector(){
        return this.sector;
    }

    /*Setters*/
    public void setY(int y){
        this.y = y;
    }
}
