package controller;

import interfaces.Initialisable;
import model.Complex;

import javax.imageio.ImageIO;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;

/**
 * The class Controller contains the parameters necessary for building a fractal.
 * It provide initial values of parameters and methods to change them while the program runs.
 */

public class Controller implements Initialisable
{
    /**
     * Constants are initials and will be changed during the changing the settings of program in GUI or in CLI
     */

    /** Boolean which shows whether the Controller has been changed **/
    private boolean isChanged;
    /** Constant from class of Complex numbers **/
    private Complex constant;
    /** Real part of the constant **/
    private double re;
    /** Imaginary part of the constant **/
    private double im;
    /** Function type **/
    private Initialisable.TypeFunction func;
    /** Fractal type **/
    private Initialisable.TypeFractal fract;
    /** Color scheme **/
    private Initialisable.ColorScheme color;
    /** Boolean which shows whether the time of fractal building is displayed **/
    private boolean isTimeDisplayed;
    /** Path to a file **/
    private String pathFile;
    /** Number of threads used **/
    private int countThreads;
    /** Maximal number of iteration **/
    private int maxIter;
    /** Radius size **/
    private double RADIUS;
    /** Width of the generated image **/
    private int widthPNG;
    /** Height of the generated image **/
    private int heightPNG;
    /** Size of the generated rectangle **/
    private Rectangle2D.Double complexRect;
    /** Variable to abort calculation **/
    private AtomicBoolean abortCalculation;

    //============================== Constructor ================================

    /**
     * {@summary Constructor which instantiates a new Controller.}
     */

    public Controller()
    {
        isChanged = false;
        re = -0.74543;
        im = 0.11301;
        constant = Complex.createComplex(re, im);
        func  = Initialisable.TypeFunction.QUADRATIC;
        fract = Initialisable.TypeFractal.JULIA;
        color = Initialisable.ColorScheme.BLUE;
        isTimeDisplayed = true;
        widthPNG = 4000;
        heightPNG = 2000;
        maxIter = 1000;
        RADIUS = 2.0;
        complexRect = new Rectangle2D.Double(-2.0, -1.0, 4.0, 2.0);
        countThreads = Runtime.getRuntime().availableProcessors();
        pathFile = "MyFractal";
        abortCalculation = new AtomicBoolean();
        abortCalculation.set(false);
    }

    //============================ Others ==================================

    /**
     * {@summary Shows whether the Controller has been changed.}
     * @return the boolean tmp
     */

    public boolean isControllerChanged()
    {
        boolean tmp = isChanged;
        isChanged = false;
        return tmp;
    }

    /**
     * {@summary Loads parameters from a config file.}
     * @return true ( or false in case of error)
     */

    public boolean exportSettings(String fileName)
    {
        try (OutputStream output = new FileOutputStream(fileName))
        {
            Properties prop = new Properties();
            prop.setProperty("constant", constant.toString());
            prop.setProperty("func", func.toString());
            prop.setProperty("fract", fract.toString());
            prop.setProperty("color", color.toString());
            prop.setProperty("countThreads", Integer.toString(countThreads));
            prop.setProperty("maxIter", Integer.toString(maxIter));
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

    /**
     * {@summary Saves parameters to a config file.}
     * @return true ( or false in case of error)
     */

    public boolean importSettings(String fileName)
    {
        try (InputStream input = new FileInputStream(fileName))
        {
            Properties prop = new Properties();
            prop.load(input);

            constant    = Complex.fromString(prop.getProperty("constant"));
            func        = Initialisable.TypeFunction.valueOf(prop.getProperty("func"));
            fract       = Initialisable.TypeFractal.valueOf(prop.getProperty("fract"));
            color       = Initialisable.ColorScheme.valueOf(prop.getProperty("color"));
            countThreads= Integer.valueOf(prop.getProperty("countThreads"));
            maxIter     = Integer.valueOf(prop.getProperty("maxIter"));
            RADIUS      = Double.valueOf(prop.getProperty("RADIUS"));
            widthPNG    = Integer.valueOf(prop.getProperty("widthPNG"));
            heightPNG   = Integer.valueOf(prop.getProperty("heightPNG"));

            String[] complexVals = prop.getProperty("complexRect").split(":");

            complexRect.x      = Double.valueOf(complexVals[0]);
            complexRect.y      = Double.valueOf(complexVals[1]);
            complexRect.width  = Double.valueOf(complexVals[2]);
            complexRect.height = Double.valueOf(complexVals[3]);

            isChanged = true;

            return true;
        } catch (IOException ex)
        {
            return false;
        }
    }

    //======================== Lambda's getters ============================

    /**
     * {@summary Provides parameters for the function type.}
     * @return a complex number
     */

    public BiFunction getFractalFunction()
    {
        BiFunction<Complex, Complex, Complex> ret = null;
        switch (func)
        {
            case QUADRATIC:
                ret = (x, y) -> x.times(x).plus(y);
                break;
            case CUBIC:
                ret = (x, y) -> x.times(x).times(x).plus(y);
                break;
            case BIQUADRATIC:
                ret = (x, y) -> x.times(x).times(x).times(x).plus(y);
                break;
        }
        return ret;
    }

    /**
     * {@summary Provides parameters for the color scheme.}
     * @return an Integer
     */

    public BiFunction getColorFunction () throws IOException {
        BiFunction <Integer, Integer, Integer> ret = null;

        switch (color)
        {
            case RED:
                BufferedImage finalImgR = ImageIO.read(getClass().getResourceAsStream("/gradient_R.jpg"));
                ret = (iter, maxIter) -> finalImgR.getRGB(iter * (finalImgR.getWidth() - 1) / maxIter, 0);
                //BufferedImage finalImgR = ImageIO.read(getClass().getResourceAsStream("/grad_R.jpg"));
                //ret = (iter) -> finalImgR.getRGB(iter%finalImgR.getWidth(), 29);
                break;

            case GREEN:
                BufferedImage finalImgG = ImageIO.read(getClass().getResourceAsStream("/gradient_G.jpg"));
                ret = (iter, maxIter) -> finalImgG.getRGB(iter*(finalImgG.getWidth() - 1)/maxIter, 0);
                break;

            case BLUE:
                BufferedImage finalImgB = ImageIO.read(getClass().getResourceAsStream("/gradient_B.jpg"));
                ret = (iter, maxIter) -> finalImgB.getRGB(iter*(finalImgB.getWidth() - 1)/maxIter, 0);
                //BufferedImage finalImgB = ImageIO.read(getClass().getResourceAsStream("/grad_B.jpg"));
                //ret = (iter) -> finalImgB.getRGB(iter%finalImgB.getWidth(), 10);
                break;
        }
        return ret;
    }

    //============================ Getters ================================

    /**
     * {@summary Gets the function type.}
     * @return the function
     */
    public Initialisable.TypeFunction        getFunctionType()        { return func; }

    /**
     * {@summary Gets the fractal type.}
     * @return the fractal
     */
    public Initialisable.TypeFractal         getTypeFractal()         { return fract; }

    /**
     * {@summary Gets the color scheme.}
     * @return the color
     */
    public Initialisable.ColorScheme         getColorScheme()         { return color; }

    /**
     * {@summary Gets the constant.}
     * @return the constant
     */
    public Complex                           getConstant()            { return constant; }

    /**
     * {@summary Gets the real part of the constant.}
     * @return the real part of the constant
     */
    public double                            getRe()                  { return constant.realPart(); }

    /**
     * {@summary Gets the imaginary part of the constant.}
     * @return the imaginary part of the constant
     */
    public double                            getIm()                  { return constant.imaginaryPart(); }

    /**
     * {@summary Gets the number of iterations.}
     * @return the number of iterations
     */
    public int                               getMaxIter()             { return maxIter; }

    /**
     * {@summary Gets the number of threads.}
     * @return the number of threads
     */
    public int                               getCountThreads()        { return countThreads; }

    /**
     * {@summary Gets the boolean showing whether the time is displayed.}
     * @return boolean
     */
    public boolean                           getIsDisplayTime()       { return isTimeDisplayed; }

    /**
     * {@summary Gets the path to a file.}
     * @return the path to a file
     */
    public String                            getPath()                { return pathFile; }

    /**
     * {@summary Gets the width of the generated image.}
     * @return the width of the generated image
     */
    public int                               getWidthPNG()            { return widthPNG; }

    /**
     * {@summary Gets the height of the generated image.}
     * @return the height of the generated image
     */
    public int                               getHeightPNG()           { return heightPNG; }

    /**
     * {@summary Gets the radius.}
     * @return the radius
     */
    public double                            getRadius()              { return RADIUS; }

    /**
     * {@summary Gets the dimensions of the generated rectangle.}
     * @return the dimensions of the generated rectangle
     */
    public Rectangle2D.Double                getComplexRect()         { return complexRect; }

    /**
     * {@summary Gets the variable to abort calculation.}
     * @return the variable to abort calculation
     */
    public boolean                           getAbort()               { return abortCalculation.get();}

    //============================ Setters ==============================

    /**
     * {@summary Setter of the function type. }
     */
    public void         setFunctionType(Initialisable.TypeFunction f) { isChanged = true; func = f; }

    /**
     * {@summary Setter of the fractal type. }
     */
    public void         setFractalType(Initialisable.TypeFractal f)   { isChanged = true; fract = f; }

    /**
     * {@summary Setter of the color scheme. }
     */
    public void         setColorScheme(Initialisable.ColorScheme c)   { isChanged = true; color = c; }

    /**
     * {@summary Setter of the constant. }
     */
    public void         setConstant(Complex c)                        { isChanged = true; constant = c; }

    /**
     * {@summary Setter of the maximum number of iterations. }
     */
    public void         setMaxIter(int maxIter)                       { isChanged = true; this.maxIter = maxIter; }

    /**
     * {@summary Setter of the number of threads. }
     */
    public void         setCountThreads(int th)                       { isChanged = true; countThreads = th; }

    /**
     * {@summary Setter of the boolean showing whether the time is displayed. }
     */
    public void         setIsDisplayTime(boolean t)                   { isChanged = true; isTimeDisplayed = t;}

    /**
     * {@summary Setter of the path to a file. }
     */
    public void         setPath(String p)                             { isChanged = true; pathFile = p; }

    /**
     * {@summary Setter of the radius. }
     */
    public void         setRadius(double r)                           { isChanged = true; RADIUS = r; }

    /**
     * {@summary Setter of the complex rectangle. }
     */
    public void         setComplexRect(Rectangle2D.Double r)          { isChanged = true; complexRect = r;}

    /**
     * {@summary Setter of the boolean to abort calculation. }
     */
    public void         setAbort(boolean a)                           { abortCalculation.set(a);}

    /**
     * {@summary Setter of the constant real part. }
     */
    public void         setNewRealPart(double r)                      { isChanged = true; constant = Complex.createComplex(r, im);}

    /**
     * {@summary Setter of the constant imaginary part. }
     */
    public void         setNewImagPart(double i)                      { isChanged = true; constant = Complex.createComplex(re, i);}

    /**
     * {@summary Setter of the complex rectangle dimensions. }
     */
    public void setSize(int w, int h)
    {
        isChanged = true;
        widthPNG = w;
        heightPNG = h;
        complexRect.height = complexRect.width * (double)h/(double)w;
    }
}
