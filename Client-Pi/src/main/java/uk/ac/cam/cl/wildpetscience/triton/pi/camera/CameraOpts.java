package uk.ac.cam.cl.wildpetscience.triton.pi.camera;

import java.util.Locale;

/**
 * Camera options
 */
public class CameraOpts {
    private int width;
    private int height;
    private boolean grayscale = false;

    private int sharpness = 0;
    private int contrast = 0;
    private int brightness = 50;
    private int saturation = 0;
    private int iso = 1600;
    private boolean autoIso = true;

    private Exposure exposure = Exposure.auto;
    private AWB awb = AWB.auto;
    private DRC drcLevel = DRC.off;
    private MeteringMode meteringMode = MeteringMode.average;

    private boolean hflip = false;
    private boolean vflip = false;

    public CameraOpts(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public String toCommandLine() {
        return String.format(Locale.ENGLISH,
                "raspistill -t 1 -o - -w %d -h %d -e bmp -sh %d -co %d -br %d" +
                        " -sa %d %s -ex %s -awb %s -drc %s -mm %s %s %s",
                getWidth(),
                getHeight(),
                getSharpness(),
                getContrast(),
                getBrightness(),
                getSaturation(),
                isAutoIso() ? "-ISO " + getIso() : "",
                getExposure().toString(),
                getAwb().toString(),
                getDrcLevel().toString(),
                getMeteringMode().toString(),
                isHflip() ? "-hf" : "",
                isVflip() ? "-vf" : ""
        );
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isGrayscale() {
        return grayscale;
    }

    public void setGrayscale(boolean grayscale) {
        this.grayscale = grayscale;
    }

    public int getSharpness() {
        return sharpness;
    }

    public void setSharpness(int sharpness) {
        this.sharpness = sharpness;
    }

    public int getContrast() {
        return contrast;
    }

    public void setContrast(int contrast) {
        this.contrast = contrast;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public int getSaturation() {
        return saturation;
    }

    public void setSaturation(int saturation) {
        this.saturation = saturation;
    }

    public int getIso() {
        return iso;
    }

    public void setIso(int iso) {
        this.iso = iso;
    }

    public boolean isAutoIso() {
        return autoIso;
    }

    public void setAutoIso(boolean autoIso) {
        this.autoIso = autoIso;
    }

    public Exposure getExposure() {
        return exposure;
    }

    public void setExposure(Exposure exposure) {
        this.exposure = exposure;
    }

    public AWB getAwb() {
        return awb;
    }

    public void setAwb(AWB awb) {
        this.awb = awb;
    }

    public DRC getDrcLevel() {
        return drcLevel;
    }

    public void setDrcLevel(DRC drcLevel) {
        this.drcLevel = drcLevel;
    }

    public MeteringMode getMeteringMode() {
        return meteringMode;
    }

    public void setMeteringMode(MeteringMode meteringMode) {
        this.meteringMode = meteringMode;
    }

    public boolean isHflip() {
        return hflip;
    }

    public void setHflip(boolean hflip) {
        this.hflip = hflip;
    }

    public boolean isVflip() {
        return vflip;
    }

    public void setVflip(boolean vflip) {
        this.vflip = vflip;
    }
}
