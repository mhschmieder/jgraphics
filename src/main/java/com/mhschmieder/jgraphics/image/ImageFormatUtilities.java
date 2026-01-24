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
 * This file is part of the GraphicsToolkit Library
 *
 * You should have received a copy of the MIT License along with the
 * GraphicsToolkit Library. If not, see <https://opensource.org/licenses/MIT>.
 *
 * Project: https://github.com/mhschmieder/graphicstoolkit
 */
package com.mhschmieder.jgraphics.image;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * {@code ImageFormatUtilities} is a utility class for Graphics2D based image
 * format methods that are used to query the capabilities of the JDK that this
 * code base is built against.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public final class ImageFormatUtilities {

    /**
     * The default constructor is disabled, as this is a static utilities class.
     */
    private ImageFormatUtilities() {}

    /**
     * Returns a flag that indicates whether the supplied Image Type is
     * supported for write actions (if {@code true}) or not (if {@code false})
     * in the invoking application's Java Virtual Machine and auxiliary
     * libraries.
     * <p>
     * This method determines whether a specific Image Format is supported on
     * a given {@link BufferedImage} based on heuristics in the Image I/O API.
     * <p>
     * The context is for checking ahead of time whether it will be possible to
     * write the supplied {@link BufferedImage} to the specified Image Format.
     * Not all Image Formats support every possible Image Type. For instance,
     * the already-generated {@link BufferedImage} might use an Indexed Color
     * Model, which not all Image Formats can handle.
     *
     * @param bufferedImage
     *            The {@link BufferedImage} to use to intuit the Image Type
     * @param imageFormatName
     *            The Image Format name to check for support
     * @return {@code true} if the specified Image Type is supported for the
     *         supplied {@link BufferedImage}, {@code false} if it is not
     *         supported
     *
     * @since 1.0
     */
    public static boolean isImageTypeSupportedForWrite( final BufferedImage bufferedImage,
                                                        final String imageFormatName ) {
        if ( bufferedImage == null ) {
            return false;
        }

        final ImageTypeSpecifier type = ImageTypeSpecifier.createFromRenderedImage( bufferedImage );
        final Iterator< ImageWriter > iter = ImageIO.getImageWriters( type, imageFormatName );
        final boolean imageTypeSupported = iter.hasNext();

        return imageTypeSupported;
    }

    /**
     * Returns a flag that indicates whether the supplied Image File Extension
     * can be read (if {@code true}) or not (if {@code false}) by the invoking
     * application's Java Virtual Machine and auxiliary libraries
     * <p>
     * This method checks a given File Extension to see if there is available
     * Image Reader support in the JVM that the application is built against.
     *
     * @param fileExt
     *            The File Extension to check for available Image Reader support
     *
     * @return {@code true} if the specified Image File Extension can be read,
     *         {@code false} if it cannot
     *
     * @since 1.0
     */
    public static boolean canReadImageExtension( final String fileExt ) {
        final Iterator< ImageReader > iter = ImageIO.getImageReadersBySuffix( fileExt );
        return iter.hasNext();
    }

    /**
     * Returns a flag that indicates whether the supplied Image Format Name can
     * be read (if {@code true}) or not (if {@code false}) by the invoking
     * application's Java Virtual Machine and auxiliary libraries
     * <p>
     * This method checks a given Format name to see if there is available
     * Image Reader support in the JDK that this code base is built against.
     *
     * @param formatName
     *            The Format Name to check for available Image Reader support
     *
     * @return {@code true} if the specified Image Format Name can be read,
     *         {@code false} if it cannot
     *
     * @since 1.0
     */
    public static boolean canReadImageFormat( final String formatName ) {
        final Iterator< ImageReader > iter = ImageIO.getImageReadersByFormatName( formatName );
        return iter.hasNext();
    }

    /**
     * Returns a flag that indicates whether the supplied Image Mime Type can
     * be read (if {@code true}) or not (if {@code false}) by the invoking
     * application's Java Virtual Machine and auxiliary libraries
     * <p>
     * This method checks a given Mime Type to see if there is available Image
     * Reader support in the JDK that this code base is built against.
     *
     * @param mimeType
     *            The Mime Type to check for available Image Reader support
     *
     * @return {@code true} if the specified Image Mime Type can be read,
     *         {@code false} if it cannot
     *
     * @since 1.0
     */
    public static boolean canReadImageMimeType( final String mimeType ) {
        final Iterator< ImageReader > iter = ImageIO.getImageReadersByMIMEType( mimeType );
        return iter.hasNext();
    }

    /**
     * Returns a flag that indicates whether the supplied Image File Extension
     * can be written (if {@code true}) or not (if {@code false}) by the
     * invoking application's Java Virtual Machine and auxiliary libraries
     * <p>
     * This method checks a given File Extension to see if there is available
     * Image Writer support in the JDK that this code base is built against.
     *
     * @param fileExt
     *            The File Extension to check for available Image Writer support
     *
     * @return {@code true} if the specified image file extension can be
     *         written, {@code false} if it cannot
     *
     * @since 1.0
     */
    public static boolean canWriteImageExtension( final String fileExt ) {
        final Iterator< ImageWriter > iter = ImageIO.getImageWritersBySuffix( fileExt );
        return iter.hasNext();
    }

    /**
     * Returns a flag that indicates whether the supplied Image Format name can
     * be written (if {@code true}) or not (if {@code false}) by the invoking
     * application's Java Virtual Machine and auxiliary libraries
     * <p>
     * This method checks a given Format name to see if there is available
     * Image Writer support in the JDK that this code base is built against.
     *
     * @param formatName
     *            The Format Name to check for available Image Writer support
     *
     * @return {@code true} if the specified Image Format Name can be written,
     *         {@code false} if it cannot
     *
     * @since 1.0
     */
    public static boolean canWriteImageFormat( final String formatName ) {
        final Iterator< ImageWriter > iter = ImageIO.getImageWritersByFormatName( formatName );
        return iter.hasNext();
    }

    /**
     * Returns a flag that indicates whether the supplied Image Mime Type can be
     * written (if {@code true}) or not (if {@code false}) by the invoking
     * application's Java
     * Virtual Machine and auxiliary libraries
     * <p>
     * This method checks a given Mime Type to see if there is available Image
     * Writer support in the JDK that this code base is built against.
     *
     * @param mimeType
     *            The Mime Type to check for available Image Writer support
     *
     * @return {@code true} if the specified Image Mime Type can be written,
     *         {@code false} if it cannot
     *
     * @since 1.0
     */
    public static boolean canWriteImageMimeType( final String mimeType ) {
        final Iterator< ImageWriter > iter = ImageIO.getImageWritersByMIMEType( mimeType );
        return iter.hasNext();
    }

    /**
     * Returns the name of the Image Format associated with the {@link File}.
     * <p>
     * This method uses the Image I/O API to determine whether the contents of
     * the supplied file correspond to a known image format, and if so, returns
     * the first one in the list of image readers that can handle the format.
     *
     * @param file
     *            The {@link File} corresponding to the image
     * @return The format name of the image in the supplied file, or
     *         {@code null} if the format is not known
     *
     * @since 1.0
     */
    public static String getImageFormatForFile( final File file ) {
        return getImageFormatName( file );
    }

    /**
     * Returns the name of the Image Format associated with the
     * {@link InputStream}.
     * <p>
     * This method uses the Image I/O API to determine whether the contents of
     * the supplied input stream correspond to a known image format, and if so,
     * returns the first one in the list of image readers that can handle the
     * format.
     *
     * @param inputStream
     *            The {@link InputStream} corresponding to the image
     * @return The format name of the image in the supplied input stream, or
     *         {@code null} if the format is not known
     *
     * @since 1.0
     */
    public static String getImageFormatForInputStream( final InputStream inputStream ) {
        return getImageFormatName( inputStream );
    }

    /**
     * Returns the Image Format Name associated with the supplied Object.
     * <p>
     * This method uses the Image I/O API to determine whether the contents of
     * a supplied File or Input Stream correspond to a known Image Format, and
     * if so, returns the first one in the list of Image Readers that can handle
     * the format.
     * <p>
     * Unfortunately the method argument has to be a generic object as that is
     * the only common base class for Files and Input Streams.
     * <p>
     * The method is declared as private, to avoid user errors, for reasons
     * explained above. This is mostly a utility methods used by the publicly
     * exposed methods in order to avoid copy/paste coding errors and divergent
     * code enhancement paths.
     *
     * @param object
     *            The object must be either a File or an Input Stream
     *            corresponding to an image
     * @return The format name of the image in the File or Input Stream, or
     *         {@code null} if the format is not known or the object does not
     *         correspond to an Image File or Stream
     *
     * @since 1.0
     */
    private static String getImageFormatName( final Object object ) {
        // Create an image input stream on the image.
        try ( final ImageInputStream imageInputStream = ImageIO.createImageInputStream( object ) ) {
            // Find all image readers that recognize the image format.
            final Iterator< ImageReader > iter = ImageIO.getImageReaders( imageInputStream );

            // Use the first available image reader, if any are present.
            final ImageReader reader = iter.hasNext() ? iter.next() : null;

            // Return the image format name, if present.
            return ( reader != null ) ? reader.getFormatName() : null;
        }
        catch ( final NullPointerException | IllegalArgumentException | IOException e ) {
            e.printStackTrace();
        }

        // The image could not be read.
        return null;
    }

}
