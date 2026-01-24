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
 * This file is part of the GraphicsToolkit Library
 *
 * You should have received a copy of the MIT License along with the
 * GraphicsToolkit Library. If not, see <https://opensource.org/licenses/MIT>.
 *
 * Project: https://github.com/mhschmieder/graphicstoolkit
 */
/**
 * This package contains classes that deal with graphics functionality specific
 * to geometry primitives.
 */
package com.mhschmieder.jgraphics.geometry;

import org.apache.commons.math3.util.FastMath;

import java.awt.Polygon;
import java.util.List;

public class GeometryUtilities {

    /**
     * The default constructor is disabled, as this is a static utilities class.
     */
    private GeometryUtilities() {
    }

    /**
     * Returns a Closed Irregular Polygon from an input list of coordinates
     * provided as a continuous pairing of (x, y) or (lat, lon) values.
     *
     * @param coordinates A list of paired (x, y) values (can be (lat, lon) )
     * @param isXMajor {@code true} if the first coordinate of each pair is the
     *                 x-coordinate; {@code false} if it is the y-coordinate
     *                 (typically the case when receiving lat/lon coordinates)
     * @return A Closed Irregular Polygon corresponding to input coordinates
     */
    public static Polygon makePolygon( final List< Double > coordinates,
                                       final boolean isXMajor ) {
        final int numberOfCoordinates = ( int ) FastMath.floor(
                0.5d * coordinates.size() );
        final int[] xpoints = new int[ numberOfCoordinates ];
        final int[] ypoints = new int[ numberOfCoordinates ];

        // NOTE: Polygons self-close in AWT; no need to repeat the first point.
        // NOTE: It is safer to round than to take floor or ceil, as we might
        //  otherwise end up with a degenerate shape that is 1D vs. 2D.
        int valueIndex = 0;
        for ( int i = 0; i < numberOfCoordinates; i++ ) {
            if ( isXMajor ) {
                xpoints[ i ] = ( int ) FastMath.round(
                        coordinates.get( valueIndex++ ) );
                ypoints[ i ] = ( int ) FastMath.round(
                        coordinates.get( valueIndex++ ) );
            }
            else {
                ypoints[ i ] = ( int ) FastMath.round(
                        coordinates.get( valueIndex++ ) );
                xpoints[ i ] = ( int ) FastMath.round(
                        coordinates.get( valueIndex++ ) );
            }
        }

        return new Polygon(
                xpoints, ypoints, numberOfCoordinates );
    }

    /**
     * Returns a Closed Irregular Polygon from an input list of coordinates
     * provided as a continuous pairing of (x, y) or (lat, lon) values.
     *
     * @param coordinates A list of paired (x, y) values (can be (lat, lon) )
     * @param isXMajor {@code true} if the first coordinate of each pair is the
     *                 x-coordinate; {@code false} if it is the y-coordinate
     *                 (typically the case when receiving lat/lon coordinates)
     * @return A Closed Irregular Polygon corresponding to input coordinates
     */
    public static Polygon2D makePolygon2D( final List< Double > coordinates,
                                           final boolean isXMajor ) {
        final int numberOfCoordinates = ( int ) FastMath.floor(
                0.5d * coordinates.size() );
        final float[] xpoints = new float[ numberOfCoordinates ];
        final float[] ypoints = new float[ numberOfCoordinates ];

        makePolygonVertices( xpoints, ypoints, coordinates, isXMajor );

        return new Polygon2D(
                xpoints, ypoints, numberOfCoordinates );
    }

    /**
     * Fills a Closed Irregular Polygon from an input list of coordinates
     * provided as a continuous pairing of (x, y) or (lat, lon) values.
     *
     * @param polygon The polygon whose vertices will be filled by this method
     * @param coordinates A list of paired (x, y) values (can be (lat, lon) )
     * @param isXMajor {@code true} if the first coordinate of each pair is the
     *                 x-coordinate; {@code false} if it is the y-coordinate
     *                 (typically the case when receiving lat/lon coordinates)
     */
    public static void updatePolygon2D( final Polygon2D polygon,
                                        final List< Double > coordinates,
                                        final boolean isXMajor ) {
        final int numberOfCoordinates = ( int ) FastMath.floor(
                0.5d * coordinates.size() );
        polygon.xpoints = new float[ numberOfCoordinates ];
        polygon.ypoints = new float[ numberOfCoordinates ];

        polygon.npoints = makePolygonVertices(
                polygon.xpoints, polygon.ypoints, coordinates, isXMajor );
    }

    /**
     * Returns the number of coordinates corresponding to the polygon vertices.
     * <p>
     * Fills a Closed Irregular Polygon from an input list of coordinates
     * provided as a continuous pairing of (x, y) or (lat, lon) values.
     *
     * @param xpoints The list of x-coordinates to fill from (x, y) pairs
     * @param ypoints The list of y-coordinates to fill from (x, y) pairs
     * @param coordinates A list of paired (x, y) values (can be (lat, lon) )
     * @param isXMajor {@code true} if the first coordinate of each pair is the
     *                 x-coordinate; {@code false} if it is the y-coordinate
     *                 (typically the case when receiving lat/lon coordinates)
     * @return The number of coordinates corresponding to the polygon vertices
     */
    public static int makePolygonVertices(
            final float[] xpoints,
            final float[] ypoints,
            final List< Double > coordinates,
            final boolean isXMajor ) {
        final int numberOfCoordinates = ( int ) FastMath.floor(
                0.5d * coordinates.size() );

        // NOTE: Polygons self-close in AWT; no need to repeat the first point.
        // NOTE: It is safer to round than to take floor or ceil, as we might
        //  otherwise end up with a degenerate shape that is 1D vs. 2D.
        int valueIndex = 0;
        for ( int i = 0; i < numberOfCoordinates; i++ ) {
            if ( isXMajor ) {
                xpoints[ i ] = coordinates.get( valueIndex++ ).floatValue();
                ypoints[ i ] = coordinates.get( valueIndex++ ).floatValue();
            }
            else {
                ypoints[ i ] = coordinates.get( valueIndex++ ).floatValue();
                xpoints[ i ] = coordinates.get( valueIndex++ ).floatValue();
            }
        }

        return numberOfCoordinates;
    }
}
