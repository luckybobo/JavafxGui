package six.lucky.graph;

import javafx.scene.canvas.GraphicsContext;
import six.lucky.swing.Window;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Render3d {
    private final Lock rect3DLock = new ReentrantLock();
    public synchronized static void stroke3dRect(Rect3D rect3D,Window window) {
        GraphicsContext g = window.getMainCanvas();
        Matrix4x4 rotatex = Graph.rotate(rect3D.modelAbsoluteAngle.getX(), true, false, false);
        Matrix4x4 rotatey = Graph.rotate(rect3D.modelAbsoluteAngle.getY(), false, true, false);
        Matrix4x4 rotatez = Graph.rotate(rect3D.modelAbsoluteAngle.getZ(), false, false, true);
        Matrix4x4 scale = Graph.scale(rect3D.modelAbsoluteScale.getX(),rect3D.modelAbsoluteScale.getY(),rect3D.modelAbsoluteScale.getZ());
        Matrix4x4 rotate = Matrix4x4.multiply(rotatex,rotatey);
        rotate = Matrix4x4.multiply(rotate,rotatez);
        window.cleanArtboard();
        for (int i = 0; i < 6; i++) {
            Vector3D[] ceil = new Vector3D[4];
            for (int j = 0; j < 4; j++) {
                Vector3D floor = rect3D.vertexArr[rect3D.elementArr[i][j]];
                floor = Matrix4x4.multiply(rotate, floor);
                floor = Matrix4x4.multiply(scale, floor);
                floor = rect3D.toAbsolute(floor);
                rect3D.print();
                floor = rect3D.perspective(floor, -200, -1000);
                floor = rect3D.toScreen(floor, window);
                ceil[j] = floor;
            }
            g.strokeLine(ceil[0].getX(), ceil[0].getY(), ceil[1].getX(), ceil[1].getY());
            g.strokeLine(ceil[1].getX(), ceil[1].getY(), ceil[2].getX(), ceil[2].getY());
            g.strokeLine(ceil[2].getX(), ceil[2].getY(), ceil[3].getX(), ceil[3].getY());
            g.strokeLine(ceil[3].getX(), ceil[3].getY(), ceil[0].getX(), ceil[0].getY());
        }
    }
}
