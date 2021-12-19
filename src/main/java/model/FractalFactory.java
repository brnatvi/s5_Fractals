package model;

import controller.Controller;
import interfaces.Calculable;
import interfaces.Initialisable;

public class FractalFactory
{
    private Controller controller;

    public FractalFactory(Controller c) { controller = c; }

    public Calculable create()
    {
        if (controller.getTypeFractal() == Initialisable.TypeFractal.JULIA)
        {
            return (Calculable) new FractalJulia(controller);
        }
        else if (controller.getTypeFractal() == Initialisable.TypeFractal.MANDELBROT)
        {
            return (Calculable) new FractalMandelbrot(controller);
        }
        else
        {
            new IllegalArgumentException("Unknown type of fractal");
        }
        return null;
    }
}
