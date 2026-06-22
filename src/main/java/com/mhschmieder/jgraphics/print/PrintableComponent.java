/*
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
 * This file is part of the jgraphics Library
 *
 * You should have received a copy of the MIT License along with the jgraphics
 * Library. If not, see <https://opensource.org/licenses/MIT>.
 *
 * Project: https://github.com/mhschmieder/jgraphics
 */
package com.mhschmieder.jgraphics.print;

import com.mhschmieder.jgraphics.util.GraphicsUtilities;
import org.apache.commons.math3.util.FastMath;

import javax.swing.JComponent;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.print.PageFormat;
import java.awt.print.Printable;

/**
 * {@code PrintableComponent} is a convenient class for wrapping an AWT
 * {@link Component} or a Swing {@link JComponent} in behavior associated with
 * the {@link Printable} interface.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public class PrintableComponent implements Printable {

    /**
     * The original parent {@link Component} whose Graphics Context should be
     * redirected for printing.
     */
    private final Component      component;

    /**
     * Declare Rendering Hints to use for printing.
     */
    private final RenderingHints renderingHints;

    //////////////////////////// Constructors ////////////////////////////////

    /**
     * This is the default constructor, which wraps an existing
     * {@link Component} in {@link Printable} behavior.
     *
     * @param parentComponent
     *            The parent {@link Component} to wrap in {@link Printable}
     *            behavior
     *
     * @since 1.0
     */
    public PrintableComponent( final Component parentComponent ) {
        component = parentComponent;

        // Set Rendering Hints that aim for quality printing of geometry.
        renderingHints = GraphicsUtilities.getRenderingHintsForPrinting();
    }

    ///////////////////// Printable method overrides /////////////////////////

    /**
     * Returns an enumerated integer value indicating whether the page was
     * rendered successfully (PAGE_EXISTS) or if the Page Index specified a
     * non-existent page (NO_SUCH_PAGE).
     * <p>
     * This method tries to create a hard copy of the component on a printer.
     *
     * @param graphicsContext
     *            A Graphics Context object
     * @param pageFormat
     *            The {@link PageFormat} representing the size and orientation
     *            of the Page being drawn
     * @param pageIndex
     *            Page number (starting with 0, as an index) to print
     * @return PAGE_EXISTS if the page is rendered successfully or NO_SUCH_PAGE
     *         if pageIndex specifies a non-existent page.
     *
     * @since 1.0
     */
    @Override
    public final int print( final Graphics graphicsContext,
                            final PageFormat pageFormat,
                            final int pageIndex ) {
        // Only one page available, when printing individual components.
        if ( ( graphicsContext == null ) || ( pageFormat == null ) || ( pageIndex > 0 ) ) {
            return Printable.NO_SUCH_PAGE;
        }

        // Cast the graphics object so we can set Rendering Hints etc.
        final Graphics2D g2 = ( Graphics2D ) graphicsContext;

        // Set the default Rendering Hints for this Component.
        g2.addRenderingHints( renderingHints );

        // Translate the Printer Graphics Context for the imageable area of the
        // Paper object associated with this Page Format.
        g2.translate( pageFormat.getImageableX(), pageFormat.getImageableY() );

        // Make sure the entire component fits on the page, at maximum fit.
        final double pageWidth = pageFormat.getImageableWidth();
        final double pageHeight = pageFormat.getImageableHeight();
        final double scale = FastMath.min( ( pageWidth / component.getWidth() ),
                                       ( pageHeight / component.getHeight() ) );
        g2.scale( scale, scale );

        // Temporarily disable double-buffering to prevent printer sync
        // problems, then reset it after painting the component.
        final boolean wasDoubleBuffered = PrintUtilities.disableDoubleBuffering( component );
        component.paint( g2 );
        PrintUtilities.setDoubleBuffering( component, wasDoubleBuffered );

        return Printable.PAGE_EXISTS;
    }

}