package view;

import java.util.LinkedHashMap;
import java.util.Map;

public class Cli
{
    public Cli()
    {

    }

    private class Parser
    {
        private Map<String, String> parameters = new LinkedHashMap<>();

        public void collectParams(String[] args) throws RuntimeException
        {
            parameters = new Parser().parse(args);

            for (String arg : args) {
                switch (arg) {
                    case "--type":        // type of fractal
                        break;

                    case "--t":            // time  GUI - field
                        break;

                    case "--color":        // color
                        break;

                    case "--file":         // load from file
                        break;

                    case "--f":            // function
                        break;

                    case "--c":            // constant  GUI - field
                        break;

                    case "--n":            // number of threads
                        break;
                }
            }
        }

        private Map<String, String> parse(String[] args)
        {
            for (int i = 0; i < args.length; i += 2)
            {
                while (i + 1 < args.length)
                {
                    parameters.put(args[i], args[i + 1]);
                }
            }
            return parameters;
        }
    }


    private static void launch()
    {

    }

    public static void main(String[] args) { launch(); }
}