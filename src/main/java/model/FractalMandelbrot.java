package model;

import controller.Controller;

import java.io.IOException;

/**
 * The class FractalMandelbrot implements method to building a fractal of type Mandelbrot.
 * It extends the abstract class Fractal.
 */

public class FractalMandelbrot extends Fractal
{

    /**
     * {@summary External use constructor. Instantiates FractalMandelbrot. }
     * @param c the Controller
     * @param nbThread the number of threads
     */
    public FractalMandelbrot(Controller c, int nbThread) throws IOException { super(c, nbThread); }

    /**
     * {@summary The principal method of FractalMandelbrot calculation.}
     * @return RGB pixels array
    */
    public int[] calculate()
    {
        double len = Math.abs(right - left);
        double haut = Math.abs(up - down);
        double epsW = len/(double)widthPNG;
        double epsH = haut/ ((double)heightPNG/ (double)countThreads);

        double i = left - epsW;
        for (int IdxW = 0; IdxW < widthPNG; IdxW++)
        {
            i += epsW;
            double j = down - epsH;
            for (int IdxH = 0; IdxH < heightPNG/countThreads; IdxH ++)
            {
                j += epsH;
                int iter = 0;
                Complex z = Complex.getZERO();
                Complex c = Complex.createComplex(i, j);
                while (z.mod() <= radius && iter < maxIt)
                {
                    z = (Complex) calc.apply(z, c);
                    iter++;
                }

                //0xFF000000 - set Alpha channel max value 0xFF/255.
				//Each pixel consist of 4 channels:
				//R - reg (8 bits, 1 byte)
				//G - green (8 bits, 1 byte)
				//B - blue (8 bits, 1 byte)
				//A - transparency/alpha (8 bits, 1 byte), 0 - fully transparent, 255 - fully visible
				//Each pixel has size of 32 bits, channels are located in next order and positions:
				// bits 31 .. 24 .. 16 .. 08 .. 00
				//      [  A  ][  B ][  G ][  R ]
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
