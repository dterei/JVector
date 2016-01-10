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

import javax.swing.JTabbedPane;

import com.terei.jvector.JVector;
import com.terei.jvector.paint.PaintCanvas;


/**
 * This class acts as a standard JTabbedPane, except that it 
 * does not allow the user to change the tab when a draw operation
 * is currently in progress.
 * 
 * @author David Terei
 * @since 26/04/2004
 * @version 1
 */
@SuppressWarnings("serial")
public class PaintTabbedPane extends JTabbedPane {
    
    /**
     * A wrapper to provide the JTabbedPane constructor.
	 *
	 * @see javax.swing.JTabbedPane#JTabbedPane()
     */
    public PaintTabbedPane() {
        super();
    }

    /**
     * A wrapper to provide the JTabbedPane constructor.
	 *
	 * @see javax.swing.JTabbedPane#JTabbedPane(int)
     */
    public PaintTabbedPane(int tabPlacement) {
        super(tabPlacement);
    }

    /**
     * A wrapper to provide the JTabbedPane constructor.
	 *
	 * @see javax.swing.JTabbedPane#JTabbedPane(int,int)
     */
    public PaintTabbedPane(int tabPlacement, int tabLayoutPolicy) {
        super(tabPlacement, tabLayoutPolicy);
    }
    
    /**
	 * Override the default change tab method, to stop the user
	 * changing the tab when a draw operation is currently in progress.
     * It also changes if the Save menu option is enabled depending on
     * the tab selected.
     * 
     * @param index the index to be selected.
     * 
     * @see JTabbedPane#setSelectedIndex(int)
	 */
    public void setSelectedIndex(int index) {
    	        
        if (PaintCanvas.getMode() == PaintCanvas.MODE_IDLE) {
            String zoom = "100%";
    	    
            try {
                
    	        PaintTab tab = (PaintTab)super.getComponentAt(index);
                
                if (tab.getCanvas().getFile() != null)
                    JVector.jmiF_save.setEnabled(true);
                else
                    JVector.jmiF_save.setEnabled(false);
                
                PaintCanvas canvas = tab.getCanvas();
                zoom = String.valueOf(canvas.getZoomLevel()) + "%";
                
            } catch (ClassCastException e) {
                e.printStackTrace();
                JVector.jmiF_save.setEnabled(false);
            }
            
            JVector.jlZoomLvl.setText(zoom);
	   		super.setSelectedIndex(index);
        }
    }
    
    /*
     *  (non-Javadoc)
     * @see java.awt.Container#remove(java.awt.Component)
     */
    public void remove(Component component) {
        super.remove(component);
        int sel = super.getSelectedIndex();
        
        if (sel != -1)
            setSelectedIndex(sel);
        else
            JVector.jmiF_save.setEnabled(false);
        
    }

}
