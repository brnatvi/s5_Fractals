package model;

public final class Complex
{
    private double re;
    private double im;
    private static final Complex ZERO = new Complex(0, 0);
    private static final Complex ONE = new Complex(1, 0);
    private static final Complex I = new Complex(0, 1);

    private Complex(double re, double im)
    {
        this.re = re;
        this.im = im;
    }

    //============= Static fabrics ======================

    public static Complex createComplex(double re, double im) { return new Complex(re, im); }

    public static Complex getZERO() { return ZERO; }

    public static Complex getONE() { return ONE; }

    public static Complex getI() { return I; }

    public static Complex fromString(String str)
    {
        String[] reIm = str.split(";");
        if (reIm.length != 2)
        {
            System.out.println("Wrong input: not two floating point numbers : " + str);
            System.exit(-1);
        }
        else
        {
            try
            {
                double re = Double.parseDouble(reIm[0]);
                double im = Double.parseDouble(reIm[1]);
                return Complex.createComplex(re, im);
            } catch (NumberFormatException e)
            {
                System.out.println("Wrong input: unexpected value of constant : " + str);
                System.exit(-1);
            }
        }
        return null;
    }

    //============= Math operations ======================

    public double realPart() { return re; }

    public double imaginaryPart() { return im; }

    public Complex plus(Complex c) { return new Complex(re + c.re, im + c.im); }

    public Complex minus(Complex c) { return new Complex(re - c.re, im - c.im); }

    public Complex times(Complex c) { return new Complex(re * c.re - im * c.im, re * c.im + im * c.re); }

    public Complex dividedBy(Complex c)
    {
        double tmp = c.re * c.re + c.im * c.im;
        return new Complex((re * c.re + im * c.im) / tmp, (im * c.re - re * c.im) / tmp);
    }

    public double mod() { return Math.sqrt(re * re + im * im); }

    //============= Others ======================
    @Override
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (!(o instanceof Complex)) return false;
        Complex c = (Complex) o;
        return (Double.compare(c.re, this.re) == 0 && Double.compare(c.im, this.im) == 0);
    }

    @Override
    public String toString()
    {
        return (re + ";" + im);
    }

}