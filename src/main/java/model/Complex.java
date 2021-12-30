package model;

/**
 * The class Complex allows to do base math on complex values
 */
public final class Complex
{
    /**  Real part of the complex value **/
    private double re;
    /**  Imaginary part of the complex value **/
    private double im;
    /**  Zero constant **/
    private static final Complex ZERO = new Complex(0, 0);
    /**  One constant **/
    private static final Complex ONE = new Complex(1, 0);
    /**  Complex I constant **/
    private static final Complex I = new Complex(0, 1);

    /**
     * {@summary Constructor which instantiates a Complex object and its parameters}
     */
	 
    private Complex(double re, double im)
    {
        this.re = re;
        this.im = im;
    }

    //============= Static fabrics ======================
    /**
     * {@summary creates new complex object}
	 * @param re Real part of the complex value 
	 * @param Imaginary part of the complex value
     * @return new complex object
     */

    public static Complex createComplex(double re, double im) { return new Complex(re, im); }

    /**
     * {@summary return Zero constant}
     * @return Zero constant
     */
	
    public static Complex getZERO() { return ZERO; }

    /**
     * {@summary return One constant}
     * @return One constant
     */
	
    public static Complex getONE() { return ONE; }

    /**
     * {@summary return I constant}
     * @return I constant
     */
	
    public static Complex getI() { return I; }

    /**
     * {@summary creates new complex object from string}
	 * @param str string value 
     * @return new complex object
     */
	
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

    /**
     * {@summary get real part}
     * @return real part
     */
	 
    public double realPart() { return re; }

    /**
     * {@summary get imaginary part}
     * @return imaginary part
     */
	
    public double imaginaryPart() { return im; }

    /**
     * {@summary increase value by c and return new object}
	 * @param c complex value
     * @return new complex object
     */
	
    public Complex plus(Complex c) { return new Complex(re + c.re, im + c.im); }

    /**
     * {@summary decrease value by c and return new object}
	 * @param c complex value
     * @return new complex object
     */
	
    public Complex minus(Complex c) { return new Complex(re - c.re, im - c.im); }

    /**
     * {@summary multiply value by c and return new object}
	 * @param c complex value
     * @return new complex object
     */
	
    public Complex times(Complex c) { return new Complex(re * c.re - im * c.im, re * c.im + im * c.re); }

    /**
     * {@summary divide value by c and return new object}
	 * @param c complex value
     * @return new complex object
     */
	
    public Complex dividedBy(Complex c)
    {
        double tmp = c.re * c.re + c.im * c.im;
        return new Complex((re * c.re + im * c.im) / tmp, (im * c.re - re * c.im) / tmp);
    }

    /**
     * {@summary calculate module and return new object}
     * @return new complex object
     */
    public double mod() { return Math.sqrt(re * re + im * im); }

    //============= Others ======================
    /**
     * {@summary compare with another compex value O}
	 * @param o complex value
     * @return true - values are equal, false - otherwise
     */

    @Override
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (!(o instanceof Complex)) return false;
        Complex c = (Complex) o;
        return (Double.compare(c.re, this.re) == 0 && Double.compare(c.im, this.im) == 0);
    }

    /**
     * {@summary convert to string}
     * @return string value
     */
	
    @Override
    public String toString()
    {
        return (re + ";" + im);
    }

}