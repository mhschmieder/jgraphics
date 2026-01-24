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

import javax.print.DocPrintJob;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;

/**
 * {@code PrintJobWatcher} is a {@link DocPrintJob} watcher that determines when
 * it is safe to close a Print Job's Input Stream.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public final class PrintJobWatcher {

    /**
     * {@code true} if it is safe to close the Print Job's Input Stream.
     */
    protected boolean done;

    //////////////////////////// Constructors ////////////////////////////////

    /**
     * This is the default constructor, which does nothing more than set up a
     * custom {@link PrintJobAdapter} to listen for each category of relevant
     * {@link PrintJobEvent} and use those events to determine when it is safe
     * to close a Print Job's Input Stream.
     *
     * @param job
     *            The {@link DocPrintJob} to watch
     *
     * @since 1.0
     */
    public PrintJobWatcher( final DocPrintJob job ) {
        done = false;

        // Add a Listener to the Print Job for all relevant Print Job Events.
        job.addPrintJobListener( new PrintJobAdapter() {

            private void allDone() {
                synchronized ( this ) {
                    done = true;
                    notify();
                }
            }

            @Override
            public void printJobCanceled( final PrintJobEvent pje ) {
                allDone();
            }

            @Override
            public void printJobCompleted( final PrintJobEvent pje ) {
                allDone();
            }

            @Override
            public void printJobFailed( final PrintJobEvent pje ) {
                allDone();
            }

            @Override
            public void printJobNoMoreEvents( final PrintJobEvent pje ) {
                allDone();
            }

        } );
    }

    /////////////////// Primary implementation methods ///////////////////////

    /**
     * Waits until the Print Job is done and it is safe to close its Input
     * Stream. The threading model of AWT printing is less than ideal so we have
     * to be careful to synchronize all related methods.
     *
     * @since 1.0
     */
    public synchronized void waitForDone() {
        try {
            while ( !done ) {
                wait();
            }
        }
        catch ( final InterruptedException ie ) {
            ie.printStackTrace();
        }
    }

}