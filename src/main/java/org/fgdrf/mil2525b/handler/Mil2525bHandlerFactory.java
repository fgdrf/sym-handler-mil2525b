package org.fgdrf.mil2525b.handler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.logging.Logger;

/**
 * Handler Factory for protocol "mil2525b". It is required if an {@link URL} Object will be generated using this protocol. The URLConnection An
 * {@link URLConnection} will not be returned because the protocol is only required for SLD Styles to delegate to the mole symbol generators (remote
 * or local with ArcGis engine)
 * 
 * @author Frank Gasdorf
 * 
 */
public class Mil2525bHandlerFactory implements URLStreamHandlerFactory {

    private static Logger LOGGER = Logger.getLogger("org.geotools.mil2525b");

    /**
     * mole protocol to resolve mole symbol codes to symbol images
     */
    public static final String PROTOCOL = "mil2525b"; //$NON-NLS-1$

    public URLStreamHandler createURLStreamHandler(final String protocol) {
        if (protocol.equalsIgnoreCase(PROTOCOL)) {
            return new URLStreamHandler() {
                protected URLConnection openConnection(final URL url) throws IOException {
                    return new URLConnection(url) {

                        public InputStream getInputStream() throws IOException {
                            String symbolId = getURL().getAuthority();

                            // concat string with "-" if the length is smaller than 15 chars
                            if (symbolId == null || symbolId.trim().length() == 0) {
                                throw new IOException("Unable to parse symbol id from URL");
                            }
                            StringBuffer buffer = new StringBuffer(symbolId.toLowerCase());

                            while (buffer.length() < 15) {
                                buffer.append("-");
                            }

                            URL imageURL = Mil2525bHandlerFactory.class.getResource("/"
                                    + buffer.toString() + ".png");

                            // here its possible to add labels and other stuff for affliction,
                            // size, parent and so on
                            if (imageURL != null) {
                                LOGGER.finest(imageURL.toString());
                                InputStream openStream = imageURL.openStream();
                                return openStream;
                            }

                            return super.getInputStream();
                        }

                        public String getContentType() {
                            return "image/png"; //$NON-NLS-1$
                        }

                        public void connect() throws IOException {
                            connected = true;
                        }
                    };
                }
            };
        }

        return null;
    }
}
