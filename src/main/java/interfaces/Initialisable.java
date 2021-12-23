package interfaces;

import model.Complex;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface Initialisable
{
    enum TypeFunction {QUADRATIC, CUBIC, BIQUADRATIC};
    enum TypeFractal {JULIA, MANDELBROT};
    enum ColorScheme {RED, GREEN, BLUE};

    public BiFunction                  getFractalFunction();
    public Function getColorFunction() throws IOException;

    public Initialisable.TypeFunction  getFunctionType();
    public Initialisable.TypeFractal   getTypeFractal();
    public Initialisable.ColorScheme   getColorScheme();
    public Complex                     getConstant();
    public int                         getMaxIter();
    public int                         getCountThreads();
    public boolean                     getIsDisplayTime();
    public String                      getPath();
    public int                         getWidthPNG();
    public int                         getHeightPNG();
    public double                      getRadius();
    public Rectangle2D.Double          getComplexRect();

    public void     setFunctionType(Initialisable.TypeFunction f);
    public void     setFractalType(Initialisable.TypeFractal f);
    public void     setColorScheme(Initialisable.ColorScheme c);
    public void     setConstant(Complex c);
    public void     setMaxIter(int maxIter);
    public void     setCountThreads(int th);
    public void     setIsDisplayTime(boolean t);
    public void     setPath(String path);
    public void     setRadius(double r);
    public void     setComplexRect(Rectangle2D.Double r);
    public void     setSize(int w, int h);
}
