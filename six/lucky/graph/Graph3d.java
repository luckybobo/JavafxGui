package six.lucky.graph;

import javafx.scene.canvas.GraphicsContext;
import six.lucky.swing.Window;

public class Graph3d {
    public static void stroke3dRect(Point xyz, double w, double h, double d, double horizon, double s, Window window){
        try {
            GraphicsContext g = window.getMainCanvas();
            double EPX = window.getPrimaryStageWidth() / 2;
            double EPY = window.getPrimaryStageHeight() / 2;
            xyz.setX(xyz.getX() - EPX);
            xyz.setY(xyz.getY() - EPY);
            Point point1 = new Point(horizon * xyz.getX() / xyz.getZ() + EPX, horizon * xyz.getY() / xyz.getZ() + EPY);
            Point point2 = new Point(horizon * xyz.getX() / xyz.getZ() + EPX, horizon * (xyz.getY() + h) / xyz.getZ() + EPY);
            Point point3 = new Point(horizon * (xyz.getX() + w) / xyz.getZ() + EPX, horizon * (xyz.getY() + h) / xyz.getZ() + EPY);
            Point point4 = new Point(horizon * (xyz.getX() + w) / xyz.getZ() + EPX, horizon * xyz.getY() / xyz.getZ() + EPY);
            Point point5 = new Point(horizon * xyz.getX() / (xyz.getZ() + d) + EPX, horizon * xyz.getY() / (xyz.getZ() + d) + EPY);
            Point point6 = new Point(horizon * xyz.getX() / (xyz.getZ() + d) + EPX, horizon * (xyz.getY() + h) / (xyz.getZ() + d) + EPY);
            Point point7 = new Point(horizon * (xyz.getX() + w) / (xyz.getZ() + d) + EPX, horizon * (xyz.getY() + h) / (xyz.getZ() + d) + EPY);
            Point point8 = new Point(horizon * (xyz.getX() + w) / (xyz.getZ() + d) + EPX, horizon * xyz.getY() / (xyz.getZ() + d) + EPY);
            g.strokePolygon(
                    new double[]{point1.getX(), point2.getX(), point3.getX(), point4.getX()},
                    new double[]{point1.getY(), point2.getY(), point3.getY(), point4.getY()},
                    4
            );
            g.strokePolygon(
                    new double[]{point5.getX(), point6.getX(), point7.getX(), point8.getX()},
                    new double[]{point5.getY(), point6.getY(), point7.getY(), point8.getY()},
                    4
            );
            g.strokeLine(point1.getX(), point1.getY(), point5.getX(), point5.getY());
            g.strokeLine(point2.getX(), point2.getY(), point6.getX(), point6.getY());
            g.strokeLine(point3.getX(), point3.getY(), point7.getX(), point7.getY());
            g.strokeLine(point4.getX(), point4.getY(), point8.getX(), point8.getY());
        }catch (Throwable t) {
            System.out.println("error");
        }
    }
}
