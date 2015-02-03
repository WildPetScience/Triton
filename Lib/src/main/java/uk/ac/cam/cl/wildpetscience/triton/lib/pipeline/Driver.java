package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline;

import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;

import java.io.IOException;

/**
 * Pumps data from an ImageInputSource to an OutputSink.
 */
public class Driver<D> extends Thread {
    private final ImageInputSource in;
    private final OutputSink<D> out;

    private final Filter<Image, D> filter;

    private boolean cancelled = false;

    public Driver(ImageInputSource in, Filter<Image, D> flt, OutputSink<D> out) {
        this.in = in;
        this.out = out;
        filter = flt;
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
        return new Driver<Image>(in, new IdentityFilter<>(), out);
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
                    D result = filter.filter(img);
                    out.onDataAvailable(result);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (!localCancelled);
    }

    public synchronized void cancel() {
        cancelled = true;
    }

    public synchronized boolean isCancelled() {
        return cancelled;
    }
}
