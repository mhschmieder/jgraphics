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
package com.mhschmieder.jgraphics.image;

import com.mhschmieder.jcommons.lang.EnumUtilities;
import com.mhschmieder.jcommons.lang.Indexed;
import com.mhschmieder.jcommons.lang.Labeled;

/**
 * Light source direction for applying shading to on-screen graphics.
 */
public enum LightSourceDirection implements Indexed< LightSourceDirection >,
        Labeled< LightSourceDirection > {
    /** No light source; no shading */
    NONE( 0, "None" ),
    /** Lighting from the northwest, or top left of screen */
    NORTHWEST( 1, "Light from Northwest" ),
    /** Lighting from the northeast, or top right of screen */
    NORTHEAST( 2, "Light from Northeast" ),
    /** Lighting from the southwest, or bottom left of screen */
    SOUTHWEST( 3, "Light from Southwest" ),
    /** Lighting from the southeast, or bottom right of screen */
    SOUTHEAST( 4, "Light from Southeast" );

    private final int index;
    private final String label;

    LightSourceDirection( final int pIndex,
                          final String pLabel ) {
        index = pIndex;
        label = pLabel;
    }

    @Override
    public int index() {
        return index;
    }

    @Override
    public LightSourceDirection valueOfIndex( final int pIndex ) {
        return ( LightSourceDirection ) EnumUtilities.getIndexedEnumFromIndex(
                pIndex, values() );
    }

    public String label() {
        return label;
    }

    @Override
    public LightSourceDirection valueOfLabel( final String text ) {
        return ( LightSourceDirection ) EnumUtilities.getLabeledEnumFromLabel(
                text, values() );
    }

    public static LightSourceDirection defaultValue() {
        return NONE;
    }
}