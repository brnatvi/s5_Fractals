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
    protected double     coefficient;
    protected double     left;
    protected double     right;
    protected double     up;
    protected double     down;
    protected BiFunction calc;
    protected BiFunction paint;

    public Fractal(Controller c)
    {
        controller = c;
        Rectangle2D.Double r = controller.getComplexRect();

        calc = controller.getFractalFunction();
        paint = controller.getColorFunction();
        constant = controller.getConstant();
        coefficient = controller.getCoeff();
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
}
