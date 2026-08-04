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
package com.mhschmieder.jgraphics.color;

import org.apache.commons.math3.util.FastMath;

/**
 * This is a utility class for methods that generate color palettes used as
 * legends for charting applications. Specifically, physics-derived palettes.
 */
public final class PaletteUtilities {

    // Number of bits per pixel for raster images
    public static int BITS_PER_PIXEL_FOR_BILEVEL = 1;
    public static int BITS_PER_PIXEL_FOR_PALETTE = 8;
    public static int BITS_PER_PIXEL_FOR_GREYSCALE = 16;
    public static int BITS_PER_PIXEL_FOR_TRUE_COLOR = 24;

    // Set the minimum and maximum color intensity as an inclusive range.
    public static int PALETTE_COLORS_DEFAULT = ( int ) FastMath.pow( 2.0d,
                                                                     BITS_PER_PIXEL_FOR_PALETTE );
    public static int PALETTE_COLORS_MAXIMUM = PALETTE_COLORS_DEFAULT - 1;
    public static int PALETTE_COLORS_MINIMUM = 0;

    /**
     * The default constructor is disabled, as this is a static utilities
     * class.
     */
    private PaletteUtilities() {
    }

    // Generate a color palette based on the NCSA JET standard, using an integer
    // array representing an ARGB set, where alpha is zeroed for total opacity.
    // NOTE: JET is a variant of HSV, and is the color map used with the NCSA
    // fluid jet image. This method assumes top-down or left-to-write context.
    public static int[] generateJetPalette( final int numberOfJetPaletteColors ) {
        final int[] jetPalette = new int[ numberOfJetPaletteColors ];

        // We alter color intensity in increments/decrements of 4 (multiplied by
        // the max/actual color table size ratio), because half of the palette
        // uses either zero intensity or full intensity for a given color (red,
        // green or blue), and the other half spends half the time incrementing
        // and the other half of the time decrementing so must double the rate
        // to cover the entire intensity range.
        final double colorIncrement = ( 4.0d * PALETTE_COLORS_DEFAULT )
                                      / numberOfJetPaletteColors;

        // Set each grouping of blue values (increment from mid-intensity to
        // full intensity, full intensity, decrement from full intensity to zero
        // intensity, zero intensity).
        int colorValue = ( int ) (
                FastMath.round( 0.5d * PALETTE_COLORS_DEFAULT ) - 1 );
        int colorIndexMinimum = 0;
        int colorIndexMaximum =
                ( int ) FastMath.round( 0.125d * numberOfJetPaletteColors ) - 1;
        for ( int i = 0, colorIndex = colorIndexMinimum;
              colorIndex <= colorIndexMaximum;
              colorIndex++ ) {
            colorValue = FastMath.min( PALETTE_COLORS_MAXIMUM,
                                       ( int ) FastMath.round(
                                               ( 0.5d * PALETTE_COLORS_DEFAULT )
                                               + ( i * colorIncrement ) ) - 1 );
            jetPalette[ colorIndex ] = colorValue;
            i++;
        }

        colorValue = PALETTE_COLORS_MAXIMUM;
        colorIndexMinimum = colorIndexMaximum + 1;
        colorIndexMaximum = ( int ) FastMath.round(
                3.0d * 0.125d * numberOfJetPaletteColors ) - 1;
        for ( int colorIndex = colorIndexMinimum;
              colorIndex <= colorIndexMaximum;
              colorIndex++ ) {
            jetPalette[ colorIndex ] = colorValue;
        }

        colorValue = PALETTE_COLORS_MAXIMUM;
        colorIndexMinimum = colorIndexMaximum + 1;
        colorIndexMaximum = ( int ) FastMath.round(
                5.0d * 0.125d * numberOfJetPaletteColors ) - 1;
        for ( int i = 0, colorIndex = colorIndexMinimum;
              colorIndex <= colorIndexMaximum;
              colorIndex++ ) {
            colorValue = Math.clamp(
                    PALETTE_COLORS_MAXIMUM - ( int ) FastMath.round(
                            i * colorIncrement ),
                    PALETTE_COLORS_MINIMUM,
                    PALETTE_COLORS_MAXIMUM );
            jetPalette[ colorIndex ] = colorValue;
            i++;
        }

        colorValue = PALETTE_COLORS_MINIMUM;
        colorIndexMinimum = colorIndexMaximum + 1;
        colorIndexMaximum = numberOfJetPaletteColors - 1;
        for ( int colorIndex = colorIndexMinimum;
              colorIndex <= colorIndexMaximum;
              colorIndex++ ) {
            jetPalette[ colorIndex ] = colorValue;
        }

        // Set each grouping of green values (zero intensity, increment from
        // zero intensity to full intensity, full intensity, decrement from full
        // intensity to zero intensity, zero intensity).
        colorValue = PALETTE_COLORS_MINIMUM;
        colorIndexMinimum = 0;
        colorIndexMaximum =
                ( int ) FastMath.round( 0.125d * numberOfJetPaletteColors ) - 1;
        for ( int colorIndex = colorIndexMinimum;
              colorIndex <= colorIndexMaximum;
              colorIndex++ ) {
            jetPalette[ colorIndex ] += ( colorValue << 8 );
        }

        colorValue = PALETTE_COLORS_MINIMUM;
        colorIndexMinimum = colorIndexMaximum + 1;
        colorIndexMaximum = ( int ) FastMath.round(
                3.0d * 0.125d * numberOfJetPaletteColors ) - 1;
        for ( int i = 0, colorIndex = colorIndexMinimum;
              colorIndex <= colorIndexMaximum;
              colorIndex++ ) {
            colorValue = FastMath.min( PALETTE_COLORS_MAXIMUM,
                                       PALETTE_COLORS_MINIMUM
                                       + ( int ) FastMath.round(
                                               i * colorIncrement ) );
            jetPalette[ colorIndex ] += ( colorValue << 8 );
            i++;
        }

        colorValue = PALETTE_COLORS_MAXIMUM;
        colorIndexMinimum = colorIndexMaximum + 1;
        colorIndexMaximum = ( int ) FastMath.round(
                5.0d * 0.125d * numberOfJetPaletteColors ) - 1;
        for ( int colorIndex = colorIndexMinimum;
              colorIndex <= colorIndexMaximum;
              colorIndex++ ) {
            jetPalette[ colorIndex ] += ( colorValue << 8 );
        }

        colorIndexMinimum = colorIndexMaximum + 1;
        colorIndexMaximum = ( int ) FastMath.round(
                7.0d * 0.125d * numberOfJetPaletteColors ) - 1;
        for ( int i = 0, colorIndex = colorIndexMinimum;
              colorIndex <= colorIndexMaximum;
              colorIndex++ ) {
            colorValue = Math.clamp(
                    PALETTE_COLORS_MAXIMUM - ( int ) FastMath.round(
                            i * colorIncrement ),
                    PALETTE_COLORS_MINIMUM,
                    PALETTE_COLORS_MAXIMUM );
            jetPalette[ colorIndex ] += ( colorValue << 8 );
            i++;
        }

        colorValue = PALETTE_COLORS_MINIMUM;
        colorIndexMinimum = colorIndexMaximum + 1;
        colorIndexMaximum = numberOfJetPaletteColors - 1;
        for ( int colorIndex = colorIndexMinimum;
              colorIndex <= colorIndexMaximum;
              colorIndex++ ) {
            jetPalette[ colorIndex ] += ( colorValue << 8 );
        }

        // Set each grouping of red values (zero intensity, increment from zero
        // intensity to full intensity, full intensity, decrement from full
        // intensity to mid-intensity)
        colorValue = PALETTE_COLORS_MINIMUM;
        colorIndexMinimum = 0;
        colorIndexMaximum = ( int ) FastMath.round(
                3.0d * 0.125d * numberOfJetPaletteColors ) - 1;
        for ( int colorIndex = colorIndexMinimum;
              colorIndex <= colorIndexMaximum;
              colorIndex++ ) {
            jetPalette[ colorIndex ] += ( colorValue << 16 );
        }

        colorIndexMinimum = colorIndexMaximum + 1;
        colorIndexMaximum = ( int ) FastMath.round(
                5.0d * 0.125d * numberOfJetPaletteColors ) - 1;
        for ( int i = 0, colorIndex = colorIndexMinimum;
              colorIndex <= colorIndexMaximum;
              colorIndex++ ) {
            colorValue = Math.clamp(
                    PALETTE_COLORS_MINIMUM + ( int ) FastMath.round(
                            i * colorIncrement ),
                    PALETTE_COLORS_MINIMUM,
                    PALETTE_COLORS_MAXIMUM );
            jetPalette[ colorIndex ] += ( colorValue << 16 );
            i++;
        }

        colorValue = PALETTE_COLORS_MAXIMUM;
        colorIndexMinimum = colorIndexMaximum + 1;
        colorIndexMaximum = ( int ) FastMath.round(
                7.0d * 0.125d * numberOfJetPaletteColors ) - 1;
        for ( int colorIndex = colorIndexMinimum;
              colorIndex <= colorIndexMaximum;
              colorIndex++ ) {
            jetPalette[ colorIndex ] += ( colorValue << 16 );
        }

        colorIndexMinimum = colorIndexMaximum + 1;
        colorIndexMaximum = numberOfJetPaletteColors - 1;
        for ( int i = 0, colorIndex = colorIndexMinimum;
              colorIndex <= colorIndexMaximum;
              colorIndex++ ) {
            colorValue = Math.clamp(
                    PALETTE_COLORS_MAXIMUM - ( int ) FastMath.round(
                            i * colorIncrement ),
                    PALETTE_COLORS_MINIMUM,
                    PALETTE_COLORS_MAXIMUM );
            jetPalette[ colorIndex ] += ( colorValue << 16 );
            i++;
        }

        return jetPalette;
    }
}
