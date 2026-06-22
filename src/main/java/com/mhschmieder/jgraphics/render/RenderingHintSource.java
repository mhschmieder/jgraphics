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
package com.mhschmieder.jgraphics.render;

import java.awt.RenderingHints;

/**
 * {@code RenderingHintSource} is an interface that establishes a simple
 * contract for components that support {@link RenderingHints}. Even though
 * related methods are defined on {@code Graphics2D}, they are done so
 * abstractly, so there isn't even a basic default implementation to take
 * advantage of in AWT. All that was really needed was a forwarding method in
 * the {@code Component} class hierarchy, and this is what this interface
 * contracts its implementers to do.
 * <p>
 * Note that classes that implement this interface will also need to declare a
 * class variable for {@link RenderingHints} as this too was overlooked by the
 * AWT design team. I have not seen code examples anywhere that disprove my
 * point, but am open to being shown better ways of handling this situation.
 * <p>
 * This interface is not relegated to descendants of the {@code Component}
 * class; {@link RenderingHints} are also applicable to buffered images and
 * rendered images, but usually that is handled differently so this interface is
 * primarily purposed towards AWT and Swing components.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public interface RenderingHintSource {

    /**
     * Returns the {@link RenderingHints} that are cached on this layout
     * container.
     *
     * @return
     *         The {@link RenderingHints} that are cached on this layout
     *         container
     *
     * @since 1.0
     */
    RenderingHints getRenderingHints();

    /**
     * Sets the {@link RenderingHints} on this layout container. The
     * {@link RenderingHints} are usually passed down from a parent component
     * for consistency within a layout hierarchy.
     *
     * @param parentRenderingHints
     *            The {@link RenderingHints} to apply to this layout container
     *
     * @since 1.0
     */
    void setRenderingHints( final RenderingHints parentRenderingHints );

}
