package sixlucky.graph;

import javafx.scene.image.Image;
import javafx.scene.text.Font;
import sixlucky.swing.Window;

import java.util.ArrayList;
import java.util.Objects;

public final class Graph {
    public static Image getImageByPath(String path){
        return new Image(Objects.requireNonNull(Graph.class.getClassLoader().getResource(path)).toExternalForm());
    }
    public static Image getImageByPath(String path,double w,double h){
        return new Image(Objects.requireNonNull(Graph.class.getClassLoader().getResource(path)).toExternalForm(),w,h,false,false);
    }
    public static Font getFontByPath(String path,double size){
        return Font.loadFont(Objects.requireNonNull(Graph.class.getClassLoader().getResource(path)).toExternalForm(),size);
    }
    public static Vector3D perspective(Vector3D v,double nearPlane, double farPlane){
        Matrix4x4 m=new Matrix4x4();
        m.set(0,0,nearPlane);
        m.set(1,1,nearPlane);
        m.set(2,2,-((farPlane+nearPlane)/(farPlane-nearPlane)));
        m.set(2,3,-(2*farPlane*nearPlane/(farPlane-nearPlane)));
        m.set(3,2,-1);
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
        v.setX(window.getWidth()/2+projectedv.getX());
        v.setY(window.getHeight()/2-projectedv.getY());
        v.setZ(projectedv.getZ());
        return v;
    }
    public static Matrix4x4 translate(double x, double y, double z){
        Matrix4x4 m = new Matrix4x4();
        m.set(0,3,x);
        m.set(1,3,y);
        m.set(2,3,z);
        return m;
    }
    public static Matrix4x4 scale(double x,double y,double z){
        Matrix4x4 m=new Matrix4x4();
        m.mat[0][0]=x;
        m.mat[1][1]=y;
        m.mat[2][2]=z;
        return m;
    }
    public static Matrix4x4 rotate(double angle, boolean x, boolean y, boolean z){
        angle= Math.toRadians(angle);
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
            m.mat[0][0]= Math.cos(angle);
            m.mat[1][0]= Math.sin(angle);
            m.mat[0][1]= -Math.sin(angle);
            m.mat[1][1]= Math.cos(angle);
        }
        return m;
    }
    public static double getDistanceToCameraRelative(Vector3D v){
        double x = Math.abs(v.getX());
        double y = Math.abs(v.getY());
        double z = Math.abs(v.getZ()*100+100);
        return ArrayMath.hypotenuse(ArrayMath.hypotenuse(x,z),y);
    }
    public static double getDistanceToCamera(Vector3D v){
        double x = Math.abs(v.getX());
        double y = Math.abs(v.getY());
        double z = Math.abs(v.getZ());
        return ArrayMath.hypotenuse(ArrayMath.hypotenuse(x,z),y);
    }
    public static Vector3D recent(Vector3D[] vs){
        double[] vss = new double[vs.length];
        for (int i = 0;i< vs.length;i++) {
            vss[i]=Graph.getDistanceToCamera(vs[i]);
        }
        int[] index = ArrayMath.sort(vss);
        return vs[index[0]];
    }
    public static double getArea(Vector3D v1,Vector3D v2,Vector3D v3,Vector3D v4){
        int i, j;
        int area = 0;
        ArrayList<Vector3D> objects = new ArrayList<>();
        objects.add(v1);
        objects.add(v2);
        objects.add(v3);
        objects.add(v4);
        for (i = 0; i < objects.size(); i++)
        {
            j = (i + 1) % objects.size();
            area += objects.get(i).getX() * objects.get(j).getY();
            area -= objects.get(i).getY() * objects.get(j).getX();
        }
        area /= 2;
        return area;
    }
}
