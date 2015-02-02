package uk.ac.cam.cl.wildpetscience.triton.lib;

import nu.pattern.OpenCV;

import java.io.File;

/**
 * Initialises things that must be done at the start of runtime.
 */
public class Bootstrap {
    public static void init() {
        if (!isWindows()) {
            OpenCV.loadShared();
        } else {
            String arch = is64Bit() ? "x64" : "x86";
            // Assuming static install dir for simplicity
            String installDir = String.format("C:\\opencv\\build\\%s\\vc12\\bin\\", arch);
            String javaDir = String.format("C:\\opencv\\build\\java\\%s\\", arch);

            System.setProperty("java.library.path",
                    System.getProperty("java.library.path") +
                            File.pathSeparator +
                            installDir +
                            File.pathSeparator +
                            javaDir);
            String path = System.getProperty("java.library.path");
            System.load(javaDir + "opencv_java2410.dll");
        }
    }

    /**
     * Checks if windows. Used for special treatment of OpenCV
     */
    static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    static boolean is64Bit() {
        return System.getProperty("sun.arch.data.model").equals("64");
    }
}
