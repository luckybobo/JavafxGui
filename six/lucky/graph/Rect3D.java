package six.lucky.graph;

public class Rect3D extends Graph3d{
    private double width;
    private Matrix[] matrixPoints = new Matrix[8];
    public Rect3D(double x, double y, double z, double w){
        for (Matrix matrix : matrixPoints) {
            matrix=new Matrix(4,1);
            matrix.st(0,0,x);
            matrix.st(1, 0, y);
            matrix.st(2, 0, z);
            matrix.st(3, 0, 1);
        }
    }
    public double getWidth() {return width;}
    public void translate(double x,double y,double z){
//        matrixRect3d.st(0,0,x);
//        matrixRect3d.st(0,0,y);
//        matrixRect3d.st(0,0,z);
    }
    public void scale(double x,double y,double z){
        Matrix m=new Matrix(4,4);
        m.st(0,0,x);
        m.st(1,1,y);
        m.st(2,2,z);
        for (Matrix matrixPoint : matrixPoints) {
            matrixPoint.clone(Matrix.multiply(matrixPoint, m));
        }
    }
    public void rotate(double angle,boolean x,boolean y,boolean z){
        angle=Math.toRadians(angle);
        Matrix m=new Matrix(4,4);
        if (x) {
            m.st(1,1,Math.cos(angle));
            m.st(2,1,Math.sin(angle));
            m.st(1,2,-Math.sin(angle));
            m.st(2,2,Math.cos(angle));
        } else if (y) {
            m.st(0,0,Math.cos(angle));
            m.st(2,0,-Math.sin(angle));
            m.st(0,2,Math.sin(angle));
            m.st(2,2,Math.cos(angle));
        } else if (z) {
            m.st(0,0,Math.cos(angle));
            m.st(1,0,Math.sin(angle));
            m.st(0,1,-Math.sin(angle));
            m.st(1,1,Math.cos(angle));
        }
        for (Matrix matrixPoint : matrixPoints) {
            matrixPoint.clone(Matrix.multiply(matrixPoint, m));
        }
    }
//    public static Rect3D multiply(Rect3D v, Matrix4x4 m){
//        return new Rect3D(
//                m.at(0,0)*v.x+m.at(1,0)*v.y+m.at(2,0)*v.z+m.at(3,0)*v.w,
//                m.at(0,1)*v.x+m.at(1,1)*v.y+m.at(2,1)*v.z+m.at(3,1)*v.w,
//                m.at(0,2)*v.x+m.at(1,2)*v.y+m.at(2,2)*v.z+m.at(3,2)*v.w,
//                m.at(0,3)*v.x+m.at(1,3)*v.y+m.at(2,3)*v.z+m.at(3,3)*v.w
//        );
//    }
//    public double length(){
//        return Math.sqrt(x*x+y*y+z*z);
//    }
}
