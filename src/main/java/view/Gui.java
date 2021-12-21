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



        /*
        FractalFactory f = new FractalFactory(controller);

        controller.setTypeFractal(Initialisable.TypeFractal.JULIA);
        Calculable frJulia = f.create();


        controller.setTypeFractal(Initialisable.TypeFractal.MANDELBROT);
        Calculable frMandelbrot = f.create();

        long st = System.currentTimeMillis();
        
        int[] pointsJ = frJulia.calculate();

        long fin = System.currentTimeMillis();
        int d = (int) ((fin - st)/1000);
        System.out.println("calculated during " + d + " seconds");

        long st2 = System.currentTimeMillis();

        int[] pointsM = frMandelbrot.calculate();

        long fin2 = System.currentTimeMillis();
        int d2 = (int) ((fin2 - st2)/1000);
        System.out.println("calculated during " + d2 + " seconds");

        BufferedImage imgJ = new BufferedImage(controller.getWidthPNG(), controller.getHeightPNG(), BufferedImage.TYPE_INT_RGB );
        BufferedImage imgM = new BufferedImage(controller.getWidthPNG(), controller.getHeightPNG(), BufferedImage.TYPE_INT_RGB );
        long start = System.currentTimeMillis();

        imgJ.setRGB(0,0,controller.getWidthPNG(),controller.getHeightPNG(), pointsJ, 0, controller.getWidthPNG());
        imgM.setRGB(0,0,controller.getWidthPNG(),controller.getHeightPNG(), pointsM, 0, controller.getWidthPNG());

        long finish = System.currentTimeMillis();
        int diff = (int) (finish - start);
        System.out.println("drawn during      " + diff + " milliseconds");
        File fileJulia = new File("/home/nata/Documents/L3_Project_POO_JavaFx/MyFile_J.png");
        ImageIO.write(imgJ, "PNG", fileJulia);
        File fileMandelbrot = new File("/home/nata/Documents/L3_Project_POO_JavaFx/MyFile_M.png");
        ImageIO.write(imgM, "PNG", fileMandelbrot);
         */


        primaryStage.show();

        
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
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
