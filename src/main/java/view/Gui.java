package view;

import controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class Gui extends Application
{
    private static GuiController controllerMain;

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

        Controller c = new Controller();

        GuiController controller = loader.getController();
        controller.setFractalController(c);
        controller.setStage(primaryStage);


        /*
        FractalFactory f = new FractalFactory(c);

        c.setTypeFractal(Initialisable.TypeFractal.JULIA);
        Calculable frJulia = f.create();


        c.setTypeFractal(Initialisable.TypeFractal.MANDELBROT);
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

        BufferedImage imgJ = new BufferedImage(c.getWidthPNG(), c.getHeightPNG(), BufferedImage.TYPE_INT_RGB );
        BufferedImage imgM = new BufferedImage(c.getWidthPNG(), c.getHeightPNG(), BufferedImage.TYPE_INT_RGB );
        long start = System.currentTimeMillis();

        imgJ.setRGB(0,0,c.getWidthPNG(),c.getHeightPNG(), pointsJ, 0, c.getWidthPNG());
        imgM.setRGB(0,0,c.getWidthPNG(),c.getHeightPNG(), pointsM, 0, c.getWidthPNG());

        long finish = System.currentTimeMillis();
        int diff = (int) (finish - start);
        System.out.println("drawn during      " + diff + " milliseconds");
        File fileJulia = new File("/home/nata/Documents/L3_Project_POO_JavaFx/MyFile_J.png");
        ImageIO.write(imgJ, "PNG", fileJulia);
        File fileMandelbrot = new File("/home/nata/Documents/L3_Project_POO_JavaFx/MyFile_M.png");
        ImageIO.write(imgM, "PNG", fileMandelbrot);
         */


        primaryStage.show();
    }


    public static void main(String[] args)
    {
        launch(args);
    }
}
