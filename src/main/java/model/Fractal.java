package model;

import controller.Controller;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.function.BiFunction;

/**
 * The abstract class Fractal contains basic parameters necessary for building a fractal.
 * It implements the Calculable interface which allows to draw fractals and returns pixels RGB array
 */

public abstract class Fractal implements interfaces.Calculable
{
    /**
     * Constants common for all fractal types
     */

    /** Constant from class of Complex numbers **/
    protected Complex    constant;
    /** Fractal's controller **/
    protected Controller controller;
    /** Array of fractal points, RGB format **/
    protected int[]      points;
    /** Radius which limits convergence **/
    protected double     radius;
    /** Maximum number of iterations **/
    protected int        maxIt;
    /** Width of the generated image **/
    protected int        widthPNG;
    /** Height of the generated image **/
    protected int        heightPNG;
    /** fractal's coordinates: left position**/
    protected double     left;
    /** fractal's coordinates: right position**/
    protected double     right;
    /** fractal's coordinates: up position**/
    protected double     up;
    /** fractal's coordinates: down position**/
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
