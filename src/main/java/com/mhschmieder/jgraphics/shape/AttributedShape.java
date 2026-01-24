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
package com.mhschmieder.jgraphics.shape;

import com.mhschmieder.jgraphics.DrawMode;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

/**
 * {@code AttributedShape} holds a Graphics2D {@link Shape} alongside related
 * attribute information. Only the most basic attributes are modeled at this
 * time, such as pen color, Draw Mode, and the transform to apply to a shape
 * that is defined at the origin. Specialty attributes such as stroke, and
 * layers, are often temporal and thus aren't modeled in this class.
 * <p>
 * The data members are currently declared as public, simply because they are
 * expected to be accessed in performance-sensitive tight loops during bulk
 * graphics operations. This class was written before Functional Programming
 * concepts were introduced to Java, so it is a candidate for refactoring.
 * <p>
 * Additionally, this class is currently modeled primarily as one that is
 * instantiated and then left as-is. This is due to its original purpose being
 * to serve as a container for shapes and their attributes while importing
 * graphics from file formats such as AutoCAD DXF. If the need arises, we can
 * add getter and setter methods, and consider making the data private.
 *
 * @version 1.0
 *
 * @author Mark Schmieder
 */
public class AttributedShape {

    /**
     * The {@link Shape} that is modified by the extra attributes.
     */
    public final Shape           shape;

    /**
     * The pen color to use for drawing or filling this shape.
     */
    public final Color           penColor;

    /**
     * The {@link DrawMode} to use for rendering this shape (Stroke or Fill).
     */
    public final DrawMode        drawMode;

    /**
     * The {@link AffineTransform} to apply to the shape to place, scale, and
     * orient it in Model Space. Identity Matrix if Shape is pre-transformed.
     */
    public final AffineTransform transform;

    //////////////////////////// Constructors ////////////////////////////////

    /**
     * This is the default constructor, to use when an unattributed
     * {@link Shape} needs to be used alongside ones that are attributed, or has
     * default attribute values.
     *
     * @param unattributedShape
     *            The original unattributed {@link Shape}
     *
     * @since 1.0
     */
    public AttributedShape( final Shape unattributedShape ) {
        this( unattributedShape, null, DrawMode.defaultValue() );
    }

    /**
     * This is the partially specified constructor, to use when the
     * {@link Shape} has known non-default attributes and is pre-transformed.
     *
     * @param unattributedShape
     *            The original unattributed {@link Shape}
     * @param color
     *            The {@link Color} to assign to the pen (foreground)
     * @param parentDrawMode
     *            The {@link DrawMode} to use for rendering this shape, usually
     *            passed from a parent context
     *
     * @since 1.0
     */
    public AttributedShape( final Shape unattributedShape,
                            final Color color,
                            final DrawMode parentDrawMode ) {
        this( unattributedShape, color, parentDrawMode, new AffineTransform() );
    }

    /**
     * This is the fully specified constructor, to use when the {@link Shape}
     * has known non-default attributes and needs to apply a transform.
     *
     * @param unattributedShape
     *            The original unattributed {@link Shape}
     * @param color
     *            The {@link Color} to assign to the pen (foreground)
     * @param parentDrawMode
     *            The {@link DrawMode} to use for rendering this shape, usually
     *            passed from a parent context
     * @param affineTransform
     *            The {@link AffineTransform} to apply to the shape
     *
     * @since 1.0
     */
    public AttributedShape( final Shape unattributedShape,
                            final Color color,
                            final DrawMode parentDrawMode,
                            final AffineTransform affineTransform ) {
        shape = unattributedShape;
        transform = affineTransform;
        penColor = color;
        drawMode = parentDrawMode;
    }

}
