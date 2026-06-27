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

import com.mhschmieder.jmath.geometry.euclidean.LightSourceDirection;
import com.sun.glf.goodies.DirectionalLight;
import com.sun.glf.goodies.ElevationMap;
import com.sun.glf.goodies.LightOp;
import com.sun.glf.goodies.LitSurface;
import org.apache.commons.math3.util.FastMath;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public final class BumpMapUtilities {

    /**
     * The default constructor is disabled, as this is a static utilities class.
     */
    private BumpMapUtilities() {}

    public static void updateBumpMap(
            final WritableRaster writableRaster,
            final double pixelX,
            final double pixelY,
            final double renderedImageScale,
            final int[] iPixel,
            final boolean shadingActive,
            final byte[] bumpValue,
            final double value,
            final double minValue,
            final double maxValue,
            final WritableRaster writableRasterTexture ) {
        // This actually sets the pixel in the image to the proper color.
        // NOTE: We now floor vs. ceil (or add 0.5 to the numerator with integer
        //  truncation of the result), to avoid index out of bounds exceptions.
        // NOTE: Reverted to earlier approach as a safety measure, as there are
        //  so many hidden dependencies on the legacy rounding, but this won't
        //  be necessary once converted to JavaFX anyway as it is floating-point.
        final int scaledPixelX = ( int ) FastMath.floor(
                ( pixelX + 0.5d ) / renderedImageScale );
        final int scaledPixelY = ( int ) FastMath.floor(
                ( pixelY + 0.5d ) / renderedImageScale );
        writableRaster.setDataElements(
                scaledPixelX,
                scaledPixelY,
                iPixel );

        if ( shadingActive ) {
            bumpValue[ 0 ] = ( byte ) ( ( 255.0d * ( value - minValue ) )
                    / ( maxValue - minValue ) );
            writableRasterTexture.setDataElements(
                    scaledPixelX,
                    scaledPixelY,
                    bumpValue );
        }
    }

    public static void addBumpMapToImage(
            final boolean shadingActive,
            final BufferedImage renderedImageTexture,
            final BufferedImage renderedImage,
            final LightSourceDirection lightSourceDirection,
            final int heightScale ) {
        // Conditionally add the bump map to the image.
        if ( shadingActive ) {
            // Create the light in the upper left hand corner as the default.
            final double[] light = { -1.0d, -1.0d, 1.0d };

            // Adjust for the user's choice of light source direction.
            switch ( lightSourceDirection ) {
                case NONE:
                    // NOTE: This case should be unreachable in this code block.
                    break;
                case NORTHWEST:
                    // NOTE: Nothing to do as we are already initialized to NW.
                    break;
                case NORTHEAST:
                    light[ 0 ] = 1.0d;
                    break;
                case SOUTHWEST:
                    light[ 1 ] = 1.0d;
                    break;
                case SOUTHEAST:
                    light[ 0 ] = 1.0d;
                    light[ 1 ] = 1.0d;
                    break;
                default:
                    break;
            }

            // Create the texture map from the bump map.
            final ElevationMap texture = new ElevationMap(
                    renderedImageTexture,
                    true,
                    heightScale );

            final DirectionalLight sunlight = new DirectionalLight(
                    light,
                    1.0d,
                    Color.WHITE );

            final LitSurface litSurface = new LitSurface( 0.0d );
            // 1.0d,
            // LitSurfaceType.NORMAL,
            // texture);

            litSurface.setElevationMap( texture );

            litSurface.addLight( sunlight );
            final LightOp lightOp = new LightOp( litSurface );

            // Actually render the bump map onto the image.
            lightOp.filter( renderedImage, renderedImage );
        }
    }
}
