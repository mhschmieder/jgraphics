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
package com.mhschmieder.jgraphics.shape;

import com.mhschmieder.jgraphics.DrawMode;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

/**
 * {@code AttributedShapeContainer} is a container class for Graphics2D shapes
 * and their attributes. As with the {@link AttributedShape} class itself, the
 * scale transform is presumed to be constant and only needs to be set at
 * construction time. so there is no setter method for the scale transform.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public final class AttributedShapeContainer {

    /**
     * Declare the list of Shapes for this Geometry container.
     */
    private final ArrayList< AttributedShape > shapes2D;

    /**
     * Current scale factor, based on the Model Space's Unit of Measurement.
     */
    private final AffineTransform              scaleTransform;

    //////////////////////////// Constructors ////////////////////////////////

    /**
     * This is the default constructor, to use when there is no idea of how many
     * shapes will be needed.
     *
     * @since 1.0
     */
    public AttributedShapeContainer() {
        this( 0 );
    }

    /**
     * This is the partially specified constructor, to use when an initial
     * capacity rough guess is possible (this improves performance vs. setting
     * to zero or the usual Java default of 16), but when the scale transform is
     * uniform and thus not needed.
     *
     * @param initialCapacity
     *            The initial capacity of the shapes list
     *
     * @since 1.0
     */
    public AttributedShapeContainer( final int initialCapacity ) {
        this( initialCapacity, new AffineTransform() );
    }

    /**
     * This is the fully specified constructor, to use when an initial capacity
     * rough guess is possible (this improves performance vs. setting to zero or
     * the usual Java default of 16), and when the scale transform is
     * non-uniform and thus is needed.
     *
     * @param initialCapacity
     *            The initial capacity of the shapes list
     * @param parentScaleTransform
     *            The scale transform to apply to the entire collection of
     *            shapes, usually from a parent context
     *
     * @since 1.0
     */
    public AttributedShapeContainer( final int initialCapacity,
                                     final AffineTransform parentScaleTransform ) {
        shapes2D = new ArrayList<>( initialCapacity );
        scaleTransform = parentScaleTransform;
    }

    /**
     * This method clones this container, and then separately clones the list
     * of shapes herein
     *
     * @return The cloned copy of this {@link AttributedShapeContainer}
     *
     * @since 1.0
     */
    @Override
    public Object clone() {
        try {
            final AttributedShapeContainer geoms = ( AttributedShapeContainer ) super.clone();
            geoms.shapes2D.clone();
            return geoms;
        }
        catch ( final CloneNotSupportedException cnse ) {
            return null;
        }
    }

    ////////////////// Accessor methods for private data /////////////////////

    /**
     * Returns the list of {@link AttributedShape} objects that are wrapped in
     * this container, as an {@link ArrayList} in order to allow list resizing.
     * <p>
     * This method returns the {@link ArrayList} of {@link AttributedShape}
     * objects in this container, modeled as an {@link ArrayList} as downstream
     * clients will assume that each member of the collection is unique and not
     * repeated, as well as being ordered.
     *
     * @return The {@link ArrayList} of {@link AttributedShape} objects in this
     *         container
     *
     * @since 1.0
     */
    public ArrayList< AttributedShape > getAttributedShapes() {
        return shapes2D;
    }

    /**
     * Returns the {@link AffineTransform} representing the global Scale Factor.
     * <p>
     * This method returns the {@link AffineTransform} corresponding to the
     * scale factor to apply to the shapes in this collection, which is separate
     * from the transforms that exist on each individual shape.
     *
     * @return The {@link AffineTransform} corresponding to the scale factor to
     *         apply to the shapes in this collection
     *
     * @since 1.0
     */
    public AffineTransform getScaleTransform() {
        return scaleTransform;
    }

    ////////////////////// Shape Collection methods //////////////////////////

    /**
     * Returns the number of shapes in this container.
     * <p>
     * This method returns the number of shapes currently in the collection.
     * Note that this is the number of actual shapes; not the allocated size of
     * the collection.
     *
     * @return The number of shapes in this collection
     *
     * @since 1.0
     */
    public int getNumberOfShapes() {
        return shapes2D.size();
    }

    /**
     * Clears the current list of {@link AttributedShape} objects.
     *
     * @since 1.0
     */
    public void clearShapes() {
        shapes2D.clear();
    }

    /**
     * Adds a {@link Shape} to the collection, with all attribute values
     * defaulted.
     *
     * @param shape
     *            The {@link Shape} to add to the collection
     *
     * @since 1.0
     */
    public void addShape( final Shape shape ) {
        addShape( shape, null );
    }

    /**
     * Adds a {@link Shape} to the collection, with all attribute values
     * defaulted except for pen color.
     * <p>
     * As this is part of a set of cascading methods, the specified pen color
     * attribute can be null or pre-defaulted.
     *
     * @param shape
     *            The {@link Shape} to add to the collection
     * @param color
     *            The pen color to apply to the {@link Shape}
     *
     * @since 1.0
     */
    public void addShape( final Shape shape, final Color color ) {
        addShape( shape, color, DrawMode.defaultValue() );
    }

    /**
     * Adds a {@link Shape} to the collection, with pen color and
     * {@link DrawMode} specified but using a default {@link AffineTransform}.
     * <p>
     * As this is part of a set of cascading methods, either specified attribute
     * can be null or pre-defaulted.
     *
     * @param shape
     *            The {@link Shape} to add to the collection
     * @param color
     *            The pen color to apply to the {@link Shape}
     * @param parentDrawMode
     *            The {@link DrawMode} to use for rendering the shape, usually
     *            passed from a parent context
     *
     * @since 1.0
     */
    public void addShape( final Shape shape, final Color color, final DrawMode parentDrawMode ) {
        addShape( shape, color, parentDrawMode, null );
    }

    /**
     * Adds a {@link Shape} to the collection, with pen color, {@link DrawMode},
     * and {@link AffineTransform} specified.
     * <p>
     * As this is part of a set of cascading methods, any or all of the
     * attributes can be null or pre-defaulted.
     *
     * @param shape
     *            The {@link Shape} to add to the collection
     * @param color
     *            The pen color to apply to the {@link Shape}
     * @param parentDrawMode
     *            The {@link DrawMode} to use for rendering the shape, usually
     *            passed from a parent context
     * @param affineTransform
     *            The {@link AffineTransform} to apply to the shape
     *
     * @since 1.0
     */
    public void addShape( final Shape shape,
                          final Color color,
                          final DrawMode parentDrawMode,
                          final AffineTransform affineTransform ) {
        if ( color == null ) {
            shapes2D.add( new AttributedShape( shape ) );
        }
        else if ( affineTransform == null ) {
            shapes2D.add( new AttributedShape( shape, color, parentDrawMode ) );
        }
        else {
            shapes2D.add( new AttributedShape( shape, color, parentDrawMode, affineTransform ) );
        }
    }

}
