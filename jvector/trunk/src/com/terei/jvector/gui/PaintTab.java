/*
 * Copyright 2004 David Terei
 * 
 * This file is part of JVector.
 * 
 * JVector is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
package com.terei.jvector.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.terei.jvector.paint.PaintCanvas;
import com.terei.jvector.paint.Tool;
import com.terei.jvector.paint.shapes.Shape;

/**
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 * 
 * @author David Terei
 * @since 21/04/2004 (dd//mm/yyyy)
 * @version 0.6
 */
@SuppressWarnings("serial")
public class PaintTab extends JPanel {
            
    private static final int ScrollFixY = 35;
	private static final int ScrollFixX = 10;
	
	/**
     * The ScrollPane used when the canvas are is too large.
     */
    public JScrollPane jsp = new JScrollPane();
    /**
     * The actual JPanel the drawing takes place on.
     * The canvas is setup this way, with;
     * <p><center> JPanel -> JScrollPane -> JPanel </center></p>
     * This is done this way due to layout issues.
     */
    private JPanel jp;
    /**
     * The current size of PaintCanvas, used in conjunction with
     * SIZE_OLD to zoom in and out.
     */
    public Point2D.Double SIZE_CURRENT = new Point2D.Double();
    /**
     * The previous size of PaintCanvas, used in conjunction with
     * SIZE_CURRENT to zoom in and out.
     */
    public Point2D.Double SIZE_OLD = new Point2D.Double();    
    
    /**
     * Creates a new paint tab with the paramaters specified.
     * 
     * @param name The name of the tab and drawing.
     * @param x1 The width of the drawing.
     * @param y1 The height of the drawing.
     * @param x2 The width of the tab.
     * @param y2 The height of the tab.
     * @param zoom The zoom of the drawing.
     * @param shapes An array of shapes to add to the image.
     */
    public PaintTab(String name, int x1, int y1, int x2, int y2, 
    		int zoom, Shape[] shapes) {
        //Setup the resizing values
        SIZE_CURRENT.x = x1;
        SIZE_CURRENT.y = y1;
        SIZE_OLD.x = x1;
        SIZE_OLD.y = y1; 
        //Setup the canvas
        //BoxLayout boxlay = new BoxLayout(this,BoxLayout.Y_AXIS);
        
        //This little step here stops the creation of a new
        //paint canvas from resetting the selected tool to null.
        Tool TOOL = PaintCanvas.getTool();
        jp = new PaintCanvas(name, x1, y1, zoom, this, shapes);
        setTool(TOOL);

        resizeView(x1,y1,x2,y2);
    }
    
    /**
     * Cleans up this class and its subclasses so that it 
     * is collected by the garbage collector.
     */
    public void dispose() {
        getCanvas().dispose();
        jp = null;
        jsp = null;
        SIZE_CURRENT = null;
        SIZE_OLD = null;
    }
    
    /**
     * Set the currently selected tool.
     * This applys for all paint canvases.
     * 
     * @param TOOL The tool to set it to
     * @see com.terei.jvector.paint.PaintCanvas
     */
    public static void setTool(Tool TOOL) {
        PaintCanvas.setTool(TOOL);
    }
    
    /**
     * This method setups the bounds for the scroll pane
     * so that it fits in the tab correctly, and does not 
     * display the scroll bars if not needed.
     * 
     * @param x1 The width of the drawing.
     * @param y1 The height of the drawing.
     * @param x2 The width of the tab. 
     * @param y2 The height of the tab.
     */
    public void resizeView(double x1, double y1, double x2, double y2) {
        //TODO Whole GUI resizing code needs to be redone.    	
        jp.setPreferredSize(new Dimension((int)x1,(int)y1));
        
        SIZE_OLD.x = SIZE_CURRENT.x;
        SIZE_OLD.y = SIZE_CURRENT.y;
        
        this.removeAll();
        //the dimensions for the tab.
        Dimension d_this = new Dimension((int)x2,(int)y2);
        //the dimensions for the scroll pane, assuming the image
        //is smaller then the tab.
        Dimension d_jsp = new Dimension((int)x2-ScrollFixX,(int)y2-ScrollFixY);
        
        //because we are assuming the scroll pane / image will fit into the tab
        //we remove the scroll bars.
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        //check to see if our assumptions were wrong.
        if(d_this.width>(int)x1) {
            //in this case, the image is bigger then the tab.
            //therefore, set the scroll pane size to the tab, 
            //and enable the scroll bars.
            jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            d_jsp.width = (int)x1;
        }
        
        //do the same but with the y axis.
        //note, they are done independently ofcourse for non square
        //images.
        if(d_this.height>(int)y1) {
            jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            d_jsp.height = (int)y1;
            int buffer = (d_this.height-(int)y1)/2-20;
            this.add(Box.createRigidArea(new Dimension(d_this.width,buffer)));
        }
        
        //okay, sizes are all worked out.
        //so now lets actually set them.
        jsp.setAlignmentY(Component.CENTER_ALIGNMENT);
        jsp.setMaximumSize(d_jsp);
        jsp.setPreferredSize(d_jsp);
        //add the image to the scrollpane.
        jsp.setViewportView(jp);
        //add the scrollpane to the tab.
        this.add(jsp);
        this.revalidate();
        
        SIZE_CURRENT.x = x1;
        SIZE_CURRENT.y = y1;
    }
    
    /**
     * Retrieve the {@link PaintCanvas PaintCanvas} used in this tab.
     * 
     * @return Returns the PaintCanvas.
     */
    public PaintCanvas getCanvas() {
        return (PaintCanvas)jp;
    }
   
}
