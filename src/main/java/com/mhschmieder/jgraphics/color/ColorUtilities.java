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

import java.awt.Color;

/**
 * {@code ColorUtilities} is a utility class for methods related to color
 * handling, such as determining foreground from background, classifying light
 * vs. dark colors, and converting between color modes or numeric resolutions,
 * as well as methods dealing with raw pixels as integers, for color
 * manipulation in any Java-based graphics toolkit.
 *
 * @author Mark Schmieder
 * @version 1.0
 */
public final class ColorUtilities {

    /**
     * The default constructor is disabled, as this is a static utilities
     * class.
     */
    private ColorUtilities() {
    }

    /////////////// Color packers for combined ARGB integer values /////////////

    /**
     * Returns an integer representing a packed ARGB color that is opaque.
     *
     * @param red   The red component to merge into the packed color
     * @param green The green component to merge into the packed color
     * @param blue  The blue component to merge into the packed color
     * @return An integer representing a packed ARGB color
     */
    public static int makePackedColor( final int red,
                                       final int green,
                                       final int blue ) {
        // Make a packed ARGB color that has no alpha transparency.
        final int packedColor = makePackedColor( red, green, blue, 0 );
        return packedColor;
    }

    /**
     * Returns an integer representing a packed ARGB color.
     *
     * @param red   The red component to merge into the packed color
     * @param green The green component to merge into the packed color
     * @param blue  The blue component to merge into the packed color
     * @param alpha The alpha transparency to apply to the packed color
     * @return An integer representing a packed ARGB color
     */
    public static int makePackedColor( final int red,
                                       final int green,
                                       final int blue,
                                       final int alpha ) {
        final int packedColor = ( ( alpha & 0xff ) << 24 ) | ( ( red & 0xff )
                                                               << 16 ) | (
                                        ( green & 0xff ) << 8 ) | ( blue
                                                                    & 0xff );
        return packedColor;
    }

    /**
     * Returns the red component of a packed ARGB integer.
     *
     * @param packedColor The integer representing a packed ARGB color
     * @return The red component of the packed ARGB integer
     */
    public static int getRed( final int packedColor ) {
        return ( ( packedColor >> 16 ) & 0xff );
    }

    /**
     * Returns the green component of a packed ARGB integer.
     *
     * @param packedColor The integer representing a packed ARGB color
     * @return The green component of the packed ARGB integer
     */
    public static int getGreen( final int packedColor ) {
        return ( ( packedColor >> 8 ) & 0xff );
    }

    /**
     * Returns the blue component of a packed ARGB integer.
     *
     * @param packedColor The integer representing a packed ARGB color
     * @return The blue component of the packed ARGB integer
     */
    public static int getBlue( final int packedColor ) {
        return ( packedColor & 0xff );
    }

    /**
     * Returns the alpha transparency value of a packed ARGB integer.
     *
     * @param packedColor The integer representing a packed ARGB color
     * @return The alpha transparency value of the packed ARGB integer
     */
    public static int getAlpha( final int packedColor ) {
        return ( ( packedColor >> 24 ) & 0xff );
    }

    /////////////// Utility methods for combined ARGB integer values
    /////////////

    /**
     * Returns a pixel dimmed by the provided decimal percent value.
     * <p>
     * NOTE: To clarify, the provided percent is presumed to have already been
     * converted from the { 0, 100 } range to a decimal number between zero and
     * one that is ready as a multiplier.
     *
     * @param pixel             the original pixel to be dimmed
     * @param dimDecimalPercent the decimal percent of dimming to apply to the
     *                          provided pixel
     * @return a pixel dimmed by the provided decimal percent value
     */
    public static int dimPixel( final int pixel,
                                final double dimDecimalPercent ) {
        int r = ( pixel >> 16 ) & 0xff;
        int g = ( pixel >> 8 ) & 0xff;
        int b = ( pixel ) & 0xff;

        r = r + ( int ) FastMath.floor( ( 255 - r ) * dimDecimalPercent );
        g = g + ( int ) FastMath.floor( ( 255 - g ) * dimDecimalPercent );
        b = b + ( int ) FastMath.floor( ( 255 - b ) * dimDecimalPercent );

        return ( ( r & 0xff ) << 16 ) | ( ( g & 0xff ) << 8 ) | ( b & 0xff );
    }

    /////////////// Color converters for hexadecimal string output /////////////

    /**
     * Returns a hexadecimal string for an RGB color value that is first
     * converted to a bitmap value.
     * <p>
     * This method converts the 0-255 integer based RGB components of the
     * supplied color to a floating-point bitmap value of either 0.0 or 1.0,
     * depending on whether the grayscale analysis shows the original RGB color
     * to be dark or light. This also matches PostScript's behavior for Bitmap
     * conversion of RGB.
     * <p>
     * The initial grayscale conversion is done using the simple equal weighted
     * formula at the RapidTables website:
     * <p>
     * https://www.rapidtables.com/convert/image/rgb-to-grayscale.html
     * <p>
     * The grayscale analysis and final bitmap conversion is effectively just a
     * simple rounding above and below 0.5 for the average of the R, G, and B
     * components of the RGB color. Using unweighted grayscale analysis leads to
     * too many anomalies in scientific charting, but might work well for art.
     *
     * @param color The color to convert from RGB to a single bitmap value
     * @return The hexadecimal string for the bitmap value; either "00" or "ff"
     * @since 1.0
     */
    public static String rgbToBitmapHex( final Color color ) {
        // Convert the 0-255 integer based RGB components of the supplied color
        // to a floating-point bitmap value of either 0.0 or 1.0, which also
        // matches PostScript.
        final float bitmapValue = rgbToBitmap( color );

        // Convert this floating-point percentage to a two-character hexadecimal
        // string.
        final String bitmapHexValue = colorComponentToHexString( bitmapValue );

        return bitmapHexValue;
    }

    /**
     * Returns a hexadecimal string that always contains two characters that
     * represent a Base 16 version of a floating-point percentage between 0.0
     * and 1.0.
     *
     * @param colorComponentPercentage A floating-point percentage from 0.0 to
     *                                 1.0, to convert to Base16
     * @return A String representing a two-digit hexadecimal number from 0 to
     *         255
     * @since 1.0
     */
    private static String colorComponentToHexString( final float colorComponentPercentage ) {
        // Get the floating-point value back into integer space, from 0 to 255,
        // as that is what is needed for the hexadecimal conversion.
        final int colorComponentValue = FastMath.round(
                colorComponentPercentage * 255 );

        // Avoid overflow beyond the allowed integer range.
        final int colorComponentValueAdjusted = FastMath.min(
                colorComponentValue,
                255 );

        // Convert this integer to a two-character hexadecimal strings.
        final String colorComponentHexValue = colorComponentToHexString(
                colorComponentValueAdjusted );

        return colorComponentHexValue;
    }

    /**
     * Returns a hexadecimal string that always contains two characters that
     * represent a Base 16 version of a decimal integer between 0 and 255.
     *
     * @param colorComponentValue A regular decimal number from 0 to 255, to
     *                            convert to Base16
     * @return A String representing a two-digit hexadecimal number from 0 to
     *         255
     * @since 1.0
     */
    @SuppressWarnings( "nls" )
    private static String colorComponentToHexString( final int colorComponentValue ) {
        // Set any negative or out-of-bounds values to black.
        if ( ( colorComponentValue < 0 ) || ( colorComponentValue > 255 ) ) {
            return ColorConstants.BLACK_HEX;
        }

        // Detect Absolute Black as a trivial case.
        if ( colorComponentValue == 0 ) {
            return ColorConstants.BLACK_HEX;
        }

        // Detect Absolute White as a trivial case.
        if ( colorComponentValue == 255 ) {
            return ColorConstants.WHITE_HEX;
        }

        // Convert the color component value to hexadecimal (no leading zeroes).
        String colorComponentHexValue
                = Integer.toHexString( colorComponentValue );

        // Fail-safe in case of unknown quirks with the hexadecimal method.
        if ( ( colorComponentHexValue == null )
             || colorComponentHexValue.isEmpty() ) {
            return ColorConstants.BLACK_HEX;
        }

        // If the result is only one digit, pad with an extra "0" in front as
        // PostScript requires a consistent number of bits for each color in an
        // image block. Our images are 8 bits, which requires two characters.
        if ( colorComponentHexValue.length() == 1 ) {
            colorComponentHexValue = "0" + colorComponentHexValue;
        }

        return colorComponentHexValue;
    }

    /**
     * Returns a bitmap conversion of an RGB color value.
     * <p>
     * This method converts the 0-255 integer based RGB components of the
     * supplied color to a floating-point bitmap value of either 0.0 or 1.0,
     * depending on whether the grayscale analysis shows the original RGB color
     * to be dark or light. This also matches PostScript's behavior for Bitmap
     * conversion of RGB.
     * <p>
     * The initial grayscale conversion is done using the simple equal weighted
     * formula at the RapidTables website:
     * <p>
     * https://www.rapidtables.com/convert/image/rgb-to-grayscale.html
     * <p>
     * The grayscale analysis and final bitmap conversion is effectively just a
     * simple rounding above and below 0.5 for the average of the R, G, and B
     * components of the RGB color. Using unweighted grayscale analysis leads to
     * too many anomalies in scientific charting, but might work well for art.
     *
     * @param color The color to convert from RGB to a single bitmap value
     * @return The floating-point bitmap value; either 0.0 or 1.0
     * @since 1.0
     */
    public static float rgbToBitmap( final Color color ) {
        // Grab the raw AWT color components for R, G, and B (ignore Alpha),
        // using AWT's built-in floating-point RGB color converter, which can
        // take advantage of a pre-cached internal conversion from integers.
        final float[] rgb = color.getRGBColorComponents( null );
        final float rgbRed = rgb[ ColorConstants.RGB_RED_INDEX ];
        final float rgbGreen = rgb[ ColorConstants.RGB_GREEN_INDEX ];
        final float rgbBlue = rgb[ ColorConstants.RGB_BLUE_INDEX ];

        // Convert the floating-point based RGB values to a single
        // floating-point bitmap value from 0.0 to 1.0.
        final float bitmapValue = rgbToBitmap( rgbRed, rgbGreen, rgbBlue );

        return bitmapValue;
    }

    /**
     * Returns a bitmap conversion of an RGB color value.
     * <p>
     * This method converts floating-point based RGB values to a floating-point
     * bitmap value of either 0.0 or 1.0, depending on whether the grayscale
     * analysis shows the original RGB color to be dark or light. This also
     * matches PostScript's behavior for Bitmap conversion of RGB.
     * <p>
     * The initial grayscale conversion is done using the simple equal weighted
     * formula at the RapidTables website:
     * <p>
     * https://www.rapidtables.com/convert/image/rgb-to-grayscale.html
     * <p>
     * The grayscale analysis and final bitmap conversion is effectively just a
     * simple rounding above and below 0.5 for the average of the R, G, and B
     * components of the RGB color. Using unweighted grayscale analysis leads to
     * too many anomalies in scientific charting, but might work well for art.
     *
     * @param rgbRed   The Red component of the RGB color, from 0.0 to 1.0
     * @param rgbGreen The Green component of the RGB color, from 0.0 to 1.0
     * @param rgbBlue  The Blue component of the RGB color, from 0.0 to 1.0
     * @return The floating-point bitmap value; either 0.0 or 1.0
     * @since 1.0
     */
    public static float rgbToBitmap( final float rgbRed,
                                     final float rgbGreen,
                                     final float rgbBlue ) {
        // Convert the RGB color to a single bitmap value between 0 (black) and
        // 1 (white), with no fractional gray values in between.
        final float grayValue = ( rgbRed + rgbGreen + rgbBlue ) / 3f;
        final boolean colorDark = grayValue < 0.5;
        final float bitmapValue = colorDark
                                  ? 0f
                                  : 1f;

        return bitmapValue;
    }

    /**
     * Returns a hexadecimal string for an RGB color value that is first
     * converted to a grayscale value.
     * <p>
     * This method converts the 0-255 integer based RGB components of the
     * supplied color to a floating-point gray value from 0.0 to 1.0, which also
     * matches PostScript. This integer value is then trivially converted to its
     * two-character hexadecimal string value.
     * <p>
     * As equal-weighting in practice results in too many mid-grays that blur
     * together in a converted color image, we instead use the NTSC video
     * standard as described on p. 474 of the PostScript Language Reference
     * Manual Version 3: gray = 0.3 x red + 0.59 x green + 0.11 x blue.
     * <p>
     * https://www-cdf.fnal.gov/offline/PostScript/PLRM3.pdf
     * <p>
     * More accurate numbers are seen in Matlab's documentation of their own
     * implementation of this standard:
     * <p>
     * https://www.mathworks.com/help/matlab/ref/rgb2gray.html
     * <p>
     * From this, we use gray = 0.2989 x red + 0.587 x green + 0.114 x blue.
     * <p>
     * The Wiki article on Grayscale also recommends HDTV and HDR standards:
     * <p>
     * https://en.wikipedia.org/wiki/Grayscale
     * <p>
     * These were both tried and compared, and it was felt that the NTSC
     * standard used by Matlab is the best for scientific charts and images.
     *
     * @param color The color to convert from RGB to a single gray value
     * @return The hexadecimal string for the grayscale value, between "00" and
     *         "ff"
     * @since 1.0
     */
    public static String rgbToGrayHex( final Color color ) {
        // Convert the 0-255 integer based RGB components of the supplied color
        // to a floating-point gray value from 0.0 to 1.0, which also matches
        // PostScript.
        final float grayValue = rgbToGray( color );

        // Convert this floating-point percentage to a two-character hexadecimal
        // string.
        final String grayHexValue = colorComponentToHexString( grayValue );

        return grayHexValue;
    }

    /////////////// Color converters for standard numeric output ///////////////

    /**
     * Returns a grayscale conversion of an RGB color value.
     * <p>
     * This method converts the 0-255 integer based RGB components of the
     * supplied color to a floating-point gray value from 0.0 to 1.0, which also
     * matches PostScript.
     * <p>
     * As equal-weighting in practice results in too many mid-grays that blur
     * together in a converted color image, we instead use the NTSC video
     * standard as described on p. 474 of the PostScript Language Reference
     * Manual Version 3: gray = 0.3 x red + 0.59 x green + 0.11 x blue.
     * <p>
     * https://www-cdf.fnal.gov/offline/PostScript/PLRM3.pdf
     * <p>
     * More accurate numbers are seen in Matlab's documentation of their own
     * implementation of this standard:
     * <p>
     * https://www.mathworks.com/help/matlab/ref/rgb2gray.html
     * <p>
     * From this, we use gray = 0.2989 x red + 0.587 x green + 0.114 x blue.
     * <p>
     * The Wiki article on Grayscale also recommends HDTV and HDR standards:
     * <p>
     * https://en.wikipedia.org/wiki/Grayscale
     * <p>
     * These were both tried and compared, and it was felt that the NTSC
     * standard used by Matlab is the best for scientific charts and images.
     *
     * @param color The color to convert from RGB to a single gray value
     * @return The floating-point gray value, from 0.0 to 1.0
     * @since 1.0
     */
    public static float rgbToGray( final Color color ) {
        // Grab the raw AWT color components for R, G, and B (ignore Alpha),
        // using AWT's built-in floating-point RGB color converter, which can
        // take advantage of a pre-cached internal conversion from integers.
        final float[] rgb = color.getRGBColorComponents( null );
        final float rgbRed = rgb[ ColorConstants.RGB_RED_INDEX ];
        final float rgbGreen = rgb[ ColorConstants.RGB_GREEN_INDEX ];
        final float rgbBlue = rgb[ ColorConstants.RGB_BLUE_INDEX ];

        // Convert the floating-point based RGB values to a single
        // floating-point gray value from 0.0 to 1.0.
        final float grayValue = rgbToGray( rgbRed, rgbGreen, rgbBlue );

        return grayValue;
    }

    /**
     * Returns a grayscale conversion of an RGB color value.
     * <p>
     * This method converts floating-point based RGB values to a floating-point
     * gray value from 0.0 to 1.0, which also matches PostScript.
     * <p>
     * As equal-weighting in practice results in too many mid-grays that blur
     * together in a converted color image, we instead use the NTSC video
     * standard as described on p. 474 of the PostScript Language Reference
     * Manual Version 3: gray = 0.3 x red + 0.59 x green + 0.11 x blue.
     * <p>
     * https://www-cdf.fnal.gov/offline/PostScript/PLRM3.pdf
     * <p>
     * More accurate numbers are seen in Matlab's documentation of their own
     * implementation of this standard:
     * <p>
     * https://www.mathworks.com/help/matlab/ref/rgb2gray.html
     * <p>
     * From this, we use gray = 0.2989 x red + 0.587 x green + 0.114 x blue.
     * <p>
     * The Wiki article on Grayscale also recommends HDTV and HDR standards:
     * <p>
     * https://en.wikipedia.org/wiki/Grayscale
     * <p>
     * These were both tried and compared, and it was felt that the NTSC
     * standard used by Matlab is the best for scientific charts and images.
     *
     * @param rgbRed   The Red component of the RGB color, from 0.0 to 1.0
     * @param rgbGreen The Green component of the RGB color, from 0.0 to 1.0
     * @param rgbBlue  The Blue component of the RGB color, from 0.0 to 1.0
     * @return The floating-point gray value, from 0.0 to 1.0
     * @since 1.0
     */
    public static float rgbToGray( final float rgbRed,
                                   final float rgbGreen,
                                   final float rgbBlue ) {
        // Compute the Matlab-compatible gray value using the NTSC formula.
        final float grayValue = ( 0.2989f * rgbRed ) + ( 0.587f * rgbGreen ) + (
                0.114f * rgbBlue );

        // Avoid overflow beyond the allowed floating-point range.
        return FastMath.min( grayValue, 1.0f );
    }

    /**
     * Returns a hexadecimal string for an RGB color value.
     * <p>
     * This method converts the supplied color's R, G, and B components to
     * individual two-character hexadecimal string values.
     *
     * @param color The color to use as reference for hexadecimal conversion
     * @return The hexadecimal strings for the color component values, between
     *         "00" and "ff"
     * @since 1.0
     */
    public static String[] rgbToRgbHex( final Color color ) {
        // Grab the raw AWT color components for R, G, and B (ignore Alpha).
        final int awtRed = color.getRed();
        final int awtGreen = color.getGreen();
        final int awtBlue = color.getBlue();

        // Convert these integers to two-character hexadecimal strings.
        final String redHexValue = colorComponentToHexString( awtRed );
        final String greenHexValue = colorComponentToHexString( awtGreen );
        final String blueHexValue = colorComponentToHexString( awtBlue );

        final String[] rgbHexValues
                = new String[ ColorConstants.NUMBER_OF_RGB_COMPONENTS ];
        rgbHexValues[ ColorConstants.RGB_RED_INDEX ] = redHexValue;
        rgbHexValues[ ColorConstants.RGB_GREEN_INDEX ] = greenHexValue;
        rgbHexValues[ ColorConstants.RGB_BLUE_INDEX ] = blueHexValue;

        return rgbHexValues;
    }

    /**
     * Returns a hexadecimal string for an RGB color value that is first
     * converted to a CMYK value.
     * <p>
     * This method converts the supplied color's C, M, Y, and K components to
     * individual two-character hexadecimal string values.
     *
     * @param color The color to use as reference for hexadecimal conversion
     * @return The hexadecimal strings for the color component values, between
     *         "00" and "ff"
     * @since 1.0
     */
    public static String[] rgbToCmykHex( final Color color ) {
        // Convert the 0-255 integer based RGB components of the supplied color
        // to a floating-point CMYK basis from 0.0 to 1.0, which also matches
        // PostScript. Special-case for Absolute Black and Absolute White, to
        // avoid masking with almost-black and almost-white.
        final float[] cmykValues = rgbToCmyk( color );
        final float cyanValue = cmykValues[ ColorConstants.CMYK_CYAN_INDEX ];
        final float magentaValue
                = cmykValues[ ColorConstants.CMYK_MAGENTA_INDEX ];
        final float yellowValue
                = cmykValues[ ColorConstants.CMYK_YELLOW_INDEX ];
        final float blackValue = cmykValues[ ColorConstants.CMYK_BLACK_INDEX ];

        // Convert these floating-point percentages to two-character hexadecimal
        // strings.
        final String cyanHexValue = colorComponentToHexString( cyanValue );
        final String magentaHexValue
                = colorComponentToHexString( magentaValue );
        final String yellowHexValue = colorComponentToHexString( yellowValue );
        final String blackHexValue = colorComponentToHexString( blackValue );

        final String[] cmykHexValues
                = new String[ ColorConstants.NUMBER_OF_CMYK_COMPONENTS ];
        cmykHexValues[ ColorConstants.CMYK_CYAN_INDEX ] = cyanHexValue;
        cmykHexValues[ ColorConstants.CMYK_MAGENTA_INDEX ] = magentaHexValue;
        cmykHexValues[ ColorConstants.CMYK_YELLOW_INDEX ] = yellowHexValue;
        cmykHexValues[ ColorConstants.CMYK_BLACK_INDEX ] = blackHexValue;

        return cmykHexValues;
    }

    /**
     * Returns a CMYK conversion of an RGB color value.
     * <p>
     * This method converts the 0-255 integer based RGB components of the
     * supplied color to a floating-point CMYK basis from 0.0 to 1.0, which also
     * matches PostScript.
     * <p>
     * The conversion is done using the formulae at the RapidTables website:
     * <p>
     * https://www.rapidtables.com/convert/color/rgb-to-cmyk.html
     * <p>
     * We check first for Absolute Black and Absolute White, to avoid masking
     * with almost-black and almost-white.
     *
     * @param color The color to convert from RGB to CMYK
     * @return The array of floating-point CMYK values, from 0.0 to 1.0
     * @since 1.0
     */
    public static float[] rgbToCmyk( final Color color ) {
        // Guarantee Absolute Black in CMYK space.
        if ( Color.BLACK.equals( color ) ) {
            final float[] cmykValues
                    = new float[ ColorConstants.NUMBER_OF_CMYK_COMPONENTS ];
            cmykValues[ ColorConstants.CMYK_CYAN_INDEX ] = 0f;
            cmykValues[ ColorConstants.CMYK_MAGENTA_INDEX ] = 0f;
            cmykValues[ ColorConstants.CMYK_YELLOW_INDEX ] = 0f;
            cmykValues[ ColorConstants.CMYK_BLACK_INDEX ] = 1f;

            return cmykValues;
        }

        // Guarantee Absolute White in CMYK space.
        if ( Color.WHITE.equals( color ) ) {
            final float[] cmykValues
                    = new float[ ColorConstants.NUMBER_OF_CMYK_COMPONENTS ];
            cmykValues[ ColorConstants.CMYK_CYAN_INDEX ] = 0f;
            cmykValues[ ColorConstants.CMYK_MAGENTA_INDEX ] = 0f;
            cmykValues[ ColorConstants.CMYK_YELLOW_INDEX ] = 0f;
            cmykValues[ ColorConstants.CMYK_BLACK_INDEX ] = 0f;

            return cmykValues;
        }

        // Grab the raw AWT color components for R, G, and B (ignore Alpha),
        // using AWT's built-in floating-point RGB color converter, which can
        // take advantage of a pre-cached internal conversion from integers.
        final float[] rgb = color.getRGBColorComponents( null );
        final float rgbRed = rgb[ ColorConstants.RGB_RED_INDEX ];
        final float rgbGreen = rgb[ ColorConstants.RGB_GREEN_INDEX ];
        final float rgbBlue = rgb[ ColorConstants.RGB_BLUE_INDEX ];

        // Convert the floating-point based RGB values to a floating-point CMYK
        // basis from 0.0 to 1.0, which also matches PostScript.
        final float[] cmykValues = rgbToCmyk( rgbRed, rgbGreen, rgbBlue );

        return cmykValues;
    }

    /**
     * Returns a CMYK conversion of an RGB color value.
     * <p>
     * This method converts floating-point based RGB values to a floating-point
     * CMYK basis from 0.0 to 1.0, which also matches PostScript.
     * <p>
     * The conversion is done using the formulae at the RapidTables website:
     * <p>
     * https://www.rapidtables.com/convert/color/rgb-to-cmyk.html
     *
     * @param rgbRed   The Red component of the RGB color, from 0.0 to 1.0
     * @param rgbGreen The Green component of the RGB color, from 0.0 to 1.0
     * @param rgbBlue  The Blue component of the RGB color, from 0.0 to 1.0
     * @return The array of floating-point CMYK values, from 0.0 to 1.0
     * @since 1.0
     */
    public static float[] rgbToCmyk( final float rgbRed,
                                     final float rgbGreen,
                                     final float rgbBlue ) {
        // This is the standard formula for computing the CMYK Black component
        // from the maximum value of the RGB color's individual color channels.
        final float cmykBlack = 1f - FastMath.max( FastMath.max( rgbRed,
                                                                 rgbGreen ),
                                                   rgbBlue );

        // Prevent divide-by-zero, as well as negative CMYK values.
        final float denominator = FastMath.max( Float.MIN_NORMAL,
                                                1f - cmykBlack );

        final float cmykCyan = 1f - ( rgbRed / denominator );
        final float cmykMagenta = 1f - ( rgbGreen / denominator );
        final float cmykYellow = 1f - ( rgbBlue / denominator );

        final float[] cmykValues
                = new float[ ColorConstants.NUMBER_OF_CMYK_COMPONENTS ];
        cmykValues[ ColorConstants.CMYK_CYAN_INDEX ] = cmykCyan;
        cmykValues[ ColorConstants.CMYK_MAGENTA_INDEX ] = cmykMagenta;
        cmykValues[ ColorConstants.CMYK_YELLOW_INDEX ] = cmykYellow;
        cmykValues[ ColorConstants.CMYK_BLACK_INDEX ] = cmykBlack;

        return cmykValues;
    }

    /**
     * Returns a bitmap conversion of an RGB color value.
     * <p>
     * This method converts the supplied 0-255 integer based RGB values to a
     * floating-point bitmap value of either 0.0 or 1.0, depending on whether
     * the grayscale analysis shows the original RGB color to be dark or light.
     * This also matches PostScript's behavior for Bitmap conversion of RGB.
     * <p>
     * The initial grayscale conversion is done using the simple equal weighted
     * formula at the RapidTables website:
     * <p>
     * https://www.rapidtables.com/convert/image/rgb-to-grayscale.html
     * <p>
     * The grayscale analysis and final bitmap conversion is effectively just a
     * simple rounding above and below 0.5 for the average of the R, G, and B
     * components of the RGB color. Using unweighted grayscale analysis leads to
     * too many anomalies in scientific charting, but might work well for art.
     *
     * @param awtRed   The Red component of the RGB color, from 0 to 255
     * @param awtGreen The Green component of the RGB color, from 0 to 255
     * @param awtBlue  The Blue component of the RGB color, from 0 to 255
     * @return The floating-point bitmap value; either 0.0 or 1.0
     * @since 1.0
     */
    public static float rgbToBitmap( final int awtRed,
                                     final int awtGreen,
                                     final int awtBlue ) {
        // Convert AWT's 0-255 integer values to a floating-point basis (between
        // 0.0 or 1.0).
        final float rgbRed = awtRed / 255f;
        final float rgbGreen = awtGreen / 255f;
        final float rgbBlue = awtBlue / 255f;

        // Convert the floating-point based RGB values to a single
        // floating-point bitmap value from 0.0 to 1.0.
        final float bitmapValue = rgbToBitmap( rgbRed, rgbGreen, rgbBlue );

        return bitmapValue;
    }

    /**
     * Returns a grayscale conversion of an RGB color value.
     * <p>
     * This method converts the supplied 0-255 integer based RGB values to a
     * floating-point gray value from 0.0 to 1.0, which also matches
     * PostScript.
     * <p>
     * As equal-weighting in practice results in too many mid-grays that blur
     * together in a converted color image, we instead use the NTSC video
     * standard as described on p. 474 of the PostScript Language Reference
     * Manual Version 3: gray = 0.3 x red + 0.59 x green + 0.11 x blue.
     * <p>
     * https://www-cdf.fnal.gov/offline/PostScript/PLRM3.pdf
     * <p>
     * More accurate numbers are seen in Matlab's documentation of their own
     * implementation of this standard:
     * <p>
     * https://www.mathworks.com/help/matlab/ref/rgb2gray.html
     * <p>
     * From this, we use gray = 0.2989 x red + 0.587 x green + 0.114 x blue.
     * <p>
     * The Wiki article on Grayscale also recommends HDTV and HDR standards:
     * <p>
     * https://en.wikipedia.org/wiki/Grayscale
     * <p>
     * These were both tried and compared, and it was felt that the NTSC
     * standard used by Matlab is the best for scientific charts and images.
     *
     * @param awtRed   The Red component of the RGB color, from 0 to 255
     * @param awtGreen The Green component of the RGB color, from 0 to 255
     * @param awtBlue  The Blue component of the RGB color, from 0 to 255
     * @return The floating-point gray value, from 0.0 to 1.0
     * @since 1.0
     */
    public static float rgbToGray( final int awtRed,
                                   final int awtGreen,
                                   final int awtBlue ) {
        // Convert AWT's 0-255 integer values to a floating-point basis for
        // colors (between 0.0 or 1.0).
        final float rgbRed = awtRed / 255f;
        final float rgbGreen = awtGreen / 255f;
        final float rgbBlue = awtBlue / 255f;

        // Convert the floating-point based RGB values to a single
        // floating-point gray value from 0.0 to 1.0.
        final float grayValue = rgbToGray( rgbRed, rgbGreen, rgbBlue );

        return grayValue;
    }

    /**
     * Returns a CMYK conversion of an RGB color value.
     * <p>
     * This method converts the supplied 0-255 integer based RGB values to a
     * floating-point CMYK basis from 0.0 to 1.0, which also matches
     * PostScript.
     * <p>
     * The conversion is done using the formulae at the RapidTables website:
     * <p>
     * https://www.rapidtables.com/convert/color/rgb-to-cmyk.html
     *
     * @param awtRed   The Red component of the RGB color, from 0 to 255
     * @param awtGreen The Green component of the RGB color, from 0 to 255
     * @param awtBlue  The Blue component of the RGB color, from 0 to 255
     * @return The array of floating-point CMYK values, from 0.0 to 1.0
     * @since 1.0
     */
    public static float[] rgbToCmyk( final int awtRed,
                                     final int awtGreen,
                                     final int awtBlue ) {
        // Convert AWT's 0-255 integer based values to a floating-point basis
        // for colors (between 0.0 or 1.0).
        final float rgbRed = awtRed / 255f;
        final float rgbGreen = awtGreen / 255f;
        final float rgbBlue = awtBlue / 255f;

        // Convert the floating-point based RGB values to a floating-point CMYK
        // basis from 0.0 to 1.0.
        final float[] cmykValues = rgbToCmyk( rgbRed, rgbGreen, rgbBlue );

        return cmykValues;
    }

    /**
     * Returns a {@link Color} intended to be used as a non-masking foreground
     * color against the supplied background color, based on brightness
     * analysis. If the background is dark, use White. If it is light, use
     * Black. this method is oriented towards {@code Graphics} canvas drawing.
     * <p>
     * This method determines whether to use White or Black as the foreground
     * color in any AWT context, to guarantee contrast and avoid masking.
     * <p>
     * There are more sophisticated versions of this method that supply shades
     * of gray, to also avoid masking of grid lines and other decorator graphics
     * most commonly associated with charts, but that code has not yet been
     * refactored to make sense outside the context of specific applications.
     *
     * @param backColor The background color to use as reference for the
     *                  foreground color
     * @return The foreground color to use for any AWT-based color context
     * @since 1.0
     */
    public static Color getForegroundFromBackground( final Color backColor ) {
        final boolean backColorDark = isColorDark( backColor );
        final Color foreColor = backColorDark
                                ? Color.WHITE
                                : Color.BLACK;

        return foreColor;
    }

    ///////////// Utility methods for context-based color contrast ///////////

    /**
     * Returns a flag for whether the supplied color should be considered dark
     * (if {@code true}) or light (if {@code false}).
     * <p>
     * This method converts the 0-255 integer based RGB components of the
     * supplied color to a floating-point HSB basis from 0.0 to 1.0, and then
     * establishes that the color is dark if its brightness component is less
     * than 0.51 (51%), and light otherwise. This has been shown to be far more
     * successful in avoiding mid-grays from being masked, than comparing the
     * average of the RGB values against 50%, when this method is used to
     * determine the best Foreground Color choice (text, graphics) for a given
     * Background Color.
     *
     * @param color The RGB color to use as reference for dark vs. light
     *              analysis
     * @return {@code true} if the supplied color should be considered dark;
     *         {@code false} if it should be considered light
     * @since 1.0
     */
    public static boolean isColorDark( final Color color ) {
        // Analyze the RGB color for dark vs. light criteria.
        final boolean colorDark = isColorDark( color, 0.51f );

        return colorDark;
    }

    /**
     * Returns a flag for whether the supplied color should be considered dark
     * (if {@code true}) or light (if {@code false}), based on a cutoff
     * percent.
     * <p>
     * This method converts the 0-255 integer based RGB components of the
     * supplied color to a floating-point HSB basis from 0.0 to 1.0, and then
     * establishes that the color is dark if its brightness component is less
     * than the provided cutoff percent, and light otherwise. This has been
     * shown to be far more successful in avoiding mid-grays from being masked,
     * than comparing the average of the RGB values against a cutoff value
     * between 0.0 and 1.0, when this method is used to determine the best
     * Foreground Color choice (text, graphics) for a given Background Color.
     *
     * @param color                   The RGB color to use as reference for dark
     *                                vs. light analysis
     * @param brightnessCutoffPercent The percentile for the Brightness level of
     *                                the Hue Analysis, to use as the dark vs.
     *                                light cutoff criterion
     * @return {@code true} if the supplied color should be considered dark;
     *         {@code false} if it should be considered light
     * @since 1.0
     */
    public static boolean isColorDark( final Color color,
                                       final float brightnessCutoffPercent ) {
        // Grab the raw AWT color components for R, G, and B (ignore Alpha).
        final int awtRed = color.getRed();
        final int awtGreen = color.getGreen();
        final int awtBlue = color.getBlue();

        // Analyze the RGB color for dark vs. light criteria.
        final boolean colorDark = isColorDark( awtRed,
                                               awtGreen,
                                               awtBlue,
                                               brightnessCutoffPercent );

        return colorDark;
    }

    /**
     * Returns a flag for whether the supplied color should be considered dark
     * (if {@code true}) or light (if {@code false}).
     * <p>
     * This method converts the supplied 0-255 integer based RGB values to a
     * floating-point HSB basis from 0.0 to 1.0, and then establishes that the
     * color is dark if its brightness component is less than the provided
     * cutoff percent, and light otherwise. This has been shown to be far more
     * successful in avoiding mid-grays from being masked, than comparing the
     * average of the RGB values against a cutoff value between 0.0 and 1.0,
     * when this method is used to determine the best Foreground Color choice
     * (text, graphics) for a given Background Color.
     *
     * @param awtRed                  The Red component of the RGB color, from 0
     *                                to 255
     * @param awtGreen                The Green component of the RGB color, from
     *                                0 to 255
     * @param awtBlue                 The Blue component of the RGB color, from
     *                                0 to 255
     * @param brightnessCutoffPercent The percentile for the Brightness level of
     *                                the Hue Analysis, to use as the dark vs.
     *                                light cutoff criterion
     * @return {@code true} if the supplied color should be considered dark;
     *         {@code false} if it should be considered light
     * @since 1.0
     */
    public static boolean isColorDark( final int awtRed,
                                       final int awtGreen,
                                       final int awtBlue,
                                       final float brightnessCutoffPercent ) {
        // Convert from RGB to HSB and use the brightness component as a
        // trivial 51% threshold test for dark vs. light colors. It has been
        // proven that we perceive darkness based on HSB bright rather than
        // equal weightings of R, G, and B values, or applying complex unequal
        // weightings to those values (something we can't do intuitively).
        final float[] hsbValues = rgbToHsb( awtRed, awtGreen, awtBlue );
        final float hsbBrightness
                = hsbValues[ ColorConstants.HSB_BRIGHTNESS_INDEX ];
        final boolean colorDark = hsbBrightness <= brightnessCutoffPercent;

        return colorDark;
    }

    /**
     * Returns an HSB conversion of an RGB color value.
     * <p>
     * This method converts the supplied 0-255 integer based RGB values to a
     * floating-point HSB basis from 0.0 to 1.0, which also matches PostScript.
     * <p>
     * We rely upon AWT to do this conversion, but the source code exactly
     * matches the original algorithm for RGB to HSB that was written by A.R.
     * Smith at the time of the creation of the HSB Color Model in 1978.
     * <p>
     * https://www.cs.rit.edu/~ncs/color/t_convert.html
     *
     * @param awtRed   The Red component of the RGB color, from 0 to 255
     * @param awtGreen The Green component of the RGB color, from 0 to 255
     * @param awtBlue  The Blue component of the RGB color, from 0 to 255
     * @return The array of floating-point HSB values, from 0.0 to 1.0
     * @since 1.0
     */
    public static float[] rgbToHsb( final int awtRed,
                                    final int awtGreen,
                                    final int awtBlue ) {
        final float[] hsbValues = Color.RGBtoHSB( awtRed,
                                                  awtGreen,
                                                  awtBlue,
                                                  null );
        return hsbValues;
    }
}
