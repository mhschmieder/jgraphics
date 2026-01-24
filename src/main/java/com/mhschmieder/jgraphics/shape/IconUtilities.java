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
package com.mhschmieder.jgraphics.shape;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

/**
 * {@code IconUtilities} is a utility class for Graphics2D vector based icons
 * that are common in most graphics applications, such as custom crosshairs.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public final class IconUtilities {

    /**
     * The default constructor is disabled, as this is a static utilities class.
     */
    private IconUtilities() {}

    /**
     * Returns a {@link Shape} representing a typical Crosshairs Icon.
     * <p>
     * This method creates a Crosshairs shape and translates it to the
     * provided coordinates.
     *
     * @param x
     *            x translation to apply to the Crosshairs graphic
     * @param y
     *            y translation to apply to the Crosshairs graphic
     * @param diameter
     *            The diameter of the crosshairs
     * @return A Crosshairs Icon as a {@link GeneralPath} composite graphic
     *
     * @since 1.0
     */
    public static Shape getCrosshairs( final double x, final double y, final double diameter ) {
        // Get a translational affine transformation matrix for translating from
        // entity space to model space.
        final AffineTransform affineTransform = AffineTransform.getTranslateInstance( x, y );

        // Return the crosshairs defined at the specified location.
        return getCrosshairs( affineTransform, diameter );
    }

    /**
     * Returns a {@link Shape} representing a typical Crosshairs Icon.
     * <p>
     * This method creates a Crosshairs shape and applies the provided
     * {@link AffineTransform} for translation, rotation, scale, and shear.
     *
     * @param affineTransform
     *            An {@link AffineTransform} to apply to the Crosshairs graphic
     * @param diameter
     *            The diameter of the crosshairs
     * @return A Crosshairs Icon as a {@link GeneralPath} composite graphic
     *
     * @since 1.0
     */
    public static Shape getCrosshairs( final AffineTransform affineTransform,
                                       final double diameter ) {
        final GeneralPath result = new GeneralPath();
        Shape currentShape;

        // Define a crosshairs graphic in entity space and immediately
        // translate each sub-path from entity space to model space.
        //
        // Since we are in model space, we need to think of the ellipse origin
        // as the lower left vs. upper left "corner" of the bounding box.
        final Ellipse2D ellipse = new Ellipse2D.Double( -0.5d * diameter,
                                                        -0.5d * diameter,
                                                        diameter,
                                                        diameter );
        currentShape = affineTransform.createTransformedShape( ellipse );
        result.append( currentShape, false );

        final double targetDimension = diameter * 1.5d;
        final Line2D targetX = new Line2D.Double( -0.5d * targetDimension,
                                                  0.0d,
                                                  0.5d * targetDimension,
                                                  0.0d );
        currentShape = affineTransform.createTransformedShape( targetX );
        result.append( currentShape, false );

        final Line2D targetY = new Line2D.Double( 0.0d,
                                                  -0.5d * targetDimension,
                                                  0.0d,
                                                  0.5d * targetDimension );
        currentShape = affineTransform.createTransformedShape( targetY );
        result.append( currentShape, false );

        return result;
    }

}