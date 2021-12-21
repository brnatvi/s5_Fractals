package model;

import controller.Controller;

public class FractalJulia extends Fractal
{
    public FractalJulia(Controller c) { super(c); }

    public FractalJulia(Controller c, int nbThread) { super(c, nbThread); }

    public int[] calculate()
    {
        double len = Math.abs(right - left);
        double haut = Math.abs(up - down);
        double epsW = len / (double) widthPNG;
        double epsH = haut / (double) (heightPNG/controller.getCountThreads());

        double i = left - epsW;
        for (int IdxW = 0; IdxW < widthPNG; IdxW++)
        {
            i += epsW;
            //System.out.println(i);
            double j = down - epsH;
            for (int IdxH = 0; IdxH < heightPNG/controller.getCountThreads(); IdxH++)
            {
                j += epsH;
                //System.out.println(j);
                int iter = 0;
                Complex z = Complex.createComplex(i, j);
                while (z.mod() < radius && iter < maxIt - 1)
                {
                    z = (Complex) calc.apply(z, constant);
                    iter++;
                }
                double val = (double) iter / (double) maxIt;
                points[IdxW + IdxH * widthPNG] = (int) paint.apply(val, z.mod()) | 0xFF000000;

                if ((0 == (IdxH % 100)) && (controller.getAbort()))
                {
                    break;
                }
            }
        }
        return points;
    }

}