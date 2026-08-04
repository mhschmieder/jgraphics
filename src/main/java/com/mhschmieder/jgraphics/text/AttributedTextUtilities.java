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
package com.mhschmieder.jgraphics.text;

import java.awt.Font;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

/**
 * {@code AttributedTextUtilities} is a utility class for methods related to
 * text, such as copying Text Attributes for purposes of rendering text directly
 * vs. writing strings to output formats and being dependent on installed
 * fonts.
 *
 * @author Mark Schmieder
 * @version 1.0
 */
public final class AttributedTextUtilities {

    /**
     * The default constructor is disabled, as this is a static utilities
     * class.
     */
    private AttributedTextUtilities() {
    }

    /**
     * Copies Text Attributes from the supplied {@link Font} to the supplied
     * {@link AttributedString}.
     *
     * @param font             The {@link Font} to use for Text Attributes
     * @param attributedString The {@link AttributedString} whose Text
     *                         Attributes should be set
     * @param startIndex       The start index of the {@link String} that
     *                         determines the substring for which the Text
     *                         Attributes apply
     * @param endIndex         The end index of the {@link String} that
     *                         determines the substring for which the Text
     *                         Attributes apply
     * @since 1.0
     */
    public static void copyTextAttributes( final Font font,
                                           final AttributedString attributedString,
                                           final int startIndex,
                                           final int endIndex ) {
        if ( ( font == null ) || ( attributedString == null ) ) {
            return;
        }

        attributedString.addAttribute( TextAttribute.FAMILY,
                                       font.getFontName(),
                                       startIndex,
                                       endIndex );
        attributedString.addAttribute( TextAttribute.SIZE,
                                       Float.valueOf( font.getSize2D() ) );
        if ( font.isBold() ) {
            attributedString.addAttribute( TextAttribute.WEIGHT,
                                           TextAttribute.WEIGHT_BOLD,
                                           startIndex,
                                           endIndex );
        }
        if ( font.isItalic() ) {
            attributedString.addAttribute( TextAttribute.POSTURE,
                                           TextAttribute.POSTURE_OBLIQUE,
                                           startIndex,
                                           endIndex );
        }
    }

    /**
     * Returns a {@link Shape} that represents the vector graphics rendering of
     * the text, or {@code null} if any of the parameters are null This method
     * iterates over a paragraph of text and renders it as vector graphics using
     * a supplied font render context.
     *
     * @param iterator          The {@link AttributedCharacterIterator} whose
     *                          text is to be rendered
     * @param fontRenderContext The {@link FontRenderContext} for the supplied
     *                          text
     * @param x                 The x coordinate where the iterator's text is to
     *                          be rendered
     * @param y                 The y coordinate where the iterator's text is to
     *                          be rendered
     * @return The {@link Shape} that represents the vector graphics rendering
     *         of the text, or {@code null} if any of the parameters are null
     * @since 1.0
     */
    public static Shape getRenderedText( final AttributedCharacterIterator iterator,
                                         final FontRenderContext fontRenderContext,
                                         final float x,
                                         final float y ) {
        if ( ( iterator == null ) || ( fontRenderContext == null ) ) {
            return null;
        }

        final TextLayout layout = new TextLayout( iterator, fontRenderContext );
        final AffineTransform transform = AffineTransform.getTranslateInstance(
                x,
                y );
        final Shape shape = layout.getOutline( transform );
        return shape;
    }
}
