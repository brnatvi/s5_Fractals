package model;

import controller.Controller;
import interfaces.Calculable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.RecursiveTask;

/**
 * The class FractalTaskAgregator proceeds in calculating the received parts of the fractal.
 * It extends RecursiveTask.
 */
public class FractalTaskAggregator extends RecursiveTask<int[]>
{
    /** FractalFactory as parameter of FractalTaskAgregator**/
    private FractalFactory factory;
    /** Width and height which will be used in image creation**/
    private int width  = 0;
    private int height = 0;

    /**
     * {@summary Constructor which instantiates FractalTaskAgregator. }
     * @param c the Controller
     * @param f the FractalFactory
     */
    public FractalTaskAggregator(FractalFactory f, Controller c)
    {
        this.factory = f;
        this.width  = c.getWidthPNG();
        this.height = c.getHeightPNG();
    }

    /**
     * {@summary Gets the width of the created image.}
     * @return the width of the created image
     */
    public int getWidth() { return width; }

    /**
     * {@summary Gets the height of the created image.}
     * @return the height of the created image
     */
    public int getHeight() { return height; }

    /**
     * {@summary The method of concatenation of the received parts of the fractal.}
     */
    private int[] concatArrays(ArrayList<int[]> arrays)
    {
        int size   = 0;

        for (int[] array : arrays)
        {
            size += array.length;
        }

        int[] result = new int[size];
        int offs = 0;
        for (int[] array : arrays)
        {
            System.arraycopy(array, 0, result, offs, array.length);
            offs += array.length;
        }
        return result;
    }

    /**
     * {@summary The method of calculation, override from RecursiveTask.}
     */
    @Override
    protected int[] compute()
    {
        //create list of sliced fractals objects
        ArrayList<Calculable> fractals = null;
        try {
            fractals = factory.createSliced();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<FractalTask> tasks = new ArrayList<>();

        //for each fractal - create list of tasks and start them
        for (Calculable fractal : fractals)
        {
            FractalTask task = new FractalTask(fractal);
            task.fork();
            tasks.add(task);
        }

        //wait async tasks and collect pixel slices into array pixels
        ArrayList<int[]> pixels = new ArrayList<>();
        for (FractalTask task : tasks)
        {
            pixels.add(task.join());
        }

        //return concatenated array of pixels
        return concatArrays(pixels);
    }
}

