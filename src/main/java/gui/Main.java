package gui;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application{

	public static void main(String[] args) {
        launch(args);
    }
	
    @Override
    public void start(Stage stage) {

    	try {
            Parent root = new FXMLLoader(getClass().getClassLoader()
                    .getResource("Mainview.fxml")).load();

            stage.setScene(new Scene(root));
            stage.setTitle("OOS Bank System");
            stage.show();
    	}catch(IOException e){
    		e.printStackTrace();
    	} 
    }
}
