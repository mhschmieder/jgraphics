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
package com.mhschmieder.jgraphics.util;

import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * {@code ZBufferManager} is an interface that establishes the contract for
 * objects that support z-buffering of background images and cached vector
 * graphics. It is specific to the AWT and Swing frameworks as it relates to
 * helper methods that affect behavior in overrides of the
 * {@code paintComponent} method.
 * <p>
 * Just because a class implements this interface doesn't mean it has to
 * actually implement z-buffering of background images, but sometimes it is a
 * good idea to anticipate future needs and/or downstream clients and their
 * extra functionality in their class derivations.
 *
 * @author Mark Schmieder
 * @version 1.0
 */
public interface ZBufferManager {

    /**
     * Creates an off-screen z-buffer to double-buffer the graphics image.
     *
     * @param graphicsContext A Graphics Context reference
     * @return A Graphics Context to use for the off-screen z-buffer
     * @since 1.0
     */
    Graphics2D createGraphics( final Graphics graphicsContext );

    /**
     * This method either shows the new background image, or re-displays the old
     * background image, depending on whether the cached image is null or not.
     * If null, show the new background image, otherwise show then old.
     *
     * @param graphicsContext A Graphics Context to use for showing the
     *                        background image
     * @since 1.0
     */
    void showBackgroundImage( final Graphics graphicsContext );

    /**
     * Resets all off-screen images based on zooming, and other factors.
     *
     * @since 1.0
     */
    void resetOffScreenImages();

    /**
     * Initializes the off-screen canvas associated with a {@link Graphics2D}
     * Graphics Context, for future rendering.
     *
     * @param graphicsContext The Graphics Context to initialize for off-screen
     *                        rendering
     * @since 1.0
     */
    void initializeOffScreenCanvas( final Graphics2D graphicsContext );
}
