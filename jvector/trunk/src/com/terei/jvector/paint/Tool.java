/*
 * Copyright 2004 David Terei
 * 
 * This file is part of JVector.
 * 
 * JVector is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * any later version.
 * 
 * JVector is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with JVector; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.terei.jvector.paint;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import com.terei.jvector.JVector;

/**
 * @author David Terei
 * @version 1
 * @since 4/09/2005
 */
public enum Tool {
	
	NULL(0, new Cursor(Cursor.DEFAULT_CURSOR)),
	SELECT(1,new Cursor(Cursor.MOVE_CURSOR)),
	LINE(2, new Cursor(Cursor.CROSSHAIR_CURSOR)),
	RECTANGLE(3, new Cursor(Cursor.CROSSHAIR_CURSOR)),
	OVAL(4, new Cursor(Cursor.CROSSHAIR_CURSOR)),
	POLYGON(5, new Cursor(Cursor.CROSSHAIR_CURSOR)),
	TEXT(6, new Cursor(Cursor.TEXT_CURSOR)),
	DELETE(7, new Cursor(Cursor.DEFAULT_CURSOR)),
	ZOOM(8, Tool.getZoomCurs());
	
	private int value;
	private Cursor curs;
	
	private Tool(int value, Cursor curs) {
		this.value = value;
		this.curs = curs;
	}
	
	Cursor getCursor() { return curs; }
	
	int value() { return value; }
	
	private static Cursor getZoomCurs() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image img = JVector.loadImageIcon("Resources/zoom_cursor.png").getImage();
        Point p = new Point(img.getWidth(null)/2, img.getHeight(null)/2);
       	return toolkit.createCustomCursor(img, p, "Zoom"); 
	}

}
