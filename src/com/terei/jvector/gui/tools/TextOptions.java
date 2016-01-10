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
package com.terei.jvector.gui.tools;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.ResourceBundle;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.terei.jvector.JVector;


/**
 * Creates a JPanel that gives the user access to several options to use when
 * drawing some text.
 * 
 * @author David Terei
 * @since 19/05/2004
 * @version 1
 */
@SuppressWarnings("serial")
public class TextOptions extends JPanel {
    
    /**
     * A JLabel for the {@link #jtfString String} text box.
     */
    private JLabel jlString = new JLabel();
    /**
     * A text box for users to enter the text they want displayed in.
     */
    private static JTextField jtfString = new JTextField(18);
    /**
     * A JLabel for the {@link #jcFont Font Box}.
     */
    private JLabel jlFont = new JLabel();
    /**
     * A combo box that allows users to select an availible font to
     * display the text in.
     */    
    private static JComboBox jcFont = new JComboBox();
    /**
     * A JLabel for the {@link #jcStyle Style Box}.
     */
    private JLabel jlStyle = new JLabel();
    /**
     * A combo box that allows users to select the display style.
     */
    private static JComboBox jcStyle = new JComboBox();
    /**
     * A JLabel for the {@link #jcSize Size box}.
     */
    private JLabel jlSize = new JLabel();
    /**
     * A combo box that allows the user to set the size of the text.
     */
    private static JComboBox jcSize = new JComboBox();
    
    /**
     * A static initilization block to set up the static gui components.
     */
    static {
        setupFontBox();
        setupStyleBox();
        setupSizeBox();
    }

    
    /**
     * Create a new TextOptions, the constructor does most the
     * work as most of it is GUI work.
     */
    public TextOptions() {
        //A very powerful layout that lays out components by specifieng
        //relative distance between them
        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        
        ResourceBundle locale = JVector.getRBLocale();
        
        jlString.setText(locale.getString("ExtraBar.Text.String"));
        jlFont.setText(locale.getString("ExtraBar.Text.Font"));
        jlStyle.setText(locale.getString("ExtraBar.Text.Style"));
        jlSize.setText(locale.getString("ExtraBar.Text.Size"));
        
        JPanel jpT = new JPanel();        
        jpT.add(jlString);
        jpT.add(jtfString);
        
        JPanel jpF = new JPanel();
        jpF.add(jlFont);
        jpF.add(jcFont);
        
        JPanel jpSt = new JPanel();
        jpSt.add(jlStyle);
        jpSt.add(jcStyle);
        
        JPanel jpSi = new JPanel();
        jpSi.add(jlSize);
        jpSi.add(jcSize);
        
        this.add(jpT);
        this.add(jpF);
        this.add(jpSt);
        this.add(jpSi);
        
        //layout jpT
        layout.putConstraint(SpringLayout.WEST, jpT, 5, 
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, jpT, 5, 
                SpringLayout.NORTH, this);
        //layout jpF
        layout.putConstraint(SpringLayout.NORTH, jpF, 5, 
                SpringLayout.SOUTH, jpT);
        layout.putConstraint(SpringLayout.WEST, jpF, 5,
                SpringLayout.WEST, this);
        //layout jpSt
        layout.putConstraint(SpringLayout.NORTH, jpSt, 5,
                SpringLayout.SOUTH, jpF);
        //layout jpSi
        layout.putConstraint(SpringLayout.NORTH, jpSi, 5,
                SpringLayout.SOUTH, jpSt);
        
    }

    /**
     * Setup the font combo box, adds all the availible fonts to it.
     * 
     * @see #jcFont
     */
    private static void setupFontBox() {
        jcFont.setEditable(false);       
        //FIXME Doesn't work with MacOSX, Test maybe it does now?
        GraphicsEnvironment gEnv = 
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] envfonts = gEnv.getAvailableFontFamilyNames();
        //String[] envfonts = Toolkit.getDefaultToolkit().getFontList();
        for ( int i = 1; i < envfonts.length; i++ )
           jcFont.addItem(envfonts[i]);
        
    }
    

    /**
     * Setups the style combo box. Adds in the various styles, eg Bold...
     * 
     * @see #jcStyle
     */
    private static void setupStyleBox() {
        jcStyle.setEditable(false);
        
        jcStyle.addItem(new String("Plain"));
        jcStyle.addItem(new String("Italic"));
        jcStyle.addItem(new String("Bold"));  
        jcStyle.addItem(new String("Bold & Italic"));
    }
    
    /**
     * Setups the text size combo box, adding in the sizes.
     * 
     * @see #jcSize
     */
    private static void setupSizeBox() {
        jcSize.setEditable(true);
        jcSize.setSelectedItem(new Integer(12));
        
        for (int i=2; i <34; i+=2)
            jcSize.addItem(new Integer(i));
        
        for (int i=38; i <100; i+=6)
            jcSize.addItem(new Integer(i));
    }

    /**
     * Get the font set by the user.
     * 
     * @return Returns the font.
     */
    public static Font getFONT() {
        String name = (String)jcFont.getSelectedItem();
        
        int style = Font.PLAIN;
        String temp1 = (String)jcStyle.getSelectedItem();
        if (temp1.compareToIgnoreCase("bold") == 0)
            style = Font.BOLD;
        if (temp1.compareToIgnoreCase("italic") == 0)
            style = Font.ITALIC;
        if (temp1.compareToIgnoreCase("bold & italic") == 0)
            style = Font.BOLD + Font.ITALIC;
        
        Integer temp2 = (Integer)jcSize.getSelectedItem();
        int size = temp2.intValue();

        Font font = new Font(name, style, size);
        return font;
    }
    /**
     * Get the text set by the user to use.
     * 
     * @return Returns the text.
     */
    public static String getTEXT() {
        return jtfString.getText();
    }
}
