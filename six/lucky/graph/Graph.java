package six.lucky.graph;

import javafx.scene.image.Image;
import six.lucky.swing.Window;

import java.util.Objects;

public class Graph {
    public static Image getImageByPath(String path){
        return new Image(Objects.requireNonNull(Graph.class.getClassLoader().getResource(path)).toExternalForm());
    }
    public static Image getImageByPath(String path,double w,double h){
        return new Image(Objects.requireNonNull(Graph.class.getClassLoader().getResource(path)).toExternalForm(),w,h,false,false);
    }
    public static Vector3D perspective(Vector3D v, double nearPlane, double farPlane){
        Matrix4x4 m=new Matrix4x4();
        m.set(0,0,nearPlane);
        m.set(1,1,nearPlane);
        m.set(2,2,-((farPlane+nearPlane)/(farPlane-nearPlane)));
        m.set(2,3,-(2*farPlane*nearPlane/(farPlane-nearPlane)));
        m.set(3,2,1);
        m.set(3,3,0);
        Vector3D e = Matrix4x4.multiply(m,v);
        e.setX(e.getX()/e.getW());
        e.setY(e.getY()/e.getW());
        e.setZ(e.getZ()/e.getW());
        e.setW(1);
        return e;
    }
    public static Vector3D toAbsolute(Vector3D v,Vector3D modelAbsoluteOrigin) {
        Matrix4x4 m = new Matrix4x4();
        m.set(0, 3, v.getX());
        m.set(1, 3, v.getY());
        m.set(2, 3, v.getZ());
        return Matrix4x4.multiply(m, modelAbsoluteOrigin);
    }
    public static Vector3D toScreen(Vector3D projectedv, Window window){
        Vector3D v = new Vector3D();
        v.setX(window.getPrimaryStageWidth()/2+projectedv.getX());
        v.setY((window.getPrimaryStageHeight()-Window.menuBarHeight)/2-projectedv.getY());
        return v;
    }
    public static Matrix4x4 translate(double x, double y, double z){
        Matrix4x4 m = new Matrix4x4();
        m.set(0,3,x);
        m.set(1,3,y);
        m.set(2,3,z);
        return m;
    }
    public static Matrix4x4 rotate(double angle, boolean x, boolean y, boolean z){
        angle=Math.toRadians(angle);
        Matrix4x4 m=new Matrix4x4();
        if (x) {
            m.mat[1][1]= Math.cos(angle);
            m.mat[2][1]= Math.sin(angle);
            m.mat[1][2]= -Math.sin(angle);
            m.mat[2][2]= Math.cos(angle);
        } else if (y) {
            m.mat[0][0]= Math.cos(angle);
            m.mat[2][0]= -Math.sin(angle);
            m.mat[0][2]= Math.sin(angle);
            m.mat[2][2]= Math.cos(angle);
        } else if (z) {
            m.mat[0][0]= (float) Math.cos(angle);
            m.mat[1][0]= (float) Math.sin(angle);
            m.mat[0][1]= (float) -Math.sin(angle);
            m.mat[1][1]= (float) Math.cos(angle);
        }
        return m;
    }
}
