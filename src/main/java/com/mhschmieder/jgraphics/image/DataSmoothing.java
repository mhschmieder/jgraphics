/*
 * MIT License
 *
 * Copyright (c) 2024, 2026 Mark Schmieder. All rights reserved.
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
package com.mhschmieder.jgraphics.image;

import com.mhschmieder.jcommons.lang.EnumUtilities;
import com.mhschmieder.jcommons.lang.Indexed;
import com.mhschmieder.jcommons.lang.Labeled;

/**
 * An enumeration of data smoothing choices, relating. to interpolation, screen
 * rendering hints, and sometimes more obscure uses
 */
public enum DataSmoothing implements Indexed< DataSmoothing >,
        Labeled< DataSmoothing > {
    NONE( 3, "No Smoothing, 1:1 Image Scaling" ),
    MODEL_SPECIFIC(  0, "No Smoothing, 1:n Image Scaling" ),
    IMAGE_BILINEAR(  1, "Bilinear Image Interpolation" ),
    IMAGE_BICUBIC(  2, "Bicubic Image Interpolation" );

    private int index;
    private String label;

    DataSmoothing( final int pIndex,
                   final String pLabel ) {
        index = pIndex;
        label = pLabel;
    }

    @Override
    public int index() {
        return index;
    }

    @Override
    public DataSmoothing valueOfIndex( final int pIndex ) {
        return ( DataSmoothing ) EnumUtilities.getIndexedEnumFromIndex(
                pIndex, values() );
    }

    public String label() {
        return label;
    }

    @Override
    public DataSmoothing valueOfLabel( final String text ) {
        return ( DataSmoothing ) EnumUtilities.getLabeledEnumFromLabel(
                text, values() );
    }

    public static DataSmoothing defaultValue() {
        return NONE;
    }

    @Override
    public String toString() {
        // NOTE: This override takes care of displaying the current choice in
        //  its custom label form when a Combo Box is hosted by a Table Cell. It
        //  also addresses an issue with the Jackson parser if in a JSON file.
        return label();
    }
}
