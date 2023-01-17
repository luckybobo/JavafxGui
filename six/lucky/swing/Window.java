package six.lucky.swing;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    private final Lock dragLock = new ReentrantLock();
    private boolean isbtcing = false;
    private boolean isCing = false;
    static boolean isStageMoving = false;
    private final int menuAmount = 10;
    private final Canvas btc = new Canvas();
    private final Canvas menuCanvas = new Canvas();
    private final Canvas mainCanvas = new Canvas();
    private final GraphicsContext g = menuCanvas.getGraphicsContext2D();
    private final GraphicsContext mg = mainCanvas.getGraphicsContext2D();
    private final GraphicsContext btg = btc.getGraphicsContext2D();
    private final Pane layout =new Pane();
    private final ObservableList<Node> layoutNode = layout.getChildren();
    private final Scene sc=new Scene(layout);
    private final Stage primaryStage = new Stage();
    private double mouseDraggedX;
    private double mouseDraggedY;
    private final ArrayList<Persona> allPersona = new ArrayList<>();
    static final int menuBarHeight = 30;
    static final Color menuColor = Color.rgb(185,185,185);
    private String title;
    public Window(){
        primaryStage.setHeight(500);
        primaryStage.setWidth(500);
        this.title = "MyLGPUnitAPP";
        initStage();
    }
    public Window(String title){
        primaryStage.setHeight(500);
        primaryStage.setWidth(500);
        this.title = title;
        initStage();
    }
    public Window(String title, double w, double h){
        primaryStage.setHeight(h);
        primaryStage.setWidth(w);
        this.title = title;
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
            primaryStage.initStyle(StageStyle.TRANSPARENT);
        }
        {
            menuCanvas.setWidth(primaryStage.getWidth());
            menuCanvas.setHeight(menuBarHeight);
            mainCanvas.setLayoutY(menuBarHeight);
            mainCanvas.setWidth(primaryStage.getWidth());
            mainCanvas.setHeight(primaryStage.getHeight()-menuBarHeight);
        }
        {
            layout.setOnKeyPressed(this::PersonaOnKeyPressed);
            layout.setOnKeyReleased(this::PersonaOnKeyReleased);
            sc.setOnMouseDragged(this::mouseDraggedEvent);
            sc.setOnMousePressed(this::mousePressedEvent);
            sc.setOnMouseReleased(event -> isStageMoving=false);
        }
        {
            btc.setWidth(menuBarHeight);
            btc.setHeight(menuBarHeight);
            btc.setLayoutX(primaryStage.getWidth()-btc.getWidth());
            btg.strokeLine(menuAmount,menuAmount,btc.getWidth()-menuAmount,btc.getHeight()-menuAmount);
            btg.strokeLine(btc.getWidth()-menuAmount,menuAmount,menuAmount,btc.getHeight()-menuAmount);
            btc.setOnMouseClicked(event -> new Thread(()-> {
                if(!isCing&&!isStageMoving) {
                    isCing=true;
                    try {
                        for (int i = 5; i >= -5; i--) {
                            btg.setFill(Color.rgb(185, 185, 185));
                            btg.fillRect(0, 0, btc.getWidth(), btc.getHeight());
                            btg.setFill(Color.rgb(255, 7, 7));
                            btg.fillRect(i, i, btc.getWidth() - 2 * i, btc.getHeight() - 2 * i);
                            btg.strokeLine(menuAmount, menuAmount, btc.getWidth() - menuAmount, btc.getHeight() - menuAmount);
                            btg.strokeLine(btc.getWidth() - menuAmount, menuAmount, menuAmount, btc.getHeight() - menuAmount);
                            Thread.sleep(20);
                            btg.clearRect(0, 0, btc.getWidth(), btc.getHeight());
                        }
                        btg.setFill(Color.rgb(185, 185, 185));
                        btg.fillRect(0, 0, btc.getWidth(), btc.getHeight());
                        btg.setFill(Color.rgb(255, 7, 7));
                        btg.fillRect(0, 0, btc.getWidth(), btc.getHeight());
                        btg.strokeLine(menuAmount, menuAmount, btc.getWidth() - menuAmount, btc.getHeight() - menuAmount);
                        btg.strokeLine(btc.getWidth() - menuAmount, menuAmount, menuAmount, btc.getHeight() - menuAmount);
                        isRunning = false;
                        isCing=false;
                        Platform.runLater(primaryStage::close);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start());
            btc.setOnMouseEntered(event -> {
                if(!isStageMoving) {
                    btg.clearRect(0, 0, btc.getWidth(), btc.getHeight());
                    btg.setFill(Color.rgb(255, 7, 7));
                    btg.fillRect(0, 0, btc.getWidth(), btc.getHeight());
                    btg.strokeLine(menuAmount, menuAmount, btc.getWidth() - menuAmount, btc.getHeight() - menuAmount);
                    btg.strokeLine(btc.getWidth() - menuAmount, menuAmount, menuAmount, btc.getHeight() - menuAmount);
                }
            });
            btc.setOnMouseExited(event -> {
                if(!isbtcing&&!isCing&&!isStageMoving) {
                    isbtcing = true;
                    new Thread(() -> {
                        try {
                            btg.clearRect(0, 0, btc.getWidth(), btc.getHeight());
                            int a = 255;
                            for (int i = 0; i <= 185; i += 5) {
                                btg.setFill(Color.rgb(a, i, i));
                                btg.fillRect(0, 0, btc.getWidth(), btc.getHeight());
                                btg.strokeLine(menuAmount, menuAmount, btc.getWidth() - menuAmount, btc.getHeight() - menuAmount);
                                btg.strokeLine(btc.getWidth() - menuAmount, menuAmount, menuAmount, btc.getHeight() - menuAmount);
                                Thread.sleep(4);
                                a -= a > 185 ? 1 : 0;
                            }
                            btg.setFill(Color.rgb(185, 185, 185));
                            btg.fillRect(0, 0, btc.getWidth(), btc.getHeight());
                            btg.strokeLine(menuAmount, menuAmount, btc.getWidth() - menuAmount, btc.getHeight() - menuAmount);
                            btg.strokeLine(btc.getWidth() - menuAmount, menuAmount, menuAmount, btc.getHeight() - menuAmount);
                            isbtcing = false;
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }).start();
                }
            });
        }
        layout.requestFocus();
        primaryStage.setScene(sc);
        refreshMenu();
    }
    public void refreshMenu(){
        g.setFill(menuColor);
        g.fillRect(0,0,primaryStage.getWidth(), menuBarHeight);
        g.setFill(Color.BLACK);
        g.fillText(title, 25,17,100);
    }
    private void mouseDraggedEvent(MouseEvent event) {
        if(isStageMoving) {
            new Thread(() -> {
                try {
                    dragLock.lock();
                    primaryStage.setX(primaryStage.getX() + event.getScreenX() - mouseDraggedX);
                    primaryStage.setY(primaryStage.getY() + event.getScreenY() - mouseDraggedY);
                    mouseDraggedX = event.getScreenX();
                    mouseDraggedY = event.getScreenY();
                } finally {
                    dragLock.unlock();
                }
            }).start();
        }
    }
    private void mousePressedEvent(MouseEvent event){
        if(event.getY()<=menuBarHeight&&!isStageMoving) {
            isStageMoving = true;
            mouseDraggedX = event.getScreenX();
            mouseDraggedY = event.getScreenY();
        }
    }
    public void show() {
        layoutNode.add(mainCanvas);
        layoutNode.add(menuCanvas);
        layoutNode.add(btc);
        primaryStage.show();
    }
    public void setTitle(String title){
        this.title = title!=null?title:this.title;
        refreshMenu();
    }
    public double getPrimaryStageX(){
        return primaryStage.getX();
    }
    public void setPrimaryStageX(double x){
        primaryStage.setX(x);
    }
    public double getPrimaryStageY(){
        return primaryStage.getX();
    }
    public void setPrimaryStageY(double y){
        primaryStage.setY(y);
    }
    public void setPrimaryStageBounds(double x, double y){
        primaryStage.setX(x);
        primaryStage.setY(y);
    }
    public double getPrimaryStageWidth(){
        return primaryStage.getWidth();
    }
    public void setPrimaryStageWidth(double w){
        primaryStage.setWidth(w);
    }
    public double getPrimaryStageHeight(){
        return primaryStage.getHeight();
    }
    public void setPrimaryStageHeight(double h){
        primaryStage.setWidth(h);
    }
    public void setPrimaryStageSize(double w,double h){
        primaryStage.setWidth(w);
        primaryStage.setHeight(h);
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