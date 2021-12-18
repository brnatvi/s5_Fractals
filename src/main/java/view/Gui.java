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
        Parent root = FXMLLoader.load(getClass().getResource("/sample2.fxml"));
        primaryStage.setTitle("Fractals");
        Scene sc = new Scene(root, 1000, 600);
        primaryStage.setScene(sc);

        Controller c = new Controller();
        Fractal fr = new Fractal(c);
        Fractal fr2 = new Fractal(c);

//        long st = System.currentTimeMillis();
//        int[][] points = fr.divergence_Julia();
//        long fin = System.currentTimeMillis();
//        int d = (int) ((fin - st)/1000);
//        System.out.println("calculated during " + d + " seconds");
//
//        long st2 = System.currentTimeMillis();
//        int[][] points2 = fr2.divergence_Mandelbrot();
//        long fin2 = System.currentTimeMillis();
//        int d2 = (int) ((fin2 - st2)/1000);
//        System.out.println("calculated during " + d2 + " seconds");
//        int w = c.getWidthPNG();
//        BufferedImage img = new BufferedImage(w, w/2, BufferedImage.TYPE_INT_RGB );
//        BufferedImage img2 = new BufferedImage(w, w/2, BufferedImage.TYPE_INT_RGB );
//        long start = System.currentTimeMillis();
//        for ( int i = 0; i < w; i++ )
//        {
//            for (int j = 0; j < w/2; j++ )
//            {
//                img.setRGB(i, j, points[i][j]);
//                img2.setRGB(i, j, points2[i][j]);
//            }
//        }
//        long finish = System.currentTimeMillis();
//        int diff = (int) (finish - start);
//        System.out.println("drawn during      " + diff + " milliseconds");
//        File f = new File("/Users/sihno/Desktop/img/MyFile_J.png");
//        ImageIO.write(img, "PNG", f);
//        File f2 = new File("/Users/sihno/Desktop/img/MyFile_M.png");
//        ImageIO.write(img2, "PNG", f2);
        primaryStage.show();
    }


    public static void main(String[] args)
    {
        launch(args);
    }
}
