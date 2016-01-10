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
package com.terei.jvector.paint;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.JPanel;

import com.terei.jvector.JVector;
import com.terei.jvector.gui.PaintTab;
import com.terei.jvector.gui.tools.ZoomOptions;
import com.terei.jvector.paint.shapes.LineManager;
import com.terei.jvector.paint.shapes.OvalManager;
import com.terei.jvector.paint.shapes.PolygonManager;
import com.terei.jvector.paint.shapes.RectangleManager;
import com.terei.jvector.paint.shapes.Shape;
import com.terei.jvector.paint.shapes.ShapeManager;
import com.terei.jvector.paint.shapes.Shapes;
import com.terei.jvector.paint.shapes.TextManager;


/**
 * Provides The actual 'paint canvas' a JPanel that the
 * user interacts with directly to draw shapes. This class
 * passes on the draw commands to lower subclasses, and 
 * displays the results. This is the GUI frontend.
 * 
 * @author David Terei
 * @since 24/04/2004
 * @version 1
 */
@SuppressWarnings("serial")
public class PaintCanvas extends JPanel implements MouseListener, 
											MouseMotionListener {
    
    /**
     * Create a new PaintCanvas.
     * 
     * @param name The name of the Image this paint canvas will manage.
     * @param width The width of the Image this paint canvas will manage.
     * @param height The height of the Image this paint canvas will manage.
     * @param tab The {@link com.terei.jvector.gui.PaintTab PaintTab} that holds
     * this image.
     */
    public PaintCanvas(String name, int width, int height, PaintTab tab) {
        this(name, width, height, 100, tab, null);
    }
    
    /**
     * Create a new PaintCanvas, with some pre-existing shpes.
     * 
     * @param name The name of the Image this paint canvas will manage.
     * @param width The width of the Image this paint canvas will manage.
     * @param height The height of the Image this paint canvas will manage.
     * @param zoom The zoom of the image.
     * @param shapes The pre-existing {@link Shape shapes} to add to the 
     *               image this paint canvas will manage.
     * @param tab The {@link com.terei.jvector.gui.PaintTab PaintTab} that holds
     * 			  this image.
     */
    public PaintCanvas(String name, int width, int height, int zoom, 
    		PaintTab tab, Shape[] shapes) {      
        this.name = name;
        this.width = width;
        this.height = height;
        if (shapes!=null)
            addShapes(shapes);
        this.tab = tab;
        this.zoomLevel = zoom;
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setBackground(Color.WHITE);   
        this.setLayout(null);
        revalidateZoom();
    }   

    /**
     * The class which manages the drawing of a line.
     */
    private static LineManager lineM = new LineManager();    
    /**
     * The class which manages the drawing of a rectangle.
     */
    private static RectangleManager rectM = new RectangleManager();    
    /**
     * The class which manages the drawing of a oval.
     */
    private static OvalManager ovalM = new OvalManager();
    /**
     * The class which manages the drawing of a polygon.
     */
    private static PolygonManager polyM = new PolygonManager();
    /**
     * The class which manages the drawing of a string of text.
     */
    private static TextManager textM = new TextManager();
    //TODO Better method of managing and selecting the current tool?
    // More suited for a plugin enviornment.
    /**
     * The current Tool.
     */
    private static Tool TOOL = Tool.NULL;
    /**
     * The value for when the canvas is idle
     * (not currently in any draw operation).
     */
    public static final int MODE_IDLE = 0;
    /**
     * The value for when the canvas is active
     * (currently in a draw operation).
     */
    public static final int MODE_ACT = 1;
    /**
     * The state of the canvas, active or idle.
     */
    private static int MODE = 0;    
    
    /**
     * The current zoom level (in percentage) of the image.
     */
    private int zoomLevel = 100; 

    /**
     * The name of the Image this paint canvas is holding.
     */
    private String name;    
    /**
     * The width of the Image this paint canvas is holding.
     */
    private int width = 100;    
    /**
     * The height of the Image this paint canvas is holding.
     */
    private int height = 100;
    /**
     * The file that this 'graphic' saves to.
     */
    private File file;
    /**
     * The state of this 'graphic', saved or not.
     */
    private boolean saved = true;
    /**
     * The PaintTab that is holding this canvas. Needed for zoom operations.
     */
    private PaintTab tab;
    
	/**
	 * Holds all the shapes that have been drawn.
	 */
	private Vector<Shape> shapes = new Vector<Shape>(); 
	
	/**
	 * Holds the {@link com.terei.jvector.paint.shapes.Shape shape} 
     * currently being drawn. The variable stands for temporary shape.
	 */
	private Shape tShape;
    
    /**
     * Holds the active {@link com.terei.jvector.paint.shapes.Shape shape},
     * the one currently selected by the selection tool, to move around and
     * modify its properties. The variable stands for aShape.
     */
    private Shape aShape;
    
    /**
     * Add an array of {@link com.terei.jvector.paint.shapes.Shape shapes} to
     * the image.
     * 
     * @param shapes The shapes to add.
     */
    private void addShapes(Shape[] shapes) {
        for (Shape s : shapes)
            this.shapes.add(s);
        repaint();
    }
    
    /**
     * Get the current mode of the Canvas, active, idle...
     * 
     * @return Returns the Mode.
     */
    public static int getMode() {
        return MODE;
    }
    
    /**
     * Get the currently selected tool of the canvas.
     * 
     * @return Returns the Tool..
     */
    public static Tool getTool() {
        return TOOL;
    }
    
    /**
     * Gets an {@link ImageFile ImageFile} that contains the current image.
     * 
     * @return An ImageFile containg the current Image.
     */
    public ImageFile getImage() {
        Shape[] shaps = new Shape[shapes.size()];
        for (int i=0; i<shaps.length; i++)
            shaps[i] = shapes.get(i);
;
        ImageFile file = new ImageFile(name, width, height, zoomLevel, shaps);
        
        return file;
    }
    
    /**
     * Sets the tool that the paint canvas will use when a user interacts
     * with it.
     * 
     * @param tool The tool to set the canvas to use.
     */
    public static void setTool(Tool tool) {
    	if(MODE==MODE_IDLE)
    		TOOL = tool;
    }
    
    /**
     * Get the output location of where the image this tab manages should be saved.
     * Used by the save option in {@link com.terei.jvector.JVector JVector}.
     * 
     * @return The file where this image is saved to.
     */
    public File getFile() {
        return file;
    }
    
    /**
     * Set the output location of where the image this tab manages should be saved.
     * Used by the save option in {@link com.terei.jvector.JVector JVector}.
     * 
     * @param file The output file the image should be saved to.
     */
    public void setFile(File file) {
        this.file = file;
    }
    
    /**
     * If the 'graphic' has been saved or not. This will return false
     * even if the graphic has been saved, if it has been modified since 
     * that point.
     * 
     * <p>So this really returns if the currently viewed graphic has been saved.
     * 
     * @return Returns the saved state of the 'graphic.'
     */
    public boolean isSaved() {
        return saved;
    }

    /**
     * Notfiy the 'graphic' that it has been saved.
     */
    public void notifySaved() {
        saved = true;
    }
    
    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent e) {                
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent e) {
    	this.setCursor(TOOL.getCursor());       
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent e) {
        this.setCursor(Tool.NULL.getCursor());        
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e) {
        switch(TOOL) {
            case NULL: ; break;
            case SELECT: handleSelectPress(e); break;
            case LINE: lineM.mousePressed(e, zoomLevel); handleLine(); break;
            case RECTANGLE: rectM.mousePressed(e, zoomLevel); handleRect(); break;
            case OVAL: ovalM.mousePressed(e, zoomLevel); handleOval(); break;
            case POLYGON: polyM.mousePressed(e, zoomLevel); handlePoly(); break;
            case TEXT: textM.mousePressed(e, zoomLevel); handleText(); break;
            case ZOOM: handleZoom(e);
        }
        
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {    
        switch(TOOL) {
            case NULL: ; break;
            case SELECT: handleSelectRelease(e); break;
            case LINE: lineM.mouseReleased(e,zoomLevel); handleLine(); break;
            case RECTANGLE: rectM.mouseReleased(e,zoomLevel); handleRect(); break;
            case OVAL: ovalM.mouseReleased(e,zoomLevel); handleOval();
        }               
        
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     */
    public void mouseDragged(MouseEvent e) {   
        if (TOOL == Tool.SELECT && MODE == MODE_ACT)                                    
            handleSelectDrag(e);   
        else {
            if(MODE == MODE_ACT) {
                
                switch(TOOL) {
                    case NULL: ; break;
                    case LINE: lineM.mouseDragged(e, zoomLevel); handleLine(); break;
                    case RECTANGLE: rectM.mouseDragged(e, zoomLevel); handleRect(); break;
                    case OVAL: ovalM.mouseDragged(e, zoomLevel); handleOval();
                }
                
            }
        }
        
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
     */
    public void mouseMoved(MouseEvent e) {
        if (MODE == MODE_ACT && TOOL == Tool.POLYGON) {
            polyM.mouseMoved(e, zoomLevel);
            handlePoly();
        }
    }
    
    /**
     * Handles the operations of the selection tool, for when the mouse is
     * pressed down.
     * 
     * @param e The mouse event that generate the mousePressed event
     *          that called this method.
     * @see #mousePressed(MouseEvent)
     */
    private void handleSelectPress(MouseEvent e) {
        aShape = null;
        for (int i=shapes.size(); i>0; i--) {
            Shape s = (Shape)shapes.get(i-1);
            if (s.contain(e.getPoint())) {
                aShape = s;
                MODE = MODE_ACT;
                saved = false;
                break;
            }
        }
    }
    
    /**
     * Handles the operations of the selection tool, for when the mouse is
     * dragged.
     * 
     * @param e The mouse event that generate the mouseDragged event
     *          that called this method.
     * @see #mouseDragged(MouseEvent)
     */
    private void handleSelectDrag(MouseEvent e) {
        Shapes s = aShape.getShapeCode();
        
        switch(s) {
            case LINE: lineM.moveShape(aShape, e, zoomLevel); break;                
            case RECTANGLE: rectM.moveShape(aShape, e, zoomLevel); break;
            case OVAL: ovalM.moveShape(aShape, e, zoomLevel); break;
            case POLYGON: polyM.moveShape(aShape, e, zoomLevel); break;
            case TEXT: textM.moveShape(aShape, e, zoomLevel); break;
        }
        
        repaint();
    }
    
    /**
     * Handles the operations of the selection tool, for when the mouse is
     * released.
     * 
     * @param e The mouse event that generate the mouseReleased event
     *          that called this method.
     * @see #mouseReleased(MouseEvent)
     */
    private void handleSelectRelease(MouseEvent e) {
        MODE = MODE_IDLE;
        lineM.setMode(ShapeManager.Mode.IDLE);
        rectM.setMode(ShapeManager.Mode.IDLE);
        ovalM.setMode(ShapeManager.Mode.IDLE);
        textM.setMode(ShapeManager.Mode.IDLE);
        polyM.setMode(ShapeManager.Mode.IDLE);       
    }
    
    /**
	 * Handles the operations required to draw a 
     * {@link com.terei.jvector.paint.shapes.Line line}.
	 */
    private void handleLine() {
        MODE = MODE_ACT;
        tShape = lineM.getShape();
        if(lineM.getDone()) {
           addShape(tShape);
           tShape = null;
           MODE = MODE_IDLE;
        }
        repaint();
    }
    
    /**
	 * Handles the operations required to draw a
     * {@link com.terei.jvector.paint.shapes.Rectangle rectangle}.
	 */
    private void handleRect() {
        MODE = MODE_ACT;
        tShape = rectM.getShape();
        if(rectM.getDone()) {
            addShape(tShape);
           tShape = null;
           MODE = MODE_IDLE;
        }
        repaint();
    }
    
    /**
     * Handles the operations required to draw a
     * {@link com.terei.jvector.paint.shapes.Oval oval}.
     */
    private void handleOval() {
        MODE = MODE_ACT;
        tShape = ovalM.getShape(zoomLevel);
        if(ovalM.getDone()) {
            addShape(tShape);
           tShape = null;
           MODE = MODE_IDLE;
        }
        repaint();
    }
    
    /**
     * Handles the operations required to draw a
     * {@link com.terei.jvector.paint.shapes.Polygon polygon}.
     */
    private void handlePoly() {
        MODE = MODE_ACT;
        tShape = polyM.getShape();
        if(polyM.getDone()) {
           if (tShape!= null)
               addShape(tShape);
           tShape = null;
           MODE = MODE_IDLE;
        }
        repaint();
    }
    
    /**
     * Handles the operations required to draw a String of 
     * {@link com.terei.jvector.paint.shapes.Text text}.
     */
    private void handleText() {
        MODE = MODE_ACT;
        tShape = textM.getShape();
        if(textM.getDone()) {
           if (tShape!= null)
               addShape(tShape);
           tShape = null;
           MODE = MODE_IDLE;
        }
        repaint();
    }
    
    /**
     * Adds a shape to this canvas.
     * 
     * @param shape The shape to add.
     */
    private void addShape(Shape shape) {
        shapes.add(shape);
        saved = false;
    }
    
    /**
     * Handles the zoom operations.
     * 
     * @param e The {@link java.awt.event.MouseEvent MouseEvent} that called
     *          this method.
     */
    private void handleZoom(MouseEvent e) {
        Point p = e.getPoint();
        int direction = ZoomOptions.getZoomMode();
        int zoom = ZoomOptions.getZoomLevel()*10;
        
        if (direction == ZoomOptions.ZOOM_IN)
            zoomLevel += zoom;
        else if (direction == ZoomOptions.ZOOM_OUT)
            zoomLevel -= zoom;
        
        if (zoomLevel<10) {
            zoomLevel = 10;
        } else if (zoomLevel>1600) {
            zoomLevel=1600;
        }            
        
        zoom(zoomLevel, p);
        JVector.jlZoomLvl.setText(String.valueOf((int)getZoomLevel()) + "%");
    }
    
    /**
     * Refreshes the paint canvas, by telling it to zoom to its current
     * size. This method should be called if there are some problems with
     * images not resizing properly (not when zooming, but other operations).
     */
    public void revalidateZoom() {
        resizeShapes(zoomLevel/100);
        repaint();
    }
    
    /**
     * Zooms in or out of the canvas.
     * 
     * @param percent The percentage for the zoom to be.
     * @param p The point to zoom in on.
     */
    private void zoom(int percent, Point p) {
        double d = (double)percent/100;
        resizeView(d, p);
        resizeShapes(d);
        
        tab.revalidate();
    }       

    /**
     * Resizes the view area, used when zooming.
     * 
     * @param nz The percentage in decimal form to resize it to relative to its
     * 100% value.
     * @param p The point to center in on.
     */
    private void resizeView(double nz, Point p) {
        //TODO stop the flashing that takes place when zooming.
        Dimension d_jtp = tab.getSize();       
        int w = (int)d_jtp.getWidth();
        if (w==0)
        	w = (int) (width*nz);
        int h = (int)d_jtp.getHeight();
        if (h==0)
        	w = (int) (height*nz);
        tab.resizeView(width*nz, height*nz, w, h);                       

        //try to center the canvas on the mouse click
        //FIXME: Doesnt work
//        int x = (int)((p.x/tab.SIZE_OLD.x)*tab.SIZE_CURRENT.x);        
//        int y = (int)((p.y/tab.SIZE_OLD.y)*tab.SIZE_CURRENT.y);
//        Rectangle r = new Rectangle(p.x,p.y,1,1);
    }
    
    /**
     * Resizes all the shapes, used when zooming.
     * 
     * @param d The percentage in decimal form to resize it to relative to its
     * 100% value.
     */
    private void resizeShapes(double d) {    
        for (Shape s : shapes)
            s.resize(d);    
    }
    
    /**
     * Paints the canvas. This includes calling all the drawn tShape's paint methods.
     * 
     * @param g The graphics component to paint to.
     */
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	Graphics2D g2 = (Graphics2D)g;    	
        //TODO provide option
    	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);    	
    	for(Shape s : shapes)
    		s.paint(g2);
    	//draw the currently active tShape.
    	if(tShape!=null)
    	    tShape.paint(g2);
    }   
    
    /**
     * Deletes the currently sleceted shape.
     */
    public void deleteSelectedShape() {
        PaintCanvas.setTool(Tool.SELECT);
        if (aShape != null) {
            shapes.remove(aShape);
            repaint();            
        }
        PaintCanvas.setTool(Tool.SELECT);
    }
    
    /**
     * Returns the zoom level of this canvas (percentage form).
     * 
     * @return The zoom level of this canvas.
     */
    public int getZoomLevel() {
        return zoomLevel;
    }
    
    public void dispose() {
        for (@SuppressWarnings("unused") Shape s : shapes)
            s = null;
        shapes.removeAllElements();
        shapes = null;
        aShape = null;        
        file = null;
        tab = null;
        tShape = null;
    }
    
}