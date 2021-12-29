package model;

import controller.Controller;

import java.io.IOException;

/**
 * The class FractalJulia contains basic parameters necessary for building a fractal of type Julia.
 * It extends the class Fractal.
 */

public class FractalJulia extends Fractal
{

    /**
     * {@summary External use constructor. Instantiates FracctalJulia. }
     * @param c the Controller
     * @param nbThread the number of threads
     */
    public FractalJulia(Controller c, int nbThread) throws IOException { super(c, nbThread); }

    /**
     * {@summary The principal method of FractalJulia calculation.}
     */
    public int[] calculate()
    {
        double len = Math.abs(right - left);
        double haut = Math.abs(up - down);
        double epsW = len / (double) widthPNG;
        double epsH = haut / ((double)heightPNG/(double)countThreads);

        double i = left - epsW;
        for (int IdxW = 0; IdxW < widthPNG; IdxW++)
        {
            i += epsW;
            double j = down - epsH;
            for (int IdxH = 0; IdxH < heightPNG/countThreads; IdxH++)
            {
                j += epsH;
                int iter = 0;
                Complex z = Complex.createComplex(i, j);
                while (z.mod() < radius && iter < maxIt - 1)
                {
                    z = (Complex) calc.apply(z, constant);
                    iter++;
                }
//                double val = (double) iter / (double) maxIt;                                                 // if use another bifunction
//                points[IdxW + IdxH * widthPNG] = (int) paint.apply(val, z.mod()) | 0xFF000000;               // if use another bifunction
                points[IdxW + IdxH * widthPNG] = (int) paint.apply(iter, maxIt) | 0xFF000000;

                if ((0 == (IdxH % 100)) && (controller.getAbort()))
                {
                    break;
                }
            }
        }
        return points;
    }

}