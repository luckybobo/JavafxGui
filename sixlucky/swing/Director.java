package sixlucky.swing;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class Director {
    public boolean isPressToListen = false;
    public boolean isBehavioralToListen = false;
    public boolean isMouseToListen = false;
    HashSet<KeyPressed> personaKeyPressedEvents = new HashSet<>();
    HashSet<KeyReleased> personaKeyReleasedEvents = new HashSet<>();
    HashSet<MouseMoved> personaMouseMovedEvents = new HashSet<>();
    HashSet<Behavioral> personaBehavioral = new HashSet<>();
    ArrayList<IndexEvent> personaIndexEvents = new ArrayList<>();
    public void addMouseMoved(MouseMoved event){
        if(event==null) {System.out.println("event:null");return;}
        personaMouseMovedEvents.add(event);
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