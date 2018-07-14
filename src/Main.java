import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("App.fxml"));
    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = loader.load();

        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
       AppController controller = loader.getController();
       controller.stopApp();
    }
}
