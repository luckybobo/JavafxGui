package sixlucky.swing;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sixlucky.graph.ArrayMath;
import sixlucky.graph.Camera;
import sixlucky.graph.Graph;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Window {
    public static String powerBy = "luckybob";
    public static boolean isRunning = false;
    public static double Timer = 1;
    private volatile double[][] buf;
    static SimpleDateFormat formatter = new SimpleDateFormat("'['HH:mm:ss']'");
    static Date date = new Date();
    public boolean isListenForData = false;
    private final Lock threadLock = new ReentrantLock();
    private final Lock mthreadLock = new ReentrantLock();
    private final Canvas mainCanvas = new Canvas();
    private final GraphicsContext mg = mainCanvas.getGraphicsContext2D();
    private final Pane layout =new Pane();
    private final ObservableList<Node> layoutNode = layout.getChildren();
    private final Scene sc=new Scene(layout);
    private final Stage primaryStage = new Stage();
    private final ArrayList<Persona> allPersona = new ArrayList<>();
    private Camera camera = new Camera();
    private Robot robot;

    {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }
    public Window() {
        primaryStage.setHeight(500+getMenuHeight());
        primaryStage.setWidth(500+getMenuWidth());
        primaryStage.setTitle("Lucky");
        initStage();
    }
    public Window(String title) {
        primaryStage.setHeight(500+getMenuHeight());
        primaryStage.setWidth(500+getMenuWidth());
        primaryStage.setTitle(title);
        initStage();
    }
    public Window(String title, double w, double h) {
        primaryStage.setHeight(h+getMenuHeight());
        primaryStage.setWidth(w+getMenuWidth());
        primaryStage.setTitle(title);
        initStage();
    }
    private void initStage(){
        buf = new double[(int) (primaryStage.getHeight())][(int) (primaryStage.getWidth())];
        for (int i = 0; i < primaryStage.getHeight(); i++) {
            for (int j = 0; j < primaryStage.getWidth(); j++) {
                buf[i][j]= camera.farPlane;
            }
        }
        new Thread(()->{
            while (isRunning){
                date = new Date();
                try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
            }
        }).start();
        isRunning = true;
        {
            primaryStage.setResizable(false);
        }
        {
            mainCanvas.setWidth(getWidth());
            mainCanvas.setHeight(getHeight());
        }
        {
            layout.setOnKeyPressed(this::PersonaOnKeyPressed);
            layout.setOnKeyReleased(this::PersonaOnKeyReleased);
            layout.setOnMouseMoved(this::PersonaOnMouseMoved);
        }
        {
            layoutNode.add(mainCanvas);
        }
        layout.requestFocus();
        primaryStage.setScene(sc);
    }
    public void show() {
        primaryStage.show();
    }
    public void setTitle(String title){
        primaryStage.setTitle(title);
    }
    public double getX(){
        return primaryStage.getX();
    }
    public void setX(double x){
        primaryStage.setX(x);
    }
    public double getY(){
        return primaryStage.getX();
    }
    public void setY(double y){
        primaryStage.setY(y);
    }
    public void setCoordinate(double x, double y){
        primaryStage.setX(x);
        primaryStage.setY(y);
    }
    public double getStageCentreScreenX(){
        return primaryStage.getX() + primaryStage.getWidth()/2;
    }
    public double getStageCentreScreenY(){
        return primaryStage.getY() + primaryStage.getHeight()/2;
    }
    public double getStageCentreX(){
        return getWidth()/2;
    }
    public double getStageCentreY(){
        return getHeight()/2;
    }
    public double getWidth(){
        return primaryStage.getWidth()-getMenuWidth();
    }
    public void setWidth(double w){
        primaryStage.setWidth(w+getMenuWidth());
    }
    public double getHeight(){
        return primaryStage.getHeight()-getMenuHeight();
    }
    public void setHeight(double h){
        primaryStage.setWidth(h+getMenuHeight());
    }
    public void setSize(double w,double h){
        primaryStage.setWidth(w+getMenuWidth());
        primaryStage.setHeight(h+getMenuHeight());
    }
    public Robot getRobot(){
        return robot;
    }
    public Stage getPrimaryStage(){
        return primaryStage;
    }
    public Pane getLayout(){
        return layout;
    }
    public ObservableList<Node> getLayoutNode(){
        return layoutNode;
    }
    public GraphicsContext getMainCanvas(){
        return mg;
    }
    public void setTimer(double Timer){
        Window.Timer =Timer;
    }
    public void cleanArtboard(){
        mg.clearRect(0,0,getWidth(),getHeight());
    }
    public void refresh(){
        cleanBuf();
        cleanArtboard();
    }
    public static double getMenuHeight(){
        return 30;
    }
    public static double getMenuWidth(){
        return 6;
    }
    public void setBuf(int y,int x, double z){
        buf[y][x]=z;
    }
    public double getBuf(int y, int x){
        return buf[y][x];
    }
    public Camera getCamera(){
        return camera;
    }
    public void setCamera(Camera c){
        camera=c;
    }
    public void hideCursor(){
        sc.setCursor(new ImageCursor(Graph.getImageByPath("sixlucky/swing/mouse.png"),-100,-100));
    }
    public void showCursor(){
        sc.setCursor(Cursor.DEFAULT);
    }
    public void setCursor(ImageCursor ic){
        sc.setCursor(ic);
    }
    public void setCursor(Cursor c){
        sc.setCursor(c);
    }
    public void cleanBuf(){
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                buf[i][j] = camera.farPlane;
            }
        }
    }
    void PersonaOnKeyPressed(KeyEvent event){
        new Thread(() -> {
            threadLock.lock();
            try {
                for (Persona persona : allPersona) {
                    if(persona.isPressToListen) {
                        if(isListenForData)printPersonaStatus(persona,"KeyPressed");
                        persona.personaKeyPressedEvents.forEach(e -> e.personaKeyPressedEvent(event));
                    }
                }
            } finally {
                threadLock.unlock();
            }
        }).start();
    }
    void PersonaOnKeyReleased(KeyEvent event) {
        new Thread(()-> {
            for (Persona persona : allPersona) {
                if (persona.isPressToListen) {
                    if (isListenForData) printPersonaStatus(persona, "KeyReleased");
                    persona.personaKeyReleasedEvents.forEach(e -> e.personaKeyReleasedEvent(event));
                }
            }
        }).start();
    }
    void PersonaOnMouseMoved(MouseEvent event) {
        new Thread(()-> {
            try {
                mthreadLock.lock();
                for (Persona persona : allPersona) {
                    if (persona.isMouseToListen) {
                        if (isListenForData) printPersonaStatus(persona, "MouseMoved");
                        persona.personaMouseMovedEvents.forEach(e -> e.personaMouseMovedEvent(event));
                    }
                }
            }finally {
                mthreadLock.unlock();
            }
        }).start();
    }
    public void printPersonaStatus(Persona persona,String Information){
        System.out.println("----------------"+Information);
        System.out.println("ThreadName " + Thread.currentThread().getName());
        System.out.println("Instance "+persona);
        for (IndexEvent pie : persona.personaIndexEvents)System.out.println("IndexEventState "+pie.index);
        System.out.println("FalseCoordinates "+persona.getX());
        System.out.println("FalseCoordinates "+persona.getY());
        System.out.println("TrueCoordinates  "+persona.getPersonaImageView().getX());
        System.out.println("TrueCoordinates  "+persona.getPersonaImageView().getY());
        System.out.println("------------------"+ Window.formatter.format(Window.date));
    }
    public void addPersona(Persona p) {
        if(p==null){System.out.println("p:null");return;}
        if(p.getPersonaImageQuantity()>0) {
            layoutNode.add(p.getPersonaImageView());
        }
        allPersona.add(p);
        p.startPersonaThread();
    }
    public void mouseCameraRotate(MouseEvent event,double ax,double ay,double sim){
        double a = event.getX() - getStageCentreX();
        double b = getStageCentreY() - event.getY();
        double f = Math.toDegrees(Math.atan(b / a));
        if(event.getScreenX()<(int) getStageCentreScreenX()-sim) {
            getCamera().rotate(0,ay, 0);
        }else if(event.getScreenX() > (int) getStageCentreScreenX()+sim) {
            getCamera().rotate(0, -ay, 0);
        }
        if(event.getScreenY()<(int) getStageCentreScreenY()-sim){
            getCamera().rotate(ax,0,0);
        }else if(event.getScreenY()>(int) getStageCentreScreenY()+sim){
            getCamera().rotate(-ax,0,0);
        }
        getRobot().mouseMove((int) getStageCentreScreenX(), (int) getStageCentreScreenY());
    }
}