package six.lucky.swing;

import java.util.ArrayList;

public abstract class Director {
    boolean isPressToListen = false;
    boolean isBehavioralToListen = false;
    ArrayList<KeyPressed> personaKeyPressedEvents = new ArrayList<>();
    ArrayList<KeyReleased> personaKeyReleasedEvents = new ArrayList<>();
    ArrayList<Behavioral> personaBehavioral = new ArrayList<>();
    ArrayList<IndexEvent> personaIndexEvents = new ArrayList<>();
    public void isPresstoListen(boolean isPressToListen){
        this.isPressToListen = isPressToListen;
    }
    public boolean getPresstoListen(){
        return isPressToListen;
    }
    public void isBehavioraltoListen(boolean isBehavioralToListen){
        this.isBehavioralToListen = isBehavioralToListen;
    }
    public boolean getBehavioraltoListen(){
        return isBehavioralToListen;
    }
    public void addKeyPressed(KeyPressed event){
        if(event==null) {System.out.println("event:null");return;}
        personaKeyPressedEvents.add(event);
    }
    public void addKeyReleased(KeyReleased event){
        if(event==null) {System.out.println("event:null");return;}
        personaKeyReleasedEvents.add(event);
    }
    public void addBehavioral(Behavioral event){
        if(event==null) {System.out.println("event:null");return;}
        personaBehavioral.add(event);
    }
    public void addIndexEvent(IndexEvent index){
        if(index==null) {System.out.println("event:null");return;}
        System.out.println(Window.formatter.format(Window.date)+"Apply IndexEvent "+personaIndexEvents.size());
        personaIndexEvents.add(index);
    }
}