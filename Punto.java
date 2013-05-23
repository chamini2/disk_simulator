public class Punto{
    private int x;      //Coordenada X
    private int y;      //Coordenada Y
    private int sector; //Sector al que corresponde

    public Punto(int x, int y, int sector){
        this.x      = x;
        this.y      = y;
        this.sector = sector;
    }


    /*Getters*/
    public int getX(){
        return this.x;
    }

    public int getY(){
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
