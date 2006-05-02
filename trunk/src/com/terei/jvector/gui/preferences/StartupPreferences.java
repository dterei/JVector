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
package com.terei.jvector.gui.preferences;

import java.awt.FlowLayout;
import java.util.MissingResourceException;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

import com.terei.jvector.JVector;


/**
 * A JPanel for the preferences dialog which displays options about JVector
 * startup.
 * 
 * @author David Terei
 * @version 1
 * @since JVector 0.18.4 (16/06/2004)
 */
@SuppressWarnings("serial")
public class StartupPreferences extends PreferencePanel {
    
    /**
     * The title of this JPanel.
     */
    private String title = "Startup";
    
    /**
     * The Check box for if the splash screen is enabled or not.
     */
    private JCheckBox check = new JCheckBox();
    
    /**
     * Construct a new Startup JPanel.
     * 
     * @param props The properties file where this information is stored.
     */
    public StartupPreferences(Properties props) {        
        title = JVector.getRBLocale().getString("Dialog.Preferences.Startup");  
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        initPanel(props);
    }
    
    /**
     * Constructs the GUI of the panel.
     * 
     * @param props The Properties File to use to setup the GUI, as it will display
     * these properties.
     */
    public void initPanel(Properties props) {
        int enabled = 1;
        try {
            enabled = Integer.parseInt(props.getProperty("ShowSplash"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        if (enabled == 1)
            check.setSelected(true);
        else
            check.setSelected(false);
        
        this.add(new JLabel(JVector.getRBLocale().
                getString("Dialog.Preferences.Startup.Splash")));
        this.add(check);
    }
    
    /* (non-Javadoc)
     * @see com.terei.jvector.gui.preferences.PreferencePanel#getProperties()
     */
    public Properties getProperties() {
        int enabled = check.isSelected() ? 1: 0;
        
        Properties props = new Properties();
        props.setProperty("ShowSplash", String.valueOf(enabled));
        return props;
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
            icon = JVector.loadImageIcon("Resources/preferences_startup.png");
        } catch (MissingResourceException e) {
            e.printStackTrace();
        }
        
        return icon;
    }

}
