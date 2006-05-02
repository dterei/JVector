/*
 * Created By David Terei
 * With the Eclispe IDE
 * 
 * Created on 17/06/2004
 */
package com.terei.jvector.gui.preferences;

import java.awt.FlowLayout;
import java.util.MissingResourceException;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.terei.jvector.JVector;


/**
 * A JPanel for the preferences dialog which displays some general options
 * about JVector.
 * 
 * @author David Terei
 * @version 1
 * @since 17/06/2004
 */
@SuppressWarnings("serial")
public class GeneralPreferences extends PreferencePanel 
								implements ChangeListener {    
    /**
     * The title of this JPanel.
     */
    private String title = "General";
    
    /**
     * Construct a new Startup JPanel.
     * 
     * @param props The properties file where this information is stored.
     */
    public GeneralPreferences(Properties props) {
        title = JVector.getRBLocale().getString("Dialog.Preferences.General");
        initPanel(props);
    }
    
    /**
     * Constructs the GUI of the panel.
     * 
     * @param props The Properties File to use to setup the GUI, as it will display
     * these properties.
     */
    public void initPanel(Properties props) {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
    }
    
    /**
     * Get the Title of this JPanel.
     * 
     * @return The title of the JPanel.
     */
    public String toString() {
        return title;
    }
    
    /* (non-Javadoc)
     * @see com.terei.jvector.gui.preferences.PreferencePanel#getBanner()
     */
    public ImageIcon getBanner() {
        ImageIcon icon = null;
        
        try {
            icon = JVector.loadImageIcon("Resources/preferences_general.png");
        } catch (MissingResourceException e) {
            e.printStackTrace();
        }
        
        return icon;
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.gui.preferences.PreferencePanel#getProperties()
     */
    public Properties getProperties() {
    	return null;
    }
    
    /* (non-Javadoc)
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged(ChangeEvent e) {
    }

}
