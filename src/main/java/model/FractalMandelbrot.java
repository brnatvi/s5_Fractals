package model;

import controller.Controller;

public class FractalMandelbrot extends Fractal
{
    public FractalMandelbrot(Controller c) { super(c); }

    public int[] calculate()
    {
        double len = Math.abs(right - left);
        double haut = Math.abs(up - down);
        double epsW = len/(double)widthPNG;
        double epsH = haut/(double)heightPNG;

        double i = left - epsW;
        for (int IdxW = 0; IdxW < widthPNG; IdxW++)
        {
            i += epsW;
            double j = down - epsH;
            for (int IdxH = 0; IdxH < heightPNG; IdxH ++)
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
                double val = (double)iter/(double)maxIt;
                points[IdxW + IdxH * widthPNG] = (int) paint.apply(val, z.mod());
            }
        }
        return points;
    }
}
