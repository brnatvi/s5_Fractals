package interfaces;

import model.Complex;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.function.BiFunction;

/**
 * The interface Initialisable is implemented by Controller and defines necessary methods
 */
public interface Initialisable
{
	/**
	 *  Fractal's function type
	 */	
    enum TypeFunction {QUADRATIC, CUBIC, BIQUADRATIC};
	/**
	 *  Fractal's type
	 */	
    enum TypeFractal {JULIA, MANDELBROT};
	/**
	 *  Fractal's color scheme
	 */	
    enum ColorScheme {RED, GREEN, BLUE};

    /**
     * {@summary Provides the function for calculation of fractal.}
     * @return a fractal's bifunction
     */
    public BiFunction                  getFractalFunction();
	
    /**
     * {@summary Provides the function for fractal colouring.}
     * @return a bifunction
     */
    public BiFunction getColorFunction() throws IOException;

    /**
     * {@summary Gets the function type.}
     * @return the enum of function type 
     */
    public Initialisable.TypeFunction  getFunctionType();
    /**
     * {@summary Gets the fractal type.}
     * @return the enum of fractal type
     */
    public Initialisable.TypeFractal   getTypeFractal();
    /**
     * {@summary Gets the color scheme.}
     * @return the enum of color scheme
     */
    public Initialisable.ColorScheme   getColorScheme();
    /**
     * {@summary Gets the complex constant.}
     * @return the complex constant
     */
    public Complex                     getConstant();
    /**
     * {@summary Gets the number of max iterations.}
     * @return the number of iterations
     */
    public int                         getMaxIter();
    /**
     * {@summary Gets the number of threads.}
     * @return the number of threads
     */
    public int                         getCountThreads();
    /**
     * {@summary Gets the boolean showing whether the time is displayed (used in CLI).}
     * @return boolean
     */
    public boolean                     getIsDisplayTime();
    /**
     * {@summary Gets the path to a configuration file.}
     * @return the path to a file
     */
    public String                      getPath();
    /**
     * {@summary Gets the width of the generated image.}
     * @return the width of the generated image
     */
    public int                         getWidthPNG();
    /**
     * {@summary Gets the height of the generated image.}
     * @return the height of the generated image
     */
    public int                         getHeightPNG();
    /**
     * {@summary Gets the radius of convergence.}
     * @return the radius
     */
    public double                      getRadius();
    /**
     * {@summary Gets the dimensions of the generated rectangle.}
     * @return the dimensions of the generated rectangle
     */
    public Rectangle2D.Double          getComplexRect();

    /**
     * {@summary Setter of the enum function type. }
	 * @param  f fractal's function type	 
     */
    public void     setFunctionType(Initialisable.TypeFunction f);
    /**
     * {@summary Setter of the enum fractal type. }
	 * @param  f fractal's type	 
     */
    public void     setFractalType(Initialisable.TypeFractal f);
    /**
     * {@summary Setter of the enum color scheme. }
	 * @param  c fractal's color scheme
     */
    public void     setColorScheme(Initialisable.ColorScheme c);
    /**
     * {@summary Setter of the complex constant. }
	 * @param  c fractal's complex constant
     */
    public void     setConstant(Complex c);
    /**
     * {@summary Setter of the maximum number of iterations. }
	 * @param  maxIter fractal's max iteration value
     */
    public void     setMaxIter(int maxIter);
    /**
     * {@summary Setter of the number of threads. }
	 * @param  th count of the threads to be used for calculation
     */
    public void     setCountThreads(int th);
    /**
     * {@summary Setter of the boolean flag to show or not consumed time for fractal calculation. }
	 * @param  t show calculation time
     */
    public void     setIsDisplayTime(boolean t);
    /**
     * {@summary Setter of the path to a config file. }
	 * @param  path path to configuration file
     */
    public void     setPath(String path);
    /**
     * {@summary Setter of the radius of convergence. }
	 * @param  r calculation radius
     */
    public void     setRadius(double r);
    /**
     * {@summary Setter of the complex rectangle. }
	 * @param  r complex rectangle
     */
    public void     setComplexRect(Rectangle2D.Double r);
    /**
     * {@summary Setter of the complex rectangle dimensions. }
	 * @param  w view width
	 * @param  h view height
     */
    public void     setSize(int w, int h);
}
