package org.fgdrf.mil2525b.handler;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * An additional protocoll-Handler that resolves image requests for mil2525b standard
 * 
 * @author Frank Gasdorf
 * 
 */
public class TestMil2525bHandlerFactory extends TestCase {

    static {
        // thats the way to register an other HandlerFactory
        URL.setURLStreamHandlerFactory(new Mil2525bHandlerFactory());
    }

    @Test
    public void testResolveMil2525bImage() throws Exception {
        BufferedImage bufferedImage = getImage("mil2525b://g-fpaa--------x");

        assertNotNull(bufferedImage);

        checkSize(bufferedImage, 30);
    }

    @Test
    public void testResolveMil2525bImageUpperCaseId() throws Exception {

        BufferedImage bufferedImage = getImage("mil2525b://G-FPAA--------X");

        assertNotNull(bufferedImage);

        checkSize(bufferedImage, 30);
    }

    @Test
    public void testResolveMil2525bImageShortCode() throws Exception {
        BufferedImage bufferedImage = getImage("mil2525b://ihupsre");

        assertNotNull(bufferedImage);

        checkSize(bufferedImage, 30);
    }

    @Test(expected = IOException.class)
    public void testMissingSymbolId() throws MalformedURLException, IOException {
        URL url = new URL("mil2525b://");
        URL url2 = new URL("mil2525b:");

        checkIOException(url);
        checkIOException(url2);
    }

    private void checkIOException(URL url) {
        InputStream stream = null;
        try {
            stream = url.openStream();
            fail("missing expected IOException");
        } catch (IOException e) {
            // that is expected behavior @Test(expected = IOException.class)
            // doesnt work
            return;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    // Hmm
                    return;
                }
            }
        }
    }

    private BufferedImage getImage(String url) throws Exception {
        // try to open url
        return ImageIO.read(new URL(url));
    }

    /**
     * check size, if the image is 30x30 pix
     * 
     * @param bufferedImage
     */
    private void checkSize(BufferedImage bufferedImage, int size) {
        assertEquals(bufferedImage.getWidth(), size);
        assertEquals(bufferedImage.getHeight(), size);
    }
}
