package view;

import controller.Controller;
import interfaces.Calculable;
import interfaces.Initialisable;
import model.Complex;
import model.FractalFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Cli
{
    private Controller controller = null;
    private Parser parser = null;
    private Report report = null;

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

    private class Parser
    {
        private Controller controller = null;
        private Map<String, String> params = null;

        public Parser(Controller c, String[] args)
        {
            controller = c;
            params = parse(args);
            params.forEach((k, v) -> initController(k, v));
        }

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

        private void initController(String k, String v)
        {
            switch (k)
                {
                    case "type":                                               // type of fractal
                        if (v.toLowerCase().equals("m"))
                        {
                            controller.setTypeFractal(Initialisable.TypeFractal.MANDELBROT);
                        } else if (v.toLowerCase().equals("j"))
                        {
                            controller.setTypeFractal(Initialisable.TypeFractal.JULIA);
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
                        
                    default:
                        System.out.println("Unknown argument: " + k + ", exit !");
                        System.exit(-1);
                        break;
                }
            }
    }

    private class Report
    {
        private Controller controller  = null;
        private long       timeOfCalc  = 0;
        private long       timeOfPaint = 0;

        public Report(Controller c) {  controller = c; }

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

        private void printReport()
        {
            if (controller.getIsDisplayTime() == true)
            {
                System.out.println("This fractal was calculated during " + timeOfCalc + " seconds");
                System.out.println("and painted on image " + controller.getWidthPNG() +
                        " pxl x " + controller.getHeightPNG() +
                        " pxl during " + timeOfPaint + " milliseconds");
            }
        }
    }

    private void showHelp()
    {
        String message = "\nWelcome to fabulous world of fractals !!\n\n" +
                "Run :    mvn exec:java@exCli -Dexec.args=\"<Your options, see below>\"\n\n" +
                "Available program options :\n" +
                " --type  - type of fractal (Julia (J) or Mandelbrot (M))\n" +
                " --color - choose color scheme to draw fractal (blue(B), green (G) or red (R))\n" +
                " --f     - type of function to calculate fractal (quadratic (Q), cubic (C))\n" +
                " --c     - set the constant: two floating point numbers <re;im>\n" +
                "           Example: -0.257;0.685 \n" +
                "           (for Julia's fractal they have to be in range [-1.0, 1.0] to obtain a beautiful result)\n" +
                " --it    - set maximum number of iterations \n" +
                " --t     - show the time of execution ( Y/N )\n" +
                " --n     - set number of threads to calculation, or <Opt> to set number of your PC's cores \n" +
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
                " --type J --color B --f Q --c -0.74543;0.11301 --t Y --n Opt --it 1000\n" +
                "and fractal is calculating for points included into rectangle x->[-2.0, 2.0] y->[-1.0, 1.0]\n\n";

        System.out.println(message);
    }

    private void launch() throws IOException
    {
        Calculable fractal = new FractalFactory(controller).create();
        System.out.println("\nProcessing...\n");
        long start1 = System.currentTimeMillis();
        int[] points = fractal.calculate();
        long finish1 = System.currentTimeMillis();
        report.timeOfCalc = (finish1 - start1)/1000;

        BufferedImage img = new BufferedImage(controller.getWidthPNG(), controller.getHeightPNG(), BufferedImage.TYPE_INT_RGB );
        long start2 = System.currentTimeMillis();
        img.setRGB(0,0, controller.getWidthPNG(), controller.getHeightPNG(), points, 0, controller.getWidthPNG());
        long finish2 = System.currentTimeMillis();
        report.timeOfPaint = (finish2 - start2);


        String name = controller.getPath() + ".png";
        File file = new File(name);
        ImageIO.write(img, "PNG", file);
        System.out.println("Done");
    }


    public static void main(String[] args)
    {
        Cli cli = new Cli(args);
    }
}

//--type J --color B --f Q --c -0.74543;0.11301 --t Y --n D