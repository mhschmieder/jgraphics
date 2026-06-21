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

import java.awt.Color;

/**
 * {@code ColorConstants} is a container for constants related to color
 * handling, such as lookup indices for individual color components of HSB.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public final class ColorConstants {

    /**
     * The default constructor is disabled, as this is a static constants class.
     */
    private ColorConstants() {}

    /**
     * This is the two-character hexadecimal representation of Absolute Black.
     */
    @SuppressWarnings("nls") public static final String BLACK_HEX                 = "00";

    /**
     * This is the two-character hexadecimal representation of Absolute White.
     */
    @SuppressWarnings("nls") public static final String WHITE_HEX                 = "ff";

    /**
     * The invariant spec-mandated RGB index for the red component.
     */
    public static final int                             RGB_RED_INDEX             = 0;

    /**
     * The invariant spec-mandated RGB index for the green component.
     */
    public static final int                             RGB_GREEN_INDEX           =
                                                                        RGB_RED_INDEX + 1;

    /**
     * The invariant spec-mandated RGB index for the blue component.
     */
    public static final int                             RGB_BLUE_INDEX            =
                                                                       RGB_GREEN_INDEX + 1;

    /**
     * The number of specified individual RGB components, which is invariant.
     */
    public static final int                             NUMBER_OF_RGB_COMPONENTS  = 3;

    /**
     * The invariant spec-mandated HSB index for the hue component.
     */
    public static final int                             HSB_HUE_INDEX             = 0;

    /**
     * The invariant spec-mandated HSB index for the saturation component.
     */
    public static final int                             HSB_SATURATION_INDEX      =
                                                                             HSB_HUE_INDEX + 1;

    /**
     * The invariant spec-mandated HSB index for the brightness component.
     */
    public static final int                             HSB_BRIGHTNESS_INDEX      =
                                                                             HSB_SATURATION_INDEX
                                                                                     + 1;

    /**
     * The number of specified individual HSB components, which is invariant.
     */
    public static final int                             NUMBER_OF_HSB_COMPONENTS  = 3;

    /**
     * The invariant spec-mandated CMYK index for the cyan component.
     */
    public static final int                             CMYK_CYAN_INDEX           = 0;

    /**
     * The invariant spec-mandated CMYK index for the magenta component.
     */
    public static final int                             CMYK_MAGENTA_INDEX        =
                                                                           CMYK_CYAN_INDEX + 1;

    /**
     * The invariant spec-mandated CMYK index for the yellow component.
     */
    public static final int                             CMYK_YELLOW_INDEX         =
                                                                          CMYK_MAGENTA_INDEX + 1;

    /**
     * The invariant spec-mandated CMYK index for the black component.
     */
    public static final int                             CMYK_BLACK_INDEX          =
                                                                         CMYK_YELLOW_INDEX + 1;

    /**
     * The number of specified individual CMYK components, which is invariant.
     */
    public static final int                             NUMBER_OF_CMYK_COMPONENTS = 4;

    //////// Custom colors to augment the AWT named color constants //////////

    /**
     * Maroon is is one of the sixteen named colors, but isn't present in AWT.
     */
    public static final Color                           MAROON                    =
                                                               new Color( 128, 0, 0 );

    /**
     * Olive is is one of the sixteen named colors, but isn't present in AWT.
     */
    public static final Color                           OLIVE                     =
                                                              new Color( 128, 128, 0 );

    /**
     * Lime is is one of the sixteen named colors, but isn't present in AWT.
     */
    public static final Color                           LIME                      =
                                                             new Color( 0, 128, 0 );

    /**
     * Teal is is one of the sixteen named colors, but isn't present in AWT.
     */
    public static final Color                           TEAL                      =
                                                             new Color( 0, 128, 128 );

    /**
     * Cyan is is one of the sixteen named colors, but isn't present in AWT.
     * <p>
     * This color is alternately known as aqua, but cyan is more common.
     */
    public static final Color                           CYAN                      =
                                                             new Color( 0, 255, 255 );

    /**
     * Navy is is one of the sixteen named colors, but isn't present in AWT.
     */
    public static final Color                           NAVY                      =
                                                             new Color( 0, 0, 128 );

    /**
     * Purple is is one of the sixteen named colors, but isn't present in AWT.
     */
    public static final Color                           PURPLE                    =
                                                               new Color( 128, 0, 128 );

    /**
     * Magenta is is one of the sixteen named colors, but isn't present in AWT.
     * <p>
     * This color is alternately known as fuschia, but magenta is more common.
     */
    public static final Color                           MAGENTA                   =
                                                                new Color( 255, 0, 255 );

    /**
     * Bright Yellow is defined in many specifications, but not AWT or CSS3.
     */
    public static final Color                           BRIGHTYELLOW              =
                                                                     new Color( 255, 255, 10 );

    /**
     * Dark Royal Blue is defined in many specifications, but not AWT or CSS3.
     */
    public static final Color                           DARKROYALBLUE             =
                                                                      new Color( 62, 86, 151 );

    /**
     * Lemon is defined in many specifications, but not AWT or CSS3.
     */
    public static final Color                           LEMON                     =
                                                              new Color( 202, 202, 70 );

    /**
     * Many monochromatic grays are needed by most applications; 5% Gray is
     * often needed when providing perceptibly contrasting tones close to Black.
     */
    public static final Color                           GRAY05                    =
                                                               new Color( 13, 13, 13 );

    /**
     * Many monochromatic grays are needed by most applications; 10% Gray is one
     * of the eight most-used ten-percentile divisions between Black and White.
     */
    public static final Color                           GRAY10                    =
                                                               new Color( 26, 26, 26 );

    /**
     * Many monochromatic grays are needed by most applications; 15% Gray is
     * often needed when providing perceptibly contrasting tones close to Black.
     */
    public static final Color                           GRAY15                    =
                                                               new Color( 39, 39, 39 );

    /**
     * Many monochromatic grays are needed by most applications; 20% Gray is one
     * of the eight most-used ten-percentile divisions between Black and White.
     */
    public static final Color                           GRAY20                    =
                                                               new Color( 51, 51, 51 );

    /**
     * Many monochromatic grays are needed by most applications; 25% Gray is
     * common as it represents the simple 1/4 ratio.
     */
    public static final Color                           GRAY25                    =
                                                               new Color( 64, 64, 64 );

    /**
     * Many monochromatic grays are needed by most applications; 30% Gray is one
     * of the eight most-used ten-percentile divisions between Black and White.
     */
    public static final Color                           GRAY30                    =
                                                               new Color( 77, 77, 77 );

    /**
     * Many monochromatic grays are needed by most applications; 33.3% Gray is
     * common as it represents the simple 1/3 ratio.
     */
    public static final Color                           GRAY33_3                  =
                                                                 new Color( 85, 85, 85 );

    /**
     * Many monochromatic grays are needed by most applications; 40% Gray is one
     * of the eight most-used ten-percentile divisions between Black and White.
     */
    public static final Color                           GRAY40                    =
                                                               new Color( 102, 102, 102 );

    /**
     * Many monochromatic grays are needed by most applications; 45% Gray is a
     * safe bet when 50% Gray might cause masking of foreground vs. background.
     */
    public static final Color                           GRAY45                    =
                                                               new Color( 115, 115, 115 );

    /**
     * Many monochromatic grays are needed by most applications; 50% Gray is one
     * of the eight most-used ten-percentile divisions between Black and White.
     */
    public static final Color                           GRAY50                    =
                                                               new Color( 128, 128, 128 );

    /**
     * Many monochromatic grays are needed by most applications; 55% Gray is a
     * safe bet when 50% Gray might cause masking of foreground vs. background.
     */
    public static final Color                           GRAY55                    =
                                                               new Color( 140, 140, 140 );

    /**
     * Many monochromatic grays are needed by most applications; 60% Gray is one
     * of the eight most-used ten-percentile divisions between Black and White.
     */
    public static final Color                           GRAY60                    =
                                                               new Color( 153, 153, 153 );

    /**
     * Many monochromatic grays are needed by most applications; 66.6% Gray is
     * common as it represents the simple 2/3 ratio.
     */
    public static final Color                           GRAY66_6                  =
                                                                 new Color( 170, 170, 170 );

    /**
     * Many monochromatic grays are needed by most applications; 70% Gray is one
     * of the eight most-used ten-percentile divisions between Black and White.
     */
    public static final Color                           GRAY70                    =
                                                               new Color( 177, 177, 177 );

    /**
     * Many monochromatic grays are needed by most applications; 75% Gray is
     * common as it represents the simple 3/4 ratio.
     * <p>
     * Additionally, this monochrome ratio is commonly known as silver in CSS3.
     */
    public static final Color                           GRAY75                    =
                                                               new Color( 192, 192, 192 );

    /**
     * Many monochromatic grays are needed by most applications; 80% Gray is one
     * of the eight most-used ten-percentile divisions between Black and White.
     */
    public static final Color                           GRAY80                    =
                                                               new Color( 203, 203, 203 );

    /**
     * Many monochromatic grays are needed by most applications; 90% Gray is one
     * of the eight most-used ten-percentile divisions between Black and White.
     */
    public static final Color                           GRAY90                    =
                                                               new Color( 231, 231, 231 );

    /**
     * Many monochromatic grays are needed by most applications; Night Mode is
     * quite common when needing almost-black, and is easier on the eyes.
     */
    public static final Color                           NIGHT_MODE                = GRAY05;

    /**
     * Many monochromatic grays are needed by most applications; Day Mode is
     * quite common when needing almost-white, and is easier on the eyes.
     */
    public static final Color                           DAY_MODE                  = GRAY90;

}