package six.lucky.graph;

import six.lucky.swing.Window;

public class Rect3D{
    private double width = 100;
    Vector3D modelAbsoluteAngle = new Vector3D(0,0,0);
    Vector3D modelAbsoluteOrigin = new Vector3D(0,0,-400);
    Vector3D[] vertexArr = {
            new Vector3D(width, width+0, width),
            new Vector3D(width, -width, width),
            new Vector3D(-width, -width, width),
            new Vector3D(-width, width+0, width),

            new Vector3D(width, width+0, -width),
            new Vector3D(width, -width, -width),
            new Vector3D(-width, -width, -width),
            new Vector3D(-width, width+0, -width)
    };
    int[][] elementArr = {
            {0, 1, 2, 3},
            {4, 5, 6, 7},
            {0, 4, 5, 1},
            {1, 5, 6, 2},
            {2, 6, 7, 3},
            {3, 7, 4, 0}
    };
    public Vector3D perspective(Vector3D v, double nearPlane, double farPlane){
        return Graph.perspective(v,nearPlane,farPlane);
    }
    public Vector3D toAbsolute(Vector3D v) {
        return Graph.toAbsolute(v,modelAbsoluteOrigin);
    }
    public Vector3D toScreen(Vector3D projectedv, Window window){
        return Graph.toScreen(projectedv,window);
    }
    public void translate(double x,double y,double z){
        Matrix4x4 m = new Matrix4x4();
        m=Graph.translate(x,y,z);
        modelAbsoluteOrigin=Matrix4x4.multiply(m,modelAbsoluteOrigin);
    }
    public void rotate(double angle,boolean x,boolean y,boolean z){
        if(x) {
            modelAbsoluteAngle.setX(modelAbsoluteAngle.getX()+angle);
        }else if(y) {
            modelAbsoluteAngle.setY(modelAbsoluteAngle.getY()+angle);
        }else if(z) {
            modelAbsoluteAngle.setZ(modelAbsoluteAngle.getZ()+angle);
        }
    }
    public void allAngleToZero(){
        modelAbsoluteAngle.setXYZ(0,0,0);
    }
    public void setWidth(double width){
        this.width=width;
        vertexArr = new Vector3D[]{
                new Vector3D(width, width + 0, width),
                new Vector3D(width, -width, width),
                new Vector3D(-width, -width, width),
                new Vector3D(-width, width + 0, width),

                new Vector3D(width, width + 0, -width),
                new Vector3D(width, -width, -width),
                new Vector3D(-width, -width, -width),
                new Vector3D(-width, width + 0, -width)
        };
    }
}
