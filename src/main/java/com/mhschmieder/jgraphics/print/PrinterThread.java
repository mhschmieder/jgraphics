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

import javax.print.attribute.PrintRequestAttributeSet;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

/**
 * {@code PrinterThread} is a specialized {@link Thread} that deals with some of
 * the details of AWT and Swing based printing.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public final class PrinterThread extends Thread {

    /**
     * The cached pre-constructed {@link PrinterJob}.
     */
    private final PrinterJob               cachedPrinterJob;

    /**
     * The cached pre-constructed {@link PrintRequestAttributeSet}.
     */
    private final PrintRequestAttributeSet printingAttributes;

    //////////////////////////// Constructors ////////////////////////////////

    /**
     * This is the default constructor for Printer Threads.
     *
     * @param printerJob
     *            The pre-constructed {@link PrinterJob} to use for this
     *            {@code PrinterThread}
     * @param requestedPrintingAttributes
     *            The requested {@link PrintRequestAttributeSet} to apply to
     *            each Print action
     *
     * @since 1.0
     */
    public PrinterThread( final PrinterJob printerJob,
                          final PrintRequestAttributeSet requestedPrintingAttributes ) {
        cachedPrinterJob = printerJob;
        printingAttributes = requestedPrintingAttributes;

        setName( getClass().getName() );
    }

    ////////////////////// Specialized Print methods /////////////////////////

    /**
     * Cancels the Printer Thread and the Print Job.
     *
     * @since 1.0
     */
    public void cancelPrint() {
        if ( isAlive() && !isInterrupted() ) {
            interrupt();
        }
    }

    /**
     * Starts the Printer Thread and the Print Job.
     *
     * @since 1.0
     */
    public void doPrint() {
        if ( !isAlive() && !isInterrupted() ) {
            start();
        }
    }

    /////////////////////// Thread method overrides //////////////////////////

    /**
     * Call the {@code run} method on a separate Runnable object, if this thread
     * was constructed using a that object; otherwise does nothing and returns.
     *
     * @since 1.0
     */
    @Override
    public void run() {
        try {
            // If the current thread is interrupted, interrupt this thread as
            // well, but don't throw up a warning dialog in that case.
            if ( Thread.interrupted() ) {
                throw new InterruptedException();
            }

            cachedPrinterJob.print( printingAttributes );
        }
        catch ( final InterruptedException ie ) {
            ie.printStackTrace();
        }
        catch ( final PrinterException pe ) {
            pe.printStackTrace();
        }
    }

}
