package sixlucky.swing;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sixlucky.graph.Graph;

import java.util.ArrayList;
public class Persona extends Director{
    private double x = 0;
    private double y = 0;
    private boolean visible = false;
    private int frequency = 0;
    private double angle = 0;
    private int personaImageQuantity = 0;
    private int personaImageIndex = 0;
    private final ImageView personaImageView;
    private final ArrayList<Image> personaImage = new ArrayList<>();
    private boolean isListenForMath = false;
    private double personaTimer = 1;
    private int last = personaImageIndex;
    public Persona() {
        personaImageView=null;
        refreshPersona();
    }
    public Persona(double x,double y) {
        personaImageView=null;
        this.x = x;
        this.y = y;
        refreshPersona();
    }
    public Persona(String personaImagePath) {
        {
            personaImage.add(Graph.getImageByPath(personaImagePath));
            personaImageView = new ImageView(personaImage.get(0));
            personaImageQuantity++;
        }
        refreshPersona();
    }
    public Persona(String personaImagePath,double x,double y) {
        {
            personaImage.add(Graph.getImageByPath(personaImagePath));
            personaImageView = new ImageView(personaImage.get(0));
            personaImageQuantity++;
        }
        this.x = x;
        this.y = y;
        refreshPersona();
    }
    public void addImage(String imagePath){
        personaImage.add(Graph.getImageByPath(imagePath));
        personaImageQuantity++;
    }
    public void selectImage(int index){
        personaImageIndex = index>=0&&index<=personaImageQuantity-1?index:0;
    }
    public int getPersonaImageIndex(){
        return personaImageIndex;
    }
    public int getPersonaImageQuantity(){
        return personaImageQuantity;
    }
    public void refreshPersona() {
        Platform.runLater(() -> {
            if(personaImageQuantity>0&&visible) {
                personaImageView.setX(x);
                personaImageView.setY(y);
                personaImageView.setRotate(angle);
                personaImageView.setVisible(visible);
                if (personaImageIndex != last) {
                    personaImageView.setImage(personaImage.get(personaImageIndex >= 0 && personaImageIndex <= personaImage.size() - 1 ? personaImageIndex : 0));
                }
                last = personaImageIndex;
            }
        });
    }
    void startPersonaThread(){
        new Thread(()->{
            while (Window.isRunning) {
                try {
                    if (isBehavioralToListen) {
                        personaBehavioral.forEach(e-> {
//                            new Thread(()-> {
                                e.personaBehavioral(frequency, frequency % 2 == 0);
//                            }).start();
                        });
                    }
                    frequency++;
                    Thread.sleep((int)Math.floor(1000/personaTimer));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(() -> {
            while (Window.isRunning) {
                try {
                    personaIndexEvents.forEach(e -> {
                        if (e.index) {
                            new Thread(e::indexEvent).start();
                        }
                    });
                    Thread.sleep((int)Math.floor(20/ Window.Timer));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void index(int index){
        personaIndexEvents.forEach(e->{
            if(personaIndexEvents.indexOf(e)==index){
                e.index=true;
            }
        });
    }
    public void overindex(int index){
        personaIndexEvents.forEach(e->{
            if(personaIndexEvents.indexOf(e)==index){
                e.index=false;
            }
        });
    }
    public void rotate(double s){
        angle=-360<=s&&s<=360?(angle+360+s)%360:0;
    }
    public double getAngle(){
        return angle;
    }
    public Image getPersonaImage(){
        return personaImageView.getImage();
    }
    public ImageView getPersonaImageView(){
        return personaImageView;
    }
    public double getX(){
        return x;
    }
    public void setX(double x){
        this.x = x;
    }
    public double getY(){
        return y;
    }
    public void setY(double y){
        this.y = y;
    }
    public void setCoordinate(double x,double y){
        this.x = x;
        this.y = y;
    }
    public void isListentoMath(boolean isListenForMath){
        this.isListenForMath = isListenForMath;
    }
    public void printMathStatus(double stride,double angle){
        System.out.println(angle>=0&&angle<=360? Window.formatter.format(Window.date)+"Math moveX "+Math.sin(Math.toRadians(angle)) * stride:"Angle:transborder");
        System.out.println(angle>=0&&angle<=360? Window.formatter.format(Window.date)+"Math moveY "+Math.cos(Math.toRadians(angle)) * stride * -1:"Angle:transborder");
    }
    public void setPersonaTimer(double s){
        personaTimer = s;
    }
    public double getPersonaTimer(){
        return personaTimer;
    }
    public void move(double stride,double angle) {
        if(isListenForMath) {printMathStatus(stride,angle);}
        x+=angle>=0&&angle<=360?Math.sin(Math.toRadians(angle))*stride:0;
        y+=angle>=0&&angle<=360?Math.cos(Math.toRadians(angle))*stride*-1:0;
    }
    public void setVisible(boolean v){
        if(personaImageQuantity>0) {
            visible = v;
        }
        refreshPersona();
    }
    public void show(){
        if(personaImageQuantity>0) {
            visible=true;
        }
        refreshPersona();
    }
    public boolean getVisible(){
        return visible;
    }
}