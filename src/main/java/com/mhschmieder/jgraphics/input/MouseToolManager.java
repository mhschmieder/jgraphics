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
package com.mhschmieder.jgraphics.input;

import javafx.scene.input.MouseEvent;

/**
 * A wrapper for variables and state associated with managing both the Active 
 * Mouse Tool and transitive intramodal changes that don't affect the selected
 * tool but take precedence until conditions and actions release the transitive
 * state. Considerations for undo/redo support are also included here.
 */
public class MouseToolManager {

    /**
     * Declare variable to keep track of the current active tool (default to
     * Select Mode).
     */
    public MouseToolMode _activeMouseTool;

    /**
     * Declare variable to keep track of the modified mouse mode (default to
     * Select Mode).
     */
    public MouseToolMode _mouseMode;

    /**
     * Declare a flag to keep track of whether the SHIFT key was engaged before
     * the mouse event.
     */
    public boolean _shiftKeyDown;

    /**
     * Declare a flag to keep track of whether the CONTROL key was engaged
     * before the mouse event.
     */
    public boolean _shortcutKeyDown;

    /**
     * Declare a flag to keep track of whether the ALT key was engaged before
     * the mouse event.
     */
    public boolean _altKeyDown;

    public MouseToolManager() {
        _activeMouseTool = MouseToolMode.SELECT;
        _mouseMode = MouseToolMode.SELECT;

        _shiftKeyDown = false;
        _shortcutKeyDown = false;
        _altKeyDown = false;
    }

    public MouseToolMode getActiveMouseTool() {
        return _activeMouseTool;
    }

    public MouseToolMode getMouseMode() {
        return _mouseMode;
    }

    // NOTE: This method should only be called from a Mouse Pressed event.
    public void initMouseMode( final MouseEvent mouseEvent ) {
        // Filter for accelerators/shortcuts that are globally applied.
        // TODO: Verify that "isAltDown()" also corresponds to the middle button
        //  or scroll wheel of a three "button" mouse being depressed, as well
        //  as if the keyboard's ALT or option key (macOS) is depressed.
        _shiftKeyDown = mouseEvent.isShiftDown();
        _altKeyDown = mouseEvent.isAltDown();
        _shortcutKeyDown = mouseEvent.isShortcutDown();

        // If multiple accelerators/shortcuts are engaged, ignore them all.
        // NOTE: A cascaded logical XOR gets the wrong result when all three
        //  accelerators are engaged!
        if ( ( _shiftKeyDown && _altKeyDown ) || ( _altKeyDown && _shortcutKeyDown )
                || ( _shiftKeyDown && _shortcutKeyDown ) ) {
            _shiftKeyDown = false;
            _altKeyDown = false;
            _shortcutKeyDown = false;
        }

        // Re-map the Mouse Mode if a relevant modifier was activated; otherwise
        // use the Active Mouse Tool as the momentary Mouse Mode, or leave as-is
        // if in a special mode such as Paste Mode, which is also momentary.
        if ( _shiftKeyDown ) {
            // If Shift key was engaged, switch to Select Mode.
            _mouseMode = MouseToolMode.SELECT;
        }
        else if ( _altKeyDown || _shortcutKeyDown ) {
            // If ALT or Shortcut key was engaged, switch to Zoom Mode.
            _mouseMode = MouseToolMode.ZOOM;
        }
        else if ( !MouseToolMode.PASTE.equals( _mouseMode ) ) {
            _mouseMode = _activeMouseTool;
        }
    }

    // Reset the Mouse Mode to match the Active Mouse Tool, to end Cut/Copy.
    public void resetMouseMode() {
        // Restore the previous Active Mouse Tool, as Copy, Cut, and Paste
        // Confirm are only meant to be transitory Mouse Modes.
        _mouseMode = _activeMouseTool;
    }
}
