package sixlucky.graph;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import sixlucky.swing.Window;

import java.util.concurrent.locks.ReentrantLock;

public final class Render2D {
    private final Window window;
    private double lw = 1;
    ReentrantLock triangleLock = new ReentrantLock();
    private final GraphicsContext pen;
    public Render2D(Window window){
        this.window = window;
        pen = window.getMainCanvas();
    }

    public void LineWidth(double lw){
        this.lw=lw;
    }
    public void Font(String path,double size){
        pen.setFont(Graph.getFontByPath(path,size));
    }
    public void absPixel(Vector3D v, Color color) {
        if(ArrayMath.bounds(window,v)) {
            int x = (int) v.getX();
            int y = (int) v.getY();
            if (v.getZ() < window.getBuf(y, x)) {
                window.setBuf(y, x, v.getZ());
                pen.setFill(color);
                pen.fillRect(x, y, 1, 1);
            }
        }
    }
    public void absQuickPixel(Vector3D v) {
        if(ArrayMath.bounds(window,v)) {
            int x = (int) v.getX();
            int y = (int) v.getY();
            if (v.getZ() < window.getBuf(y, x)) {
                window.setBuf(y, x, v.getZ());
                pen.fillRect(x, y, 1, 1);
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
    public Vector3D[] absVectorLine(Vector3D v1, Vector3D v2) {
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
            absQuickPixel(v);
            v.setX(v.getX() + r);
        }
    }
    public void absTriangle(Vector3D v1, Vector3D v2, Vector3D v3,int low, Color color) {
        int[] box = ArrayMath.boxSelection(v1, v2, v3);
        Vector3D[][] xys = new Vector3D[box[5]][box[4]];
        for (Vector3D v : absVectorLine(v1, v2)) {
            xys[(int) (v.getY() - box[1])][(int) (v.getX() - box[0])] = v;
        }
        for (Vector3D v : absVectorLine(v1, v3)) {
            xys[(int) (v.getY() - box[1])][(int) (v.getX() - box[0])] = v;
        }
        for (Vector3D v : absVectorLine(v2, v3)) {
            xys[(int) (v.getY() - box[1])][(int) (v.getX() - box[0])] = v;
        }
        for (int i = 0; i < xys.length; i += low) {
            Vector3D begin = null;
            Vector3D end = null;
            for (Vector3D vector3D : xys[i]) {
                if (vector3D != null) {
                    begin = vector3D;
                    break;
                }
            }
            for (int j = xys[i].length - 1; j >= 0; j--) {
                if (xys[i][j] != null) {
                    end = xys[i][j];
                    break;
                }
            }
            if (begin != null & end != null) {
                absLine(begin, end, color);
            }
        }
    }
    public void absQuickTriangle(Vector3D v1, Vector3D v2, Vector3D v3,int low, Color color) {
        int[] box = ArrayMath.boxSelection(v1, v2, v3);
        Vector3D[][] xys = new Vector3D[box[5]][box[4]];
        for (Vector3D v : absVectorLine(v1, v2)) {
            xys[(int) (v.getY() - box[1])][(int) (v.getX() - box[0])] = v;
        }
        for (Vector3D v : absVectorLine(v1, v3)) {
            xys[(int) (v.getY() - box[1])][(int) (v.getX() - box[0])] = v;
        }
        for (Vector3D v : absVectorLine(v2, v3)) {
            xys[(int) (v.getY() - box[1])][(int) (v.getX() - box[0])] = v;
        }
        pen.setFill(color);
        for (int i = 0; i < xys.length; i += low) {
            Vector3D begin = null;
            Vector3D end = null;
            for (Vector3D vector3D : xys[i]) {
                if (vector3D != null) {
                    begin = vector3D;
                    break;
                }
            }
            for (int j = xys[i].length - 1; j >= 0; j--) {
                if (xys[i][j] != null) {
                    end = xys[i][j];
                    break;
                }
            }
            if (begin != null & end != null) {
                absQuickLine(begin, end, color);
            }
        }
    }
    public void Rect(double x,double y,double w,double h,boolean isfill, Color c){
        pen.setFill(c);
        if(isfill) {
            pen.fillRect(x, y, w, h);
        }else {
            pen.fillRect(x, y, lw, h);
            pen.fillRect(x, y, w, lw);
            pen.fillRect(x+w-lw, y, lw, h);
            pen.fillRect(x, y+h-lw, w, lw);
        }
    }
    public void Line(double x1,double y1,double x2,double y2, Color c) {
        absLine(new Vector3D(x1,y1,0),new Vector3D(x2,y2,0),c);
    }

    public void Polygon(double[] x,double[] y,int n,boolean isfill, Color c){
        if(isfill) {
            pen.setFill(c);
            pen.fillPolygon(x,y,n);
        }else {
            pen.setStroke(c);
            pen.strokePolygon(x,y,n);
        }
    }

    public void Oval(double x,double y,double w,double h,boolean isfill, Color c){
        if(isfill) {
            pen.setFill(c);
            pen.fillOval(x,y,w,h);
        }else {
            pen.setStroke(c);
            pen.strokeOval(x,y,w,h);
        }
    }

    public void Arc(double x, double y, double w, double h, double startAngle, double arcExtent, ArcType closure, boolean isfill, Color c){
        if(isfill) {
            pen.setFill(c);
            pen.fillArc(x,y,w,h,startAngle,arcExtent,closure);
        }else {
            pen.setStroke(c);
            pen.strokeArc(x,y,w,h,startAngle,arcExtent,closure);
        }
    }
    public void roundRect(double x,double y,double w,double h,double arcw,double arch,boolean isfill, Color c){
        if(isfill) {
            pen.setFill(c);
            pen.fillRoundRect(x,y,w,h,arcw,arch);
        }else {
            pen.setStroke(c);
            pen.strokeRoundRect(x,y,w,h,arcw,arch);
        }
    }
    public void Text(String string,double x,double y,Color c){
        pen.setFill(c);
        pen.fillText(string,x,y);
    }
    public void Text(String string,double x,double y,Color c,Font font){
        pen.setFont(font);
        pen.setFill(c);
        pen.fillText(string,x,y);
    }
    public void Text(String string,double x,double y,double w,Color c){
        pen.setFill(c);
        pen.fillText(string,x,y,w);
    }
    public void Text(String string,double x,double y,double w,Color c,Font font){
        pen.setFont(font);
        pen.setFill(c);
        pen.fillText(string,x,y,w);
    }

    public void Image(Image image, double x, double y) {
        pen.drawImage(image,x, y);
    }
    public void Image(Image image, double x, double y, double w, double h) {
        pen.drawImage(image,x, y, w, h);
    }
}
