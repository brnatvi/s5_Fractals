package controller;

import model.Complex;

public class Controller
{
    private Complex constant ;
    public int MAX_ITER;
    public double RADIUS;
    private int widthPNG;
    private int heightPNG;

    public Controller()
    {
        constant = Complex.createComplex(-0.74543, 0.11301);
        widthPNG = 4000;
        heightPNG = 4000;
        MAX_ITER = 1000;
        RADIUS = 2.0;
    }
   // private double coefficient;
   // private int countThreads = 1;
   // private long timeOfExecution;
   // private String pathFile;

   // private Initialisable.TypeFunction func;
   // private Initialisable.TypeFractal fract;
   // private Initialisable.ColorScheme color;

  //  public TypeFunction getFunction()   { return func; }
  //  public TypeFractal getTypeFractal() { return fract; }
  //  public ColorScheme getColor()       { return color; }
    public Complex getConstant()        { return constant; }
  //  public double getCoeff()            { return coefficient; }
  //  public int getCountThreads()        { return countThreads; }
  //  public long getTime()               { return timeOfExecution; }
  //  public String getPath()             { return pathFile; }
    public int getWidthPNG()           { return widthPNG; }
    public int getHeightPNG()          { return heightPNG; }
    public int getMAX_ITER()          { return MAX_ITER; }
    public double getRadius()          { return RADIUS; }

  //  public void setFunction(Initialisable.TypeFunction f)   { func = f; }
  //  public void setTypeFractal(Initialisable.TypeFractal f) { fract = f; }
  //  public void setColor(Initialisable.ColorScheme c)       { color = c; }
    public void setConstant(Complex c)                      { constant = c; }
  //  public void setCoeff(double c)                          { coefficient = c; }
  //  public void setCountThreads(int th)                     { countThreads = th; }
  //  public void setTime(long t)                             { timeOfExecution = t; }
  //  public void setPath(String p)                           { pathFile = p; }
    public void setWidthPNG(int w)                         { widthPNG = w; }
    public void setHeightPNG(int l)                        { heightPNG = l; }
}
