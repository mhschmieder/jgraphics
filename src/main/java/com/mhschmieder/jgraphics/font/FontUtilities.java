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
package com.mhschmieder.jgraphics.font;

import com.mhschmieder.jgraphics.text.AttributedTextUtilities;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

/**
 * {@code FontUtilities} is a utility class for methods related to fonts, such
 * as getting Font Metrics when we don't already have a Graphics Context at hand
 * for measuring fonts.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public final class FontUtilities {

    /**
     * The default constructor is disabled, as this is a static utilities class.
     */
    private FontUtilities() {}

    /**
     * Declare a default drawing context to measure text for vectorization.
     * <p>
     * In this context, for non-screen output (e.g. targeting for output formats
     * used for graphics editing or printing), anti-aliasing is too expensive
     * and serves no purpose as we will only use this Font Rendering Context for
     * measuring fonts while vectorizing to TextLayout and/or GlyphVector, but
     * it has been proven and documented by many developers (in several major
     * Java libraries for various graphics output formats) that the fractional
     * metrics hints are required for accuracy (though not until Java 1.6).
     */
    public static final FontRenderContext FONT_RENDER_CONTEXT_FOR_VECTORIZATION
            = new FontRenderContext(
                    null,
            false,
            true );

    /**
     * Returns the {@link FontRenderContext} used for measuring text when
     * vectorizing via TextLayout or GlyphVector to preserve scalability.
     *
     * @return A default {@link FontRenderContext} to use for vectorization of
     *         text
     *
     * @since 1.0
     */
    public static FontRenderContext getFontRenderContextForVectorization() {
        return FONT_RENDER_CONTEXT_FOR_VECTORIZATION;
    }

    /**
     * Returns the {@link FontMetrics} for the specified {@link Font}.
     * <p>
     * This method computes the font metrics when we don't already have a
     * graphics context at hand for measuring fonts.
     *
     * @param fontToMeasure
     *            The {@link Font} to use for measuring {@link FontMetrics}
     * @return The {@link FontMetrics} associated with the results of analyzing
     *         the supplied {@link Font}
     *
     * @since 1.0
     */
    public static FontMetrics getFontMetrics( final Font fontToMeasure ) {
        final BufferedImage image = new BufferedImage( 1, 1, BufferedImage.TYPE_INT_RGB );
        final Graphics2D graphics = image.createGraphics();

        return graphics.getFontMetrics( fontToMeasure );
    }

    /**
     * Returns a {@link AttributedCharacterIterator} that is used for traversing
     * a {@link String} character by character, which is typical for most vector
     * graphics output formats.
     *
     * @param str
     *            The {@link String} to convert to an
     *            {@link AttributedCharacterIterator}
     * @param font
     *            The font to use for determining the text attributes to apply
     * @return The {@link AttributedCharacterIterator} for this {@link String}
     *         and {@link Font} combination
     *
     * @since 1.0
     */
    public static AttributedCharacterIterator getAttributeCharacterIterator(
            final String str,
            final Font font ) {
        final AttributedString attributedString = new AttributedString( str );
        AttributedTextUtilities.copyTextAttributes( font, attributedString, 0, str.length() );
        return attributedString.getIterator();
    }

    /**
     * Returns a {@link Font} whose size fits the provided criteria and ensures
     * that the entire string will display without clipping. It arose from the
     * specific needs of a polar chart display, making sure that its title and
     * tic labels don't clip, but can be used in other contexts.
     *
     * @param fontCandidate
     *            The {@link Font} to use as the initial candidate, and which
     *            defines the Font Family for sizing a good fit
     * @param maxFontSize
     *            The minimum Font Size allowed, in points
     * @param maxCharacterHeight
     *            The maximum allowed height (in pixels) of a single character
     * @param maxStringWidth
     *            The number of pixels available for the string to display
     *            without clipping in the horizontal dimension
     * @param longString
     *            The string to be displayed with the picked {@link Font}
     * @return The {@link Font} that best meets the input criteria
     *
     * @since 1.0
     */
    public static Font pickFont( final Font fontCandidate,
                                 final float maxFontSize,
                                 final int maxCharacterHeight,
                                 final int maxStringWidth,
                                 final String longString ) {
        Font fontToUse = fontCandidate;

        float fontSize = fontCandidate.getSize2D();
        while ( fontSize > maxFontSize ) {
            final FontMetrics fontMetrics = getFontMetrics( fontToUse );
            if ( ( fontMetrics.getHeight() <= maxCharacterHeight )
                    && ( fontMetrics.stringWidth( longString ) <= maxStringWidth ) ) {
                break;
            }

            fontSize -= 0.5f;
            fontToUse = fontToUse.deriveFont( fontSize );
        }

        return fontToUse;
    }

    /**
     * Returns the {@link FontMetrics} of the {@link Font} whose size fits the
     * provided criteria and ensures that the entire string will display without
     * clipping. It arose from the specific needs of a polar chart display,
     * making sure that its title and tic labels don't clip, but can be used in
     * other contexts.
     * <p>
     * This version of the Font Picker is to be used when you need the new font
     * to be set but only need to know its metrics for downstream code such as
     * resulting string width calculations; usually in the context of
     * calculating label placement (especially for chart axes).
     *
     * @param graphicsContext
     *            The {@link Graphics} Graphics Context to use for setting the
     *            resulting {@link Font} and measuring its {@link FontMetrics}
     * @param fontCandidate
     *            The {@link Font} to use as the initial candidate, and which
     *            defines the Font Family for sizing a good fit
     * @param maxFontSize
     *            The minimum Font Size allowed, in points
     * @param maxCharacterHeight
     *            The maximum allowed height (in pixels) of a single character
     * @param maxStringWidth
     *            The number of pixels available for the string to display
     *            without clipping in the horizontal dimension
     * @param longString
     *            The string to be displayed with the picked {@link Font}
     * @return The {@link Font} that best meets the input criteria
     *
     * @since 1.0
     */
    public static FontMetrics pickFont( final Graphics graphicsContext,
                                        final Font fontCandidate,
                                        final float maxFontSize,
                                        final int maxCharacterHeight,
                                        final int maxStringWidth,
                                        final String longString ) {
        // Find the best fitting font to display the supplied text while meeting
        // the additional criteria.
        final Font fontToUse = pickFont( fontCandidate,
                                         maxFontSize,
                                         maxCharacterHeight,
                                         maxStringWidth,
                                         longString );

        // Use this font for subsequent drawing on the Graphics Context.
        graphicsContext.setFont( fontToUse );

        // Measure the Font Metrics for the newly chosen Font.
        return graphicsContext.getFontMetrics( fontToUse );
    }
}
