package controller;

import interfaces.Initialisable;
import model.Complex;

import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;

public class Controller implements Initialisable
{
    private boolean isChanged;
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
    private AtomicBoolean abortCalculation;

    public Controller()
    {
        isChanged = false;
        re = -0.74543;
        im = 0.11301;
        constant = Complex.createComplex(re, im);
        coefficient = 1.0;
        func  = Initialisable.TypeFunction.QUADRATIC;
        fract = Initialisable.TypeFractal.JULIA;
        color = Initialisable.ColorScheme.BLUE;
        isTimeDisplayed = true;
        widthPNG = 4000;
        heightPNG = 2000;
        MAX_ITER = 1000;
        RADIUS = 2.0;
        complexRect = new Rectangle2D.Double(-2.0, -1.0, 4.0, 2.0);
        countThreads = Runtime.getRuntime().availableProcessors();
        pathFile = "MyFractal";
        abortCalculation = new AtomicBoolean();
        abortCalculation.set(false);
    }

    public boolean isControllerChanged()
    {
        boolean tmp = isChanged;
        isChanged = false;
        return tmp;
    }

    public boolean exportSettings(String fileName)
    {
        try (OutputStream output = new FileOutputStream(fileName))
        {
            Properties prop = new Properties();
            prop.setProperty("constant", constant.toString());

            prop.setProperty("coefficient", Double.toString(coefficient));
            prop.setProperty("func", func.toString());
            prop.setProperty("fract", fract.toString());
            prop.setProperty("color", color.toString());
            prop.setProperty("countThreads", Integer.toString(countThreads));
            prop.setProperty("MAX_ITER", Integer.toString(MAX_ITER));
            prop.setProperty("RADIUS", Double.toString(RADIUS));
            prop.setProperty("widthPNG", Integer.toString(widthPNG));
            prop.setProperty("heightPNG", Integer.toString(heightPNG));
            prop.setProperty("complexRect",
                             Double.toString(complexRect.x) + ":" +
                             Double.toString(complexRect.y) + ":" +
                             Double.toString(complexRect.width) + ":" +
                             Double.toString(complexRect.height)
                             );

            prop.store(output, null);
            return true;
        }
        catch (IOException io)
        {
            return false;
        }
    }

    public boolean importSettings(String fileName)
    {
        try (InputStream input = new FileInputStream(fileName))
        {
            Properties prop = new Properties();
            prop.load(input);

            constant    = Complex.fromString(prop.getProperty("constant"));
            coefficient = Double.valueOf(prop.getProperty("coefficient"));
            func        = Initialisable.TypeFunction.valueOf(prop.getProperty("func"));
            fract       = Initialisable.TypeFractal.valueOf(prop.getProperty("fract"));
            color       = Initialisable.ColorScheme.valueOf(prop.getProperty("color"));
            countThreads= Integer.valueOf(prop.getProperty("countThreads"));
            MAX_ITER    = Integer.valueOf(prop.getProperty("MAX_ITER"));
            RADIUS      = Double.valueOf(prop.getProperty("RADIUS"));
            widthPNG    = Integer.valueOf(prop.getProperty("widthPNG"));
            heightPNG   = Integer.valueOf(prop.getProperty("heightPNG"));

            String[] complexVals = prop.getProperty("complexRect").split(":");

            complexRect.x      = Double.valueOf(complexVals[0]);
            complexRect.y      = Double.valueOf(complexVals[1]);
            complexRect.width  = Double.valueOf(complexVals[2]);
            complexRect.height = Double.valueOf(complexVals[3]);

            return true;
        } catch (IOException ex)
        {
            return false;
        }
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
                ret = (val, zMod) -> (int)(4280*(Math.sin(val+600))+700) << 16 |
                                     (int) (2056*((Math.sin(val+600))+700)) << 8 |
                                     (int) (1800 *(Math.sin(val+600))+700);
                break;
            case GREEN:
                ret = (val, zMod) -> (int)(5208 * (zMod > RADIUS ? 1.0 : zMod / RADIUS)) << 16 |
                                     (int)(4280*(Math.sin(val+600))+8508) << 8 |
                                     (int) (1697 *(Math.sin(val+160))+333);
                break;
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
    public boolean                           getAbort()               { return abortCalculation.get();}

    public void         setFunctionType(Initialisable.TypeFunction f) { isChanged = true; func = f; }
    public void         setFractalType(Initialisable.TypeFractal f)   { isChanged = true; fract = f; }
    public void         setColorScheme(Initialisable.ColorScheme c)   { isChanged = true; color = c; }
    public void         setConstant(Complex c)                        { isChanged = true; constant = c; }
    public void         setMaxIter(int maxIter)                       { isChanged = true; MAX_ITER = maxIter; }
    public void         setCoeff(double c)                            { isChanged = true; coefficient = c; }
    public void         setCountThreads(int th)                       { isChanged = true; countThreads = th; }
    public void         setIsDisplayTime(boolean t)                   { isChanged = true; isTimeDisplayed = t;}
    public void         setPath(String p)                             { isChanged = true; pathFile = p; }
    public void         setWidthPNG(int w)                            { isChanged = true; widthPNG = w; }
    public void         setHeightPNG(int w)                           { isChanged = true; heightPNG = w; }
    public void         setRadius(double r)                           { isChanged = true; RADIUS = r; }
    public void         setComplexRect(Rectangle2D.Double r)          { isChanged = true; complexRect = r;}
    public void         setAbort(boolean a)                           { abortCalculation.set(a);}
    public void         setNewRealPart(double r)                      { isChanged = true; constant = Complex.createComplex(r, im);}
    public void         setNewImagPart(double i)                      { isChanged = true; constant = Complex.createComplex(re, i);}

}
