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
package com.mhschmieder.jgraphics.shape;

import java.util.Locale;

/**
 * {@code DrawMode} is an enumeration of available Draw Modes that are common to
 * many graphics contexts; especially for import and export of graphics files.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public enum DrawMode {
    /**
     * Stroke Mode says the next shape should draw its outline
     */
    STROKE,
    /**
     * Fill Mode says the next shape should fill its outline
     */
    FILL,
    /**
     * Clip Mode says the next shape should be used for clipping
     */
    CLIP;

    /**
     * Returns the default Draw Mode, which generally is only useful for
     * unexpected values that can't convert to a representational string due to
     * not being one of the listed or supported enumerations for this type. It
     * can also be used to initialize variables and avoid nulls.
     *
     * @return The safest default Draw Mode when the context is unknown
     *
     * @since 1.0
     */
    public static DrawMode defaultValue() {
        return STROKE;
    }

    /**
     * Returns the Draw Mode that corresponds to the string that is passed in,
     * after converting to all upper case so that the conversion is
     * case-agnostic and thus also version-independent (Java has changed its
     * auto-conversion policies for enum values to strings over the years;
     * especially regarding special characters such as underscores).
     *
     * @param drawModeCanonicalString
     *            The canonical string representation of a valid Draw Mode
     *
     * @return The Draw Mode that corresponds to the canonical string
     *
     * @since 1.0
     */
    public static DrawMode fromCanonicalString( final String drawModeCanonicalString ) {
        return ( drawModeCanonicalString != null )
            ? valueOf( drawModeCanonicalString.toUpperCase( Locale.ENGLISH ) )
            : defaultValue();
    }

    /**
     * Returns an all-lower-case string representation of the current Draw Mode,
     * suitable for writing directly as the active drawing command, for several
     * graphics file formats such as EPS and PostScript.
     *
     * @return The all-lower-case canonical string representation of the current
     *         Draw Mode, set to the English Locale as that's how the enum
     *         values themselves are defined
     *
     * @since 1.0
     */
    public final String toCanonicalString() {
        return toString().toLowerCase( Locale.ENGLISH );
    }

}
