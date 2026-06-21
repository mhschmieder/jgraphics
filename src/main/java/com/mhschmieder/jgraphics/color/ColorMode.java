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

/**
 * {@code ColorMode} is an enumeration of available Color Modes for standard
 * graphics export functionality. As most graphics formats derive from
 * PostScript (or even Smalltalk Subset G, before PostScript came into being),
 * the same level of usefulness tends to apply to most potential export formats,
 * such as EPS, SVG, PDF, and even VRML (which isn't used much anymore).
 * <p>
 * Color Modes in EPS and SVG are similar to those in Adobe Photoshop, but not
 * identical. The concept is slightly more expansive than that of Color Model,
 * as it also includes more nuanced and specialized use cases such as Duotone
 * and Bitmap, the former of which has multiple variations, and the latter of
 * which is more commonly referred to as Bitmap (1-bit images), due to every
 * pixel being posterized and trivially represented as 0 (Black) or 1 (White).
 * <p>
 * Graphics files produced for the CMYK Color Mode can look slightly different
 * from the RGB Color Mode output when comparing on the screen, but look much
 * more similar once printed. The choice of CMYK vs. RGB should be based on
 * whether the downstream consumers of the file are likely to be printers or
 * viewing applications. Several direct conversions were done off-line with RGB
 * output to prove that the CMYK algorithm in this library is 100% correct.
 * <p>
 * There are an associated number of channels associated with each Color Mode:
 * either 1, 2, or 3. That information is decoupled from this enumeration class
 * as its application is context specific, so belongs in upstream domain code.
 * <p>
 * Additional specialized color spaces for PostScript include Pattern, Indexed,
 * Separation, and DeviceN. All of these are fairly complex and require extra
 * work by the client to set up the full context for using the color spaces, so
 * are unlikely to be of much use in a limited library such as this one.
 * <p>
 * Duotone is not supported, as there are so many ways to generate it, most of
 * which fall under the creative design category, and as PostScript implements
 * it within the DeviceN and Indexed color spaces, which we do not support. In
 * addition, the concept has now expanded to include monotone, tritone, and
 * quadtone images, with non-constant curves applied vs. duplicating grayscale.
 * <p>
 * HSB is not a true color mode; it is just an alternate color specification
 * within the traditional RGB color space. As PostScript simply converts the HSB
 * value to RGB before rendering, and supports HSB for strokes and fills but not
 * for images, there isn't any reason to expose HSB as an available Color Mode.
 * As AWT doesn't support HSB as a native format, clients of this library would
 * not be working directly in HSB anyway when interfacing with its methods, so
 * exposing HSB leads to conversion from RGB to HSB and back again, introducing
 * minor color inaccuracy as the two algorithms are not 100% invertible either.
 * <p>
 * Although CIELAB (also known as L*a*b*) and HSL are excellent color space
 * standards due to being closer to human perception, their device independence
 * makes them difficult to convert to and from standard device-dependent RGB,
 * HSB (also known as HSV), or CMYK. As PostScript and most other graphics
 * output formats do not support them anyway, any conversion of these color
 * spaces are the responsibility of client applications and dedicated libraries.
 * <p>
 * There was an initial attempt to support both HSB and LAB, but they were both
 * backed out as neither has much utility in the context of file export vs.
 * on-screen rendering. These modes will both be considered for e-introduction
 * if enough clients provide feedback that they are helpful. For instance, not
 * all users of this library will be producing file-based documents. There is
 * support for LAB in several AWT utility classes, but they are somewhat hidden
 * and a bit difficult to use. HSB is trivial, but maps back to RGB anyway, so
 * why bother, as the two-way conversions eventually introduce color inaccuracy?
 *
 * @see <a href=
 *      "https://en.wikipedia.org/wiki/CIELAB_color_space">https://en.wikipedia.org/wiki/CIELAB_color_space</a>
 * @see <a href=
 *      "https://en.wikipedia.org/wiki/HSL_and_HSV">https://en.wikipedia.org/wiki/HSL_and_HSV</a>
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public enum ColorMode {
    /**
     * Bitmap Mode corresponds to 1-bit images, which only have Black and White
     * as individual values.
     */
    BITMAP,
    /**
     * Grayscale Mode corresponds to achromatic gray tone images.
     */
    GRAYSCALE,
    /**
     * RGB Mode corresponds to full color images that use three color channels
     * for Red, Green, and Blue. Especially use for screen viewing.
     */
    RGB,
    /**
     * CMYK Mode corresponds to full color images that use four color channels
     * for Cyan, Magenta, Yellow, and Black. Especially useful for printing.
     */
    CMYK;

    /**
     * Returns the default Color Mode, for safe initialization and for clients
     * that have no way of dealing with alternate Color Modes or of providing
     * user choice. CMYK would be the preferred mode, but it is harder for
     * clients to deal with, so the more common RGB mode is chosen instead.
     *
     * @return The most common preferred default Color Mode, which is RGB
     *
     * @since 1.0
     */
    public static ColorMode defaultValue() {
        return RGB;
    }

}
