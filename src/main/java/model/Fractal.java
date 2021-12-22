package model;

import controller.Controller;

import java.awt.geom.Rectangle2D;
import java.util.function.BiFunction;

public abstract class Fractal implements interfaces.Calculable
{
    protected Complex    constant;
    protected Controller controller;
    protected int[]      points;
    protected double     radius;
    protected int        maxIt;
    protected int        widthPNG;
    protected int        heightPNG;
    protected double     left;
    protected double     right;
    protected double     up;
    protected double     down;
    protected BiFunction calc;
    protected BiFunction paint;
    protected int        countThreads;

    public Fractal(Controller c)
    {
        controller = c;
        Rectangle2D.Double r = controller.getComplexRect();
        calc = controller.getFractalFunction();
        paint = controller.getColorFunction();
        constant = controller.getConstant();
        widthPNG = controller.getWidthPNG();
        heightPNG = controller.getHeightPNG();
        maxIt = controller.getMaxIter();
        radius = controller.getRadius();

        left = r.x;
        right = r.x + r.width;
        down = r.y;
        up = r.y + r.height;

        points = new int[widthPNG * heightPNG];
    }

    public Fractal(Controller c, int nbThread)
    {
        controller = c;
        Rectangle2D.Double r = controller.getComplexRect();
        calc = controller.getFractalFunction();
        paint = controller.getColorFunction();
        constant = controller.getConstant();
        widthPNG = controller.getWidthPNG();
        heightPNG = controller.getHeightPNG();
        maxIt = controller.getMaxIter();
        radius = controller.getRadius();
        countThreads = controller.getCountThreads();

        left = r.x;
        right = r.x + r.width;
        // take a slice
        up = r.y + ((double)nbThread + 1.0)*(r.height/(double)countThreads);
        down = r.y + (double)nbThread*(r.height/(double)countThreads);


        // take a slice
        points = new int[widthPNG * (heightPNG/countThreads)];
    }

}
