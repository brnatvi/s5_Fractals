package view;

import controller.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.stage.WindowEvent;


public class Gui extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(Gui.class.getResource("/mainWindow.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Fractals");

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));

        Scene sc = new Scene(root, 1000, 600);
        primaryStage.setScene(sc);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        
        Controller controller = new Controller();

        GuiController guiController = loader.getController();
        guiController.setFractalController(controller);
        guiController.setStage(primaryStage);

        primaryStage.show();

        
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
        {
            @Override
            public void handle(WindowEvent event)
            {
                try
                {
                    Platform.exit();
                    System.exit(0);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) { launch(args); }
}
