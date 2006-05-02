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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.apache.log4j.Logger;

import com.terei.jvector.JVector;


/**
 * A Dialog that contains a list of preferences for JVector, loading and saving them
 * to the preferences.properties text file.
 * 
 * @author David Terei
 * @version 1
 * @since JVector 0.18.4 (16/06/2004)
 */
@SuppressWarnings("serial")
public class PreferencesDialog extends JDialog implements ActionListener,
												TreeSelectionListener {
    /**
     * Log4J Logger for this class
     */
    private static final Logger logger = Logger
        .getLogger(PreferencesDialog.class);
    
    /**
     * The selection tree for the various preferences.
     */
    private JTree tree;
    
    /**
     * The JPanel where the banner and preferences panel of the current active
     * preference panel is stored.
     */
    private JPanel prefPanel = new JPanel(); 
    
    /**
     * The Banner Pnael, that displays the name of the currently selected
     * preferences panel.
     */
    private JPanel banerPanel = new JPanel();
    
    /**
     * The okay button.
     */
    private JButton okayBut = new JButton("Okay");
    
    /**
     * The cancel button.
     */
    private JButton cancBut = new JButton("Cancel");
    
    /**
     * The properties file where the preferences are stored.
     */
    private Properties props;
    
    /**
     * The preference panels.
     */
    private PreferencePanel[] prefPanels;
    
    /**
     * Construct a new PreferencesDialog.
     */
    public PreferencesDialog() {
        props = loadProperties();
        setupPanels();
        setupDialog();
    }

    /**
     * Loads the porperies file.
     * 
     * @return The properties file in which the preferences are stored.
     */
    private Properties loadProperties() {
        if (logger.isDebugEnabled()) {
            logger.debug("loadProperties() - start");
        }

        Properties props = new Properties();
        try {
            InputStream in = new FileInputStream("preferences.properties");
            
            props.load(in);
        } catch (IOException e) {
            logger.error("loadProperties() - catch", e);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("loadProperties() - end");
        }
        return props;
    }
    
    /**
     * Sets up the individual preferences panels.
     */
    private void setupPanels() {
        if (logger.isDebugEnabled()) {
            logger.debug("setupPanels() - start");
        }

        prefPanels = new PreferencePanel[1];
        
        prefPanels[0] = new StartupPreferences(props);
        //prefPanels[1] = new GeneralPreferences(props);

        if (logger.isDebugEnabled()) {
            logger.debug("setupPanels() - end");
        }
    }

    /**
     * Setups the Dialog.
     */
    private void setupDialog() {
        if (logger.isDebugEnabled()) {
            logger.debug("setupDialog() - start");
        }

        //title
        this.setTitle(JVector.getRBLocale().
                getString("Dialog.Preferences.Title"));
        this.setSize(530, 435);        
        this.setModal(true);

        tree = createTree();
        
        //a JPanel used for the JTree and Preference panel.        
        //layout the jpanel with the tree and preferences
        SpringLayout layout = new SpringLayout();        
        JPanel jp = new JPanel(layout);        
        jp.setBorder(new EtchedBorder());
        
        //the JPanel used for the preference panel and banner.        
        prefPanel.setLayout(new BoxLayout(prefPanel, BoxLayout.PAGE_AXIS));                
        setPrefPanel(prefPanels[0]);
        
        jp.add(tree);
        jp.add(prefPanel);
        
        //layout components
        layout.putConstraint(SpringLayout.WEST, tree, 10, 
                	SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, tree, 10,
                	SpringLayout.NORTH, this);
        
        layout.putConstraint(SpringLayout.NORTH, prefPanel, 10,
                	SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.WEST, prefPanel, 10,
                	SpringLayout.EAST, tree);
        
        //the jpanel with the buttons.
        JPanel butPanel = new JPanel();
        butPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        okayBut.setText(JVector.getRBLocale().
                getString("Dialog.Preferences.Button.Okay"));
        okayBut.addActionListener(this);
        
        cancBut.setText(JVector.getRBLocale().
                getString("Dialog.Preferences.Button.Cancel"));
        cancBut.addActionListener(this);
        
        butPanel.add(cancBut);
        butPanel.add(okayBut);
             
        this.add(jp, BorderLayout.CENTER);
        this.add(butPanel, BorderLayout.SOUTH);
        
        if (logger.isDebugEnabled()) {
            logger.debug("setupDialog() - end");
        }
    }
    
    /**
     * Sets the currently shown preference panel to the one specified.
     * 
     * @param Panel The PreferencePanel to use.
     */
    private void setPrefPanel(PreferencePanel Panel) {
        //remove all components still there.
        prefPanel.removeAll();
        
        //create components
        banerPanel = initBannerPanel(Panel);
        
        //add components        
        prefPanel.add(banerPanel);
        prefPanel.add(Panel);
        
        prefPanel.revalidate();        
    }

    /**
     * Creates the Banner Panel.
     * 
     * @param jp The preferences panel currently active.
     * @return The Banner Pannel.
     */
    private JPanel initBannerPanel(PreferencePanel jp) {
        if (logger.isDebugEnabled()) {
            logger.debug("initBannerPanel(JPanel) - start");
        }

        ImageIcon icon = jp.getBanner(); 
        
        JPanel panel = new JPanel();
        JLabel label = new JLabel(icon);
        
        panel.add(label);
        
        if (logger.isDebugEnabled()) {
            logger.debug("initBannerPanel(JPanel) - end");
        }
        return panel;
    }

    /**
     * Creates the JTree that displays the list of preferences to choose from.
     * 
     * @return The create JTree.
     */
    private JTree createTree() {
        if (logger.isDebugEnabled()) {
            logger.debug("createTree() - start");
        }

        //create the root level on the JTree.
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("JVector");
        //create the individual sub elements that point to panels.
        createNodes(top);
        //create a JTree with the above create Node layout.
        JTree tree = new JTree(top);
        
        //set some properties on the tree.
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.putClientProperty("JTree.lineStyle", "Angled");
        tree.setBorder(new EtchedBorder());
        tree.setPreferredSize(new Dimension(120, 340));
        tree.addTreeSelectionListener(this);
        
        if (logger.isDebugEnabled()) {
            logger.debug("createTree() - end");
        }
        
        return tree;
    }
    
    /**
     * Creates the various nodes of the JTree.
     * 
     * @param top The root of the tree.
     */
    private void createNodes(DefaultMutableTreeNode top) {
        if (logger.isDebugEnabled()) {
            logger.debug("createNodes(DefaultMutableTreeNode) - start");
        }        
        //add in the preferences panel to the JTree.
        for (PreferencePanel p : prefPanels) {
            top.add(new DefaultMutableTreeNode(p));    
        }               
        if (logger.isDebugEnabled()) {
            logger.debug("createNodes(DefaultMutableTreeNode) - end");
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
     */
    public void valueChanged(TreeSelectionEvent e) {
        if (logger.isDebugEnabled()) {
            logger.debug("valueChanged(TreeSelectionEvent) - start");
        }

        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
        		tree.getLastSelectedPathComponent();

		if (node == null) return;
		
		Object nodeObject = node.getUserObject();
		
		if (node.isLeaf())
		    setPrefPanel((PreferencePanel)nodeObject);
		prefPanel.revalidate();
		
        if (logger.isDebugEnabled()) {
            logger.debug("valueChanged(TreeSelectionEvent) - end");
        }
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0) {
        if (logger.isDebugEnabled()) {
            logger.debug("actionPerformed(ActionEvent) - start");
        }

        Object src = arg0.getSource();
        
        if (src.equals(okayBut)) {
            savePreferences();
            this.dispose();
        } else if (src.equals(cancBut))
            this.dispose();
        
        if (logger.isDebugEnabled()) {
            logger.debug("actionPerformed(ActionEvent) - end");
        }
    }

    /**
     * Saves the preferences back to the preferences.propperties file.
     */
    private void savePreferences() {
        if (logger.isDebugEnabled()) {
            logger.debug("savePreferences() - start");
        }

        //create a new properties file to save all the properties to.
        Properties saveProps = new Properties();
        
        //get all the individual properties from each preference panel and store
        //them in our save one.
        for (int i=0; i< prefPanels.length; i++) {
            Properties temp = prefPanels[i].getProperties();
            
            Enumeration keys = temp.keys();
            Enumeration values = temp.elements();        
            
            while (keys.hasMoreElements()) {
                Object key = keys.nextElement();
                Object value = values.nextElement();
                
                saveProps.put(key, value);
            }    
        }        
        
        //save our save properties file.
        try {
            FileOutputStream out = new FileOutputStream("preferences.properties");
            saveProps.store(out,"JVector Preferences File");
        } catch (FileNotFoundException e) {
            logger.error("savePreferences() - catch", e);
        } catch (IOException e) {
            logger.error("savePreferences() - catch", e);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("savePreferences() - end");
        }
    }
    
}
