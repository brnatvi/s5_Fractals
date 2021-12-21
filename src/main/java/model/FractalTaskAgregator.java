package model;

import controller.Controller;
import interfaces.Calculable;

import java.util.ArrayList;
import java.util.concurrent.RecursiveTask;

public class FractalTaskAgregator extends RecursiveTask<int[]>
{
    private FractalFactory factory;
    private int width  = 0;
    private int height = 0;

    public FractalTaskAgregator(FractalFactory f, Controller c)
    {
        this.factory = f;
        this.width  = c.getWidthPNG();
        this.height = c.getHeightPNG();
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

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

    @Override
    protected int[] compute()
    {
        //create list of sliced fractals objects
        ArrayList<Calculable> fractals = factory.createSliced();
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

