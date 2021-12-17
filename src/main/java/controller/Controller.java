package controller;

import interfaces.Initialisable;
import model.Complex;

import java.awt.geom.Rectangle2D;
import java.util.function.BiFunction;

public class Controller implements Initialisable
{
    private Complex constant;
    private double re;
    private double im;
    private double coefficient;
    private Initialisable.TypeFunction func;
    private Initialisable.TypeFractal fract;
    private Initialisable.ColorScheme color;
    private boolean isTimeDisplayed;
    private String pathFile;
    private int countThreads;
    private int MAX_ITER;
    private double RADIUS;
    private int widthPNG;
    private int heightPNG;
    private Rectangle2D.Double complexRect;

    public Controller()
    {
        re = -0.74543;
        im = 0.11301;
        constant = Complex.createComplex(re, im);
        coefficient = 1.0;
        func = Initialisable.TypeFunction.QUADRATIC;
        fract = Initialisable.TypeFractal.JULIA;
        color =  Initialisable.ColorScheme.BLUE;
        isTimeDisplayed = true;
        widthPNG = 4000;
        heightPNG = 2000;
        MAX_ITER = 1000;
        RADIUS = 2.0;
        complexRect = new Rectangle2D.Double(-2.0, -1.0, 4.0, 2.0);
        countThreads = Runtime.getRuntime().availableProcessors();
    }

    public BiFunction getFractalFunction()
    {
        BiFunction<Complex, Complex, Complex> ret = null;
        switch (func)
        {
            case QUADRATIC:
                ret = (x, y) -> x.times(x).plus(y);
                break;
            case CUBIC:
                ret = (x, y) -> x.times(x).times(x).plus(x.times(x)).plus(y);
                break;
        }
        return ret;
    }

    public BiFunction getColorFunction()
    {
        BiFunction<Double, Double, Integer> ret = null;
        switch (color)
        {
            case RED:
            case GREEN:
            case BLUE:
                ret = (val, zMod) -> ((int)(5855.0 * (val)) << 16) |
                                       ((int)(3658.0 * (1-val)) << 8) |
                                       (int)(1255.0 * (zMod > RADIUS ? 1.0 : zMod / RADIUS));
                break;
        }
        return ret;
    }

    public Initialisable.TypeFunction        getFunctionType()        { return func; }
    public Initialisable.TypeFractal         getTypeFractal()         { return fract; }
    public Initialisable.ColorScheme         getColorScheme()         { return color; }
    public Complex                           getConstant()            { return constant; }
    public int                               getMaxIter()             { return MAX_ITER; }
    public double                            getCoeff()               { return coefficient; }
    public int                               getCountThreads()        { return countThreads; }
    public boolean                           getIsDisplayTime()       { return isTimeDisplayed; }
    public String                            getPath()                { return pathFile; }
    public int                               getWidthPNG()            { return widthPNG; }
    public int                               getHeightPNG()           { return heightPNG; }
    public double                            getRadius()              { return RADIUS; }
    public Rectangle2D.Double                getComplexRect()         { return complexRect; }

    public void         setFunctionType(Initialisable.TypeFunction f) { func = f; }
    public void         setTypeFractal(Initialisable.TypeFractal f)   { fract = f; }
    public void         setColorScheme(Initialisable.ColorScheme c)   { color = c; }
    public void         setConstant(Complex c)                        { constant = c; }
    public void         setMaxIter(int maxIter)                       { MAX_ITER = maxIter; }
    public void         setCoeff(double c)                            { coefficient = c; }
    public void         setCountThreads(int th)                       { countThreads = th; }
    public void         setIsDisplayTime(boolean t)                   { isTimeDisplayed = t;}
    public void         setPath(String p)                             { pathFile = p; }
    public void         setWidthPNG(int w)                            { widthPNG = w; }
    public void         setHeightPNG(int w)                           { heightPNG = w; }
    public void         setRadius(double r)                           { RADIUS = r; }
    public void         setComplexRect(Rectangle2D.Double r)          { complexRect = r;}
}
