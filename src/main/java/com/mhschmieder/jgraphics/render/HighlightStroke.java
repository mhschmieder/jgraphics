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
package com.mhschmieder.jgraphics.render;

import org.apache.commons.math3.util.FastMath;

import java.awt.BasicStroke;
import java.awt.RenderingHints;

/**
 * {@code HighlightStroke} is a custom {@link BasicStroke} used for highlighting
 * on the Graphics2D canvas; especially for indicating selected shapes.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public final class HighlightStroke extends BasicStroke {

    /**
     * Define a basic default highlight stroke pattern that is symmetric and not
     * too short in its segments as to cause anomalies in curves and angled
     * lines. This pattern is close to that of most applications in the 2000's.
     * <p>
     * We must use a resolution greater than one pixel for the alternating
     * pattern, as platforms such as macOS use such high resolution vectorizing
     * graphics that an on-off-on-off pattern looks like a solid line.
     */
    private static final float HIGHLIGHT_DASH_PATTERN[] = { 3f, 3f };

    //////////////////////////// Constructors ////////////////////////////////

    /**
     * This is the default constructor for making a custom highlight stroke that
     * is based off of common highlighting styles in Adobe applications and
     * others in the early 2000's when AWT was still the new kid on the block.
     * <p>
     * Stroke width on custom stroke classes should generally be greater than
     * 1.0, to force the internal line widening algorithm to kick in; otherwise
     * there may be side effects that cause a solid line, even if anti-aliasing
     * is turned on in the {@link RenderingHints}. No dash phase is used.
     *
     * @param basicStroke
     *            The {@link BasicStroke} to use as a reference for the line
     *            width to use for highlighting
     *
     * @version 1.0
     */
    public HighlightStroke( final BasicStroke basicStroke ) {
        super( FastMath.max( basicStroke.getLineWidth(), 1.01f ),
               BasicStroke.CAP_BUTT,
               BasicStroke.JOIN_MITER,
               basicStroke.getMiterLimit(),
               HIGHLIGHT_DASH_PATTERN,
               0f );
    }

}
