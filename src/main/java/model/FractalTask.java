package model;

import interfaces.Calculable;
import java.util.concurrent.RecursiveTask;

/**
 * The class FractalTask proceeds in calculating the received parts of the fractal.
 * It extends RecursiveTask.
 */
public class FractalTask extends RecursiveTask<int[]>
{
    /** Calculable as parameter of FractalTask**/
    private Calculable fractal;

    /**
     * {@summary Constructor which instantiates FractalTask. }
     * @param f Calculable
     */
    public FractalTask(Calculable f)
    {
        this.fractal = f;
    }

    /**
     * {@summary The method of calculation, override from RecursiveTask.}
     */
    @Override
    protected int[] compute()
    {
        return this.fractal.calculate();
    }
}
