package gui;
import javafx.stage.*;

/**
 * Global Object for accessing Stages one at a time
 *
 */
public class StageSingleton {
	
    private Stage stage;
    private static final StageSingleton instance = new StageSingleton();

    private StageSingleton() {}

    public static StageSingleton getInstance() {return instance;}
    
    public Stage getStage() {return stage;}
    public void setStage(Stage stage) {this.stage = stage;}
}
