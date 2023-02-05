package sixlucky.graph;

import sixlucky.swing.Window;

public class ArrayMath {
    private static final int INSIDE = 0;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int BOTTOM = 4;
    private static final int TOP = 8;
    public static int[] sort(double[] arr, boolean desc) {
        double temp;
        int index;
        int k = arr.length;
        int[] Index = new int[k];
        for (int i = 0; i < k; i++) {
            Index[i] = i;
        }
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (desc) {
                    if (arr[j] < arr[j + 1]) {
                        temp = arr[j];
                        arr[j] = arr[j + 1];
                        arr[j + 1] = temp;

                        index = Index[j];
                        Index[j] = Index[j + 1];
                        Index[j + 1] = index;
                    }
                } else {
                    if (arr[j] > arr[j + 1]) {
                        temp = arr[j];
                        arr[j] = arr[j + 1];
                        arr[j + 1] = temp;

                        index = Index[j];
                        Index[j] = Index[j + 1];
                        Index[j + 1] = index;
                    }
                }
            }
        }
        return Index;
    }
    public static int[] sort(double[] arr) {
        return sort(arr, false);
    }
    public static double hypotenuse(double a, double b){
        return Math.sqrt(Math.pow(a,2)+Math.pow(b,2));
    }
    public static double rightAngledEdges(double a,double b){
        if(a>b) {
            return Math.sqrt(Math.pow(a, 2) - Math.pow(b, 2));
        }else {
            return Math.sqrt(Math.pow(b, 2) - Math.pow(a, 2));
        }
    }
    public static Vector3D[] interpolate(Vector3D v1, Vector3D v2, int n){
        Vector3D[] vs = new Vector3D[n];
        int ns = n-1;
        for (int i = 0; i < n; i++) {
            vs[i]=Vector3D.add(Vector3D.multiply(v1,(ns-i)/(double)ns),Vector3D.multiply(v2,i/(double)ns));
        }
        return vs;
    }
    public static int[] boxSelection(Vector3D v1,Vector3D v2,Vector3D v3){
        Vector3D xmin = v1;
        Vector3D ymin = v1;
        Vector3D xmax = v2;
        Vector3D ymax = v2;
        int[] box = new int[6];
        if (Math.min(v1.getX(),Math.min(v2.getX(),v3.getX()))==v2.getX()) {
            xmin = v2;
        } else if (Math.min(v1.getX(),Math.min(v2.getX(),v3.getX()))==v3.getX()) {
            xmin = v3;
        }
        if (Math.min(v1.getY(),Math.min(v2.getY(),v3.getY()))==v2.getY()) {
            ymin = v2;
        } else if (Math.min(v1.getY(),Math.min(v2.getY(),v3.getY()))==v3.getY()) {
            ymin = v3;
        }
        if (Math.max(v1.getX(),Math.max(v2.getX(),v3.getX()))==v1.getX()) {
            xmax = v1;
        } else if (Math.max(v1.getX(),Math.max(v2.getX(),v3.getX()))==v3.getX()) {
            xmax = v3;
        }
        if (Math.max(v1.getY(),Math.max(v2.getY(),v3.getY()))==v1.getY()) {
            ymax = v1;
        } else if (Math.max(v1.getY(),Math.max(v2.getY(),v3.getY()))==v3.getY()) {
            ymax = v3;
        }
        box[0]= (int) Math.floor(xmin.getX());
        box[1]= (int) Math.floor(ymin.getY());
        box[2]= (int) Math.ceil(xmax.getX());
        box[3]= (int) Math.ceil(ymax.getY());
        box[4]=box[2]-box[0]+1;
        box[5]=box[3]-box[1]+1;
        return box;
    }
    public static boolean bounds(Window window,Vector3D v){
        if(v.getX()>window.getWidth()-1|v.getX()<0)return false;
        return !(v.getY() > window.getHeight() - 1 | v.getY() < 0);
    }
    public static Vector3D[] clip(Window window,Vector3D v1,Vector3D v2) {
        double p1x = v1.getX(), p1y = v1.getY();
        double p2x = v2.getX(), p2y = v2.getY();
        double qx = 0d, qy = 0d;
        boolean vertical = p1x == p2x;
        double slope = vertical ? 0d : (p2y - p1y) / (p2x - p1x);

        int code1 = getRegionCode(window,p1x, p1y);
        int code2 = getRegionCode(window,p2x, p2y);

        while (!(code1 == INSIDE & code2 == INSIDE)) {
            if ((code1 & code2) != INSIDE) {
                return new Vector3D[]{new Vector3D(v1), new Vector3D(v2)};
            }
            int codeout = code1 == INSIDE ? code2 : code1;

            if ((codeout & LEFT) != INSIDE) {
                qx = 0;
                qy = (qx - p1x) * slope + p1y;
            } else if ((codeout & RIGHT) != INSIDE) {
                qx = window.getWidth() - 1;
                qy = (qx - p1x) * slope + p1y;
            } else if ((codeout & BOTTOM) != INSIDE) {
                qy = 0;
                qx = vertical ? p1x : (qy - p1y) / slope + p1x;
            } else if ((codeout & TOP) != INSIDE) {
                qy = window.getHeight() - 1;
                qx = vertical ? p1x : (qy - p1y) / slope + p1x;
            }

            if (codeout == code1) {
                p1x = qx;
                p1y = qy;
                code1 = getRegionCode(window, p1x, p1y);
            } else {
                p2x = qx;
                p2y = qy;
                code2 = getRegionCode(window, p2x, p2y);
                code2 = getRegionCode(window, p2x, p2y);
            }
        }
        return new Vector3D[]{new Vector3D(p1x,p1y,v1.getZ()),new Vector3D(p2x,p2y,v2.getZ())};
    }
    private static int getRegionCode(Window window,double x, double y) {
        int xcode = x < 0 ? LEFT : x > window.getWidth() ? RIGHT : INSIDE;
        int ycode = y < 0 ? BOTTOM : y > window.getHeight() ? TOP : INSIDE;
        return xcode | ycode;
    }
}