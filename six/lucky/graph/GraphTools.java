package six.lucky.graph;

import javafx.scene.image.Image;

import java.util.Objects;

public class GraphTools {
    public static Image getImageByPath(String path){
        return new Image(Objects.requireNonNull(GraphTools.class.getClassLoader().getResource(path)).toExternalForm());
    }
    public static Image getImageByPath(String path,double w,double h){
        return new Image(Objects.requireNonNull(GraphTools.class.getClassLoader().getResource(path)).toExternalForm(),w,h,false,false);
    }
    public static int[] solveBinaryPrimaryEquations(int[] n){
        if(n.length!=6){return new int[]{0,0};}
        int y = (n[5]*n[0] - n[3]*n[2])/(n[0]*n[4]-n[3]*n[1]);
        int x = (n[2]-n[1]*y)/n[0];
        return new int[]{x,y};
    }
    public static Point getIntersection(Point p1, Point p2, Point p3, Point p4){
        double A1=p1.getY()-p2.getY();
        double B1=p2.getX()-p1.getX();
        double C1=A1*p1.getX()+B1*p1.getY();
        double A2=p3.getY()-p4.getY();
        double B2=p4.getX()-p3.getX();
        double C2=A2*p3.getX()+B2*p3.getY();
        double det_k=A1*B2-A2*B1;
        if(Math.abs(det_k)<0.00001){
            Point p = new Point(-999,-9999);
            p.setNAN(true);
            return p;
        }
        double a=B2/det_k;
        double b=-1*B1/det_k;
        double c=-1*A2/det_k;
        double d=A1/det_k;
        double x=a*C1+b*C2;
        double y=c*C1+d*C2;
        return  new Point(x,y);
    }
}
