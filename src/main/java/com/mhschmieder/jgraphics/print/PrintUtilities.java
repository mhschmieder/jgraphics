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
package com.mhschmieder.jgraphics.print;

import javax.swing.JComponent;
import java.awt.Component;

/**
 * {@code PrintUtilities} is a utility class for methods dealing with printing.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public final class PrintUtilities {

    /**
     * The default constructor is disabled, as this is a static utilities class.
     */
    private PrintUtilities() {}

    /**
     * Disables double buffering on the provided {@link Component}, as long as
     * it is a Swing {@link JComponent} instance. Otherwise it takes no action.
     *
     * @param component
     *            The {@link Component} to disable from using double buffering
     * @return {@code true} if double buffering was previously enabled on the
     *         provided {@link Component}, {@code false} if it wasn't, or if the
     *         provided {@link Component} is not a Swing {@link JComponent}
     *         instance
     *
     * @since 1.0
     */
    public static boolean disableDoubleBuffering( final Component component ) {
        if ( component instanceof JComponent ) {
            final JComponent jComponent = ( JComponent ) component;
            final boolean wasDoubleBuffered = jComponent.isDoubleBuffered();
            jComponent.setDoubleBuffered( false );
            return wasDoubleBuffered;
        }

        return false;
    }

    /**
     * Sets the provided {@link Component} to double buffering, if the flag is
     * also set to {@code true}; otherwise turns off double buffering if set to
     * {@code false}.
     *
     * @param component
     *            The {@link Component} to set to double buffering
     * @param setToDoubleBuffered
     *            {@code true} if we should set the provided {@link Component}
     *            to use a buffer to paint, {@code false} if no double buffering
     *            is required
     *
     * @since 1.0
     */
    public static void setDoubleBuffering( final Component component,
                                           final boolean setToDoubleBuffered ) {
        if ( component instanceof JComponent ) {
            final JComponent jComponent = ( JComponent ) component;
            jComponent.setDoubleBuffered( setToDoubleBuffered );
        }
    }

}
