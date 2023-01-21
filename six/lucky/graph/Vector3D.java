package six.lucky.graph;

public class Vector3D {
    private double x;
    private double y;
    private double z;
    private double w;
    public Vector3D(double x,double y,double z){
        this.x=x; this.y=y; this.z=z;w=1;
    }
    public Vector3D(double x,double y,double z,double w){
        this.x=x;this.y= y;this.z= z;this.w= w;
    }
    public Vector3D(){
        x=y=z=0;w=1;
    }
    public Vector3D(Vector3D v){
        x=v.x;y=v.y;z=v.z;w=v.w;
    }

    public double getX() {return x;}
    public double getY() {return y;}
    public double getZ() {return z;}
    public double getW() {return w;}

    public void setX(double x) {this.x = x;}
    public void setY(double y) {this.y = y;}
    public void setZ(double z) {this.z = z;}
    public void setW(double w) {this.w = w;}
    public void setXYZ(double x,double y,double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public static Vector3D minus(Vector3D v1,Vector3D v2){
        return new Vector3D(v1.x-v2.x, v1.y-v2.y, v1.z-v2.z);
    }
    public static Vector3D divise(Vector3D v1,Vector3D v2){
        return new Vector3D(v1.x/v2.x, v1.y/v2.y, v1.z/v2.z);
    }
    public static Vector3D add(Vector3D v1,Vector3D v2){
        return new Vector3D(v1.x+v2.x, v1.y+v2.y, v1.z+v2.z);
    }
    public static Vector3D multiply(Vector3D v,float n){
        return new Vector3D(v.x*n,v.y*n,v.z*n);
    }
    public static Vector3D multiply(Vector3D v1,Vector3D v2){
        return new Vector3D(v1.x* v2.x, v1.y*v2.y, v1.z* v2.z);
    }
    public static double dot(Vector3D v1,Vector3D v2){
        return v1.x*v2.x+v1.y*v2.y+v1.z*v2.z;
    }
    public static Vector3D cross(Vector3D v1,Vector3D v2){
        return new Vector3D(v1.y*v2.z-v1.z*v2.y, -(v1.x*v2.z-v1.z*v2.x), v1.x*v2.y-v1.y*v2.x);
    }
}
