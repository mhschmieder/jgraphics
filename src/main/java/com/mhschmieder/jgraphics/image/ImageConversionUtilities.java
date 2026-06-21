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
package com.mhschmieder.jgraphics.image;

import org.apache.commons.math3.util.FastMath;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.PixelGrabber;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;

/**
 * {@code ImageConversionUtilities} is a utility class for Graphics2D based
 * image conversion methods that are needed in many different contexts.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public final class ImageConversionUtilities {

    /**
     * The default constructor is disabled, as this is a static utilities class.
     */
    private ImageConversionUtilities() {}

    /**
     * Converts a supplied {@link RenderedImage} to its corresponding
     * {@link BufferedImage}.
     *
     * @param renderedImage
     *            The Rendered Image to use as a source for a new Buffered Image
     * @return A {@link BufferedImage} that is either copied from the original
     *         {@link RenderedImage} or is the original reference when it was
     *         already a {@link RenderedImage}, or {@code null} if {@code null}
     *
     * @since 1.0
     */
    public static BufferedImage convertRenderedImage( final RenderedImage renderedImage ) {
        if ( renderedImage == null ) {
            return null;
        }

        if ( renderedImage instanceof BufferedImage ) {
            final BufferedImage bufferedImage = ( BufferedImage ) renderedImage;
            return bufferedImage;
        }

        final ColorModel colorModel = renderedImage.getColorModel();
        final int width = renderedImage.getWidth();
        final int height = renderedImage.getHeight();
        final WritableRaster raster = colorModel.createCompatibleWritableRaster( width, height );
        final boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();

        final Hashtable< String, Object > properties = new Hashtable<>();
        final String[] keys = renderedImage.getPropertyNames();
        if ( keys != null ) {
            for ( final String key : keys ) {
                properties.put( key, renderedImage.getProperty( key ) );
            }
        }

        final BufferedImage bufferedImage = new BufferedImage( colorModel,
                                                               raster,
                                                               isAlphaPremultiplied,
                                                               properties );
        renderedImage.copyData( raster );

        return bufferedImage;
    }

    /**
     * Returns a two-dimensional array of integer-based pixels grabbed from
     * the supplied {@link Image}, using the format that PostScript needs
     * for images. Depending on the parameters, this might not be the full
     * image, as image cropping is supported by the {@link PixelGrabber}.
     *
     * @param image
     *            The source image to convert to pixels
     * @param x
     *            The x coordinate of the upper left corner of the region to
     *            retrieve from the image
     * @param y
     *            The y coordinate of the upper left corner of the region to
     *            retrieve from the image
     * @param width
     *            The width of the rectangle of pixels to retrieve
     * @param height
     *            The height of the rectangle of pixels to retrieve
     * @return An integer array of pixels grabbed from the original
     *         {@link Image}
     *
     * @since 1.0
     */
    public static int[] getImagePixels( final Image image,
                                        final int x,
                                        final int y,
                                        final int width,
                                        final int height ) {
        final int[] pixels = new int[ width * height ];
        final PixelGrabber pixelGrabber = new PixelGrabber( image,
                                                            x,
                                                            y,
                                                            width,
                                                            height,
                                                            pixels,
                                                            0,
                                                            width );

        try {
            pixelGrabber.grabPixels();
        }
        catch ( final InterruptedException e ) {
            // Report this language-level exception and make sure the invoker
            // doesn't think they have a valid pixel array to use.
            e.printStackTrace();
            return null;
        }

        return pixels;
    }

    /**
     * Returns a {@link BufferedImage} created as a packed raster using the
     * supplied two-dimensional array of integer-based pixels and applying
     * the standard default RGB Color Model with proper ARGB Band Masks.
     * 
     * @param pixels The two-dimensional integer-based array of pixels
     * @param width The width (number of columns per row) to use for reading
     *              the one-dimensional array as a two-dimensional value set
     * @param height The height (number of pixels per column) to hint the 
     *               packed raster utility for converting to a BufferedImage.
     * @return a {@link BufferedImage} in ARGB color space as a packed raster
     */
    public static BufferedImage createBufferedImage( final int[] pixels,
                                                     final int width,
                                                     final int height ) {
        // NOTE: Although referred to as ARGB, the actual order of the masks
        //  when consumed by the Raster utility is { R, G, B, A }.
        final int[] bandMasks = {
                0xFF0000, 0xFF00, 0xFF, 0xFF000000
        };

        final DataBufferInt buffer = new DataBufferInt( pixels, pixels.length );
        final WritableRaster raster = Raster.createPackedRaster( buffer,
                                                                 width,
                                                                 height,
                                                                 width,
                                                                 bandMasks,
                                                                 null );
        
        final ColorModel cm = ColorModel.getRGBdefault();
        return new BufferedImage( cm, raster, cm.isAlphaPremultiplied(), null );
    }

    /**
     * This method takes an existing {@link BufferedImage} and converts it to
     * the requested Image Format, using the original AWT Imaging API.
     *
     * @param bufferedImage
     *            The {@link BufferedImage} whose Image Type should be swapped
     * @param imageFormatName
     *            The Image Format to use as the new format for the supplied
     *            {@link BufferedImage}
     * @return A new {@link BufferedImage} corresponding to the original
     *         {@link BufferedImage} with the Image Type swapped
     *
     * @since 1.0
     */
    @SuppressWarnings("nls")
    public static BufferedImage swapImageType( final BufferedImage bufferedImage,
                                               final String imageFormatName ) {
        BufferedImage convertedImage = bufferedImage;
        
        // There is a known bug in Oracle's code, so we do the recommended
        // workaround to replace the incorrect Image Type that their utility
        // method sets for JPEG and BMP (the latter won't output).
        //
        // The solution that was found on-line has been modified however,
        // because Oracle sets TYPE_INT_ARGB_PRE regardless of the image format
        // name, so it's safer to just automatically set JPEG and BMP to what
        // will work, regardless.
        //
        // The TIFF format conversion is still imperfect, as there seem to be
        // inconsistencies in what is included in the JDK distribution.
        //
        // It does not seem to be necessary anymore to ensure that the source
        // image adheres to the four-byte ABGR Image Type.
        // final int imageType = bufferedImage.getType();
        // if ( imageType == BufferedImage.TYPE_4BYTE_ABGR ) {
        final ColorConvertOp op = new ColorConvertOp( null );
        final String imageFormatNameCaseInsensitive = imageFormatName.toLowerCase( Locale.ENGLISH );
        switch ( imageFormatNameCaseInsensitive ) {
        case "jpg":
        case "bmp":
            // Make a Component Color Image.
            convertedImage = new BufferedImage( bufferedImage.getWidth(),
                                                bufferedImage.getHeight(), 
                                                BufferedImage.TYPE_3BYTE_BGR );
            op.filter( bufferedImage, convertedImage );
            break;
        case "tiff":
        case "tif":
            // Make a Direct Color Image.
            convertedImage = new BufferedImage( bufferedImage.getWidth(),
                                                bufferedImage.getHeight(),
                                                BufferedImage.TYPE_INT_RGB );
            op.filter( bufferedImage, convertedImage );
            break;
        default:
            break;
        }
        // }

        return bufferedImage;
    }

    /**
     * This method take a provided AWT (or Swing) {@link Component} and
     * renders it to a JPEG image which is written to the provided {@link File}.
     * <p>
     * The Buffered Image is returned to the client for purposes of querying the
     * actual pixel dimensions after adjusting for Aspect Ratio.
     *
     * @param component
     *            The {@link Component} to render to a JPEG file
     * @param imageFile
     *            The {@link File} to use for writing the JPEG image
     * @param pixelWidth
     *            The preferred width in pixels for the JPEG image
     * @param pixelHeight
     *            The preferred height in pixels for the JPEG image
     * @param autoSizeImage
     *            {@code true} if the image should be auto-sized; {@code false}
     *            if the real Aspect Ratio should be retained
     * @return The JPEG {@link Image} that was written to the supplied
     *         {@link File}
     *
     * @since 1.0
     */
    @SuppressWarnings("nls")
    public static BufferedImage renderComponentToJpegFile( final Component component,
                                                           final File imageFile,
                                                           final double pixelWidth,
                                                           final double pixelHeight,
                                                           final boolean autoSizeImage ) {
        try ( final FileOutputStream fileOutputStream = new FileOutputStream( imageFile );
                final BufferedOutputStream imageStream =
                                                       new BufferedOutputStream( fileOutputStream ) ) {
            final BufferedImage image = renderComponent( component,
                                                         imageStream,
                                                         pixelWidth,
                                                         pixelHeight,
                                                         autoSizeImage,
                                                         "jpg",
                                                         1.0f );

            return image;
        }
        catch ( final NullPointerException | SecurityException | IOException e ) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method take a provided AWT (or Swing) {@link Component} and
     * renders it to an image which is written to the provided
     * {@link OutputStream} using the provided Image Format.
     * <p>
     * The Buffered Image is returned to the client for purposes of querying the
     * actual pixel dimensions after adjusting for Aspect Ratio.
     *
     * @param component
     *            The {@link Component} to render to an output stream
     * @param outputStream
     *            The {@link OutputStream} to use for writing the produced image
     * @param pixelWidth
     *            The preferred width in pixels for the produced image
     * @param pixelHeight
     *            The preferred height in pixels for the produced image
     * @param autoSizeImage
     *            {@code true} if the image should be auto-sized; {@code false}
     *            if the real Aspect Ratio should be retained
     * @param imageFormatName
     *            The Image Format Name to use for the produced image
     * @param compressionQuality
     *            The Compression Quality to use for the produced image; not
     *            relevant to all Image Formats
     * @return The JPEG {@link Image} that was written to the supplied
     *         {@link OutputStream}
     *
     * @since 1.0
     */
    @SuppressWarnings("nls")
    public static BufferedImage renderComponent( final Component component,
                                                 final OutputStream outputStream,
                                                 final double pixelWidth,
                                                 final double pixelHeight,
                                                 final boolean autoSizeImage,
                                                 final String imageFormatName,
                                                 final float compressionQuality ) {
        // Avoid throwing unnecessary exceptions by filtering for bad output
        // streams and null component contexts.
        if ( ( component == null ) || ( outputStream == null ) ) {
            return null;
        }

        // Create a Buffered Image based on rasterization of the component.
        final String imageFormatNameCaseInsensitive = imageFormatName.toLowerCase( Locale.ENGLISH );
        int imageType = BufferedImage.TYPE_INT_RGB;
        switch ( imageFormatNameCaseInsensitive ) {
        case "wbm":
        case "wbmp":
            imageType = BufferedImage.TYPE_BYTE_BINARY;
            break;
        default:
            break;
        }
        final BufferedImage bufferedImage = renderComponent( component,
                                                             pixelWidth,
                                                             pixelHeight,
                                                             autoSizeImage,
                                                             imageType );
        if ( bufferedImage == null ) {
            return null;
        }

        // Switch on whether we need to customize for compression quality.
        //
        // It is uncertain whether Java supports 100% compression, or just the
        // listed 5%, 75% and 95% levels; may need to switch to default writer?
        boolean handleCompressionQuality = false;
        switch ( imageFormatNameCaseInsensitive ) {
        case "jpg":
        case "jpeg":
        case "jpe":
            handleCompressionQuality = true;
            break;
        default:
            if ( compressionQuality < 1.0f ) {
                handleCompressionQuality = true;
            }
            break;
        }

        try {
            if ( handleCompressionQuality ) {
                // Make sure there is an Image Writer installed for the selected
                // Image Format.
                ImageWriter imageWriter = null;
                final Iterator< ImageWriter > iter = ImageIO
                        .getImageWritersByFormatName( imageFormatName );
                if ( iter.hasNext() ) {
                    imageWriter = iter.next();
                }
                if ( imageWriter == null ) {
                    return null;
                }

                // Make an Image Output Stream for more efficient output.
                try ( final ImageOutputStream imageOutputStream = ImageIO
                        .createImageOutputStream( outputStream ) ) {
                    // Assign the Image Writer to the Image Output Stream.
                    //
                    // Although the API supports using any Output Stream, we get
                    // runtime errors if it isn't an Image Output Stream.
                    imageWriter.setOutput( imageOutputStream );

                    // Set the compression quality.
                    //
                    // We use explicit compression ratios vs. string-matching
                    // compression quality to "good", "fine", etc., so we have
                    // no need for Locale and can set it to null via defaults.
                    final ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
                    switch ( imageFormatNameCaseInsensitive ) {
                    case "jpg":
                    case "jpeg":
                    case "jpe":
                        imageWriteParam.setCompressionMode( ImageWriteParam.MODE_EXPLICIT );
                        imageWriteParam.setCompressionQuality( compressionQuality );
                        break;
                    case "png":
                        imageWriteParam.setProgressiveMode( ImageWriteParam.MODE_DEFAULT );
                        break;
                    default:
                        break;
                    }

                    // Construct an Image I/O API custom Image object, sans
                    // thumbnail image, and sans image metadata.
                    final IIOImage iioImage = new IIOImage( bufferedImage, null, null );

                    // Write the Buffered Image to the Output Stream via the
                    // Image Writer, sans stream metadata.
                    imageWriter.write( null, iioImage, imageWriteParam );
                }
                catch ( NullPointerException | IOException e ) {
                    e.printStackTrace();
                    return null;
                }

                return bufferedImage;
            }

            // As long as no compression or other customization is needed, it is
            // simpler and less risky to use the default Image Writer.
            final boolean succeeded = ImageIO.write( bufferedImage, imageFormatName, outputStream );

            // Cleanup.
            outputStream.flush();

            return succeeded ? bufferedImage : null;
        }
        catch ( final IOException | IllegalArgumentException | IllegalStateException
                | UnsupportedOperationException | NoSuchElementException e ) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method take a provided AWT (or Swing) {@link Component} and
     * renders it to a {@link BufferedImage}.
     *
     * @param component
     *            The {@link Component} to render to an output stream
     * @param pixelWidth
     *            The preferred width in pixels for the produced image
     * @param pixelHeight
     *            The preferred height in pixels for the produced image
     * @param autoSizeImage
     *            {@code true} if the image should be auto-sized; {@code false}
     *            if the real Aspect Ratio should be retained
     * @param imageType
     *            The Image Type to use for the produced image
     * @return The JPEG {@link Image} that was written to the supplied
     *         {@link OutputStream}
     *
     * @since 1.0
     */
    private static BufferedImage renderComponent( final Component component,
                                                  final double pixelWidth,
                                                  final double pixelHeight,
                                                  final boolean autoSizeImage,
                                                  final int imageType ) {
        final int componentWidth = component.getWidth();
        final int componentHeight = component.getHeight();

        double imageWidth = componentWidth;
        double imageHeight = componentHeight;

        // If not in Image Auto-Size Mode, adjust the image for the real Aspect
        // Ratio, then use it to create a Buffered Image. The larger of the
        // desired Pixel Dimensions is preferred.
        if ( !autoSizeImage ) {
            final double componentAspectRatio = componentWidth / ( double ) componentHeight;
            final double pixelAspectRatio = pixelWidth / pixelHeight;

            // Prioritize the larger of the Pixel Dimensions, and then match the
            // other to the Aspect Ratio of the Component.
            if ( componentAspectRatio >= pixelAspectRatio ) {
                if ( pixelWidth >= pixelHeight ) {
                    imageWidth = pixelWidth;
                    imageHeight = ( int ) FastMath.round( pixelWidth / componentAspectRatio );
                }
                else {
                    imageWidth = ( int ) FastMath.round( pixelHeight * componentAspectRatio );
                    imageHeight = pixelHeight;
                }
            }
            else {
                if ( pixelWidth >= pixelHeight ) {
                    imageWidth = ( int ) FastMath.round( pixelHeight * componentAspectRatio );
                    imageHeight = pixelHeight;
                }
                else {
                    imageWidth = pixelWidth;
                    imageHeight = ( int ) FastMath.round( pixelWidth / componentAspectRatio );
                }
            }
        }

        // Set up the basic Buffered Image metrics for the Component.
        final BufferedImage bufferedImage = new BufferedImage( componentWidth,
                                                               componentHeight,
                                                               imageType );

        // Create a Graphics Context on the Buffered Image.
        final Graphics2D g2 = bufferedImage.createGraphics();

        // Paint the Component's current contents onto the Graphics Context.
        component.paint( g2 );

        // Dispose of the no longer needed Graphics Context.
        g2.dispose();

        // Set up the scaled Buffered Image metrics for the Component.
        BufferedImage scaledImage = new BufferedImage( ( int ) FastMath.round( imageWidth ),
                                                       ( int ) FastMath.round( imageHeight ),
                                                       imageType );

        // Scale the image using bilinear interpolation, so that text labels are
        // legible and raster transitions are more precise.
        final double sx = imageWidth / componentWidth;
        final double sy = imageHeight / componentHeight;
        final AffineTransform imageScale = AffineTransform.getScaleInstance( sx, sy );
        final AffineTransformOp op = new AffineTransformOp( imageScale,
                                                            AffineTransformOp.TYPE_BILINEAR );
        scaledImage = op.filter( bufferedImage, scaledImage );

        // Return the scaled image.
        return scaledImage;
    }

}
