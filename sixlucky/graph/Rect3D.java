package sixlucky.graph;

import javafx.scene.paint.Color;
import sixlucky.swing.Window;

public class Rect3D{
    private final Vector3D modelAbsoluteSize = new Vector3D(100,100,100);
    private Vector3D modelAbsoluteScale = new Vector3D(1,1,1);
    private final Vector3D modelAbsoluteAngle = new Vector3D(0,0,0);
    private final Vector3D modelAngle = new Vector3D(0,0,0);
    private Vector3D modelAbsoluteOrigin = new Vector3D(0,0,-200);
    Vector3D[] vertexArr = {
            new Vector3D(50, 50, 50),
            new Vector3D(50, -50, 50),
            new Vector3D(-50, -50, 50),
            new Vector3D(-50, 50, 50),

            new Vector3D(50, 50, -50),
            new Vector3D(50, -50, -50),
            new Vector3D(-50, -50, -50),
            new Vector3D(-50, 50, -50)
    };
int[][] elementArr = {
        {0,1,3},
        {1,2,3},
        {4,5,7},
        {5,6,7},
        {4,0,7},
        {0,3,7},
        {5,1,4},
        {1,0,4},
        {6,2,5},
        {2,1,5},
        {7,3,6},
        {3,2,6}
};
    Color[] elementColor = {
            Color.GREEN,
            Color.GREEN,
            Color.BROWN,
            Color.BROWN,
            Color.YELLOW,
            Color.YELLOW,
            Color.BLUE,
            Color.BLUE,
            Color.PINK,
            Color.PINK,
            Color.GOLD,
            Color.GOLD,
    };
    public Rect3D(){}
    public Rect3D(double width,double height,double depth){
        modelAbsoluteSize.setX(width);
        modelAbsoluteSize.setY(height);
        modelAbsoluteSize.setZ(depth);
        vertexArr = new Vector3D[]{
                new Vector3D(width / 2, height / 2, depth / 2),
                new Vector3D(width / 2, -height / 2, depth / 2),
                new Vector3D(-width / 2, -height / 2, depth / 2),
                new Vector3D(-width / 2, height / 2, depth / 2),

                new Vector3D(width / 2, height / 2, -depth / 2),
                new Vector3D(width / 2, -height / 2, -depth / 2),
                new Vector3D(-width / 2, -height / 2, -depth / 2),
                new Vector3D(-width / 2, height / 2, -depth / 2)
        };
    }
    public Rect3D(double width,double height,double depth,double x,double y,double z){
        modelAbsoluteSize.setX(width);
        modelAbsoluteSize.setY(height);
        modelAbsoluteSize.setZ(depth);
        modelAbsoluteOrigin.setX(x);
        modelAbsoluteOrigin.setY(y);
        modelAbsoluteOrigin.setZ(z);
        vertexArr = new Vector3D[]{
                new Vector3D(width / 2, height / 2, depth / 2),
                new Vector3D(width / 2, -height / 2, depth / 2),
                new Vector3D(-width / 2, -height / 2, depth / 2),
                new Vector3D(-width / 2, height / 2, depth / 2),

                new Vector3D(width / 2, height / 2, -depth / 2),
                new Vector3D(width / 2, -height / 2, -depth / 2),
                new Vector3D(-width / 2, -height / 2, -depth / 2),
                new Vector3D(-width / 2, height / 2, -depth / 2)
        };
    }
    public Rect3D(double width,double height,double depth,double x,double y){
        modelAbsoluteSize.setX(width);
        modelAbsoluteSize.setY(height);
        modelAbsoluteSize.setZ(depth);
        modelAbsoluteOrigin.setX(x);
        modelAbsoluteOrigin.setY(y);
        vertexArr = new Vector3D[]{
                new Vector3D(width / 2, height / 2, depth / 2),
                new Vector3D(width / 2, -height / 2, depth / 2),
                new Vector3D(-width / 2, -height / 2, depth / 2),
                new Vector3D(-width / 2, height / 2, depth / 2),

                new Vector3D(width / 2, height / 2, -depth / 2),
                new Vector3D(width / 2, -height / 2, -depth / 2),
                new Vector3D(-width / 2, -height / 2, -depth / 2),
                new Vector3D(-width / 2, height / 2, -depth / 2)
        };
    }
    public Vector3D perspective(Vector3D v, double nearPlane, double farPlane){
        return Graph.perspective(v,nearPlane,farPlane);
    }
    public Vector3D toAbsolute(Vector3D v) {
        return Graph.toAbsolute(v,modelAbsoluteOrigin);
    }
    public Vector3D toScreen(Vector3D projectedv, Window window){
        return Graph.toScreen(projectedv,window);
    }
    public void translate(double stepx,double  stepy,double  stepz){
        Matrix4x4 m = Graph.translate( stepx, stepy, stepz);
        modelAbsoluteOrigin=Matrix4x4.multiply(m,modelAbsoluteOrigin);
    }
    public void scale(double x,double y,double z) {
        Matrix4x4 m = Graph.scale(x, y, z);
        modelAbsoluteScale = Matrix4x4.multiply(m, modelAbsoluteScale);
    }
    public void rotate(double anglex,double angley,double anglez) {
        modelAngle.setX((modelAngle.getX() + anglex + 360) % 360);
        modelAngle.setY((modelAngle.getY() + angley + 360) % 360);
        modelAngle.setZ((modelAngle.getZ() + anglez + 360) % 360);
    }
    public void rotateAbsolute(double anglex,double angley,double anglez) {
        modelAbsoluteAngle.setX((modelAbsoluteAngle.getX() + anglex + 360) % 360);
        modelAbsoluteAngle.setY((modelAbsoluteAngle.getY() + angley + 360) % 360);
        modelAbsoluteAngle.setZ((modelAbsoluteAngle.getZ() + anglez + 360) % 360);
    }
    public void setOrigin(double x,double y){
        modelAbsoluteOrigin.setX(x);
        modelAbsoluteOrigin.setY(y);
    }
    public void setOrigin(double x,double y,double z){
        modelAbsoluteOrigin.setX(x);
        modelAbsoluteOrigin.setY(y);
        modelAbsoluteOrigin.setZ(z);
    }
    public Color[] getColor(){
        return elementColor;
    }
    public void setColor(int i,Color c){
        elementColor[i]=c;
    }
    public void setColor(Color[] c){
        elementColor=c;
    }
    public Vector3D getAngle(){
        return modelAngle;
    }
    public Vector3D getAbsoluteAngle(){
        return modelAbsoluteAngle;
    }
    public Vector3D getOrigin(){
        return modelAbsoluteOrigin;
    }
    public Vector3D getScale(){
        return modelAbsoluteScale;
    }
    public Vector3D getSize(){
        return modelAbsoluteSize;
    }
    public void allAngleToZero(){
        modelAbsoluteAngle.setXYZ(0,0,0);
    }
    public void setSize(double width,double height,double depth){
        modelAbsoluteSize.setX(width);
        modelAbsoluteSize.setY(height);
        modelAbsoluteSize.setZ(depth);
        vertexArr = new Vector3D[]{
                new Vector3D(width / 2, height / 2, depth / 2),
                new Vector3D(width / 2, -height / 2, depth / 2),
                new Vector3D(-width / 2, -height / 2, depth / 2),
                new Vector3D(-width / 2, height / 2, depth / 2),

                new Vector3D(width / 2, height / 2, -depth / 2),
                new Vector3D(width / 2, -height / 2, -depth / 2),
                new Vector3D(-width / 2, -height / 2, -depth / 2),
                new Vector3D(-width / 2, height / 2, -depth / 2)
        };
    }
    public void print(){
        System.out.println("--------------------");
        for (Vector3D v : vertexArr) {
            v.print();
        }
        System.out.println("--------------------");
    }
}
