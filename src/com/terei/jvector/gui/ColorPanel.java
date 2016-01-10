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

import org.apache.log4j.Logger;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.terei.jvector.JVector;


/**
 * Creates a panel that allows the user to set a foreground and background 
 * colour using swatches (<code>com.terei.jvector.GUI.ColourSwatch</code>)
 * and some tools to manipulate these swatches. 
 * 
 * @author David Terei
 * @since 7/05/2004
 * @version 1
 */
@SuppressWarnings("serial")
public class ColorPanel extends JPanel implements ActionListener {
    private static final Dimension BUT_SIZE = new Dimension(20,20);

	/**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(ColorPanel.class);
    
    /**
     * The width of this panel.
     */
    public static final int WIDTH = 194;    
    /**
     * The height of this panel.
     */
    public static final int HEIGHT = 320;
    
    /**
     * The location of the directory where the swatches propertie files are stored.
     */
    private static final String SWATCH_PATH = "Resources/Swatches/";
    
    /**
     * The locale file for the program.
     * Contains all the strings for the program.
     */
    private ResourceBundle rbLocale;
    
    /**
     * A JButton that allows the user to add a swatch to the swatch tabbed pane.
     */
    private JButton jbAddSwatch;
    /**
     * A JButton that allows the user to remove a swatch to the swatch tabbed pane.
     */
    private JButton jbRemSwatch;
    /**
     * A JButton that allows the user to view information on the currently selected swatch.
     */
    private JButton jbInfSwatch;
    
    /**
     * Provides a tabbed pane for the various swatches.
     */
    private JTabbedPane jtpSwatch;

    /**
     * The file extension for swatch file's.
     */
    public static final String SWATCH_EXT = "swatch";
    
    /**
     * Create a new ColourPanel.
     */
    public ColorPanel() {
        super(new BorderLayout(), true);
        rbLocale = JVector.getRBLocale();
        initGUI();
        loadSwatches();
    }
    
    
    /**
     * Initialises the GUI.
     */
    private void initGUI() {
        if (logger.isDebugEnabled()) {
            logger.debug("initGUI() - start");
        }
        
        //create the three swatch edditing buttons.
        jbAddSwatch = createButton("Resources/new.png", 
                			rbLocale.getString("ColorPanel.AddSwatch"));
        jbRemSwatch = createButton("Resources/bin.png", 
    						rbLocale.getString("ColorPanel.RemSwatch"));
        jbInfSwatch = createButton("Resources/info.png", 
        					rbLocale.getString("ColorPanel.InfSwatch"));
        
        JVector.cSbm.addHintFor(jbAddSwatch, rbLocale.getString("ColorPanel.AddSwatch"));
        JVector.cSbm.addHintFor(jbRemSwatch, rbLocale.getString("ColorPanel.RemSwatch"));
        JVector.cSbm.addHintFor(jbInfSwatch, rbLocale.getString("ColorPanel.InfSwatch"));
        
        //create the top jpanel that holds the swatch edditing buttons.
        JPanel jpTop = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 7));
        jpTop.add(jbAddSwatch);
        jpTop.add(jbRemSwatch);
        jpTop.add(jbInfSwatch);
        
        //create the JTabbedPane to hold the swatches.
        jtpSwatch = new JTabbedPane();
        
        //create the middle jpanel to hold the tabbed pane.
        JPanel jpMid = new JPanel(new BorderLayout());
        jpMid.add(jtpSwatch);
        
        //create the bottom jpanel that holds the colour displayer.
        //the colour displayer shows the currently selected foreground
        //and background.
        JPanel jpBot = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 7));
        jpBot.add(new ColorSelectedPanel());        
        
        //setup the jpanel by setting the size and adding in the three jpanels.
        this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        this.add(jpTop,BorderLayout.NORTH);
        this.add(jpMid,BorderLayout.CENTER);
        this.add(jpBot,BorderLayout.SOUTH);

        if (logger.isDebugEnabled()) {
            logger.debug("initGUI() - end");
        }
    }
    
    /**
     * Creates a JButon with settings appropriate to this panel.
     * 
     * @param icon The path of the icon to use.
     * @param tooltip The String to set as the buttons tooltip.
     * 
     * @return A JButton with the properties set.
     */
    private JButton createButton(String icon, String tooltip) {
        if (logger.isDebugEnabled()) {
            logger.debug("createButton(String, String) - start");
        }

        JButton button = new JButton(JVector.loadImageIcon(icon));
        button.setToolTipText(tooltip);
        button.setPreferredSize(BUT_SIZE);
        button.setContentAreaFilled(false);
        button.addActionListener(this);
        
        if (logger.isDebugEnabled()) {
            logger.debug("createButton(String, String) - end");
        }
        return button;
        
    }   
    
    /**
     * Loads all the swatches specified in the SWATCH_PATH/swatches.properties
     * file. The swatches specified in that file, are paths relative to it.
     * 
     * @see com.terei.jvector.gui.ColorPanel#SWATCH_PATH
     */
    private void loadSwatches() {
        if (logger.isDebugEnabled()) {
            logger.debug("loadSwatches() - start");
        }

        //get the paths relative to the SWATCH_PATH, of the swatches to load.
        String[] swatches = getSwatches();
        for (String s : swatches) {
            JPanel panel = new ColorSwatch(SWATCH_PATH + s);
            jtpSwatch.addTab(panel.getName(), panel);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("loadSwatches() - end");
        }
    }
    
    /**
     * Loads the file SWATCH_PATH/swatches.properties, and returns all of its
     * values. This file should only contain the paths of the swatches to load,
     * relative to the SWATCH_PATH directory.
     * 
     * @see com.terei.jvector.gui.ColorPanel#SWATCH_PATH
     * @return An array of strings containing the paths to the swatches.
     */
    private String[] getSwatches() {
        if (logger.isDebugEnabled()) {
            logger.debug("getSwatches() - start");
        }

        String[] list = new String[1];
        try {	        
            //create an input stream pointing to the file.
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(
                                                    SWATCH_PATH+"swatches.properties");
            //load the file.
            Properties p = new Properties();
            p.load(in);
            in.close();
            
            list = new String[p.size()];
            Enumeration e = p.elements();            
            for (int i=0; e.hasMoreElements(); i++)
                list[i] = (String)e.nextElement(); 
            
        } catch (Exception e) {
            logger.error("getSwatches() - catch", e);
            e.printStackTrace();
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("getSwatches() - end");
        }
        return list;
    }    
 
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if (logger.isDebugEnabled()) {
            logger.debug("actionPerformed(ActionEvent) - start");
        }

        Object src = e.getSource();
        if (src.equals(jbAddSwatch))
            addSwatch();
        else if (src.equals(jbRemSwatch))
            removeSwatch();
        else if (src.equals(jbInfSwatch)) {
            ColorSwatch swatch = (ColorSwatch)jtpSwatch.getSelectedComponent();
            swatch.showInfoDialog();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("actionPerformed(ActionEvent) - end");
        }
    }

    /**
     * 
     */
    private void addSwatch() {
        if (logger.isDebugEnabled()) {
            logger.debug("addSwatch() - start");
        }

        // TODO addSwatch
        JFileChooser jfc = new JFileChooser();
        String description = rbLocale.getString("Dialog.Swatch.FilterDesc");
        jfc.setFileFilter(new SwingFileFilter(SWATCH_EXT, description, true));
        jfc.setDialogTitle(rbLocale.getString("Dialog.Swatch.Title"));
        jfc.setDialogType(JFileChooser.OPEN_DIALOG);
        jfc.setMultiSelectionEnabled(false);
        File file = new File(SWATCH_PATH);
        jfc.setSelectedFile(file);
        
        int returnVal = jfc.showOpenDialog(this);
        
        if(returnVal != JFileChooser.APPROVE_OPTION)
            return;
        
        if (logger.isDebugEnabled()) {
            logger.debug("addSwatch() - end");
        }
    }
    
    /**
     * 
     */
    private void removeSwatch() {
        if (logger.isDebugEnabled()) {
            logger.debug("removeSwatch() - start");
        }

        // TODO removeSwatch
        
        if (logger.isDebugEnabled()) {
            logger.debug("removeSwatch() - end");
        }
    }    
  
}