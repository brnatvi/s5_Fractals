package model;

import controller.Controller;
import interfaces.Calculable;
import interfaces.Initialisable;

import java.util.ArrayList;

public class FractalFactory
{
    private Controller controller;
    private int nbThread;

    public FractalFactory(Controller c) { controller = c; }
    //TODO: remove
    public FractalFactory(Controller c, int nb) { controller = c; nbThread = nb;}
    //TODO: remove
    public Calculable create()
    {
        if (controller.getTypeFractal() == Initialisable.TypeFractal.JULIA)
        {
            return new FractalJulia(controller);
        }
        else if (controller.getTypeFractal() == Initialisable.TypeFractal.MANDELBROT)
        {
            return new FractalMandelbrot(controller);
        }
        else
        {
            new IllegalArgumentException("Unknown type of fractal");
        }
        return null;
    }

    public ArrayList<Calculable> createSliced()
    {
        ArrayList<Calculable> ret = new ArrayList<>();

        for (int i = 0; i < controller.getCountThreads(); i++)
        {
            if (controller.getTypeFractal() == Initialisable.TypeFractal.JULIA)
            {
                ret.add(new FractalJulia(controller, i));
            }
            else if (controller.getTypeFractal() == Initialisable.TypeFractal.MANDELBROT)
            {
                ret.add(new FractalMandelbrot(controller, i));
            }
            else
            {
                new IllegalArgumentException("Unknown type of fractal");
            }
        }
        return ret;
    }
}
