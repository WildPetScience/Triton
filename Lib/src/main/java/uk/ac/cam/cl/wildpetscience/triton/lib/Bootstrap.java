package uk.ac.cam.cl.wildpetscience.triton.lib;

import nu.pattern.OpenCV;
import uk.ac.cam.cl.wildpetscience.triton.lib.config.CodeGenerator;
import uk.ac.cam.cl.wildpetscience.triton.lib.config.store.AppConfig;

import java.io.File;
import java.io.IOException;

/**
 * Initialises things that must be done at the start of runtime.
 */
public class Bootstrap {
    public static void init() {
        if (isPi()) {
            System.load("/usr/lib/jni/libopencv_java249.so");
        } else if (!isWindows()) {
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

        // I'm not proud of this code
        try {
            AppConfig primary = AppConfig.getPrimaryConfig();
            primary.saveAsPrimaryConfig();
        } catch(IOException e) {
            // Couldn't get the primary config so see if we can get a default
            try {
                AppConfig def = AppConfig.getDefaultConfig();
                def.setDataCode(new CodeGenerator().nextCode());
                def.saveAsPrimaryConfig();
            } catch(IOException ee) {
                System.err.println("Problem getting default config.");
            }
        }
    }

    /**
     * Checks if windows. Used for special treatment of OpenCV
     */
    static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    static boolean isPi() {
        return System.getProperty("os.arch").equals("arm");
    }

    static boolean is64Bit() {
        return System.getProperty("sun.arch.data.model").equals("64");
    }
}
