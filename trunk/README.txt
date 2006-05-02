Thankyou for your interest in JVector.
This file explains some brief general information of JVector, 
and the process needed to build and run JVector.

-------
JVector
-------
JVector is a Vector (wow!) based paint program. It aims to currently
provide fairly simple tools, simliar to MS Paint, but being Vector based.
New and exciting features, such as texturing, are more important then advance
node editing for this project.

JVector is written in, you guessed it, Java! The project is hosted on 
sourceforge.net (http://sourceforge.net/projects/t-bone-paint). This project
is an open source project of course, under the GPL (GNU General Public Licence).

---------------
Acknowlegements
---------------
This project makes use of the follwing java libaries/prjects.
This product includes software developed by the Apache Software Foundation 
(http://www.apache.org/)
  
  * Apache log4j (http://logging.apache.org/log4j/docs/) - Java Logging Toolkit.
  
  * Apache Batik (http://xml.apache.org/batik/) - Java SVG Toolkit.
  
  * IzPack Installer (http://www.izforge.com/izpack/)
  
  * BrowserLauncher2 (http://browserlaunch2.sourceforge.net/) - Java Toolkit to 
    open the default browser on several OS's.

------------
Requirnments
------------
* Java VM 1.5.0.
* Ant 1.6.1 (Probally works on lower versions, but not tested).
            (Ant is not actually needed, but will handle the build
             process for you.)
* Assumed understanding of how to use Ant. (If u want to build yourself)
* log4j - Java Logging API. (http://logging.apache.org/log4j/docs/).
        - Download this API, then extract it to anywhere you would like, except in pre-existing
          folders in the JVector project directory. The required file is the jar distribution
          of the log4j API (found under, jakarta-log4j-1.2.8\dist\lib\log4j-1.2.8.jar). Place
          this jar file where ever disired (you can leave it where it is if you would like) but
          make sure you know the path to it, you will need it for building JVector.

(Optional - Needed to build the SVG Plugin)
* Batik SVG Toolkit - (http://xml.apache.org/batik/)
                    - Download the Toolkit, and place it anywhere you would like, but remember the
                      path, as it is required for building the SVG Plugin

If you fail these requirnments, then you really shouldn't be trying to build your own CVS
version. Binary builds wil be provided on a regular basis.

------------------
Build File Targets
------------------
* compile - Compiles the program, this target should be used to generate a binary version
            of JVector Initially.
* clean - Deletes all the files in the build (bin) directory, except those in the Resources folder and plugins.
* clean_all - Clean everything.
* clean_plugins - Same as clean but for plugins.
* build - Cleans and then compiles JVector.
* build_all - Builds JVector and the plugins.
* compile_plugins - Builds all the plugins for JVector.
* compile_plugins_jpg - Compiles, amd then Jar's the JPG plugin.
* compile-plugins_png - Compiles, amd then Jar's the PNG plugin.
* compile_plugins_svg - Compiles, amd then Jar's the SVG plugin.
* jar - Packages JVector into a self executable jar archive, ("lib/JVector.jar").
* installer - Creates the IzPack installer application for JVector. (IzPack required).
* run_classes - Runs JVector.
* run_jar - Runs the jar packaged version of the program, ("lib/JVector.jar").
* run_doc - Attempts to Browser set by ${browser}, and view the javadoc.
* pack - Creates a compressed tar archive (tar.gz) containing the source code 
		 and documentation for it (JVector-${version}-source.tar.gz).
* doc - Builds the JavaDoc for JVector, placing it in the "javadoc" (default) directory.


----------------
Building JVector
----------------
To build JVector follow the follwing steps.

   1. You need to first run the ant build target, but specifing the location of the log4j jar. This is
      through the -D command, witht he variable to override being "log4j".

      eg           $ ant build -Dlog4j=<YOUR_LOG4J_PATH>

   2. JVector should now be built, compiled into classes. To create a jar, use the jar target, specifing the
      log4j variable again.

      eg           $ ant jar -Dlog4j=<YOUR_LOG4J_PATH>

   3. Now get all the files and directory created under lib/dist, and place them anywhere your
      heart desires.

   4. Double click on JVector.jar to run. If JVector doesnt run, eg nothing happens, or an archive
      prgram opens it instead, open up a console window in that directory, and type;
      
                   $ java -jar JVector.jar
                OR $ java -classpath JVector.jar com.terei.jvector.JVector
      
      Ethier of those commands should work. If this all fails, then please contact us for help
      at our sourceforge homepage.
      
   5. (OPTIONAL) Run the "build-plugins" target to build the JVector file output plugins. This requires
      that you specify the location of the Bantik lib directory. (Note: You can build the plugins
      sepperatley if you want so that you dont need to have Bantik, as it is only required for the
      SVG plugin).
      
      eg           $ ant build-plugins -Dbantik=<YOUR_BANTIK_PATH>

      This will create numerous Jar files in the bin/Plug-Ins/File Formats directory.
      Copy these to the Plug-Ins/File Formats directory along side JVector.jar (create the
      folders if needed).