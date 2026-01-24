/**
 * MIT License
 *
 * Copyright (c) 2020, 2026 Mark Schmieder. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * This file is part of the GraphicsToolkit Library
 *
 * You should have received a copy of the MIT License along with the
 * GraphicsToolkit Library. If not, see <https://opensource.org/licenses/MIT>.
 *
 * Project: https://github.com/mhschmieder/graphicstoolkit
 */
package com.mhschmieder.jgraphics.print;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.SimpleDoc;
import javax.print.StreamPrintService;
import javax.print.StreamPrintServiceFactory;
import javax.print.attribute.PrintRequestAttributeSet;
import java.awt.Component;
import java.io.OutputStream;

/**
 * {@code PostScriptUtilities} is a utility class for methods dealing with
 * standard PostScript output. Even though this writes PostScript (not
 * Encapsulated PostScript) to an Output Stream, the functionality is part of
 * the Print Services API, so this functionality belongs here rather than in
 * a specialized PostScript exporter module or package.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public final class PostScriptUtilities {

    /**
     * The default constructor is disabled, as this is a static utilities class.
     */
    private PostScriptUtilities() {}

    /**
     * Returns a flag indicating whether the {@link Component} was successfully
     * rendered to PostScript (if {@code true}) or not (if {@code false}), after
     * attempting a conversion to that vector graphics file format.
     * <p>
     * This method converts the provided {@link Component} to PostScript via
     * the AWT Printing API and then saves it to the supplied
     * {@link OutputStream}.
     *
     * @param component
     *            The {@link Component} to render to PostScript
     * @param outputStream
     *            The {@link OutputStream} where we will redirect the printer's
     *            PostScript conversion
     * @param printerAttributes
     *            The requested {@link PrintRequestAttributeSet} to apply to the
     *            PostScript rendering
     * @return {@code true} if the {@link Component} was successfully rendered
     *         to PostScript and saved to the {@link OutputStream}.
     *         {@code false} if it was not for any reason
     *
     * @since 1.0
     */
    public static boolean saveToPostScript( final Component component,
                                            final OutputStream outputStream,
                                            final PrintRequestAttributeSet printerAttributes ) {
        // Avoid throwing unnecessary exceptions by filtering for bad Output
        // Streams and/or target components.
        if ( ( outputStream == null ) || ( component == null ) ) {
            return false;
        }

        try {
            // Use the predefined flavor for a Printable from an Output Stream.
            final DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;

            // Specify the Mime Type of the Output Stream.
            final String mimeType = DocFlavor.BYTE_ARRAY.POSTSCRIPT.getMimeType();

            // Locate factories that can export a GIF image stream as
            // PostScript.
            final StreamPrintServiceFactory[] factories = StreamPrintServiceFactory
                    .lookupStreamPrintServiceFactories( flavor, mimeType );
            if ( factories.length <= 0 ) {
                return false;
            }

            // Create a stream printer service for PostScript.
            final StreamPrintService service = factories[ 0 ].getPrintService( outputStream );

            // Create the Print Job.
            final DocPrintJob docPrintJob = service.createPrintJob();

            // Wrap the supplied component in a Printable Component, for
            // Rendering Hints, etc.
            final PrintableComponent printableComponent = new PrintableComponent( component );

            // Prepare the Document for printing.
            final Doc doc = new SimpleDoc( printableComponent, flavor, null );

            // Monitor Print Job Events (this is safer than threading).
            final PrintJobWatcher printJobWatcher = new PrintJobWatcher( docPrintJob );

            // Print the Document using the potentially customized attributes.
            docPrintJob.print( doc, printerAttributes );

            // Wait for the Print Job to be done.
            //
            // Although export PostScript is slow, and we would prefer it to be
            // non-blocking, threading causes the application to hang.
            printJobWatcher.waitForDone();
        }
        catch ( final PrintException pe ) {
            pe.printStackTrace();
            return false;
        }

        return true;
    }

}
