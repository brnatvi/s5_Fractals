package model;

import interfaces.Calculable;

import java.util.ArrayList;
import java.util.concurrent.RecursiveTask;

public class FractalTaskAgregator extends RecursiveTask<int[]>
{
    private FractalFactory factory;

    public FractalTaskAgregator(FractalFactory f) {
        this.factory = f;
    }

    private int[] concatArrays(ArrayList<int[]> arrays) {
        int size = 0;
        for (int[] array : arrays) {
            size += array.length;
        }

        System.out.println("Pixel arrays count: " + arrays.size() + " total size: " + size);

        int[] result = new int[size];
        int offs = 0;
        for (int[] array : arrays) {
            System.arraycopy(array, 0, result, offs, array.length);
            System.out.println("Copy to dest " + array.length + " bytes");
            offs += array.length;
        }
        return result;
    }

    @Override
    protected int[] compute() {
        //create list of sliced fractals objects
        ArrayList<Calculable> fractals = factory.createSliced();
        ArrayList<FractalTask> tasks = new ArrayList<>();

        System.out.println("Fractals slices count: " + fractals.size());

        //for each fractal - create list of tasks and start them
        for (Calculable fractal : fractals) {
            FractalTask task = new FractalTask(fractal);
            task.fork();
            tasks.add(task);
        }
        //wait async tasks and collect pixel slices into array pixels
        ArrayList<int[]> pixels = new ArrayList<>();
        for (FractalTask task : tasks) {
            pixels.add(task.join());
        }

        //return concatenated array of pixels
        return concatArrays(pixels);
    }
}

