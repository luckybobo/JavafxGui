package six.lucky.graph;

public class Point {
    private double x;
    private double y;
    private double z;
    private boolean NAN = false;

    public Point(double x,double y){
        this.x=x;
        this.y=y;
    }
    public Point(double x,double y,double z){
        this.x=x;
        this.y=y;
        this.z=z;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    public double getZ() {
        return z;
    }
    public void setZ(double z) {
        this.z = z;
    }
    public void setNAN(boolean N){
        NAN=N;
    }
    public boolean getNAN(){
        return NAN;
    }
}
