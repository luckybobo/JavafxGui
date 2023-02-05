package sixlucky.graph;

import sixlucky.swing.Window;

import java.util.concurrent.locks.ReentrantLock;

public final class Render3D {
    Window window;
    Render2D render2D;
    ReentrantLock rectLock = new ReentrantLock();

    public boolean performance = false;
    public Render3D(Window window){
        this.window = window;
        this.render2D = new Render2D(window);
    }
    public void Rect(Rect3D rect3d, boolean isfill, boolean refresh) {
        try {
            rectLock.lock();
            Matrix4x4 cameraTranslate = Graph.translate(-window.getCamera().getOrigin().getX(), -window.getCamera().getOrigin().getY(), -window.getCamera().getOrigin().getZ());
            Matrix4x4 scale = Graph.scale(rect3d.getScale().getX(), rect3d.getScale().getY(), rect3d.getScale().getZ());
            Matrix4x4 rotate = Matrix4x4.multiply(Graph.rotate(rect3d.getAngle().getX(), true, false, false), Graph.rotate(rect3d.getAngle().getY(), false, true, false));
            rotate = Matrix4x4.multiply(rotate, Graph.rotate(rect3d.getAngle().getZ(), false, false, true));
            Matrix4x4 cameraRotate = Matrix4x4.multiply(Graph.rotate(-window.getCamera().getAngle().getX(), true, false, false), Graph.rotate(-window.getCamera().getAngle().getY(), false, true, false));
            cameraRotate = Matrix4x4.multiply(cameraRotate, Graph.rotate(-window.getCamera().getAngle().getZ(), false, false, true));
            Matrix4x4 rotateAbsolute = Matrix4x4.multiply(Graph.rotate(rect3d.getAbsoluteAngle().getX(), true, false, false), Graph.rotate(rect3d.getAbsoluteAngle().getY(), false, true, false));
            rotateAbsolute = Matrix4x4.multiply(rotateAbsolute, Graph.rotate(rect3d.getAbsoluteAngle().getZ(), false, false, true));
            if (refresh) {
                window.cleanBuf();
                window.cleanArtboard();
            }
            Vector3D[] ceil = new Vector3D[8];
            boolean[] ceilbool = new boolean[8];
            for (int i = 0; i < rect3d.vertexArr.length; i++) {
                ceilbool[i] = true;
                Vector3D floor = new Vector3D(rect3d.vertexArr[i]);
                floor = Matrix4x4.multiply(rotate, floor);
                floor = Matrix4x4.multiply(scale, floor);
                floor = rect3d.toAbsolute(floor);
                floor = Matrix4x4.multiply(cameraTranslate, floor);
                floor = Matrix4x4.multiply(cameraRotate, floor);
                floor = Matrix4x4.multiply(rotateAbsolute, floor);
                if (floor.getZ() > -50&performance) ceilbool[i] = false;
                if (floor.getZ() > -20) {
                    ceil[i] = null;
                    continue;
                }
                floor = rect3d.perspective(floor, window.getCamera().nearPlane, window.getCamera().farPlane);
                floor = rect3d.toScreen(floor, window);
                ceil[i] = floor;
            }
            if (isfill) {
                for (int i = 0; i < rect3d.elementArr.length; i++) {
                    if(ceil[rect3d.elementArr[i][0]]!=null&ceil[rect3d.elementArr[i][1]]!=null&ceil[rect3d.elementArr[i][2]]!=null) {
                        if (ceilbool[rect3d.elementArr[i][0]] & ceilbool[rect3d.elementArr[i][1]] & ceilbool[rect3d.elementArr[i][2]]) {
                            render2D.absQuickTriangle(ceil[rect3d.elementArr[i][0]], ceil[rect3d.elementArr[i][1]], ceil[rect3d.elementArr[i][2]], 1, rect3d.elementColor[i]);
                        } else {
                            render2D.absQuickTriangle(ceil[rect3d.elementArr[i][0]], ceil[rect3d.elementArr[i][1]], ceil[rect3d.elementArr[i][2]], 16,  rect3d.elementColor[i]);
                        }
                    }
                }
            } else {
                for (int i = 0; i < rect3d.elementArr.length; i++) {
                    if(ceil[rect3d.elementArr[i][0]]!=null&ceil[rect3d.elementArr[i][1]]!=null&ceil[rect3d.elementArr[i][2]]!=null) {
                        render2D.absQuickTriangle(ceil[rect3d.elementArr[i][0]], ceil[rect3d.elementArr[i][1]], ceil[rect3d.elementArr[i][2]], 512, rect3d.elementColor[i]);
                    }
                }
            }
        }finally {
            rectLock.unlock();
        }
    }
}
