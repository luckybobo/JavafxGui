package six.lucky.swing;

import javafx.scene.image.Image;

import java.util.Objects;

public class GraphTools {
    public static Image getImageByPath(String path){
        return new Image(Objects.requireNonNull(GraphTools.class.getClassLoader().getResource(path)).toExternalForm());
    }
    public static Image getImageByPath(String path,double w,double h){
        return new Image(Objects.requireNonNull(GraphTools.class.getClassLoader().getResource(path)).toExternalForm(),w,h,false,false);
    }
}
