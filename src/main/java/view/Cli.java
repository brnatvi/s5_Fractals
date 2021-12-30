package view;

import controller.Controller;
import interfaces.Initialisable;
import model.Complex;
import model.FractalFactory;
import model.FractalTaskAggregator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

/**
 * The class Cli provides a command line interface to interact with the program.
 */
public class Cli
{
    /** Controller as parameter of Cli**/
    private Controller controller = null;
    /** Parser as inner class of Cli**/
    private Parser parser = null;
    /** Report as inner class of Cli**/
    private Report report = null;

    //============================ Constructor ==============================
    /**
     * {@summary External use constructor. Instantiates Cli. }
     */
    public Cli(String[] args)
    {
        for (String arg : args)
        {
            if (arg.equalsIgnoreCase("--help"))
            {
                showHelp();
                System.exit(0);
            }
        }

        controller = new Controller();
        parser     = new Parser(controller, args);
        report     = new Report(controller);
        try
        {
            report.printParameters();
            launch();
            controller.exportSettings(controller.getPath() + ".fct");
            report.printReport();
        } catch (IOException e)
        {
            System.out.println("Can't write file ...");
            System.exit(-1);
        }
    }

    //=========================== Inner classes ==============================
    /**
     * Inner class Parser, reads command line arguments. }
     */
    private class Parser
    {
        /** Controller as parameter of Parser**/
        private Controller controller = null;
        /** Map containing parameters**/
        private Map<String, String> params = null;

        /**
         * {@summary Constructor which instantiates Parser. }
         * @param c Controller
         */
        public Parser(Controller c, String[] args)
        {
            controller = c;
            params = parse(args);
            params.forEach((k, v) -> initController(k, v));
        }

        /**
         * {@summary The method for parsing command line arguments.}
         */
        private Map<String, String> parse(String[] args)
        {
            if (args.length % 2 == 1)
            {
                throw new IllegalArgumentException("Wrong input: invalid number of arguments");
            }
            return IntStream.range(0, args.length/2)
                    .map(i -> i*2)
                    .collect(LinkedHashMap::new, (m, i) -> m.put(args[i].substring(2), args[i + 1]), Map::putAll);
        }

        /**
         * {@summary Instantiates a Controller with given parameters.}
         */
        private void initController(String k, String v)
        {
            switch (k)
                {
                    case "type":                                               // type of fractal
                        if (v.toLowerCase().equals("m"))
                        {
                            controller.setFractalType(Initialisable.TypeFractal.MANDELBROT);
                        } else if (v.toLowerCase().equals("j"))
                        {
                            controller.setFractalType(Initialisable.TypeFractal.JULIA);
                        } else
                        {
                            System.out.println("Wrong input: unexpected type of fractal : " + v);
                            System.exit(-1);
                        }
                        break;

                    case "color":                                              // color scheme
                        if (v.toLowerCase().equals("b"))
                        {
                            controller.setColorScheme(Initialisable.ColorScheme.BLUE);
                        } else if (v.toLowerCase().equals("r"))
                        {
                            controller.setColorScheme(Initialisable.ColorScheme.RED);
                        } else if (v.toLowerCase().equals("g"))
                        {
                            controller.setColorScheme(Initialisable.ColorScheme.GREEN);
                        } else
                        {
                            System.out.println("Wrong input: unexpected color scheme : " + v);
                            System.exit(-1);
                        }
                        break;


                    case "f":                                                 // function
                        if (v.toLowerCase().equals("q"))
                        {
                            controller.setFunctionType(Initialisable.TypeFunction.QUADRATIC);
                        } else if (v.toLowerCase().equals("bq"))
                        {
                            controller.setFunctionType(Initialisable.TypeFunction.BIQUADRATIC);
                        } else if (v.toLowerCase().equals("c"))
                        {
                            controller.setFunctionType(Initialisable.TypeFunction.CUBIC);
                        } else
                        {
                            System.out.println("Wrong input: unexpected type of function : " + v);
                            System.exit(-1);
                        }
                        break;

                    case "c":                                                 // constant
                        controller.setConstant(Complex.fromString(v));
                        break;

                    case "it":                                                // number of max iterations
                        try
                        {
                            int n = Integer.parseInt(v);
                            controller.setMaxIter(n);
                        } catch (NumberFormatException e)
                        {
                            System.out.println("Wrong input: unexpected value of number max of iterations : " + v);
                            System.exit(-1);
                        }
                        
                        break;
                        
                    case "t":                                                // display time of execution
                        if (v.toLowerCase().equals("y"))
                        {
                            controller.setIsDisplayTime(true);
                        } else if (v.toLowerCase().equals("n"))
                        {
                            controller.setIsDisplayTime(false);
                        } else
                        {
                            System.out.println("Wrong input: unexpected value of " + k + " : " + v);
                            System.exit(-1);
                        }
                        break;

                    case "n":                                                // number of threads
                        if (v.toLowerCase().equals("opt"))
                        {
                            int numb = Runtime.getRuntime().availableProcessors();
                            controller.setCountThreads(numb);
                        } else
                        {
                            try
                            {
                                int numb = Integer.parseInt(v);
                                controller.setCountThreads(numb);
                            } catch (NumberFormatException e)
                            {
                                System.out.println("Wrong input: unexpected value of number of threads : " + v);
                                System.exit(-1);
                            }
                        }
                        break;
                    case "load":                                              // load from file
                        controller.importSettings(v);
                        break;

                    case "save":                                              // save to file
                        controller.setPath(v);
                        break;

                    case "size":                                               // size of image
                        String[] size = v.split("x");
                        if (size.length != 2)
                        {
                            System.out.println("Wrong input: not two numbers : " + size);
                            System.exit(-1);
                        }
                        else
                        {
                            try
                            {
                                int w = Integer.parseInt(size[0]);
                                int h = Integer.parseInt(size[1]);
                                if ((w <= 16) || (h <= 16))
                                {
                                    System.out.println("Wrong input: unsupported image size : " + size);
                                    System.exit(-1);
                                }
                                controller.setSize(w, h);
                            } catch (NumberFormatException e)
                            {
                                System.out.println("Wrong input: unexpected value of WxH : " + size);
                                System.exit(-1);
                            }
                        }
                        break;
                        
                    default:
                        System.out.println("Unknown argument: " + k + ", exit !");
                        System.exit(-1);
                        break;
                }
            }
    }

    /**
     * Inner class Report, reads command line arguments. }
     */
    private class Report
    {
        /** Controller as parameter of Report**/
        private Controller controller  = null;
        /** Stores the time necessary to calculate**/
        private double     timeOfCalc  = 0;
        /** Stores the time necessary to paint**/
        private long       timeOfPaint = 0;

        /**
         * {@summary Constructor which instantiates Report. }
         * @param c Controller
         */
        public Report(Controller c) {  controller = c; }

        /**
         * {@summary prints Report parameters. }
         */
        private void printParameters()
        {
            System.out.println("\nFractal parameters: " + controller.getTypeFractal().toString() +
                    " with " + controller.getFunctionType().toString() + " function" +
                    " and constant = " + controller.getConstant().toString() +
                    "\non complex rectangle A=" + controller.getComplexRect().x +
                    " B=" + (controller.getComplexRect().x+controller.getComplexRect().width) +
                    " C=" + controller.getComplexRect().y +
                    " D=" + (controller.getComplexRect().y+controller.getComplexRect().height) +
                    "\nwith max iterations = " + controller.getMaxIter() +
                    " on " + controller.getCountThreads() + " thread(s)\n" +
                    "Image W:" + controller.getWidthPNG() + " H:" + controller.getHeightPNG());
        }

        /**
         * {@summary prints Report. }
         */
        private void printReport()
        {
            if (controller.getIsDisplayTime() == true)
            {
                System.out.println("This fractal has been calculated in " + timeOfCalc + " seconds");
                System.out.println("and painted on image " + controller.getWidthPNG() +
                        " pxl x " + controller.getHeightPNG() +
                        " pxl during " + timeOfPaint + " milliseconds");
            }
        }
    }

    //============================ Lambda's getters ==============================

    /**
     * {@summary Help with options available. }
     */
    private void showHelp()
    {
        String message = "\nWelcome to fabulous world of fractals !!\n\n" +
                "Run :    mvn exec:java@exCli -Dexec.args=\"<Your options, see below>\"\n\n" +
                "Available program options :\n" +
                " --type  - type of fractal (Julia (J) or Mandelbrot (M))\n" +
                " --color - choose color scheme to draw fractal (blue(B), green (G) or red (R))\n" +
                " --f     - type of function to calculate fractal (quadratic (Q), cubic (C), biquadratic (BQ))\n" +
                " --c     - set the constant: two floating point numbers <re;im>\n" +
                "           Example: -0.257;0.685 \n" +
                "           (for Julia's fractal they have to be in range [-1.0, 1.0] to obtain a beautiful result)\n" +
                " --it    - set maximum number of iterations \n" +
                " --t     - show the time of execution ( Y/N )\n" +
                " --n     - set number of threads to calculation, or <Opt> to set number of your PC's cores \n" +
                " --size  - image size in pixels (WxH)\n" +
                "           Example: 2000x1000 \n" +
                " --load  - load fractal parameters by file path\n" +
                "           Example: --load MyFile.fct\n" +
                " --save  - save result png & config files by path file\n" +
                "           Example: --save /home/user/MyFile\n" +
                "           Program will generate 2 files: )\n" +
                "             /home/user/MyFile.png\n" +
                "             /home/user/MyFile.fct\n" +
                "           If parameter isn't specified - files will be generate in current folder\n" +
                "            ./MyFractal.png\n" +
                "            ./MyFractal.fct\n" +
                " --help  - show this help\n\n" +

                "Default parameters are:\n" +
                " --type J --color B --f Q --c -0.74543;0.11301 --t Y --n Opt --it 1000 --size 4000x2000\n" +
                "and fractal is calculating for points included into rectangle x->[-2.0, 2.0] y->[-1.0, 1.0]\n\n";

        System.out.println(message);
    }

    /**
     * {@summary launches Cli. }
     */
    private void launch() throws IOException
    {
        System.out.println("\nProcessing...\n");
        long start1 = System.currentTimeMillis();

        FractalFactory factory = new FractalFactory(controller);

        FractalTaskAggregator taskAgregator = new FractalTaskAggregator(factory, controller);

        ForkJoinPool forkJoinPool = new ForkJoinPool(controller.getCountThreads(),
                                                     ForkJoinPool.defaultForkJoinWorkerThreadFactory,
                                                     null,
                                                     true);
        forkJoinPool.execute(taskAgregator);
        int[] points = taskAgregator.join();

        int width = taskAgregator.getWidth();
        int height = points.length / width;

        long finish1 = System.currentTimeMillis();
        report.timeOfCalc = (double)(finish1 - start1)/1000.0;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB );
        long start2 = System.currentTimeMillis();
        img.setRGB(0,0, width, height, points, 0, width);
        long finish2 = System.currentTimeMillis();
        report.timeOfPaint = (finish2 - start2);


        String name = controller.getPath() + ".png";
        File file = new File(name);
        ImageIO.write(img, "PNG", file);
        System.out.println("Done");
    }

    //============================ Main function ==============================
    /**
     * main method
     */
    public static void main(String[] args)
    {
        Cli cli = new Cli(args);
        System.exit(0);
    }
}

//--type J --color B --f Q --c -0.74543;0.11301 --t Y --n Opt --size 1000x400