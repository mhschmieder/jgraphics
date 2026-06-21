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
package com.mhschmieder.jgraphics.render;

public class RenderingProgress {

    /**
     * Mutually exclusive rendering state to manage the full rendering
     * lifecycle.
     * <p>
     * This field is volatile because render workers update progress while the
     * JavaFX progress monitor reads it from another thread.
     */
    public volatile RenderingState renderingState;

    /**
     * The current step for this rendering progress context.
     * <p>
     * This field is volatile for cross-thread visibility between render worker
     * updates and JavaFX progress monitor reads.
     */
    public volatile long currentStep;

    /**
     * The number of steps estimated for this rendering progress context.
     * <p>
     * This field is volatile for cross-thread visibility between render worker
     * updates and JavaFX progress monitor reads.
     */
    public volatile long numberOfSteps;

    /**
     * Creates a new instance of RenderingStatus with default values.
     * <p>
     * NOTE: Number of steps is defaulted to 1 vs. 0, to avoid divide by zero.
     */
    public RenderingProgress() {
        init();
    }

    public void init() {
        // NOTE: Number of steps is defaulted to 1 to avoid divide by zero.
        update( RenderingState.INACTIVE, 0L, 1L );
    }

    public void start( final long pNumberOfSteps ) {
        update( RenderingState.STARTED, 0L, pNumberOfSteps );
    }

    public void finish() {
        // NOTE: Number of steps is defaulted to 1 to avoid divide by zero.
        update( RenderingState.FINISHED, 0L, 1L );
    }

    public void update( final RenderingState pRenderingState,
                        final long pCurrentStep,
                        final long pNumberOfSteps ) {
        renderingState = pRenderingState;
        currentStep = pCurrentStep;
        numberOfSteps = pNumberOfSteps;
    }
}
