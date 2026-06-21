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
package com.mhschmieder.jgraphics;

import org.apache.commons.math3.util.FastMath;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * {@code GraphicsUtilities} is a utility class for Graphics2D based methods,
 * usable in either the AWT or Swing GUI toolkits.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public final class GraphicsUtilities {

    /**
     * This method serves merely as a sanity check that the Maven integration
     * and builds work properly and also behave correctly inside Eclipse IDE. It
     * will likely get removed once I gain more confidence that I have solved
     * the well-known issues with Maven inside Eclipse as I move on to more
     * complex projects with dependencies (this project is quite simple and has
     * no dependencies at this time, until more functionality is added).
     *
     * @param args
     *            The command-line arguments for executing this class as the
     *            main entry point for an application
     *
     * @since 1.0
     */
    public static void main( final String[] args ) {
        System.out.println( "Hello Maven from GraphicsToolkit!" );
    }

    /**
     * The default constructor is disabled, as this is a static utilities class.
     */
    private GraphicsUtilities() {}

    /**
     * Returns a single-precision scale factor for mapping the source to the
     * destination without clipping.
     * <p>
     * This method calculates the source to destination scale factor that
     * prevents either the width or the height from clipping or distorting.
     * <p>
     * The source and destination are both unit agnostic, but the width and
     * height of each pair should be consistent in order to avoid distortion.
     * <p>
     * Note that this method is general enough to be completely
     * toolkit-agnostic regarding AWT, Swing, JavaFX, SWT, JFace, etc.
     * <p>
     * This version of the method is for when you have single-precision
     * dimensions to work with.
     *
     * @param sourceWidth
     *            The width of the source (unit-agnostic)
     * @param sourceHeight
     *            The height of the source (unit-agnostic)
     * @param destinationWidth
     *            The width of the destination (unit-agnostic)
     * @param destinationHeight
     *            The height of the destination (unit-agnostic)
     *
     * @return The single-precision scale factor for mapping the source to the
     *         destination without clipping or distorting (that is, stretching)
     *
     * @since 1.0
     */
    public static float calculateSourceToDestinationScaleFactor( final float sourceWidth,
                                                                 final float sourceHeight,
                                                                 final float destinationWidth,
                                                                 final float destinationHeight ) {
        // Find the smallest of the initial source to destination scale factors
        // for width and height and use it for both, to avoid clipping and
        // distorting (stretching) of either dimension.
        final float scaleWidth = destinationWidth / sourceWidth;
        final float scaleHeight = destinationHeight / sourceHeight;
        float scaleFactor = FastMath.min( scaleHeight, scaleWidth );

        // Calculate new dimensions based on the most inclusive scale factor.
        final float newWidth = sourceWidth * scaleFactor;
        final float newHeight = sourceHeight * scaleFactor;

        // Check if one of the two scaled dimensions exceeds the destination
        // dimensions, and if so, use that destination dimension to readjust the
        // scale factor so that we avoid any clipping.
        if ( newWidth > destinationWidth ) {
            scaleFactor *= ( destinationWidth / newWidth );
        }
        if ( newHeight > destinationHeight ) {
            scaleFactor *= ( destinationHeight / newHeight );
        }

        return scaleFactor;
    }

    /**
     * Returns a double-precision scale factor for mapping the source to the
     * destination without clipping.
     * <p>
     * This method calculates the source to destination scale factor that
     * prevents either the width or the height from clipping or distorting.
     * <p>
     * The source and destination are both unit agnostic, but the width and
     * height of each pair should be consistent in order to avoid distortion.
     * <p>
     * Note that this method is general enough to be completely
     * toolkit-agnostic regarding AWT, Swing, JavaFX, SWT, JFace, etc.
     * <p>
     * This version of the method is for when you have double-precision
     * dimensions to work with.
     *
     * @param sourceWidth
     *            The width of the source (unit-agnostic)
     * @param sourceHeight
     *            The height of the source (unit-agnostic)
     * @param destinationWidth
     *            The width of the destination (unit-agnostic)
     * @param destinationHeight
     *            The height of the destination (unit-agnostic)
     *
     * @return The double-precision scale factor for mapping the source to the
     *         destination without clipping or distorting (that is, stretching)
     *
     * @since 1.0
     */
    public static double calculateSourceToDestinationScaleFactor(
            final double sourceWidth,
            final double sourceHeight,
            final double destinationWidth,
            final double destinationHeight ) {
        // Find the smallest of the initial source to destination scale factors
        // for width and height and use it for both, to avoid clipping and
        // distorting (stretching) of either dimension.
        final double scaleWidth = destinationWidth / sourceWidth;
        final double scaleHeight = destinationHeight / sourceHeight;
        double scaleFactor = FastMath.min( scaleHeight, scaleWidth );

        // Calculate new dimensions based on the most inclusive scale factor.
        final double newWidth = sourceWidth * scaleFactor;
        final double newHeight = sourceHeight * scaleFactor;

        // Check if one of the two scaled dimensions exceeds the destination
        // dimensions, and if so, use that destination dimension to readjust the
        // scale factor so that we avoid any clipping.
        if ( newWidth > destinationWidth ) {
            scaleFactor *= ( destinationWidth / newWidth );
        }
        if ( newHeight > destinationHeight ) {
            scaleFactor *= ( destinationHeight / newHeight );
        }

        return scaleFactor;
    }

    /**
     * Applies (after pre-calculating) the source to destination transform to
     * the supplied {@link Graphics2D} Graphics Context.
     * <p>
     * The most common usage context for this function will be for Vector
     * Graphics Export functionality, where many of the common file formats are
     * paper-oriented and use different units (and/or origin) from screen
     * coordinates.
     * <p>
     * The transform is applied as a translation followed by a scaling, due to
     * the ultimate purpose of this method in the context of Vector Graphics
     * Export functionality. There is no need to support shearing or rotation in
     * this particular context.
     *
     * @param graphicsContext
     *            The {@link Graphics2D} Graphics Context to which the
     *            calculated transform should be applied
     * @param sourceMinX
     *            The lower left x-coordinate of the source bounds
     * @param sourceMinY
     *            The lower left y-coordinate of the source bounds
     * @param sourceMaxX
     *            The upper right x-coordinate of the source bounds
     * @param sourceMaxY
     *            The upper right y-coordinate of the source bounds
     * @param destinationWidth
     *            The width of the destination bounds/dimensions
     * @param destinationHeight
     *            The height of the destination bounds/dimensions
     *
     * @since 1.0
     */
    public static void applySourceToDestinationTransform( final Graphics2D graphicsContext,
                                                          final double sourceMinX,
                                                          final double sourceMinY,
                                                          final double sourceMaxX,
                                                          final double sourceMaxY,
                                                          final double destinationWidth,
                                                          final double destinationHeight ) {
        // Calculate the source dimensions from the original mix/max values.
        final double sourceWidth = FastMath.abs( sourceMaxX - sourceMinX );
        final double sourceHeight = FastMath.abs( sourceMaxY - sourceMinY );

        // Calculate the scale factor of the source layout to the destination,
        // in such a way that neither dimension is clipped nor distorted.
        final double scaleFactor = GraphicsUtilities
                .calculateSourceToDestinationScaleFactor( sourceWidth,
                                                          sourceHeight,
                                                          destinationWidth,
                                                          destinationHeight );

        // Note that any non-zero minimum (x, y) for the source bounds needs a
        // translation offset, so that the destination origin is effectively at
        // zero and the destination bounds do not go beyond destination width or
        // height. For instance, if minX = 65, we subtract 65 from all
        // coordinates for destination transform purposes; whereas if minX =
        // -65, we effectively add 65 to all coordinates during destination
        // transform. Similarly with the minY offset. Both must be scaled as we
        // apply the global translation offset before the global scale factor.
        final double upperLeftX = -( sourceMinX * scaleFactor );
        final double upperLeftY = -( sourceMinY * scaleFactor );

        // As the translation factor is specified in destination units, and the
        // scale factor is to be applied from source to destination, the
        // translation offsets must be written before the uniform scale factors.
        graphicsContext.translate( upperLeftX, upperLeftY );

        // Scale the source to fit the destination, sans aspect ratio
        // distortion (that is, with matching x-axis and y-axis scaling).
        graphicsContext.scale( scaleFactor, scaleFactor );
    }

    /**
     * Returns the inverse of the supplied {@link AffineTransform} matrix.
     *
     * @param matrix
     *            The Affine Transform to use as the basis of inversion
     *
     * @return An Affine Transform that is the inverse of the original, or
     *         {@code null} if the original matrix is null or singular
     *         (that is, degenerate or otherwise non-invertible)
     *
     * @since 1.0
     */
    public static AffineTransform getInverseMatrix( final AffineTransform matrix ) {
        if ( matrix == null ) {
            return null;
        }

        AffineTransform inverseMatrix = null;
        try {
            inverseMatrix = matrix.createInverse();
        }
        catch ( final NoninvertibleTransformException nte ) {
            // A non-invertible transform is one that is singular, degenerate,
            // and has a zero determinant. We return a {@code null} in this
            // case, just as for other error conditions, as there are too many
            // usage contexts where throwing a checked (or even unchecked) error
            // or exception can break API inheritance rules (such as the many
            // graphics methods in a {@link Graphics2D} derived class).
            return null;
        }

        return inverseMatrix;
    }

    /**
     * Returns the Device Configuration associated with the local Graphics
     * Environment, which is dependent on machine architecture and screen
     * configurations, unique per user.
     *
     * @return The first {@link GraphicsConfiguration} found in the Graphics
     *         Environment's list of Graphics Devices
     *
     * @since 1.0
     */
    public static GraphicsConfiguration getDeviceConfiguration() {
        final GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment
                .getLocalGraphicsEnvironment();

        final GraphicsDevice[] graphicsDevices = graphicsEnvironment.getScreenDevices();

        for ( final GraphicsDevice graphicsDevice : graphicsDevices ) {
            final GraphicsConfiguration[] graphicsConfigurations =
                                                                 graphicsDevice.getConfigurations();

            if ( graphicsConfigurations.length > 0 ) {
                final GraphicsConfiguration graphicsConfiguration = graphicsConfigurations[ 0 ];
                return graphicsConfiguration;
            }
        }

        return null;
    }

    /**
     * Returns a flag indicating whether the primary {@link GraphicsDevice} is a
     * retina display (if {@code true}) or not (if {@code false}).
     *
     * This method determines if the primary monitor is a retina display, as
     * determined by the OS and the user's settings. We do not care if the
     * secondary monitor is a retina display or not.
     * <p>
     * This method is primarily useful at application startup time, when most
     * visible windows are likely to be displayed on the primary monitor.
     *
     * @return {@code true} if the current primary {@link GraphicsDevice} is a
     *         retina display; {@code false} if it is not a retina display
     *
     * @since 1.0
     */
    @SuppressWarnings("nls")
    public static boolean isRetina() {
        boolean isRetina = false;
        try {
            // Get the Graphics Device associated with the default screen. This
            // is then taken as the primary monitor.
            final GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment
                    .getLocalGraphicsEnvironment();
            final GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();

            // We expect null if not running on macOS, 1 if the primary monitor
            // is a non-retina device, and 2 if it is a retina device.
            final Field field = graphicsDevice.getClass().getDeclaredField( "scale" );
            if ( field != null ) {
                field.setAccessible( true );
                final Object scale = field.get( graphicsDevice );
                if ( ( scale instanceof Integer ) && ( ( ( Integer ) scale ).intValue() == 2 ) ) {
                    isRetina = true;
                }
            }
        }
        catch ( final NullPointerException | SecurityException | IllegalAccessException
                | IllegalArgumentException | NoSuchFieldException | HeadlessException
                | ExceptionInInitializerError e ) {
            // We don't really care about errors and exceptions here, as this
            // method provides information that is usually optional.
            // e.printStackTrace();
        }

        return isRetina;
    }

    /**
     * Returns {@link RenderingHints} that are idealized for scientific charting
     * contexts where "accurate" matters more than "pretty".
     *
     * @return A {@link RenderingHints} instance that is set for rendering
     *         details that are specific to the needs of scientific charting
     *
     * @since 1.0
     */
    public static RenderingHints getRenderingHintsForCharting() {
        // Preserve scientific accuracy by turning anti-aliasing off and using
        // nearest neighbor interpolation. Also support fractional fonts for
        // better placed/spaced axes labels. Use pure stroke control for vector
        // graphics, as normalized stroke control can break up straight lines
        // and curves, partially defeating anti-aliasing.
        final Map< Key, Object > renderingHintMap = new HashMap<>( 9 );
        renderingHintMap.put( RenderingHints.KEY_ALPHA_INTERPOLATION,
                              RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY );
        renderingHintMap.put( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF );
        renderingHintMap.put( RenderingHints.KEY_COLOR_RENDERING,
                              RenderingHints.VALUE_COLOR_RENDER_QUALITY );
        renderingHintMap.put( RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE );
        renderingHintMap.put( RenderingHints.KEY_FRACTIONALMETRICS,
                              RenderingHints.VALUE_FRACTIONALMETRICS_ON );
        renderingHintMap.put( RenderingHints.KEY_INTERPOLATION,
                              RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR );
        renderingHintMap.put( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY );
        renderingHintMap.put( RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE );
        renderingHintMap.put( RenderingHints.KEY_TEXT_ANTIALIASING,
                              RenderingHints.VALUE_TEXT_ANTIALIAS_GASP );

        final RenderingHints renderingHints = new RenderingHints( renderingHintMap );

        return renderingHints;
    }

    /**
     * Returns {@link RenderingHints} that are idealized for typical printing
     * needs, where settings such as anti-aliasing generally aren't relevant.
     *
     * @return A {@link RenderingHints} instance that is set for rendering
     *         details that are specific to the typical needs of printing
     *
     * @since 1.0
     */
    public static RenderingHints getRenderingHintsForPrinting() {
        // As printing is done at the level of top-level GUI components, we must
        // take the worst case for each rendering hint as it is applied down the
        // GUI hierarchy.
        final Map< Key, Object > renderingHintMap = new HashMap<>( 9 );
        renderingHintMap.put( RenderingHints.KEY_ALPHA_INTERPOLATION,
                              RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY );
        renderingHintMap.put( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF );
        renderingHintMap.put( RenderingHints.KEY_COLOR_RENDERING,
                              RenderingHints.VALUE_COLOR_RENDER_QUALITY );
        renderingHintMap.put( RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE );
        renderingHintMap.put( RenderingHints.KEY_FRACTIONALMETRICS,
                              RenderingHints.VALUE_FRACTIONALMETRICS_OFF );
        renderingHintMap.put( RenderingHints.KEY_INTERPOLATION,
                              RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR );
        renderingHintMap.put( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY );
        renderingHintMap.put( RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE );
        renderingHintMap.put( RenderingHints.KEY_TEXT_ANTIALIASING,
                              RenderingHints.VALUE_TEXT_ANTIALIAS_ON );

        final RenderingHints renderingHints = new RenderingHints( renderingHintMap );

        return renderingHints;
    }

    /**
     * Returns {@link RenderingHints} that are idealized for AWT/Swing GUI
     * controls, where we typically want the sharpest rendering possible.
     *
     * @return A {@link RenderingHints} instance that is set for rendering
     *         details that maximize the legibility of AWT/Swing GUI controls
     *
     * @since 1.0
     */
    public static RenderingHints getRenderingHintsForControls() {
        // Make sure graphics, text, and images are anti-aliased and use the
        // highest rendering capabilities. The only exceptions are for font
        // metrics and stroke control. Fractional font metrics can cause odd
        // spacing between characters, making it harder to read labels.
        // Normalized stroke control can break up straight lines and curves,
        // partially defeating anti-aliasing, but is necessary at the GUI
        // level to avoid fuzzy edges of buttons and other controls.
        final Map< Key, Object > renderingHintMap = new HashMap<>( 9 );
        renderingHintMap.put( RenderingHints.KEY_ALPHA_INTERPOLATION,
                              RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY );
        renderingHintMap.put( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        renderingHintMap.put( RenderingHints.KEY_COLOR_RENDERING,
                              RenderingHints.VALUE_COLOR_RENDER_QUALITY );
        renderingHintMap.put( RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE );
        renderingHintMap.put( RenderingHints.KEY_FRACTIONALMETRICS,
                              RenderingHints.VALUE_FRACTIONALMETRICS_OFF );
        renderingHintMap.put( RenderingHints.KEY_INTERPOLATION,
                              RenderingHints.VALUE_INTERPOLATION_BICUBIC );
        renderingHintMap.put( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY );
        renderingHintMap.put( RenderingHints.KEY_STROKE_CONTROL,
                              RenderingHints.VALUE_STROKE_NORMALIZE );
        renderingHintMap.put( RenderingHints.KEY_TEXT_ANTIALIASING,
                              RenderingHints.VALUE_TEXT_ANTIALIAS_GASP );

        final RenderingHints renderingHints = new RenderingHints( renderingHintMap );

        return renderingHints;
    }
}
