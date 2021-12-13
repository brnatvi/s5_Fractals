package interfaces;

import model.Complex;

public interface Initialisable
{
    enum TypeFunction {QUADRATIC};
    enum TypeFractal {JULIA, MANDELBROT};
    enum ColorScheme {RED, GREEN, BLUE};

    public Initialisable.TypeFunction getFunction();
    public Initialisable.TypeFractal getTypeFractal();
    public Initialisable.ColorScheme getColor();

    public Complex getConstant();
    public double getCoeff();
    public int getCountThreads();
    public long getTime();
    public String getPath();
    public long getWidthPNG();
    public long getLengthPNG();

    public void setFunction(Initialisable.TypeFunction f);
    public void setTypeFractal(Initialisable.TypeFractal f);
    public void setColor(Initialisable.ColorScheme c);

    public void setConstant(Complex c);
    public void setCoeff(double c);
    public void setCountThreads(int th);
    public void setTime(long t);
    public void setPath(String path);
    public void setWidthPNG(long w);
    public void setLengthPNG(long l);

}
