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
package com.mhschmieder.jgraphics.geometry;

import org.apache.commons.math3.util.FastMath;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

/**
 * This class is a Polygon with float coordinates.
 */
public class Polygon2D implements Shape, Cloneable, Serializable {

    /**
     * The total number of points.  The value of <code>npoints</code>
     * represents the number of valid points in this <code>Polygon</code>.
     */
    public int npoints;

    /**
     * The array of <i>x</i> coordinates. The value of {@link #npoints npoints}
     * is equal to the number of points in this <code>Polygon2D</code>.
     */
    public float[] xpoints;

    /**
     * The array of <i>x</i> coordinates. The value of {@link #npoints npoints}
     * is equal to the number of points in this <code>Polygon2D</code>.
     */
    public float[] ypoints;

    /**
     * Bounds of the Polygon2D.
     *
     * @see #getBounds()
     */
    protected Rectangle2D bounds;

    private GeneralPath path;
    private GeneralPath closedPath;

    /**
     * Creates an empty Polygon2D.
     */
    public Polygon2D() {
        xpoints = new float[4];
        ypoints = new float[4];
    }

    /**
     * Constructs and initializes a <code>Polygon2D</code> from the specified
     * Rectangle2D.
     *
     * @param rect the Rectangle2D
     * @throws NullPointerException rect is <code>null</code>.
     */
    public Polygon2D(Rectangle2D rect) {
        if (rect == null) {
            throw new IndexOutOfBoundsException("null Rectangle");
        }
        npoints = 4;
        xpoints = new float[4];
        ypoints = new float[4];
        xpoints[0] = (float) rect.getMinX();
        ypoints[0] = (float) rect.getMinY();
        xpoints[1] = (float) rect.getMaxX();
        ypoints[1] = (float) rect.getMinY();
        xpoints[2] = (float) rect.getMaxX();
        ypoints[2] = (float) rect.getMaxY();
        xpoints[3] = (float) rect.getMinX();
        ypoints[3] = (float) rect.getMaxY();
        calculatePath();
    }

    /**
     * Constructs and initializes a <code>Polygon2D</code> from the specified
     * Polygon.
     *
     * @param polygon the Polygon
     * @throws NullPointerException pol is <code>null</code>.
     */
    public Polygon2D(Polygon polygon) {
        if (polygon == null) {
            throw new IndexOutOfBoundsException("null Polygon");
        }
        this.npoints = polygon.npoints;
        this.xpoints = new float[polygon.npoints];
        this.ypoints = new float[polygon.npoints];
        for (int i = 0; i < polygon.npoints; i++) {
            xpoints[i] = polygon.xpoints[i];
            ypoints[i] = polygon.ypoints[i];
        }
        calculatePath();
    }

    /**
     * Constructs and initializes a <code>Polygon2D</code> from the specified
     * parameters.
     *
     * @param xpoints an array of <i>x</i> coordinates
     * @param ypoints an array of <i>y</i> coordinates
     * @param npoints the total number of points in the <code>Polygon2D</code>
     * @throws NegativeArraySizeException if the value of
     *                                    <code>npoints</code> is negative.
     * @throws IndexOutOfBoundsException  if <code>npoints</code> is
     *                                    greater than the length of <code>xpoints</code>
     *                                    or the length of <code>ypoints</code>.
     * @throws NullPointerException       if <code>xpoints</code> or
     *                                    <code>ypoints</code> is <code>null</code>.
     */
    public Polygon2D(float[] xpoints, float[] ypoints, int npoints) {
        if (npoints > xpoints.length || npoints > ypoints.length) {
            throw new IndexOutOfBoundsException(
                    "npoints > xpoints.length || npoints > ypoints.length");
        }
        this.npoints = npoints;
        this.xpoints = new float[npoints];
        this.ypoints = new float[npoints];
        System.arraycopy(xpoints, 0, this.xpoints, 0, npoints);
        System.arraycopy(ypoints, 0, this.ypoints, 0, npoints);
        calculatePath();
    }

    /**
     * Constructs and initializes a <code>Polygon2D</code> from the specified
     * parameters.
     *
     * @param xpoints an array of <i>x</i> coordinates
     * @param ypoints an array of <i>y</i> coordinates
     * @param npoints the total number of points in the <code>Polygon2D</code>
     * @throws NegativeArraySizeException if the value of
     *                                    <code>npoints</code> is negative.
     * @throws IndexOutOfBoundsException  if <code>npoints</code> is
     *                                    greater than the length of <code>xpoints</code>
     *                                    or the length of <code>ypoints</code>.
     * @throws NullPointerException       if <code>xpoints</code> or
     *                                    <code>ypoints</code> is <code>null</code>.
     */
    public Polygon2D(int[] xpoints, int[] ypoints, int npoints) {
        if (npoints > xpoints.length || npoints > ypoints.length) {
            throw new IndexOutOfBoundsException(
                    "npoints > xpoints.length || npoints > ypoints.length");
        }
        this.npoints = npoints;
        this.xpoints = new float[npoints];
        this.ypoints = new float[npoints];
        for (int i = 0; i < npoints; i++) {
            this.xpoints[i] = xpoints[i];
            this.ypoints[i] = ypoints[i];
        }
        calculatePath();
    }

    /**
     * Resets this <code>Polygon</code> object to an empty polygon.
     */
    public void reset() {
        npoints = 0;
        bounds = null;
        path = new GeneralPath();
        closedPath = null;
    }

    public Object clone() {
        Polygon2D pol = new Polygon2D();
        for (int i = 0; i < npoints; i++) {
            pol.addPoint(xpoints[i], ypoints[i]);
        }
        return pol;
    }

    private void calculatePath() {
        path = new GeneralPath();
        path.moveTo(xpoints[0], ypoints[0]);
        for (int i = 1; i < npoints; i++) {
            path.lineTo(xpoints[i], ypoints[i]);
        }
        bounds = path.getBounds2D();
        closedPath = null;
    }

    private void updatePath(float x, float y) {
        closedPath = null;
        if (path == null) {
            path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
            path.moveTo(x, y);
            bounds = new Rectangle2D.Float(x, y, 0, 0);
        } else {
            path.lineTo(x, y);
            float _xmax = (float) bounds.getMaxX();
            float _ymax = (float) bounds.getMaxY();
            float _xmin = (float) bounds.getMinX();
            float _ymin = (float) bounds.getMinY();
            if (x < _xmin) _xmin = x;
            else if (x > _xmax) _xmax = x;
            if (y < _ymin) _ymin = y;
            else if (y > _ymax) _ymax = y;
            bounds = new Rectangle2D.Float(
                    _xmin, _ymin, _xmax - _xmin, _ymax - _ymin);
        }
    }

    /**
     * Returns the generated {@link Polyline2D}.
     *
     * @return The generated {@link Polyline2D}
     */
    public Polyline2D makePolyline2D() {
        Polyline2D polygon = new Polyline2D(xpoints, ypoints, npoints);

        polygon.addPoint(xpoints[0], ypoints[0]);

        return polygon;
    }

    /**
     * Returns the generated integer-based {@link Polygon}.
     *
     * @return The generated integer-based {@link Polygon}
     */
    public Polygon makePolygon() {
        int[] _xpoints = new int[npoints];
        int[] _ypoints = new int[npoints];

        // NOTE: It is safer to round than to take floor or ceil, as we might
        //  otherwise end up with a degenerate shape that is 1D vs. 2D.
        for (int i = 0; i < npoints; i++) {
            _xpoints[i] = FastMath.round(xpoints[i]);
            _ypoints[i] = FastMath.round(ypoints[i]);
        }

        return new Polygon(_xpoints, _ypoints, npoints);
    }

    public void addPoint(Point2D p) {
        addPoint((float)p.getX(), (float)p.getY());
    }

    /**
     * Appends the specified coordinates to this {@code Polygon2D}.
     *
     * @param       x the specified x coordinate
     * @param       y the specified y coordinate
     */
    public void addPoint(float x, float y) {
        if (npoints == xpoints.length) {
            float[] tmp;

            tmp = new float[npoints * 2];
            System.arraycopy(xpoints, 0, tmp, 0, npoints);
            xpoints = tmp;

            tmp = new float[npoints * 2];
            System.arraycopy(ypoints, 0, tmp, 0, npoints);
            ypoints = tmp;
        }
        xpoints[npoints] = x;
        ypoints[npoints] = y;
        npoints++;
        updatePath(x, y);
    }

    /**
     * Determines whether the specified {@link Point} is inside this
     * {@code Polygon2D}.
     *
     * @param p the specified {@code Point} to be tested
     * @return {@code true} if the {@code Polygon2D} contains the {@code Point};
     *         {@code false} otherwise.
     * @see #contains(double, double)
     */
    public boolean contains(Point p) {
        return contains(p.x, p.y);
    }

    /**
     * Determines whether the specified coordinates are inside this
     * {@code Polygon2D}.
     * <p>
     * @param x the specified x coordinate to be tested
     * @param y the specified y coordinate to be tested
     * @return  {@code true} if this {@code Polygon2D} contains the specified
     *          coordinates, (<i>x</i>,&nbsp;<i>y</i>); {@code false} otherwise.
     */
    public boolean contains( int x, int y ) {
        return contains( x, (double) y );
    }

    /**
     * Returns the high precision bounding box of the {@link Shape}.
     * @return a {@link Rectangle2D} that precisely bounds the {@code Shape}.
     */
    public Rectangle2D getBounds2D() {
        return bounds;
    }

    public Rectangle getBounds() {
        if (bounds == null) return null;
        else return bounds.getBounds();
    }

    /**
     * Determines if the specified coordinates are inside this {@code
     * Polygon2D}. For the definition of <i>insideness</i>, see the class
     * comments of {@link Shape}.
     *
     * @param x the specified x coordinate
     * @param y the specified y coordinate
     * @return {@code true} if the {@code Polygon2D} contains the specified
     *         coordinates; {@code false} otherwise.
     */
    public boolean contains(double x, double y) {
        if (npoints <= 2 || !bounds.contains(x, y)) {
            return false;
        }
        updateComputingPath();

        return closedPath.contains(x, y);
    }

    private void updateComputingPath() {
        if (npoints >= 1) {
            if (closedPath == null) {
                closedPath = (GeneralPath)path.clone();
                closedPath.closePath();
            }
        }
    }

    /**
     * Tests if a specified {@link Point2D} is inside the boundary of this
     * <code>Polygon</code>.
     * @param p a specified <code>Point2D</code>
     * @return <code>true</code> if this <code>Polygon</code> contains the
     *                 specified <code>Point2D</code>; <code>false</code>
     *          otherwise.
     * @see #contains(double, double)
     */
    public boolean contains(Point2D p) {
        return contains(p.getX(), p.getY());
    }

    /**
     * Tests if the interior of this <code>Polygon</code> intersects the
     * interior of a specified set of rectangular coordinates.
     * @param x the x coordinate of the specified rectangular
     *                        shape's top-left corner
     * @param y the y coordinate of the specified rectangular
     *                        shape's top-left corner
     * @param w the width of the specified rectangular shape
     * @param h the height of the specified rectangular shape
     * @return <code>true</code> if the interior of this
     *                        <code>Polygon</code> and the interior of the
     *                        specified set of rectangular
     *                         coordinates intersect each other;
     *                        <code>false</code> otherwise.
     */
    public boolean intersects(double x, double y, double w, double h) {
        if (npoints <= 0 || !bounds.intersects(x, y, w, h)) {
            return false;
        }
        updateComputingPath();
        return closedPath.intersects(x, y, w, h);
    }

    /**
     * Tests if the interior of this <code>Polygon</code> intersects the
     * interior of a specified <code>Rectangle2D</code>.
     * @param r a specified <code>Rectangle2D</code>
     * @return <code>true</code> if this <code>Polygon</code> and the
     *                         interior of the specified <code>Rectangle2D</code>
     *                         intersect each other; <code>false</code>
     *                         otherwise.
     */
    public boolean intersects(Rectangle2D r) {
        return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    /**
     * Tests if the interior of this <code>Polygon</code> entirely
     * contains the specified set of rectangular coordinates.
     * @param x the x coordinate of the top-left corner of the
     *                         specified set of rectangular coordinates
     * @param y the y coordinate of the top-left corner of the
     *                         specified set of rectangular coordinates
     * @param w the width of the set of rectangular coordinates
     * @param h the height of the set of rectangular coordinates
     * @return <code>true</code> if this <code>Polygon</code> entirely
     *                         contains the specified set of rectangular
     *                         coordinates; <code>false</code> otherwise.
     */
    public boolean contains(double x, double y, double w, double h) {
        if (npoints <= 0 || !bounds.intersects(x, y, w, h)) {
            return false;
        }

        updateComputingPath();
        return closedPath.contains(x, y, w, h);
    }

    /**
     * Tests if the interior of this <code>Polygon</code> entirely
     * contains the specified <code>Rectangle2D</code>.
     * @param r the specified <code>Rectangle2D</code>
     * @return <code>true</code> if this <code>Polygon</code> entirely
     *                         contains the specified <code>Rectangle2D</code>;
     *                        <code>false</code> otherwise.
     * @see #contains(double, double, double, double)
     */
    public boolean contains(Rectangle2D r) {
        return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    /**
     * Returns an iterator object that iterates along the boundary of this
     * <code>Polygon</code> and provides access to the geometry
     * of the outline of this <code>Polygon</code>.  An optional
     * {@link AffineTransform} can be specified so that the coordinates
     * returned in the iteration are transformed accordingly.
     * @param at an optional <code>AffineTransform</code> to be applied to the
     *                 coordinates as they are returned in the iteration, or
     *                <code>null</code> if untransformed coordinates are desired
     * @return a {@link PathIterator} object that provides access to the
     *                geometry of this <code>Polygon</code>.
     */
    public PathIterator getPathIterator(AffineTransform at) {
        updateComputingPath();
        if (closedPath == null) return null;
        else return closedPath.getPathIterator(at);
    }

    /**
     * Returns an iterator object that iterates along the boundary of
     * the <code>Polygon2D</code> and provides access to the geometry of the
     * outline of the <code>Shape</code>.  Only SEG_MOVETO, SEG_LINETO, and
     * SEG_CLOSE point types are returned by the iterator.
     * Since polygons are already flat, the <code>flatness</code> parameter
     * is ignored.
     * @param at an optional <code>AffineTransform</code> to be applied to the
     *                 coordinates as they are returned in the iteration, or
     *                <code>null</code> if untransformed coordinates are desired
     * @param flatness the maximum amount that the control points
     *                 for a given curve can vary from colinear before a subdivided
     *                curve is replaced by a straight line connecting the
     *                 endpoints.  Since polygons are already flat the
     *                 <code>flatness</code> parameter is ignored.
     * @return a <code>PathIterator</code> object that provides access to the
     *                 <code>Shape</code> object's geometry.
     */
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return getPathIterator(at);
    }
}
