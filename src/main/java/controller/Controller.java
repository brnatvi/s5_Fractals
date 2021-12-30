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
 * It is in charge of storing and providing fractal parameters.
 */

public class Controller implements Initialisable
{
    /**
     * Private parameters 
     */

    /** boolean flag, indicates when any parameter(s) was changes, used by model to detect a moment when fractal should be recalculated  **/
    private boolean isChanged;
    /** Complex fractal's constant for calculations **/
    private Complex constant;
    /** Real part of the complex constant **/
    private double re;
    /** Imaginary part of the complex constant **/
    private double im;
    /** Function type **/
    private Initialisable.TypeFunction func;
    /** Fractal type **/
    private Initialisable.TypeFractal fract;
    /** Color scheme **/
    private Initialisable.ColorScheme color;
    /** Boolean which shows whether the time of fractal building is displayed (used in CLI) **/
    private boolean isTimeDisplayed;
    /** Path for configuration file **/
    private String pathFile;
    /** Number of threads to be used used **/
    private int countThreads;
    /** Maximal number of iteration **/
    private int maxIter;
    /** Radius of convergence **/
    private double RADIUS;
    /** Width of the generated image **/
    private int widthPNG;
    /** Height of the generated image **/
    private int heightPNG;
    /** Size of the complex rectangle **/
    private Rectangle2D.Double complexRect;
    /** Variable to abort calculation (used in GUI) **/
    private AtomicBoolean abortCalculation;

    //============================== Constructor ================================

    /**
     * {@summary Constructor which instantiates a Controller and its parameters}
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
     * {@summary Shows whether any parameter of the Controller has been changed.}
     * @return the boolean isChanged
     */

    public boolean isControllerChanged()
    {
        boolean tmp = isChanged;
        isChanged = false;
        return tmp;
    }

    /**
     * {@summary Loads parameters from a configuration file.}
	 * @param fileName path to configuration file
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
     * {@summary Saves parameters to a configuration file.}
	 * @param fileName path to configuration file
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

    public BiFunction getColorFunction () throws IOException {
        BiFunction <Integer, Integer, Integer> ret = null;

        switch (color)
        {
            case RED:
                BufferedImage finalImgR = ImageIO.read(getClass().getResourceAsStream("/gradient_R.jpg"));
                ret = (iter, maxIter) -> finalImgR.getRGB(iter * (finalImgR.getWidth() - 1) / maxIter, 0);
                //BufferedImage finalImgR = ImageIO.read(getClass().getResourceAsStream("/grad_R.jpg"));
                //ret = (iter, z.mod()) -> finalImgR.getRGB(iter%finalImgR.getWidth(), 29);
                break;

            case GREEN:
                BufferedImage finalImgG = ImageIO.read(getClass().getResourceAsStream("/gradient_G.jpg"));
                ret = (iter, maxIter) -> finalImgG.getRGB(iter*(finalImgG.getWidth() - 1)/maxIter, 0);
                break;

            case BLUE:
                BufferedImage finalImgB = ImageIO.read(getClass().getResourceAsStream("/gradient_B.jpg"));
                ret = (iter, maxIter) -> finalImgB.getRGB(iter*(finalImgB.getWidth() - 1)/maxIter, 0);
                //BufferedImage finalImgB = ImageIO.read(getClass().getResourceAsStream("/grad_B.jpg"));
                //ret = (iter, z.mod()) -> finalImgB.getRGB(iter%finalImgB.getWidth(), 10);
                break;
        }
        return ret;
    }

    //============================ Getters ================================
    public Initialisable.TypeFunction        getFunctionType()        { return func; }
    public Initialisable.TypeFractal         getTypeFractal()         { return fract; }
    public Initialisable.ColorScheme         getColorScheme()         { return color; }
    public Complex                           getConstant()            { return constant; }

    /**
     * {@summary Gets the real part of the constant.}
     * @return the real part of the complex constant
     */
    public double                            getRe()                  { return constant.realPart(); }

    /**
     * {@summary Gets the imaginary part of the constant.}
     * @return the imaginary part of the complex constant
     */
    public double                            getIm()                  { return constant.imaginaryPart(); }
    public int                               getMaxIter()             { return maxIter; }
    public int                               getCountThreads()        { return countThreads; }
    public boolean                           getIsDisplayTime()       { return isTimeDisplayed; }
    public String                            getPath()                { return pathFile; }
    public int                               getWidthPNG()            { return widthPNG; }
    public int                               getHeightPNG()           { return heightPNG; }
    public double                            getRadius()              { return RADIUS; }
    public Rectangle2D.Double                getComplexRect()         { return complexRect; }

    /**
     * {@summary Gets the variable to abort calculation.}
     * @return the variable to abort calculation
     */
    public boolean                           getAbort()               { return abortCalculation.get();}

    //============================ Setters ==============================
    public void         setFunctionType(Initialisable.TypeFunction f) { isChanged = true; func = f; }
    public void         setFractalType(Initialisable.TypeFractal f)   { isChanged = true; fract = f; }
    public void         setColorScheme(Initialisable.ColorScheme c)   { isChanged = true; color = c; }
    public void         setConstant(Complex c)                        { isChanged = true; constant = c; }
    public void         setMaxIter(int maxIter)                       { isChanged = true; this.maxIter = maxIter; }
    public void         setCountThreads(int th)                       { isChanged = true; countThreads = th; }
    public void         setIsDisplayTime(boolean t)                   { isChanged = true; isTimeDisplayed = t;}
    public void         setPath(String p)                             { isChanged = true; pathFile = p; }
    public void         setRadius(double r)                           { isChanged = true; RADIUS = r; }
    public void         setComplexRect(Rectangle2D.Double r)          { isChanged = true; complexRect = r;}

    /**
     * {@summary Setter of the boolean to abort calculation. Multithread access protected!}
	 * @param  a abort flag
     */
    public void         setAbort(boolean a)                           { abortCalculation.set(a);}

    /**
     * {@summary Setter of the complex constant real part. }
	 * @param  r real part
     */
    public void         setNewRealPart(double r)                      { isChanged = true; constant = Complex.createComplex(r, im);}

    /**
     * {@summary Setter of the complex constant imaginary part. }
	 * @param  i imaginary part
     */
    public void         setNewImagPart(double i)                      { isChanged = true; constant = Complex.createComplex(re, i);}

    /**
     * {@summary Setter of the complex rectangle dimensions. }
	 * @param  w view width
	 * @param  h view height
     */
    public void setSize(int w, int h)
    {
        isChanged = true;
        widthPNG = w;
        heightPNG = h;
        complexRect.height = complexRect.width * (double)h/(double)w;
    }
}
