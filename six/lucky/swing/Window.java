package six.lucky.swing;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Window {
    public static String powerBy = "luckybob";
    public static boolean isRunning = false;
    public static double Timer = 1;
    static SimpleDateFormat formatter = new SimpleDateFormat("'['HH:mm:ss']'");
    static Date date = new Date();
    private boolean isListenForData = false;
    private final Lock threadLock = new ReentrantLock();
    private final Canvas mainCanvas = new Canvas();
    private final GraphicsContext mg = mainCanvas.getGraphicsContext2D();
    private final Pane layout =new Pane();
    private final ObservableList<Node> layoutNode = layout.getChildren();
    private final Scene sc=new Scene(layout);
    private final Stage primaryStage = new Stage();
    private final ArrayList<Persona> allPersona = new ArrayList<>();
    public Window(){
        primaryStage.setHeight(500+getMenuHeight());
        primaryStage.setWidth(500);
        primaryStage.setTitle("Lucky");
        initStage();
    }
    public Window(String title){
        primaryStage.setHeight(500+getMenuHeight());
        primaryStage.setWidth(500);
        primaryStage.setTitle(title);
        initStage();
    }
    public Window(String title, double w, double h){
        primaryStage.setHeight(h+getMenuHeight());
        primaryStage.setWidth(w);
        primaryStage.setTitle(title);
        initStage();
    }
    private void initStage(){
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
        }
        {
        }
        layout.requestFocus();
        primaryStage.setScene(sc);
    }
    public void show() {
        layoutNode.add(mainCanvas);
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
    public double getWidth(){
        return primaryStage.getWidth();
    }
    public void setWidth(double w){
        primaryStage.setWidth(w);
    }
    public double getHeight(){
        return primaryStage.getHeight()-getMenuHeight();
    }
    public void setHeight(double h){
        primaryStage.setWidth(h+getMenuHeight());
    }
    public void setSize(double w,double h){
        primaryStage.setWidth(w);
        primaryStage.setHeight(h+getMenuHeight());
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
    public void isListenForData(boolean isListenForData){
        this.isListenForData = isListenForData;
    }
    public void cleanArtboard(){
        mg.clearRect(0,0,getWidth(),getHeight());
    }
    public static double getMenuHeight(){
        return 30;
    }
    void PersonaOnKeyPressed(KeyEvent event){
        new Thread(() -> {
            threadLock.lock();
            try {
                for (Persona persona : allPersona) {
                    if(persona.getPresstoListen()) {
                        if(isListenForData){printPersonaStatus(persona,"KeyPressed");}
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
                if (persona.getPresstoListen()) {
                    if (isListenForData) {printPersonaStatus(persona, "KeyReleased");}
                    persona.personaKeyReleasedEvents.forEach(e -> e.personaKeyReleasedEvent(event));
                }
            }
        }).start();
    }
    public void printPersonaStatus(Persona persona,String Information){
        System.out.println("----------------"+Information);
        System.out.println("ThreadName " + Thread.currentThread().getName());
        System.out.println("Instance "+persona);
        for (IndexEvent pie : persona.personaIndexEvents) {System.out.println("IndexEventState "+pie.index);}
        System.out.println("FalseCoordinates "+persona.getX());
        System.out.println("FalseCoordinates "+persona.getY());
        System.out.println("TrueCoordinates  "+persona.getPersonaImageView().getX());
        System.out.println("TrueCoordinates  "+persona.getPersonaImageView().getY());
        System.out.println("------------------"+ Window.formatter.format(Window.date));
    }
    public void addPersona(Persona p) {
        if(p==null){System.out.println("p:null");return;}
        layoutNode.add(p.getPersonaImageView());
        allPersona.add(p);
        p.startPersonaThread();
    }
}