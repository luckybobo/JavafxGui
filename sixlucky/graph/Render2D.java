package sixlucky.graph;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import sixlucky.swing.Window;

import java.util.concurrent.locks.ReentrantLock;

public final class Render2D {
    private final Window window;
    ReentrantLock triangleLock = new ReentrantLock();
    public Render2D(Window window){
        this.window = window;
    }
    public void absPixel(Vector3D v, Color color) {
        if(ArrayMath.bounds(window,v)) {
            if (v.getZ() < window.getBuf((int) v.getY(), (int) v.getX())) {
                window.setBuf((int) v.getY(), (int) v.getX(), v.getZ());
                if(color.getOpacity()==1) {
                    window.getMainCanvas().setFill(color);
                }else {
                    window.getMainCanvas().setFill(Color.rgb((int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255),color.getOpacity()));
                }
                window.getMainCanvas().fillRect((int) v.getX(), (int) v.getY(), 1, 1);
            }
        }
    }
    public Vector3D[] absLine(Vector3D v1, Vector3D v2, Color color) {
        Vector3D vv1 = new Vector3D((int)v1.getX(),(int)v1.getY(),v1.getZ());
        Vector3D vv2 = new Vector3D((int)v2.getX(),(int)v2.getY(),v2.getZ());
        int w = (int) (vv2.getX() - vv1.getX());
        int h = (int) (vv2.getY() - vv1.getY());
        int dx1 = Integer.compare(w, 0);
        int dy1 = Integer.compare(h, 0);
        int dx2 = Integer.compare(w, 0);
        int dy2 = 0;
        int fastStep = Math.abs(w);
        int slowStep = Math.abs(h);
        if (fastStep <= slowStep) {
            fastStep = Math.abs(h);
            slowStep = Math.abs(w);
            dx2 = 0;
            dy2 = Integer.compare(h, 0);
        }
        int numerator = fastStep >> 1;
        Vector3D[] vs = new Vector3D[fastStep+1];
        Vector3D[] ins = ArrayMath.interpolate(v1, v2, fastStep + 1);
        for (int i = 0; i <= fastStep; i++) {
            vs[i]=new Vector3D(vv1);
            vs[i].setZ(ins[i].getZ());
            absPixel(vs[i], color);
            numerator += slowStep;
            if (numerator >= fastStep) {
                numerator -= fastStep;
                vv1.setX(vv1.getX() + dx1);
                vv1.setY(vv1.getY() + dy1);
            } else {
                vv1.setX(vv1.getX() + dx2);
                vv1.setY(vv1.getY() + dy2);
            }
        }
        return vs;
    }
    public void absQuickLine(Vector3D v1, Vector3D v2, Color color) {
        Vector3D v = new Vector3D(v1);
        int step = Math.abs((int) (v2.getX()-v1.getX()));
        int r = v1.getX()< v2.getX()?1:-1;
        Vector3D[] ins = ArrayMath.interpolate(v1, v2, step+ 1);
        for (int i = 0; i <= step; i++) {
            v.setZ(ins[i].getZ());
            absPixel(v, color);
            v.setX(v.getX() + r);
        }
    }

    public void absTriangle(Vector3D v1, Vector3D v2, Vector3D v3, boolean isfill, Color color) {
        try {
            triangleLock.lock();
            if (isfill) {
                int[] box = ArrayMath.boxSelection(v1, v2, v3);
                Vector3D[][] xys = new Vector3D[box[5]][box[4]];
                for (Vector3D v : absLine(v1,v2,color)) {
                    xys[(int) (v.getY() - box[1])][(int) (v.getX() - box[0])] = v;
                }
                for (Vector3D v : absLine(v1,v3,color)) {
                    xys[(int) (v.getY() - box[1])][(int) (v.getX() - box[0])] = v;
                }
                for (Vector3D v : absLine(v2,v3,color)) {
                    xys[(int) (v.getY() - box[1])][(int) (v.getX() - box[0])] = v;
                }
                for (Vector3D[] xy : xys) {
                    Vector3D begin = null;
                    Vector3D end = null;
                    for (Vector3D vector3D : xy) {
                        if (vector3D != null){begin = vector3D;break;}
                    }
                    for (int j = xy.length - 1; j >= 0; j--) {
                        if (xy[j] != null){end = xy[j];break;}
                    }
                    if (begin != null & end != null) {
                        absQuickLine(begin, end, color);
                    }
                }
            } else {
                absLine(v1, v2, color);
                absLine(v1, v3, color);
                absLine(v2, v3, color);
            }
        }finally {
            triangleLock.unlock();
        }
    }
    public void Rect(double x,double y,double w,double h,boolean isfill, Color c){
        if(isfill) {
            window.getMainCanvas().setFill(c);
            window.getMainCanvas().fillRect(x, y, w, h);
        }else {
            window.getMainCanvas().setStroke(c);
            window.getMainCanvas().strokeRect(x, y, w, h);
        }
    }
    public synchronized void Line(double x1,double y1,double x2,double y2, Color c) {
        window.getMainCanvas().setStroke(c);
        window.getMainCanvas().strokeLine(x1, y1, x2, y2);
    }

    public synchronized void Polygon(double[] x,double[] y,int n,boolean isfill, Color c){
        if(isfill) {
            window.getMainCanvas().setFill(c);
            window.getMainCanvas().fillPolygon(x,y,n);
        }else {
            window.getMainCanvas().setStroke(c);
            window.getMainCanvas().strokePolygon(x,y,n);
        }
    }

    public synchronized void Oval(double x,double y,double w,double h,boolean isfill, Color c){
        if(isfill) {
            window.getMainCanvas().setFill(c);
            window.getMainCanvas().fillOval(x,y,w,h);
        }else {
            window.getMainCanvas().setStroke(c);
            window.getMainCanvas().strokeOval(x,y,w,h);
        }
    }

    public synchronized void Arc(double x, double y, double w, double h, double startAngle, double arcExtent, ArcType closure, boolean isfill, Color c){
        if(isfill) {
            window.getMainCanvas().setFill(c);
            window.getMainCanvas().fillArc(x,y,w,h,startAngle,arcExtent,closure);
        }else {
            window.getMainCanvas().setStroke(c);
            window.getMainCanvas().strokeArc(x,y,w,h,startAngle,arcExtent,closure);
        }
    }
    public synchronized void roundRect(double x,double y,double w,double h,double arcw,double arch,boolean isfill, Color c){
        if(isfill) {
            window.getMainCanvas().setFill(c);
            window.getMainCanvas().fillRoundRect(x,y,w,h,arcw,arch);
        }else {
            window.getMainCanvas().setStroke(c);
            window.getMainCanvas().strokeRoundRect(x,y,w,h,arcw,arch);
        }
    }

    public synchronized void Image(Image image, double x, double y) {
        window.getMainCanvas().drawImage(image,x, y);
    }
    public synchronized void Image(Image image, double x, double y, double w, double h) {
        window.getMainCanvas().drawImage(image,x, y, w, h);
    }
}
