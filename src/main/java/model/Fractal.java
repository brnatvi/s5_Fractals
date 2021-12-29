package model;

import controller.Controller;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.function.BiFunction;

/**
 * The abstract class Fractal contains basic parameters necessary for building a fractal.
 * It implements the Calculable interface which lets building fractals.
 */

public abstract class Fractal implements interfaces.Calculable
{
    /**
     * Constants common for all fractal types
     */

    /** Constant from class of Complex numbers **/
    protected Complex    constant;
    /** Controller provided **/
    protected Controller controller;
    /** Array of fractal points **/
    protected int[]      points;
    /** Radius which limites convergence **/
    protected double     radius;
    /** Maximum number of iterations **/
    protected int        maxIt;
    /** Width of the generated image **/
    protected int        widthPNG;
    /** Height of the generated image **/
    protected int        heightPNG;
    /** Positions on left, right, up and down for calculations **/
    protected double     left;
    protected double     right;
    protected double     up;
    protected double     down;
    /** BiFunction which calculates the fractal **/
    protected BiFunction calc;
    /** BiFunction which provides the color scheme of the fractal **/
    protected BiFunction paint;
    /** Number of threads used **/
    protected int        countThreads;

    /**
     * {@summary Constructor which instantiates a new Fractal.}
     * @param c the provided Controller
     * @param nbThread the number of threads used
     */

    public Fractal(Controller c, int nbThread) throws IOException {
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
