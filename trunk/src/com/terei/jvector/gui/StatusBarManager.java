package com.terei.jvector.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;


/**
 * A reusable class that provides functions to easily implement a status bar on
 * an appliaction that displays tool tips. 
 * See <a href="http://builder.com.com/5100-6370-5185994.html#Listing A"> Builder.com -
 * Improving your Java GUI with status-bar hints</a> for more information.
 * 
 * @author Alexandre Pereira Calsavara
 */
public class StatusBarManager implements MouseListener {
    
    /**
     * A map (searchable) which holds all the various 'hints' for the various componenets.
     */
    private Map<Component,String> hintMap;   
    /**
     * A reference to the JLabel that is used to display the hints.
     */
    private JLabel hintLabel;
    
    
    /**
     * Create a new StatusBarManager.
     * 
     * @param hintLabel Specifies the JLabel in which the hints should appear.
     */
    public StatusBarManager( JLabel hintLabel ) {        
        hintMap = new WeakHashMap<Component,String>();        
        this.hintLabel = hintLabel;        
    }
    
    
    /**
     * Adds a 'hint'/tooltip for the componenet specified.
     * 
     * @param comp The component that the hint is for, when the mouse is over this
     *             component the hint will appear.
     * @param hintText The hint to display.
     */
    public void addHintFor( Component comp, String hintText ) {        
        hintMap.put( comp, hintText );        
    }    
    
    
    /**
     * The component to enable the 'hints'/tooltips for. This enable's
     * the component and its children to display hints in the status bar.
     * This method should be called intitially once the GUI is setup, before
     * it is deiplayed, and also, when new Components are added, although in
     * this case, this method should be called on just the new component.
     * 
     * @param comp The component to register this class with to displays hints.
     */
    public void enableHints( Component comp ) {        
        //make sure its not an instance of a jtabbedpane as if added to one
    	//it causes a run time error.
    	//FIX to bug 10.
    	if (comp instanceof JTabbedPane)
        	return;
        comp.addMouseListener(this);        
        //check if the component can contain children components.
        //if so, add a mouse listener to these children as well.
        if ( comp instanceof Container) {
            Component[] components = ((Container)comp).getComponents();            
            for (Component c : components)                
                enableHints(c);
        }        
        //This section enables menu items to have tool tips added to them.
        //it works in the same way as before.
        if ( comp instanceof MenuElement ) {            
            MenuElement[] elements = ((MenuElement)comp).getSubElements();            
            for (MenuElement m : elements)
                enableHints(m.getComponent());            
        }
        
    }
    
    
    /**
     * Gets the hint for the component. If no hint has been specified,
     * then it checks to see if it is a <code>JLabel</code>, or 
     * <code>JTabbedHeader</code> and if so, returns their text.
     * 
     * @param comp The component to which this hint is associated.
     * @return The hint.
     */
    private String getHintFor( Component comp ) {
        //search the map for the hint.
        String hint = (String)hintMap.get(comp);
        
        //check if there was no hint found, if so
        //check if its a JLabel or JTabbedHeader, and return its
        //text.
        if ( hint == null ) {            
        
            if ( comp instanceof JLabel )                
                hint = (String)hintMap.get(((JLabel)comp).getLabelFor());            
            else if ( comp instanceof JTableHeader )                
                hint = (String)hintMap.get(((JTableHeader)comp).getTable());            
        
        }
        
        //return the hint.
        return hint;        
    }
    
    
    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     * 
     * This method monitors when the mouse goes over a component, and then
     * trys to get the hint for the component and display it.
     */
    public void mouseEntered( MouseEvent e ) {        
        Component comp = (Component)e.getSource();        
        String hint;
        
        do {            
            hint = getHintFor(comp);            
            comp = comp.getParent();            
        } while ( (hint == null) && (comp != null) );
        
        if ( hint != null )            
            hintLabel.setText( hint );        
    }
    
    
    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     * 
     * Set the status bar manager back to displaying no text when it leaves
     * the component.
     */
    public void mouseExited( MouseEvent e ) {        
        hintLabel.setText(" ");        
    }
    
    
    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseClicked( MouseEvent e ) {}
    
    
    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mousePressed( MouseEvent e ) {}
    
    
    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseReleased( MouseEvent e ) {}

}
