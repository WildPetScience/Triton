package uk.ac.cam.cl.wildpetscience.triton.demo.config;

import uk.ac.cam.cl.wildpetscience.triton.lib.Bootstrap;
import uk.ac.cam.cl.wildpetscience.triton.lib.config.ConfigServer;

public class ConfigDemo {

    public static void main(String[] args) {
        Bootstrap.init();
        ConfigServer.start(8000);
    }

}
