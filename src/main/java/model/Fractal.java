package model;

import controller.Controller;

import java.util.concurrent.RecursiveTask;


public class Fractal
{
    private Complex constant;
   // private double coefficient;
   // private int countThreads;
   // private long timeOfExecution;
   // private String pathFile;
   //
   // private Initialisable.TypeFunction f;
   // private Initialisable.TypeFractal fract;
   // private Initialisable.ColorScheme color;

    private double EPSILON;
    private Controller controller;
    private int[][] points;
    private double raduis;
    private int maxIt;
    private int size;

    public Fractal (Controller c)
    {
        this.controller = c;
        constant = controller.getConstant();
      //  coefficient = c.getCoeff();
      //  countThreads = c.getCountThreads();
      //  timeOfExecution = c.getTime();
      //  pathFile = c.getPath();
      //  f = c.getFunction();
      //  fract = c.getTypeFractal();
      //  color = c.getColor();
      size = controller.getWidthPNG();

        this.points = new int[size][size];
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                this.points[i][j] = 0;
            }
        }

       // EPSILON = controller.getRadius()/(double)size;
        EPSILON = controller.getRadius()/(double)size;
        System.out.println("epsilon       = " + EPSILON);
        maxIt = controller.getMAX_ITER();
        System.out.println("max iteration = " + maxIt);
        raduis = controller.getRadius();
        System.out.println("radius        = " + raduis);
    }

    public int[][] divergence_Julia()
    {
        //Complex z = Complex.createComplex(0, 0);
        double d_size = (double)size/2.0;
        for (double i = -1.0; i < 1.0; i += EPSILON)
        {
            for (double j = -1.0; j < 1.0; j += EPSILON)
            {
                int iter = 0;
               // z.setRe(i);
               // z.setIm(j);
                Complex z = Complex.createComplex(i, j);
                while (z.mod() < raduis && iter < maxIt)
                {
                    z = z.times(z).plus(constant);
                    iter++;
                }

                double val = (double)(iter) / (double)(maxIt);
                int colorR = (int)(1251.0 * val);                                     // color interpretation
                int colorG = (int)(4567.0 * (1.0-val));
                int colorB = (int)(1852.0 * (z.mod() / raduis));
                if ((z.mod() / raduis) > 1.0)
                {
                    colorB = 255;
                }
                points[(int)((i+1.0)*d_size)][(int)((j+1.0)*d_size)] = (colorR << 16) | (colorG << 8) | colorB;
            }
        }
        return points;
    }

    public int[][] divergence_Mandelbrot()
    {
        Complex z = Complex.createComplex(0, 0);
        double d_size = (double)size/2.0;
        for (double i = -1.0; i < 1.0; i += EPSILON)
        {
            for (double j = -1.0; j < 1.0; j += EPSILON)
            {
                int iter = 0;
                Complex c = Complex.createComplex(i, j);
                while (z.mod() < raduis && iter < maxIt)
                {
                    z = z.times(z).plus(c);
                    iter++;
                }

                double val = (double)(iter) / (double)(maxIt);
                int colorR = (int)(255.0 * val);                                     // color interpretation
                int colorG = (int)(255.0 * (1.0-val));
                int colorB = (int)(255.0 * (z.mod() / raduis));
                if ((z.mod() / raduis) > 1.0)
                {
                    colorB = 0;
                }
                points[(int)((i+1.0)*d_size)][(int)((j+1.0)*d_size)] = (colorR << 16) | (colorG << 8) | colorB;
            }
        }
        return points;
    }
    
}
