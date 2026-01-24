/**
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
package com.mhschmieder.jgraphics.print;

/**
 * {@code PaperConstants} is a container for constants related to paper size,
 * when all that is needed is a basic default and more complex query mechanisms
 * are undesirable due to pulling in extra dependencies such as AWT or JavaFX.
 * <p>
 * As these constants are primarily for supporting export of PostScript family
 * graphics export formats, such as EPS, SVG, PDF, only North American Letter
 * dimensions are implemented at this time, as some formats only support that.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public final class PaperConstants {

    /**
     * The default constructor is disabled, as this is a static constants class.
     */
    private PaperConstants() {}

    /**
     * The standard units for printing are points, defined as 1/72 inch.
     */
    public static final float INCHES_TO_POINTS_RATIO  = 72f;

    /**
     * North American Letter Size paper is defined as 8.5 inches wide.
     */
    public static final float NA_LETTER_WIDTH_INCHES  = 8.5f;

    /**
     * North American Letter Size paper is defined as 11 inches tall.
     */
    public static final float NA_LETTER_HEIGHT_INCHES = 11f;

    /**
     * North American Letter Size width in points is needed for printing.
     */
    public static final float NA_LETTER_WIDTH_POINTS  = INCHES_TO_POINTS_RATIO
            * NA_LETTER_WIDTH_INCHES;

    /**
     * North American Letter Size height in points is needed for printing.
     */
    public static final float NA_LETTER_HEIGHT_POINTS = INCHES_TO_POINTS_RATIO
            * NA_LETTER_HEIGHT_INCHES;

}
