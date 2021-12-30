[main]: https://gaufre.informatique.univ-paris-diderot.fr/raveneau/corona-bounce
[comment-shield]: https://img.shields.io/badge/Javadoc-0%25-brightgreen
[ ![comment-shield][] ][main]


# **Overview**

Fractals is a programme to create a graphic representation of Julia's and Mandelbrot's sets.

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
* 


![](code/target/project_1_resized.mp4)


