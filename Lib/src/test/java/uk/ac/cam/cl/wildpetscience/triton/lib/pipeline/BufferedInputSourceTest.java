package uk.ac.cam.cl.wildpetscience.triton.lib.pipeline;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import uk.ac.cam.cl.wildpetscience.triton.lib.image.Image;

import java.io.IOException;
import java.util.LinkedList;

import static org.mockito.Mockito.*;

/**
 * Tests for BufferedInputSource
 */
public class BufferedInputSourceTest {
    @Test
    public void testBufferedInputSourceCreation() {
        ImageInputSource inner = mock(ImageInputSource.class);
        BufferedInputSource buffer = new BufferedInputSource(inner);
    }

    @Test
    public void testBufferedInputSource() throws IOException {
        ImageInputSource inner = mock(ImageInputSource.class);
        BufferedInputSource buffer = new BufferedInputSource(inner);

        final LinkedList<Image> created = new LinkedList<Image>();

        when(inner.getNext()).thenAnswer(new Answer<Image>() {
            int count = 0;
            @Override
            public Image answer(InvocationOnMock invocation) throws Throwable {
                if (count++ < 300) {
                    Image img = new Image(null);
                    created.add(img);
                    return img;
                }
                return null;
            }
        });

        for (int i = 0; i < 300; i++) {
            Image i1 = buffer.getNext();
            Image i2 = created.remove(0);
            if (i1 != i2) {
                throw new RuntimeException("Unequal images found");
            }
        }
        if (buffer.getNext() != null) {
            throw new RuntimeException("Non-null image found");
        }
        buffer.close();
    }
}
