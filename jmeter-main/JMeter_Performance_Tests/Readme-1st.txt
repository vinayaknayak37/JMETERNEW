Basic instructions to build the Java extension package as part of the performance testing of EST 2.0 Desktop using JMeter
=========================================================================================================================

1.  Install EST 2.0 Desktop on your machine (keep a copy of the directory path where it is installed)

2.  Download a copy of the "performance-testing-wrapper-<version>.jar" from the EST Desktop release build.
    Take note of the version string from that jar file, and use it to replace the one that is currently in the
    "mvninstall-ESTlib-in-repo.bat" (see below)

3.  Modify the file "mvninstall-ESTlib-in-repo.bat" and set the variable "_EST_DT_LIB_DIR_" to the "lib" subfolder
    where EST 2.0 Desktop was installed (from step 1)

4.  Modify the file "mvninstall-ESTlib-in-repo.bat" and set the variable "_EST_DT_VERSION_" to the version string (from step 2)

5.  From a command-line shell run the batch file to install the .jar files in your repo. Below is an example output of one of the mvn install:
    -------------------
        mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=C:\Users\thai.nguyen\AppData\Local\EST_Desktop\2002_262\artifact-generate.jar -Dfile=C:\Users\thai.nguyen\AppData\Local\EST_Desktop\2002_262\artifact-generate-mailing.jar
        [INFO] Scanning for projects...
        [INFO]
        [INFO] -------------< com.innovapost.desktop:EST2_0_Desktop_Test >-------------
        [INFO] Building EST_Desktop_Testing 1.0-SNAPSHOT
        [INFO] --------------------------------[ jar ]---------------------------------
        [INFO]
        [INFO] --- maven-install-plugin:2.5.2:install-file (default-cli) @ EST2_0_Desktop_Test ---
        [INFO] Installing C:\Users\thai.nguyen\AppData\Local\EST_Desktop\2002_262\artifact-generate-mailing.jar to C:\Users\thai.nguyen\.m2\repository\cpdt\artifact-generate-mailing\2002.0.262\artifact-generate-mailing-2002.0.262.jar
        [INFO] ------------------------------------------------------------------------
        [INFO] BUILD SUCCESS
        [INFO] ------------------------------------------------------------------------
        [INFO] Total time:  1.094 s
        [INFO] Finished at: 2020-02-19T10:12:41-05:00
        [INFO] ------------------------------------------------------------------------
    -------------------

6.  Make a copy of the version from the mvn install from step 3 (the folder in the repository and extension in the name of the jar).
    For example, the above output tells us that the version of the jar file is "2002.0.262"

7.  Make note that the version number from steps 2 and 5 must match!!!! Double-check the release build if these values do *not* match!!!

8.  Replace all existing version string in the "pom.xml" file with the new version string (from steps 6 and 7)

9.  From the same command-line shell, clean and create a new "EST2_0_Desktop_Test-1.0-SNAPSHOT.jar":
        "mvn clean install"

        9a. (Optional) Start up IntelliJ and open the folder where this "pom.xml" file is located

        9b. (Optional) Click on the "Maven" tab in the upper-right corner of the IntelliJ window

        9c. (Optional) Expand "EST_Desktop_Testing" list

        9d. (Optional) Expand "Lifecycle" list

        9e. (Optional) Multi-select "clean" and "package", and click on the green triangle in the "Maven" sub-window to build the "EST2_0_Desktop_Test-1.0-SNAPSHOT.jar"

