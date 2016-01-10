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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import org.apache.log4j.Logger;

import com.terei.jvector.JVector;

/** 
 * Creates a dialog window that allows the user to
 * create a new image, providing various options. Such
 * as size, name and some presets to choose from.
 * 
 * @author David Terei
 * @since 21/04/04
 * @version 0.8
 */
@SuppressWarnings("serial")
public class NewImageDialog extends JDialog implements ActionListener {
    /**
     * Log4J Logger for this class
     */
    private static final Logger logger = Logger.getLogger(NewImageDialog.class);

    /**
     * Initialises the class, and show the dialog box.
     * 
     * @param x The X posistion of the top left corner.
     * @param y The Y posistion of the top left corner.
     * @param jvector The JVector class that called this.
     */
    public static void showGUI(int x, int y, JVector jvector) {
        if (logger.isDebugEnabled()) {
            logger.debug("showGUI(int, int, JVector) - start");
        }

        try {
            NewImageDialog inst = new NewImageDialog(jvector);
            inst.setModal(true);            
            inst.setVisible(true);
        } catch (Exception e) {
            logger.error("showGUI(int, int, JVector)", e);
            e.printStackTrace();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("showGUI(int, int, JVector) - end");
        }
    }
    
    /**
     * The ratio of pixels:cm.
     */
    private static final double PIX_CM = 28.3465;
    /**
     * The ratio of pixels:inches.
     */
    private static final double PIX_INCHES = 72.00011;

    /**
     * Holds a reference to the JVector class that initialised this dialog,
     * so that it can notify it when its done.
     */
    //TODO better method? seems like a hack.
    private JVector parent;
    
    /**
	 * Holds wether or not the dialog is finished its work. It finishes its work
	 * when the user has chosen valid settings.
	 */
    private boolean done = false;
    
    /**
     * Holds the resource bundle that contains the current locale,
     * so that this class is language independent. 
     */
    private ResourceBundle rbLocale;
    
    /**
     * Holds a reference to the base location of where the locale strings
     * for this class start in the resource bundle.
     */
    private static String LOC_BASE = "Dialog.NewImage.";
    /**
     * The dimensions of this dialog
     */
    private  static final Dimension SIZE = new Dimension(345,270);
    
    //TODO do i need all the swing components as global variables?
    
    /**
     * The Cancel Button.
     */
    private JButton jbCancel;

    /**
     * The Okay Button.
     */
    private JButton jbOk;

    /**
     * The Combo box which holds the choice for which units to use,
     * when creating new image.  
     */
    private JComboBox jcImSize;

    /**
     * The combo box which holds some common image size, or 'presets.'
     */
    private JComboBox jcPresets;

    /**
     * The label for the Image Height.
     */
    private JLabel jlHeight;

    /**
     * The label for the Image Name.
     */
    private JLabel jlName;

    /**
     * The label for the persets combo box.
     */
    private JLabel jlPresetSmall;

    /**
     * The heading label for the presets.
     */
    private JLabel jlPresetLarge;

    /**
     * The label for the image width. 
     */
    private JLabel jlWidth;
    
    /**
     * The heading label for the image size.
     */
    private JLabel jlSize;

    /**
     * The first sepperator, divides the image name and image size area.
     */
    private JSeparator jSeparator1;

    /**
     * The second sepperator, divides the image size and presets area.
     */
    private JSeparator jSeparator2;

    /**
     * The third sepperator, divides the presets and button area.
     */
    private JSeparator jSeparator3;

    /**
     * The text field for the image width.
     */
    private JTextField jtX;

    /**
     * The text field for the image name.
     */
    private JTextField jtName;

    /**
     * The text field for the image height.
     */
    private JTextField jtY;

    /**
     * Initializes the dialog.
     * 
     * @param jvector The JVector class that called this.
     */
    public NewImageDialog(JVector jvector) {
        parent = jvector;
        rbLocale = JVector.getRBLocale();
        
        //Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();        
        //Determine the new location of the window
        int x2 = (dim.width-SIZE.width)/2;
        int y2 = (dim.height-SIZE.height)/2;
        
        initGUI(x2,y2);
    }
    
    /**
     * This method overrides the default createRootPane method of the JDialog class
     * to register some key events with it.
     * 
     * @return A custom JRootPane that has some key actions registered with it.
     */
    protected JRootPane createRootPane() {
        if (logger.isDebugEnabled()) {
            logger.debug("createRootPane() - start");
        }
        
        JRootPane rootPane = new JRootPane();
        
        //Create the cancel action that occurs when escape is pressed.
        ActionListener actQuit = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (logger.isDebugEnabled()) {
                    logger.debug("create cancel action");
                }
                dispose();
            }
        };
        KeyStroke k_esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(actQuit, k_esc, JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        //Create the confirm/okay action that occurs when enter is pressed.
        ActionListener actOk = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (logger.isDebugEnabled()) {
                    logger.debug("create okay/confirm action");
                }
                actionOk();
            }
        };
        KeyStroke k_ok = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        rootPane.registerKeyboardAction(actOk, k_ok, JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        //Now we have created our custom root pane and registered our
        //keyboard actions, so return it to be used with this dialog.

        if (logger.isDebugEnabled()) {
            logger.debug("createRootPane() - end");
        }
        return rootPane;
    }

    /**
     * Initializes the GUI.
     * 
     * @param x The starting x co-ordinate of the dialog.
     * @param y The starting y co-ordinate of the dialog.
     */
    public void initGUI(int x, int y) {
        if (logger.isDebugEnabled()) {
            logger.debug("initGUI(int, int) - start");
        }
        
        //A absolute layout is used due to its ease,
        //I dislike the Java Layout managers.
        //Also, this is a dialog that will be the same
        //size on all computers, and cant be resized,
        //so an absolute layout is fine.
        this.getContentPane().setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(
                WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle(rbLocale.getString(LOC_BASE+"Title"));
        this.setBounds(x, y, SIZE.width, SIZE.height);
        
        //create the image name section.
        initFirstSection();
        
        //create the first sepperator.
        jSeparator1 = new JSeparator();
        jSeparator1.setBounds(76, 60, 257, 6);
        this.getContentPane().add(jSeparator1);
        
        //create the image size area.
        initSecondSection();
        
        //create the second sepperator.
        jSeparator2 = new JSeparator();
        jSeparator2.setBounds(59, 155, 274, 3);
        this.getContentPane().add(jSeparator2);
        
        //create the image presets area.
        initThirdSection();
        
        //create the third sepperator.
        jSeparator3 = new JSeparator();
        jSeparator3.setBounds(5, 195, 328, 3);
        this.getContentPane().add(jSeparator3);
        
        //create the buttons.
        initButtons();       
                                                                                                       
        if (logger.isDebugEnabled()) {
            logger.debug("initGUI(int, int) - end");
        }
    }    

    /**
     * Creates the first (top) section of the dialog
     * that contains the name section.
     */
    private void initFirstSection() {
        if (logger.isDebugEnabled()) {
            logger.debug("initFirstSection() - start");
        }
        
        jlName = new JLabel();
        jtName = new JTextField();
        
        jlName.setText(rbLocale.getString(LOC_BASE+"Name"));
        jlName.setFont(new Font("Dialog", 0, 12));
        jlName.setBounds(17, 13, 60, 20);
        this.add(jlName);
        
        jtName.setBounds(72, 14, 238, 18);
        this.add(jtName);
        
        if (logger.isDebugEnabled()) {
            logger.debug("initFirstSection() - end");
        }
    }
    
    /**
     * Creates the Second (middle) section of the 
     * dialog that contains the image size parts.
     */
    private void initSecondSection() {
        if (logger.isDebugEnabled()) {
            logger.debug("initSecondSection() - start");
        }
        
        jlSize = new JLabel();
        jlWidth = new JLabel();
        jtX = new JTextField();
        jcImSize = new JComboBox();
        jlHeight = new JLabel();
        jtY = new JTextField();
        
        jlSize.setText(rbLocale.getString(LOC_BASE+"Size"));
        jlSize.setForeground(new Color(0, 0, 128));
        jlSize.setFont(new Font("Dialog", 1, 12));
        jlSize.setBounds(9,50,100,20);        
        this.add(jlSize);
        
        jlWidth.setText(rbLocale.getString(LOC_BASE+"Size.X"));
        jlWidth.setFont(new Font("Dialog", 0, 12));
        jlWidth.setBounds(77, 80, 35, 20);        
        this.add(jlWidth);
        
        jtX.setBounds(120, 80, 90, 20);
        jtX.setDocument(new IntDocument(6));        
        this.add(jtX);
                
        jcImSize.setBackground(new Color(255, 255, 255));
        jcImSize.setFont(new Font("Dialog", 0, 12));
        jcImSize.setBounds(220, 92, 90, 20);
        jcImSize.addActionListener(this);
        jcImSize.addItem(new String(rbLocale.getString(LOC_BASE+"Size.Unit.Pixel")));
        jcImSize.addItem(new String(rbLocale.getString(LOC_BASE+"Size.Unit.Cm")));        
        jcImSize.addItem(new String(rbLocale.getString(LOC_BASE+"Size.Unit.Inches")));
        this.add(jcImSize);
        
        jlHeight.setText(rbLocale.getString(LOC_BASE+"Size.Y"));
        jlHeight.setFont(new Font("Dialog", 0, 12));
        jlHeight.setAutoscrolls(true);
        jlHeight.setBounds(73, 105, 39, 20);     
        this.add(jlHeight);
        
        jtY.setBounds(120, 105, 90, 20);
        jtY.setDocument(new IntDocument(6));
        this.add(jtY);
                
        if (logger.isDebugEnabled()) {
            logger.debug("initSecondSection() - end");
        }
    }
    
    /**
     * Creates the Third (bottom) section of the 
     * dialog that contains the presets.
     */
    private void initThirdSection() {
        if (logger.isDebugEnabled()) {
            logger.debug("initThirdSection() - start");
        }

        jlPresetSmall = new JLabel();
        jlPresetLarge = new JLabel();
        jcPresets = new JComboBox();
        
        jlPresetLarge.setText(
                rbLocale.getString(LOC_BASE+"Presets"));
        jlPresetLarge.setFont(new Font("Dialog", 1, 12));
        jlPresetLarge.setForeground(new Color(0, 0, 128));
        jlPresetLarge.setBounds(9, 144, 60, 20);        
        this.add(jlPresetLarge);
        
        jlPresetSmall.setText(
                rbLocale.getString(LOC_BASE+"Presets")+":");
        jlPresetSmall.setFont(new Font("Dialog", 0, 12));
        jlPresetSmall.setBounds(65, 168, 47, 20);        
        this.add(jlPresetSmall); 
        
        jcPresets.setBackground(new Color(255, 255, 255));
        jcPresets.setBounds(120, 168, 190, 20);
        //TODO add some image presets. Allow user creation of own?
        jcPresets.addItem(new String("Line 1"));
        jcPresets.addItem(new String("Line 2"));
        jcPresets.addItem(new String("Line 3"));
        this.add(jcPresets);        
        
        if (logger.isDebugEnabled()) {
            logger.debug("initThirdSection() - end");
        }
    }
    
    /**
     * Creates the fourth section, (very bottom),
     * where the buttons are stored.
     */
    private void initButtons() {
        if (logger.isDebugEnabled()) {
            logger.debug("initButtons() - start");
        }
        
        jbOk = new JButton();
        jbCancel = new JButton();
        
        jbOk.setText(rbLocale.getString(LOC_BASE+"Button.Ok"));
        jbOk.setBounds(220, 205, 90, 25);
        jbOk.addActionListener(this);
        this.add(jbOk);        
        
        jbCancel.setText(rbLocale.getString(LOC_BASE+"Button.Cancel"));
        jbCancel.setBounds(120, 205, 90, 25);
        jbCancel.addActionListener(this);        
        this.add(jbCancel);
        
        if (logger.isDebugEnabled()) {
            logger.debug("initButtons() - end");
        }
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if (logger.isDebugEnabled()) {
            logger.debug("actionPerformed(ActionEvent) - start");
        }
        
        Object src = e.getSource();
        
        if (src.equals(jbOk))
            actionOk();
        else if(src.equals(jbCancel))
            this.dispose();
    }
    
    /**
     * The action to perform when the user wants to create the new
     * image. This method is called by the user clicking the okay button,
     * or pressing enter.
     * 
     * <p>It checks the settings, and if they are incorrect, displays
     * an error dialog, otherwise, if correct, it passes them back to
     * the main JVector class.</p>
     * 
     * @see NewImageDialog#validateSettings()
     * @see com.terei.jvector.JVector#newImage(String,int,int,int,
     *                              com.terei.jvector.paint.shapes.Shape[])
     */
    private void actionOk() {
        if (logger.isDebugEnabled()) {
            logger.debug("actionOk() - start");
        }

        if (validateSettings()){
            String n = jtName.getText();
            int x = Integer.parseInt(jtX.getText());
            int y = Integer.parseInt(jtY.getText());
            if (jcImSize.getSelectedItem().equals(
            		rbLocale.getString(LOC_BASE+"Size.Unit.Cm"))) {
                x = (int)(x*PIX_CM);
                y = (int)(y*PIX_CM);
            } else if (jcImSize.getSelectedItem().equals(
            		rbLocale.getString(LOC_BASE+"Size.Unit.Inches"))) {
            	x = (int)(x*PIX_INCHES);
                y = (int)(y*PIX_INCHES);
            }
            done = true;
            parent.newImage(n, x, y, 1,null);
            this.dispose();
        } else {
        	//They are bad, so show an error dialog.
            JOptionPane.showMessageDialog(this,
                    	rbLocale.getString("Dialog.Error.InvalImSets.Message"),
                        rbLocale.getString("Dialog.Error.InvalImSets.Title"),
                        JOptionPane.ERROR_MESSAGE);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("actionOk() - end");
        }
    }

    /**
     * Checks to see if the image size and name is valid.
     * 
     * @return If the image is valid
     */
    private boolean validateSettings() {
        if (logger.isDebugEnabled()) {
            logger.debug("validateSettings() - start");
        }

        String n;
        int x;
        int y;
        
        try {
             n = jtName.getText();
             x = Integer.parseInt(jtX.getText());
             y = Integer.parseInt(jtY.getText());
        } catch (Exception e) {
            logger.error("validateSettings()", e);

            if (logger.isDebugEnabled()) {
                logger.debug("validateSettings() - end");
            }
            return false;
        }
        
        if (n.equalsIgnoreCase(""))
        	return false;
        
        //Make sure the image is at least 1 x 1
        //And no larger then 5000 x 5000.
        if (!(x>0 && x<5001))
			return false;
        if (!(y>0 && y<5001))
			return false;
        
        if (logger.isDebugEnabled()) {
            logger.debug("validateSettings() - end");
        }
        return true;
    }
    
    /**
	 * This method allows the status of the dialog to be checked.
	 *
	 * @return If the user has made his valid selections.
	 */
    public boolean getDone() {
        if (logger.isDebugEnabled()) {
            logger.debug("getDone() - start");
        }
        return done;    
	}
    
    /**
     * Get the name of the New Image.
     * 
     * @return The name of the new image.
     */
    public String getImName() {
        if (logger.isDebugEnabled()) {
            logger.debug("getImName() - start");
        }
        
        String returnString = jtName.getText();
        
        return returnString;      
    }
    
    /**
     * Get the width of the new image.
     * 
     * @return The width of the new image.
     * @throws NumberFormatException If the text in the width field isn't a number.
     */
    public int getImWidth() throws NumberFormatException {
        if (logger.isDebugEnabled()) {
            logger.debug("getImWidth() - start");
        }

        int returnint = Integer.parseInt(jtX.getText());
        
        return returnint;
    }

    /**
     * Get the height of the new image.
     * 
     * @return The height of the new image.
     * @throws NumberFormatException If the text in the width field isn't a number.
     */
    public int getImHeight() throws NumberFormatException {
        if (logger.isDebugEnabled()) {
            logger.debug("getImHeight() - start");
        }

        int returnint = Integer.parseInt(jtY.getText());

        return returnint;
    }
    
}
