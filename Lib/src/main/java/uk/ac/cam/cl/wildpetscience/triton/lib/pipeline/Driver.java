package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline;

import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;

import java.io.IOException;

/**
 * Pumps data from an ImageInputSource to an OutputSink.
 */
public class Driver<D> extends Thread {
    protected final ImageInputSource in;
    protected final OutputSink<D> out;

    protected final Filter<Image, D> filter;

    private Image latest;
    private boolean latestWanted = false;
    private final Object latestLock = new Object();

    private boolean keepInputAlive = false;

    private boolean cancelled = false;

    private TapFilter tapFilter;

    public Driver(ImageInputSource in, Filter<Image, D> flt, OutputSink<D> out) {
        this.in = in;
        this.out = out;
        filter = flt;
        setDaemon(true);
    }

    public <A> Driver(ImageInputSource in,
                  Filter<Image, A> f1,
                  Filter<A, D> f2,
                  OutputSink<D> out) {
        this(in, new ConnectingFilter<>(f1, f2), out);
    }

    public <A, B> Driver(ImageInputSource in,
                         Filter<Image, A> f1,
                         Filter<A, B> f2,
                         Filter<B, D> f3,
                         OutputSink<D> out) {
        this(in,
                new ConnectingFilter<>(
                        new ConnectingFilter<>(f1, f2),
                        f3)
                , out);
    }

    public <A, B, C> Driver(ImageInputSource in,
                      Filter<Image, A> f1,
                      Filter<A, B> f2,
                      Filter<B, C> f3,
                      Filter<C, D> f4,
                      OutputSink<D> out) {
        this(in,
                new ConnectingFilter<>(
                        new ConnectingFilter<>(f1, f2),
                        new ConnectingFilter<>(f3, f4))
                        , out);
    }

    public static Driver<Image> makeSimpleDriver(ImageInputSource in,
                                                 OutputSink<Image> out) {
        return new Driver<>(in, new IdentityFilter<>(), out);
    }

    @Override
    public void run() {
        boolean localCancelled;
        do {
            synchronized (this) {
                localCancelled = cancelled;
            }
            try {
                Image img = in.getNext();
                if (img == null) {
                    out.close();
                    cancel();
                } else {
                    synchronized (latestLock) {
                        if (latestWanted) {
                            latest = new Image(img);
                            latestLock.notify();
                        }
                    }
                    D result = filter.filter(img);
                    out.onDataAvailable(result);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (!localCancelled);
        try {
            System.out.println("Stopping Driver");
            if (!keepInputAlive) {
                in.close();
            }
            filter.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void cancel() {
        cancelled = true;
        synchronized (latestLock) {
            latestLock.notify();
        }
    }

    public synchronized boolean isCancelled() {
        return cancelled;
    }

    public Image getMostRecentInput() {
        if (isCancelled()) {
            return null;
        }
        synchronized (latestLock) {
            latestWanted = true;
            try {
                latestLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isCancelled()) {
                return null;
            }
            latestWanted = false;
            Image img = latest;
            latest = null;
            return img;
        }
    }

    public boolean isKeepInputAlive() {
        return keepInputAlive;
    }

    public void setKeepInputAlive(boolean keepInputAlive) {
        this.keepInputAlive = keepInputAlive;
    }

    public TapFilter getTapFilter() {
        return tapFilter;
    }

    public void setTapFilter(TapFilter tapFilter) {
        this.tapFilter = tapFilter;
    }
}
