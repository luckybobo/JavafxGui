package sixlucky.graph;

public class Camera {
    private Vector3D origin = new Vector3D();
    private final Vector3D angle = new Vector3D();
    public double nearPlane = 300;
    public double farPlane = 1000;
    public Vector3D getOrigin(){
        return origin;
    }
    public Vector3D getAngle(){
        return angle;
    }
    public void translate(double stepx,double  stepy,double  stepz,boolean face){
        if(face) {
            Matrix4x4 m1 = Graph.translate(Math.sin(Math.toRadians((angle.getY()+450)%360))*stepx, 0, Math.cos(Math.toRadians((angle.getY()+450)%360))*stepx);
            Matrix4x4 m2 = Graph.translate(Math.sin(Math.toRadians((angle.getY()+360)%360))*stepz, 0, Math.cos(Math.toRadians((angle.getY()+360)%360))*stepz);
            origin = Matrix4x4.multiply(Matrix4x4.multiply(m1,m2), origin);
        }else {
            Matrix4x4 m = Graph.translate(stepx, stepy, stepz);
            origin = Matrix4x4.multiply(m, origin);
        }
    }
    public void rotate(double anglex,double  angley,double  anglez){
        angle.setX((angle.getX() + anglex + 360) % 360);
        angle.setY((angle.getY() + angley + 360) % 360);
        angle.setZ((angle.getZ() + anglez + 360) % 360);
    }
}
