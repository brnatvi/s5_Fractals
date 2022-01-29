[main]: https://gaufre.informatique.univ-paris-diderot.fr/raveneau/corona-bounce
[comment-shield]: https://img.shields.io/badge/Javadoc-100%25-brightgreen
[ ![comment-shield][] ][main]


# **Overview**

Fractals is a programme to create a graphic representation of Julia's and Mandelbrot's sets.

![](https://github.com/brnatvi/s5_Fractals/blob/main/gifs/video_1.gif)

![](https://github.com/brnatvi/s5_Fractals/blob/main/gifs/video_2.gif)

# **How to use**

## Dependencies
1) Maven v3.6 or greater
2) Java OpenJDK v11.0 or greater
3) JavaFX

## Compile (to do in code/)
If version of Java OpenJKD is different please update export JAVA_HOME parameter
```                
export JAVA_HOME=/usr/lib/jvm/java-1.11.0-openjdk-amd64
mvn compile package
```

## Run
N.B.: JAVA_HOME env. variable has to be properly configured, see Compile chapter for details

**GUI mode**
```
mvn exec:java@exGui
```                
**CLI mode**

```
mvn exec:java@exCli -Dexec.args="--help"
```        
or
```
mvn exec:java@exCli -Dexec.args="<Your options, see help>" 
```                

## Open JavaDoc
Launch /docs/javadoc/apidocs/index.html on the web browser.

# **Maintainers:**

* Natalia Bragina
* Anna Golikova



# **Features**

It's possible to modify follows parameters:
* type of fractal (fractals of Julia and Mandelbrot are available)

![](https://github.com/brnatvi/s5_Fractals/blob/main/gifs/video_4.gif)

* function for calculation (quadratic, cubic and biquadratic functions are available)

![](https://github.com/brnatvi/s5_Fractals/blob/main/gifs/video_6.gif)

* complex constant and number of max iterations
* number of threads for calculation
* color representation
* size of generated image (in GUI mode it happens with changing of window's dimensions)

It is also possible to load fractal from config file (few examples with extension .fct are available in the root of project) 
and to save current parameters to config (with .fct extension).
Process logs are displayed on GUI mode, report is available on CLI mode. 

![](https://github.com/brnatvi/s5_Fractals/blob/main/gifs/video_3.gif)


![](https://github.com/brnatvi/s5_Fractals/blob/main/gifs/video_5.gif)

