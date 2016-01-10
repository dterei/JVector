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

import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


/**
 * Very simple interface for Preference Panels to use.
 * 
 * @author David Terei
 * @version 1
 * @since 17/06/2004
 */
public abstract class PreferencePanel extends JPanel {
    
    /**
     * Get the image icon to use as the heading/banner for this preference panel.
     * 
     * @return The ImageIcon to use as the banner.
     */
    public abstract ImageIcon getBanner();
    
    /**
     * Get the current properties of this preferences panel.
     * 
     * @return The properties that this preferences panel sets.
     */
    public abstract Properties getProperties();

}
