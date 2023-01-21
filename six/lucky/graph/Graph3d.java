package six.lucky.graph;

import javafx.scene.canvas.GraphicsContext;
import six.lucky.swing.Window;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Graph3d {
    private final Lock rect3DLock = new ReentrantLock();
    public static void stroke3dRect(Rect3D rect3D,Window window) {
        GraphicsContext g = window.getMainCanvas();
        Matrix4x4 rotate = Matrix4x4.multiply(Graph.rotate(rect3D.modelAbsoluteAngle.getX(), true, false, false), Graph.rotate(rect3D.modelAbsoluteAngle.getY(), false, true, false));
        window.cleanArtboard();
        for (int i = 0; i < 6; i++) {
            Vector3D[] ceil = new Vector3D[4];
            for (int j = 0; j < 4; j++) {
                Vector3D floor = rect3D.vertexArr[rect3D.elementArr[i][j]];
                floor = Matrix4x4.multiply(rotate, floor);
                floor = rect3D.toAbsolute(floor);
                floor = rect3D.perspective(floor, -150, -100);
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
