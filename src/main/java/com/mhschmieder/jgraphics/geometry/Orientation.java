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
package com.mhschmieder.jgraphics.geometry;

import com.mhschmieder.jcommons.lang.Abbreviated;
import com.mhschmieder.jcommons.lang.EnumUtilities;
import com.mhschmieder.jcommons.lang.Labeled;

import java.util.Locale;

/**
 * <code>Orientation</code> is an enumeration for conventional orientation
 * values for graphical objects. For example, it may be relative to a plane
 * cutting through an object's geometric center or COG.
 * <p>
 * NOTE: Other than for string conversions, this is now redundant with JavaFX.
 */
public enum Orientation implements Labeled< Orientation >,
        Abbreviated< Orientation > {
    HORIZONTAL( "Horizontal", "hz" ),
    VERTICAL( "Vertical", "vt" );

    private final String label;
    private final String abbreviation;

    Orientation( final String pLabel,
                 final String pAbbreviation ) {
        label = pLabel;
        abbreviation = pAbbreviation;
    }

    @Override
    public String label() {
        return label;
    }

    @Override
    public Orientation valueOfLabel( final String text ) {
        return ( Orientation ) EnumUtilities.getLabeledEnumFromLabel(
                text, values() );
    }

    @Override
    public String abbreviation() {
        return abbreviation;
    }

    @Override
    public Orientation valueOfAbbreviation(
            final String abbreviatedText ) {
        return ( Orientation ) EnumUtilities
                .getAbbreviatedEnumFromAbbreviation(
                        abbreviatedText, values() );
    }

    @Override
    public String toString() {
        // NOTE: This override takes care of displaying the current choice in
        //  its custom label form when a Combo Box is hosted by a Table Cell. It
        //  also addresses an issue with the Jackson parser if in a JSON file.
        return label();
    }

    public static Orientation defaultValue() {
        return HORIZONTAL;
    }

    public static Orientation canonicalValueOf(
            final String canonicalOrientation ) {
        return ( canonicalOrientation != null )
            ? valueOf( canonicalOrientation.toUpperCase( Locale.ENGLISH ) )
            : defaultValue();
    }

    public final String toCanonicalString() {
        return toString().toLowerCase( Locale.ENGLISH );
    }
}
