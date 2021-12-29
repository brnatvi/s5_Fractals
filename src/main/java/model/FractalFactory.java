package model;

import controller.Controller;
import interfaces.Calculable;
import interfaces.Initialisable;

import java.io.IOException;
import java.util.ArrayList;

/**
 * The class FractalFactory creates a fractal as a list of sub-fractals.
 * It takes into consideration the threads number.
 */

public class FractalFactory
{
    /** Controller as parameter of FractalFactory**/
    private Controller controller;

    /**
     * {@summary External use constructor. Instantiates FractalFactory. }
     * @param c the Controller
     */
    public FractalFactory(Controller c) { controller = c; }

    /**
     * {@summary Creates a sliced fractal as an array of Calculable.}
     */
    public ArrayList<Calculable> createSliced() throws IOException {
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
