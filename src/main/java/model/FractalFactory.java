package model;

import controller.Controller;
import interfaces.Calculable;
import interfaces.Initialisable;

import java.util.ArrayList;

public class FractalFactory
{
    private Controller controller;

    public FractalFactory(Controller c) { controller = c; }

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
