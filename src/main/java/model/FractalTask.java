package model;

import interfaces.Calculable;
import java.util.ArrayList;
import java.util.concurrent.RecursiveTask;

public class FractalTask extends RecursiveTask<int[]> {
    private Calculable fractal;

    public FractalTask(Calculable f) {
        this.fractal = f;
    }

    @Override
    protected int[] compute() {
        return this.fractal.calculate();
    }
}
