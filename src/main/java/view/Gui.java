package view;

import controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Fractal;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;


public class Gui extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("My window");
        Scene sc = new Scene(root, 400, 400);
        primaryStage.setScene(sc);

        Controller c = new Controller();
        Fractal fr = new Fractal(c);

        long st = System.currentTimeMillis();
        int[][] points = fr.divergence_Julia();
       // int[][] points = fr.divergence_Mandelbrot();
        long fin = System.currentTimeMillis();
        int d = (int) ((fin - st)/1000);
        System.out.println("calculated during " + d + " seconds");

        BufferedImage img = new BufferedImage(c.getWidthPNG(), c.getHeightPNG(), BufferedImage.TYPE_INT_RGB );
        long start = System.currentTimeMillis();
        for ( int i = 0; i < c.getWidthPNG(); i++ )
        {
            for (int j = 0; j < c.getHeightPNG(); j++ )
            {
                img.setRGB(i, j, points[i][j]);
            }
        }
        long finish = System.currentTimeMillis();
        int diff = (int) (finish - start);
        System.out.println("drawn during      " + diff + " milliseconds");
        File f = new File("/home/nata/Documents/L3_Project_POO_JavaFx/MyFile.png");
        ImageIO.write(img, "PNG", f);

        primaryStage.show();
    }


    public static void main(String[] args)
    {
        launch(args);
    }
}
