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
package com.terei.jvector.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JEditorPane;

/**
 * A Window that displays an 'about' [JVector] box.
 * 
 * @author David Terei
 * @version 2
 * @since 15/06/2004
 */
@SuppressWarnings("serial")
public class AboutDialog extends JDialog {     
    
    /**
     * Construct a new About Dialog.
     */
    public AboutDialog() {
    	String type = "text/html";
    	String text = "<p align=\"center\"><strong>JVector 0.20.0</strong></p>" +
    					"<p><strong>Author: </strong>David Terei</p>" +
    					"<p>This program uses the following software:</p>" +
    					"<blockquote>" +
    					"<p>- <strong>Apache Log4j</strong><br>" +
    					"- <strong>Apache Batik SVG Toolkit (SVG Plugin)</strong><br>" +
    					"- <strong>BrowserLauncher 2</strong></p>" +
    					"</blockquote>" +
    					"<p>Many thanks for all this great free open source software<br>" +
    					"they have provided. Information about them can be found<br>" +
    					"in the readme file. (or a quick search of google.)</p>";

        JEditorPane label = new JEditorPane(type,text);
        label.setEditable(false);
        add(label);
        
        pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dim.width - this.getWidth())/2;
        int y = (dim.height - this.getHeight())/2 - 60;
        setLocation(x,y);
    }
    
}