package uk.ac.cam.cl.wildpetscience.triton.lib.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * An InputStream that doesn't close its inner stream when closed, because
 * anti-patterns are the best kind of patterns.
 */
public class NonClosingInputStream extends InputStream {
    private final InputStream inner;
    public NonClosingInputStream(InputStream inner) {
        super();
        this.inner = inner;
    }

    @Override
    public boolean markSupported() {
        return inner.markSupported();
    }

    @Override
    public synchronized void reset() throws IOException {
        inner.reset();
    }

    @Override
    public synchronized void mark(int readlimit) {
        inner.mark(readlimit);
    }

    @Override
    public void close() throws IOException {
        // Nothing!
    }

    public void reallyClose() throws IOException {
        inner.close();
    }

    @Override
    public int available() throws IOException {
        return inner.available();
    }

    @Override
    public long skip(long n) throws IOException {
        return inner.skip(n);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return inner.read(b, off, len);
    }

    @Override
    public int read(byte[] b) throws IOException {
        return inner.read(b);
    }

    @Override
    public int read() throws IOException {
        return inner.read();
    }
}
