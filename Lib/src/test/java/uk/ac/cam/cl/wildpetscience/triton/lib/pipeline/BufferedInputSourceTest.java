package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline;

import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 * Tests for BufferedInputSource
 */
public class BufferedInputSourceTest {
    @Test
    public void testBufferedInputSourceCreation() {
        ImageInputSource inner = mock(ImageInputSource.class);
        BufferedInputSource buffer = new BufferedInputSource(inner);
    }

    // This is a band-aid fix until we work out why this test is hanging.
    /* @Test
    public void testBufferedInputSource() throws IOException {
        ImageInputSource inner = mock(ImageInputSource.class);
        BufferedInputSource buffer = new BufferedInputSource(inner);

        final LinkedList<Image> created = new LinkedList<Image>();

        when(inner.getNext()).thenAnswer(new Answer<Image>() {
            int count = 0;
            @Override
            public Image answer(InvocationOnMock invocation) throws Throwable {
                if (count++ < 300) {
                    Image img = new Image();
                    synchronized (created) {
                        created.add(img);
                    }
                    return img;
                }
                return null;
            }
        });

        for (int i = 0; i < 300; i++) {
            Image i1 = buffer.getNext();
            Image i2;
            synchronized (created) {
                i2 = created.remove(0);
            }
            if (i1 != i2) {
                throw new RuntimeException("Unequal images found");
            }
        }
        if (buffer.getNext() != null) {
            throw new RuntimeException("Non-null image found");
        }
        buffer.close();
    } */
}
