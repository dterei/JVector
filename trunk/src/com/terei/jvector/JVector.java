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
package com.terei.jvector;

//TODO class to large? split up? better OO design?
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import com.terei.jvector.gui.AboutDialog;
import com.terei.jvector.gui.ColorPanel;
import com.terei.jvector.gui.ImageExporter;
import com.terei.jvector.gui.NewImageDialog;
import com.terei.jvector.gui.PaintTab;
import com.terei.jvector.gui.PaintTabbedPane;
import com.terei.jvector.gui.SplashScreen;
import com.terei.jvector.gui.StatusBarManager;
import com.terei.jvector.gui.SwingFileFilter;
import com.terei.jvector.gui.preferences.PreferencesDialog;
import com.terei.jvector.gui.tools.ZoomOptions;
import com.terei.jvector.language.LangLoader;
import com.terei.jvector.paint.ImageFile;
import com.terei.jvector.paint.PaintCanvas;
import com.terei.jvector.paint.Tool;
import com.terei.jvector.paint.shapes.LineManager;
import com.terei.jvector.paint.shapes.OvalManager;
import com.terei.jvector.paint.shapes.PolygonManager;
import com.terei.jvector.paint.shapes.RectangleManager;
import com.terei.jvector.paint.shapes.Shape;
import com.terei.jvector.paint.shapes.ShapeManager;
import com.terei.jvector.paint.shapes.TextManager;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.BrowserLauncherRunner;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;

/**
 * This is the main class for the JVector project. This
 * project aims to create a vector based paint program, for
 * more details, see the documentation that should have
 * accompinied this source code.
 * 
 * This class initializes the program and provides the GUI
 * frontend of the porgram.
 * 
 * @author David Terei
 * @since 19/04/2004 (dd/mm/yyyy)
 * @version 1
 */
@SuppressWarnings("serial")
public class JVector extends JFrame implements ActionListener {

	/**
	 * Initializes the class.
	 * 
	 * @param args No arguments used.
	 */
	public static void main(String[] args) {

		setupLog();
		setLookAndFeel();

		if (logger.isInfoEnabled()) {
			logger.info("Initialize JVector");
		}

		JVector vect = new JVector();

		if (logger.isInfoEnabled()) {
			logger.info("Open any images passed in as arguments");
		}

		for (String arg : args)
			vect.openImage(arg);

		if (logger.isDebugEnabled()) {
			logger.debug("Create new image for debugging");
			vect.newImage("Debug 1", 400, 400, 100, null);
			vect.newImage("Debug 2", 400, 400, 100, null);
		}

	}

	/**
	 * The logger for this class.
	 */
	public static final Logger logger = Logger.getLogger(JVector.class);

	/**
	 * Sets the Look and Feel of the program.
	 */
	private static void setLookAndFeel() {
		if (logger.isInfoEnabled()) {
			logger.info("Setting Look and Feel");
		}
		try { 
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.print("Hello");
			logger.error("Error setting native LAF: " + e);
			logger.error("Trying to set Cross Platform L&F");
			try {
				UIManager.setLookAndFeel(UIManager
						.getCrossPlatformLookAndFeelClassName());
			} catch (Exception e2) {
				logger.fatal("Unable to set Cross Platform L&F", e2);
				System.exit(1);
			}

		}

		if (logger.isDebugEnabled()) {
			logger.debug("setLookAndFeel() - end");
		}
	}

	/**
	 * Setups the logging.
	 * 
	 * @see #logger
	 */
	private static void setupLog() {
		//TODO Check this method wont crash if loading log file fails
		Properties props = new Properties();
		try {
			InputStream in = new FileInputStream("log.properties");
			props.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}

		PropertyConfigurator.configure(props);

		if (logger.isDebugEnabled()) {
			logger.debug("Log Started...");
		}

		if (logger.isInfoEnabled()) {

			logger.info("***System Environment As Seen By Java***");
			logger.info("***Format: PROPERTY = VALUE***");

			java.util.Properties a = System.getProperties();
			java.util.Enumeration b = a.propertyNames();

			while (b.hasMoreElements()) {
				String key = (String) b.nextElement();
				logger.info(key + " = " + System.getProperty(key));
			}

		}

		if (logger.isDebugEnabled()) {
			logger.debug("************************************");
		}

	}

	/**
	 * The extensio, exlcuiding the dot, of the native image format of JVector.
	 */
	private static final String IMAGE_EXT = "jvi";

	/**
	 * The path to where the tool icons are stored.
	 */
	private static final String TOOL_PATH = "Resources/Tools/";

	/**
	 * The name of the locale to load.
	 */
	private static String LOCALE = "jvector";

	/**
	 * The url of the JVector homepage.
	 */
	private static String HOMEPAGE_URL = "http://t-bone-paint.sourceforge.net/";

	/**
	 * The locale file for the program.
	 * Contains all the strings for the program.
	 */
	private static ResourceBundle rbLocale;

	/**
	 * A clas which provides an easy method to implement
	 * a status bar.
	 */
	public static StatusBarManager cSbm;

	/**
	 * Holds this classes content pane.
	 */
	private Container cont;

	/**
	 * Holds the edditing components, incluiding the tabbed
	 * canvas and toolbars.
	 */
	private JPanel jpEditor;

	/**
	 * Holds the help status bar at the bottom of the 
	 * application.
	 */
	private JPanel jpStatus = new JPanel();

	/**
	 * Holds a panel added to the extra tool bar that provides
	 * the ability to select a colour.
	 */
	private ColorPanel jpColor;

	/**
	 * Holds a panel added to the extra tool bar that provides
	 * some options specific to the tool currently selected.
	 */
	private JPanel jpTool = new JPanel();

	/**
	 * The Menu Bar.
	 */
	private JMenuBar jmbM = new JMenuBar();

	/**
	 * The File Menu Bar.
	 */
	private JMenu jmFile;

	/**
	 * The Options Menu Bar.
	 */
	private JMenu jmOptions;

	/**
	 * The Help Menu Bar.
	 */
	private JMenu jmHelp;

	/**
	 * The new menu item under the file menu.
	 */
	private JMenuItem jmiF_new;

	/**
	 * The open menu item under the file menu.
	 */
	private JMenuItem jmiF_open;

	/**
	 * The save menu item under the file menu.
	 */
	public static JMenuItem jmiF_save;

	/**
	 * The save as menu item under the file menu.
	 */
	private JMenuItem jmiF_saveAs;

	/**
	 * The close menu item under the file menu.
	 */
	private JMenuItem jmiF_close;

	/**
	 * The export menu item under the file menu.
	 */
	private JMenuItem jmiF_export;

	/**
	 * The exit menu item under the file menu.
	 */
	private JMenuItem jmiF_exit;

	/**
	 * The preferences menu item under the options menu.
	 */
	private JMenuItem jmiO_prefs;

	/**
	 * The Home Page menu item under the help menu.
	 */
	private JMenuItem jmiH_hompg;

	/**
	 * The about menu item under the help menu.
	 */
	private JMenuItem jmiH_about;

	/**
	 * The Tabbed Pane where the various open images are held.
	 */
	private PaintTabbedPane cPtp;

	/**
	 * The Tool Bar that holds the drawing tools.
	 */
	private JToolBar jtbDraw;

	/**
	 * The tool bar that holds the colors and tool options.
	 */
	private JToolBar jtbO;

	/**
	 * The help status line the appears at the bottom of
	 * the screen.
	 */
	private static JLabel jlStatus = new JLabel(" ");

	/**
	 * Holds the zoom level of the currently selected image.
	 */
	public static JLabel jlZoomLvl = new JLabel("100%");

	/**
	 * Initialise the StatusBarManager class, cSbm, and load the
	 * language locale resource bundle for these static variables.
	 */
	static {
		try {
			rbLocale = LangLoader.getLocale(LOCALE);
			cSbm = new StatusBarManager(jlStatus);
			jlStatus.setBorder(BorderFactory.createLoweredBevelBorder());
		} catch (Exception e) {
			logger.fatal("static block: Loading Locale and creating new "
					+ "StatusBarManager", e);
			System.exit(1);
		}
	}

	/**
	 * Initialises the program. Calls the various methods to
	 * set up the GUI and event handlers.
	 */
	public JVector() {
		//open up a loading splash.
		if (logger.isInfoEnabled()) {
			JVector.logger.info("Starting SplashScreen");
		}

		Properties props = loadProperties();
		int enabled = 1;
		try {
			enabled = Integer.parseInt(props.getProperty("ShowSplash"));
		} catch (NumberFormatException e) {
			logger.error("JVector() - catch", e);
			e.printStackTrace();
		}

		SplashScreen splash = null;
		if (enabled == 1) {
			splash = new SplashScreen(loadImageIcon("Resources/splash.png"));
			splash.pack();
			splash.setVisible(true);
		}

		if (logger.isInfoEnabled()) {
			logger.info("Setting up GUI...");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Creating ColorPanel");
		}

		jpColor = new ColorPanel();
		setupFrame();
		setupStatusBar();
		setupMenu();
		setupEditorPane();
		cSbm.enableHints(this);

		//show the program.
		EventQueue.invokeLater(new FrameShower(this));
		try {
			if (enabled == 1)
				splash.setVisible(false);
		} catch (NullPointerException e) {
			logger.error("JVector() - catch", e);
			e.printStackTrace();
		}
	}

	/**
	 * Loads the porperies file.
	 * 
	 * @return The properties file in which the preferences are stored.
	 */
	public static Properties loadProperties() {
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
	 * A small class that takes a frame, and calls its <Code>show()</Code>
	 * method, using a thread. This allows the given frame to be shown in
	 * a thread safe manner by enqueing it on the event dispatch thread.
	 *
	 * @see <a href="http://java.sun.com/developer/JDCTechTips/2003/tt1208.html#1"> An Sun Java Article Explaining this</a>
	 * 
	 * @author David Terei
	 * @since 22/01/2004
	 * @version 1
	 */
	private static class FrameShower implements Runnable {
		/**
		 * Log4J Logger for this class
		 */
		private static final Logger logger = Logger
				.getLogger(FrameShower.class);

		/**
		 * The frame to show.
		 */
		final Frame frame;

		/**
		 * Constuct a new FrameShower.
		 * 
		 * @param frame The frame to show.
		 */
		public FrameShower(Frame frame) {
			if (logger.isDebugEnabled()) {
				logger.debug("FrameShower(Frame) - start");
			}
			this.frame = frame;
		}

		/**
		 * The thread, just calls the given frame <Code>show()</Code> method.
		 */
		public void run() {
			if (logger.isDebugEnabled()) {
				logger.debug("run() - start");
			}
			frame.setVisible(true);

			if (logger.isDebugEnabled()) {
				logger.debug("run() - end");
			}
		}
	}

	/**
	 * A inner class that handles component events.
	 * Specifically it handles the component events, of
	 * the various toolbars, calling appropriate methods
	 * when they are moved to resize the various windows.
	 * 
	 * @author David Terei
	 * @since 22/04/2004
	 * @version 1
	 */
	private class MyComponentListener implements ComponentListener {

		/* (non-Javadoc)
		 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
		 */
		public void componentHidden(ComponentEvent e) {
			if (logger.isDebugEnabled()) {
				logger.debug("componentHidden(ComponentEvent) - start");
			}

			if (logger.isDebugEnabled()) {
				logger.debug("componentHidden(ComponentEvent) - end");
			}
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
		 */
		public void componentMoved(ComponentEvent e) {
			if (logger.isDebugEnabled()) {
				logger.debug("componentMoved(ComponentEvent) - start");
			}

			if (logger.isDebugEnabled()) {
				logger.debug("componentMoved(ComponentEvent) - end");
			}
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
		 */
		public void componentResized(ComponentEvent e) {
			if (logger.isDebugEnabled()) {
				logger.debug("componentResized(ComponentEvent) - start");
			}
			setupExtraBar();

			Component[] comps = cPtp.getComponents();
			for (int i = 0; i < comps.length; i++) {
				if (comps[i] instanceof PaintTab) {
					PaintTab tab = (PaintTab) comps[i];
					tab.resizeView(tab.SIZE_CURRENT.x, tab.SIZE_CURRENT.y, cPtp
							.getSize().width, cPtp.getSize().height);
				}

			}

			if (logger.isDebugEnabled()) {
				logger.debug("componentResized(ComponentEvent) - end");
			}

		}

		/* (non-Javadoc)
		 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
		 */
		public void componentShown(ComponentEvent e) {
			if (logger.isDebugEnabled()) {
				logger.debug("componentShown(ComponentEvent) - start");
			}

			if (logger.isDebugEnabled()) {
				logger.debug("componentShown(ComponentEvent) - end");
			}
		}

	}

	/**
	 * Get the resource bundle containg the current locale.
	 * 
	 * @return Returns the rbLocale.
	 */
	public static ResourceBundle getRBLocale() {
		if (logger.isDebugEnabled()) {
			logger.debug("getRBLocale()");
		}
		return rbLocale;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (logger.isDebugEnabled()) {
			logger.debug("actionPerformed(ActionEvent) - start");
		}
		//Get the source of the object
		Object src = e.getSource();

		if (src.equals(jmiF_new))
			NewImageDialog.showGUI(300, 200, this);
		else if (src.equals(jmiF_open))
			openImage();
		else if (src.equals(jmiF_save))
			saveImage();
		else if (src.equals(jmiF_saveAs))
			saveImageAs();
		else if (src.equals(jmiF_close))
			close();
		else if (src.equals(jmiF_export))
			export();
		else if (src.equals(jmiF_exit))
			System.exit(0); //Exit if its the exit item.
		else if (src.equals(jmiO_prefs))
			new PreferencesDialog().setVisible(true);
		else if (src.equals(jmiH_hompg)) {
			try {
				//launch the homepage with browser launcher
				BrowserLauncher bl = new BrowserLauncher(null);
				BrowserLauncherRunner runner = 
					new BrowserLauncherRunner(bl,HOMEPAGE_URL,null);
				Thread launcherThread = new Thread(runner);
				launcherThread.start();
			} catch (UnsupportedOperatingSystemException e1) {
				logger.error("actionPerformed(ActionEvent) - catch", e1);
			} catch (BrowserLaunchingInitializingException e2) {
				logger.error("actionPerformed(ActionEvent) - catch", e2);
			}
		} else if (src.equals(jmiH_about))
			new AboutDialog().setVisible(true);

		if (logger.isDebugEnabled()) {
			logger.debug("actionPerformed(ActionEvent) - end");
		}
	}

	/**
	 * Setups The JFrame properties, such as title.
	 */
	private void setupFrame() {
		if (logger.isDebugEnabled()) {
			logger.debug("Setting up the frame");
		}
		setTitle(rbLocale.getString("Title"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 700, 550);

		//The following statement needs to be commented out
		//to work on Max OS X, as there VM doesn't include
		//this method. (Only for when trying to compile).
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		//set the frame icon.
		try {
			Image icon = loadImageIcon("Resources/icon.png").getImage();
			setIconImage(icon);
		} catch (NullPointerException e) {
			logger.warn("Missing JVector Frame Icon", e);
		}

		//set the frame layout.
		cont = getContentPane();
		cont.setLayout(new BorderLayout());

		if (logger.isDebugEnabled()) {
			logger.debug("setupFrame() - end");
		}
	}

	/**
	 * Setups the status bar at the bottom of the application
	 * that displays helpful hints
	 */
	private void setupStatusBar() {
		if (logger.isDebugEnabled()) {
			logger.debug("Setup Status Bar");
		}
		jpStatus.setLayout(new BorderLayout());
		jpStatus.add(jlStatus);
		cont.add(jpStatus, BorderLayout.SOUTH);

		if (logger.isDebugEnabled()) {
			logger.debug("setupStatusBar() - end");
		}
	}

	/**
	 * Setups the Menu Bar
	 */
	private void setupMenu() {
		if (logger.isDebugEnabled()) {
			logger.debug("Setup Menu");
		}
		jmFile = setupFileJM();
		jmOptions = setupOptionsJM();
		jmHelp = setupHelpJM();
		jmbM.add(jmFile);
		jmbM.add(jmOptions);
		jmbM.add(jmHelp);
		this.setJMenuBar(jmbM);

		if (logger.isDebugEnabled()) {
			logger.debug("setupMenu() - end");
		}
	}

	/**
	 * Setups the File Menu on the Menu Bar
	 * 
	 * @return The file menu.
	 */
	private JMenu setupFileJM() {
		if (logger.isDebugEnabled()) {
			logger.debug("Setup File Menu");
		}
		//Create the File Menu
		JMenu jm = new JMenu(rbLocale.getString("Menu.File"));

		//Create the vrious menu items
		jmiF_new = createMenuItem("File.New");
		jmiF_new.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.CTRL_MASK));

		jmiF_open = createMenuItem("File.Open");
		jmiF_open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				ActionEvent.CTRL_MASK));

		jmiF_save = createMenuItem("File.Save");
		jmiF_save.setEnabled(false);
		jmiF_save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.CTRL_MASK));

		jmiF_saveAs = createMenuItem("File.SaveAs");

		jmiF_close = createMenuItem("File.Close");
		jmiF_close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				ActionEvent.CTRL_MASK));

		jmiF_export = createMenuItem("File.Export");
		jmiF_export.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
				ActionEvent.CTRL_MASK));

		jmiF_exit = createMenuItem("File.Exit");
		jmiF_exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				ActionEvent.CTRL_MASK));

		//Add the menu items to the File Menu
		jm.add(jmiF_new);
		jm.add(jmiF_open);
		jm.add(jmiF_close);
		jm.addSeparator();
		jm.add(jmiF_save);
		jm.add(jmiF_saveAs);
		jm.addSeparator();
		jm.add(jmiF_export);
		jm.addSeparator();
		jm.add(jmiF_exit);

		//Return the menu

		if (logger.isDebugEnabled()) {
			logger.debug("setupFileJM() - end");
		}
		return jm;
	}

	/**
	 * Setups the Options Menu on the Menu Bar
	 * 
	 * @return The Options JMenu.
	 */
	private JMenu setupOptionsJM() {
		if (logger.isDebugEnabled()) {
			logger.debug("setupOptionsJM() - start");
		}

		JMenu jm = new JMenu(rbLocale.getString("Menu.Options"));

		jmiO_prefs = createMenuItem("Options.Preferences");
		jmiO_prefs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				ActionEvent.CTRL_MASK));

		jm.add(jmiO_prefs);

		if (logger.isDebugEnabled()) {
			logger.debug("setupOptionsJM() - end");
		}
		return jm;
	}

	/**
	 * Setups the Help Menu on the Menu Bar
	 * 
	 * @return The Help JMenu.
	 */
	private JMenu setupHelpJM() {
		if (logger.isDebugEnabled()) {
			logger.debug("setupHelpJM() - start");
		}

		JMenu jm = new JMenu(rbLocale.getString("Menu.Help"));

		jmiH_hompg = createMenuItem("Help.Homepage");
		jmiH_about = createMenuItem("Help.About");

		jm.add(jmiH_hompg);
		jm.addSeparator();
		jm.add(jmiH_about);

		if (logger.isDebugEnabled()) {
			logger.debug("setupHelpJM() - end");
		}
		return jm;
	}

	/**
	 * Provides an easy method to create menu items to
	 * avoide repetative coding.
	 * 
	 * @param item The internal name of the menu item, generally the english name.
	 * @return A setup JMenuItem.
	 */
	private JMenuItem createMenuItem(String item) {
		if (logger.isDebugEnabled()) {
			logger.debug("Create Menu Item: " + item);
		}
		JMenuItem jmi = new JMenuItem("Error!");
		try {
			//Crete Menu Item and Listener
			jmi.setText(rbLocale.getString("Menu." + item));
			jmi.addActionListener(this);

			//try To Get its tooltip.
			//If none exists, catch the exception, and set
			//its name as the tooltip.        
			cSbm.addHintFor(jmi, rbLocale.getString("Menu." + item + ".Tip"));
		} catch (MissingResourceException e) {
			logger.error("Creating Menu Item: " + item, e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("createMenuItem(String) - end");
		}
		return jmi;
	}

	/**
	 * Setups the Editor pane, which containts the
	 * drawing tabs and toolbars.
	 */
	private void setupEditorPane() {
		if (logger.isDebugEnabled()) {
			logger.debug("Setup the image editing area");
		}
		//Setup the JPanel
		jpEditor = new JPanel(new BorderLayout());

		//Setups the various items on it
		setupToolBar();
		initExtraBar();
		setupTabbedPane();

		//add it to the frame
		cont.add(jpEditor, BorderLayout.CENTER);

		if (logger.isDebugEnabled()) {
			logger.debug("setupEditorPane() - end");
		}
	}

	/**
	 * Setupd the toolbar where all the image drawing tools
	 * are situated.
	 */
	private void setupToolBar() {
		if (logger.isDebugEnabled()) {
			logger.debug("setup the tool toolbar");
		}

		//Setup the tool bar, first load the locale string, catching an
		//exception generated if the string is missing.
		try {
			jtbDraw = new JToolBar(rbLocale.getString("DrawBar.Title"),
					JToolBar.VERTICAL);
		} catch (MissingResourceException e) {
			jtbDraw = new JToolBar("ToolBar", JToolBar.VERTICAL);
			logger.error("Missing locale string for Tool tool bar", e);
		}

		jtbDraw.setRollover(true);
		cSbm.addHintFor(jtbDraw, rbLocale.getString("DrawBar.ToolTip"));
		jtbDraw.setToolTipText(rbLocale.getString("DrawBar.ToolTip"));
		jtbDraw.addComponentListener(new MyComponentListener());

		//Create and Add the tools
		JButton sele = createTool("select.png", "DrawBar.Select.Tip",Tool.SELECT);
		jtbDraw.add(sele);
		JButton line = createTool("line.png", "DrawBar.Line.Tip",Tool.LINE);
		jtbDraw.add(line);
		JButton rect = createTool("rectangle.png", "DrawBar.Rectangle.Tip",Tool.RECTANGLE);
		jtbDraw.add(rect);
		JButton oval = createTool("oval.png", "DrawBar.Oval.Tip",Tool.OVAL);
		jtbDraw.add(oval);
		JButton poly = createTool("polygon.png", "DrawBar.Polygon.Tip",Tool.POLYGON);
		jtbDraw.add(poly);
		JButton text = createTool("text.png", "DrawBar.Text.Tip",Tool.TEXT);
		jtbDraw.add(text);
		jtbDraw.addSeparator();
		JButton bin = createTool("bin.png", "DrawBar.Bin.Tip",Tool.DELETE);
		jtbDraw.add(bin);
		JButton zoom = createTool("zoom.png", "DrawBar.Zoom.Tip",Tool.ZOOM);
		jtbDraw.add(zoom);
		jtbDraw.addSeparator();
		jtbDraw.add(jlZoomLvl);

		//add the toolbar in
		jpEditor.add(jtbDraw, BorderLayout.WEST);
		if (logger.isDebugEnabled()) {
			logger.debug("setupToolBar() - end");
		}
	}

	/**
	 * Creates a Tool with the paramaters specified.
	 *
	 * @param icon_name The Icon for the Tool.
	 * @param TipReference The mouse over ToolTip for the tool 
	 *        (appears in status bar). This is just the string that 
	 *        is used in the locale file to identify it. Its key value.
	 * @param tool Which tool this tool is.
	 * 
	 * @return A Tool Bar Button.
	 */
	private JButton createTool(String icon_name, String TipReference, Tool tool) {
		if (logger.isDebugEnabled()) {
			logger.debug("create tool: " + icon_name);
		}

		ImageIcon icon = loadImageIcon(TOOL_PATH + icon_name);
		JButton button = new JButton(icon);
		try {
			String toolTip = rbLocale.getString(TipReference);
			cSbm.addHintFor(button, toolTip);
		} catch (MissingResourceException e) {
			logger.error("Missing locale string for tool tip for: "
					+ TipReference, e);
		}

		button.setPreferredSize(new Dimension(36, 36));
		button.setMaximumSize(new Dimension(48, 48));
		button.setMinimumSize(new Dimension(24, 24));
		button.addActionListener(createToolListener(tool));

		if (logger.isDebugEnabled()) {
			logger.debug("createTool(String, String, int) - end");
		}
		return button;
	}

	/**
	 * Creates an Action listener for a tool button, that
	 * notifies the MapInterface class of a tool change.
	 *
	 *@param tool2 The tool that this listener will activate when called.
	 *@return An ActionListener that will set the tool to the one with the 
	 *        value specified.
	 *
	 * @see com.terei.jvector.gui.PaintTab#setTool(Tool)
	 */
	private ActionListener createToolListener(Tool tool2) {

		if (logger.isDebugEnabled()) {
			logger.debug("Create tool listener for: " + tool2);
		}

		//because of the way this is done, a final int must be used
		//within the action listener, so that it cant change.
		final Tool tool = tool2;

		//SDK 1.5? Enumeration
		//create a new action listener and return it.
		if (tool == Tool.DELETE)
			return createDeleteListener();
		else
			return (new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					if (logger.isDebugEnabled()) {
						logger.debug("actionPerformed(ActionEvent) - start");
					}

					PaintTab.setTool(tool);
					setupExtraBar();

					if (logger.isDebugEnabled()) {
						logger.debug("actionPerformed(ActionEvent) - end");
					}
				}
			});

	}

	/**
	 * Creates a listener for a shape deletion tool, that deletes  the currently
	 * selected shape.
	 * 
	 * @return An action listener
	 */
	private ActionListener createDeleteListener() {
		if (logger.isDebugEnabled()) {
			logger.debug("createDeleteListener Listener");
		}
		return (new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (logger.isDebugEnabled()) {
					logger.debug("actionPerformed(ActionEvent) - start");
				}

				PaintTab tab = (PaintTab) cPtp.getSelectedComponent();
				PaintCanvas canvas = (PaintCanvas) tab.getCanvas();
				canvas.deleteSelectedShape();

				if (logger.isDebugEnabled()) {
					logger.debug("actionPerformed(ActionEvent) - end");
				}
			}
		});
	}

	/**
	 * Initialises the extra tool bar. This method needs to be called
	 * only once at the start, after that, <code>setupExtraBar()</code>
	 * should be called.
	 */
	private void initExtraBar() {

		if (logger.isDebugEnabled()) {
			logger.debug("Create 'Extra' tool bar");
		}

		try {
			jtbO = new JToolBar(rbLocale.getString("ExtraBar.Title"),
					JToolBar.VERTICAL);
		} catch (MissingResourceException e) {
			logger.warn("Missing locale string for extra tool bar title", e);
			jtbO = new JToolBar("Extra ToolBar", JToolBar.VERTICAL);
		}

		jtbO.addComponentListener(new MyComponentListener());

		setupExtraBar();

		jpEditor.add(jtbO, BorderLayout.EAST);

		if (logger.isDebugEnabled()) {
			logger.debug("initExtraBar() - end");
		}
	}

	/**
	 * Setups the extra tool bar which contains the colour chooser and 
	 * the tool options bar. This method should be called every time
	 * the Extra Bar is modified.
	 */
	private void setupExtraBar() {
		if (logger.isDebugEnabled()) {
			logger.debug("Setup the extra bar");
		}

		//get the tool bars orientation.
		int orit = jtbO.getOrientation();

		//the tool bar already exist, so remove everything.
		jtbO.removeAll();

		//add a rigid component to the tool bar, so that it doesnt push right
		//against the panel added to it. The dimensions of the rigid area depend
		//on the orientation of the tool bar.
		if (orit == JToolBar.VERTICAL)
			jtbO.add(Box
					.createRigidArea(new Dimension(ColorPanel.WIDTH + 20, 2)));
		else
			jtbO.add(Box.createRigidArea(new Dimension(5, 2)));

		//Add the tools back in
		jtbO.add(jpColor);
		jpTool = getToolOptionBar();
		jtbO.add(jpTool);

		//tell it to 'refresh' itself.
		jtbO.revalidate();

		if (logger.isDebugEnabled()) {
			logger.debug("setupExtraBar() - end");
		}
	}

	/**
	 * Creates the jpanel to add to the extra tool bar
	 * which contains the various options for the currently
	 * selected tool.
	 * 
	 * @see com.terei.jvector.paint.shapes.ShapeManager
	 * @return A JPanel that contains various options for the current tool.
	 */
	private JPanel getToolOptionBar() {
		if (logger.isDebugEnabled()) {
			logger.debug("getToolOptionBar() - start");
		}

		//get the currently selected tool.
		Tool TOOL = PaintCanvas.getTool();

		if (logger.isDebugEnabled()) {
			logger.debug("get shape option panel: " + TOOL);
		}

		//new jpanel that will hold the tools option jpanel.
		JPanel panel = ShapeManager.getOptionPanel();

		//Get the option panel for the currently selected tool.
		switch (TOOL) {
			case NULL: ; break;
			case LINE: panel = LineManager.getOptionPanel(); break;
			case RECTANGLE: panel = RectangleManager.getOptionPanel(); break;
			case OVAL: panel = OvalManager.getOptionPanel(); break;
			case POLYGON: panel = PolygonManager.getOptionPanel(); break;
			case TEXT: panel = TextManager.getOptionPanel(); break;
			case ZOOM: panel = new ZoomOptions();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getToolOptionBar() - end");
		}
		return panel;
	}

	/**
	 * seups the tabbed pane which holds all the open
	 * images currently being worked on.
	 */
	private void setupTabbedPane() {

		if (logger.isDebugEnabled()) {
			logger.debug("Setup the tabbed image pane GUI");
		}

		cPtp = new PaintTabbedPane();
		cPtp.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		jpEditor.add(cPtp, BorderLayout.CENTER);

		if (logger.isDebugEnabled()) {
			logger.debug("setupTabbedPane() - end");
		}
	}

	/**
	 * Loads an image icon in a way that allows
	 * for the application to be stored in a rar
	 * or nativley compiled, as well as run as
	 * just plain class files.
	 *
	 * @param path The path, relative to the working directory, to the image.
	 * @return The image icon.
	 */
	public static ImageIcon loadImageIcon(String path) {

		if (logger.isDebugEnabled()) {
			logger.debug("loadImageIcon: " + path);
		}

		try {
			URL url = JVector.class.getClassLoader().getResource(path);
			ImageIcon returnImageIcon = (new ImageIcon(url));
			if (logger.isDebugEnabled()) {
				logger.debug("loadImageIcon(String) - end");
			}
			return returnImageIcon;
		} catch (MissingResourceException e) {
			logger.error("Missing Image Icon: " + path, e);
		}

		ImageIcon returnImageIcon = new ImageIcon();
		if (logger.isDebugEnabled()) {
			logger.debug("loadImageIcon(String) - end");
		}
		return returnImageIcon;

	}

	/**
	 * Creates a new Image/Canvas.
	 * 
	 * @param name The name of the image
	 * @param x The width of the image
	 * @param y The height of the image
	 * @param zoom The current zoom level
	 * @param shapes An array of shapes to add to the new image.
	 */
	public void newImage(String name, int x, int y, int zoom, Shape[] shapes) {
		if (logger.isInfoEnabled()) {
			logger.info("new Image: " + name + "," + x + "," + y + ","
					+ "Shapes: " + (shapes != null));
		}

		Dimension d_jtp = cPtp.getSize();
		PaintTab can = new PaintTab(name, x, y, d_jtp.width, d_jtp.height,
				zoom, shapes);
		cPtp.addTab(name, null, can);
		cPtp.setSelectedComponent(can);

		if (logger.isDebugEnabled()) {
			logger.debug("newImage(String, int, int, Shape[]) - end");
		}
	}

	/**
	 * Saves the currently selected image to JVectors native format.
	 * 
	 * @see #saveImageAs()
	 * @see ImageFile
	 * @see PaintCanvas#getImage()
	 */
	private void saveImage() {
		//TODO Fix zoom save bug
		if (logger.isInfoEnabled()) {
			logger.info("Save Image");
		}

		try {

			PaintTab tab = (PaintTab) cPtp.getSelectedComponent();
			File file = tab.getCanvas().getFile();
			if (logger.isDebugEnabled()) {
				logger.debug("Image: " + file);
			}

			ImageFile image = tab.getCanvas().getImage();

			FileOutputStream fos = new FileOutputStream(file);

			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(image);

			oos.close();
			fos.close();

			tab.getCanvas().notifySaved();
			tab.getCanvas().revalidateZoom();

		} catch (IOException e) {
			logger.error("Saving Image: writing Image to stream failed", e);
		} catch (ClassCastException e) {
			logger.error("Casting  selected tab to a PaintTab", e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("saveImage() - end");
		}
	}

	/**
	 * Saves the currently selected image to JVectors native format, launching
	 * a JFileChooser that allows the user to select the output file.
	 *
	 * @see #saveImage()
	 * @see ImageFile
	 * @see PaintCanvas#getImage()
	 */
	private void saveImageAs() {
		if (logger.isDebugEnabled()) {
			logger.debug("Saving Image As...");
		}

		//make sure they have an image open, if not, dont show the save as dialog.
		try {
			Component comp = cPtp.getSelectedComponent();
			if (!(comp instanceof PaintTab))
				return;
		} catch (Exception e) {
			logger.warn("saveImageAs() - catch", e);
		}

		JFileChooser chooser = new JFileChooser();
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);

		try {
			String description = rbLocale
					.getString("Dialog.Save.FilterDescription");
			chooser.setFileFilter(new SwingFileFilter(IMAGE_EXT, description,
					true));
			chooser.setDialogTitle(rbLocale.getString("Dialog.Save.Title"));
		} catch (MissingResourceException e) {
			logger.error("Missing locale string for Save dialog", e);
		}

		int returnVal = chooser.showSaveDialog(this);

		if (returnVal != JFileChooser.APPROVE_OPTION)
			return;

		try {

			PaintTab tab = (PaintTab) cPtp.getSelectedComponent();

			File file = chooser.getSelectedFile();

			String name = file.getName();

			if (!name.endsWith("." + IMAGE_EXT)) {
				String path = file.getAbsolutePath() + "." + IMAGE_EXT;
				file = new File(path);
			}

			tab.getCanvas().setFile(file);
			saveImage();
			jmiF_save.setEnabled(true);

		} catch (ClassCastException e) {
			logger.error("Casting  selected tab to a PaintTab", e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("saveImageAs() - end");
		}
	}

	/**
	 * Opens up a pre existing image from the local disc, launching a 
	 * file chooser dialog to find the file.
	 */
	private void openImage() {
		if (logger.isDebugEnabled()) {
			logger.debug("Open an Image, launchung File Chooser");
		}

		JFileChooser chooser = new JFileChooser();
		String description = rbLocale
				.getString("Dialog.Open.FilterDescription");
		chooser
				.setFileFilter(new SwingFileFilter(IMAGE_EXT, description, true));
		chooser.setDialogTitle(rbLocale.getString("Dialog.Open.Title"));
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);

		int returnVal = chooser.showOpenDialog(this);

		if (returnVal != JFileChooser.APPROVE_OPTION)
			return;

		openImage(chooser.getSelectedFile().getAbsolutePath());

		if (logger.isDebugEnabled()) {
			logger.debug("openImage() - end");
		}
	}

	/**
	 * Opens up a pre existing image from the local disc.
	 * 
	 * @param path The path to the image to open.
	 */
	private void openImage(String path) {
		if (logger.isInfoEnabled()) {
			logger.info("openImage: " + path);
		}

		try {
			//load the image through serilization.
			File file = new File(path);
			FileInputStream fis = new FileInputStream(file);

			ObjectInputStream ois = new ObjectInputStream(fis);
			ImageFile image = (ImageFile) ois.readObject();

			ois.close();
			fis.close();

			//get the image properties
			String name = image.getName();
			int width = image.getWidth();
			int height = image.getHeight();
			int zoom = image.getZoom();
			//TODO: Prompt user for input box if any of these values illegal.
			if (zoom == 0)
				zoom = 100;
			//create a new image from the loaded one.
			newImage(name, width, height, zoom, image.getShapes());

			//set the tab to be last
			cPtp.setSelectedIndex(cPtp.getTabCount() - 1);
			PaintTab tab = (PaintTab) cPtp.getSelectedComponent();

			//setup the canvas and refresh it.
			tab.getCanvas().setFile(file);
			tab.getCanvas().revalidateZoom();
			tab.resizeView(tab.SIZE_CURRENT.x * (zoom/100), 
					tab.SIZE_CURRENT.y * (zoom/100), 
					cPtp.getSize().width, cPtp.getSize().height);
			//set the save option to true now.
			jmiF_save.setEnabled(true);
			//tell the canvas that this image currently has been saved
			tab.getCanvas().notifySaved();
			//refresh the whole tab
			tab.revalidate();

		} catch (Exception e) {
			logger.error("Error Opening Image: " + path, e);

			String b = "Dialog.Error.OpenImage.";
			JOptionPane.showMessageDialog(this, rbLocale.getString(b
					+ "Message"), rbLocale.getString(b + "Title"),
					JOptionPane.ERROR_MESSAGE);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("openImage(String) - end");
		}
	}

	/**
	 * Closes the currently selected image, checking if the image has been
	 * modified since the last save, and if so, launching a "Do You Want to
	 * Save..." dialog. 
	 */
	private void close() {
		if (logger.isInfoEnabled()) {
			logger.info("close - start");
		}

		try {
			Component comp = cPtp.getSelectedComponent();
			if (!(comp instanceof PaintTab))
				return;
		} catch (Exception e) {
			logger.warn("close() - catch", e);
		}

		PaintTab tab = (PaintTab) cPtp.getSelectedComponent();
		boolean saved = tab.getCanvas().isSaved();

		if (!saved) {
			String b = "Dialog.Close.";
			Object[] options = { rbLocale.getString(b + "Yes"),
					rbLocale.getString(b + "No"),
					rbLocale.getString(b + "Cancel") };

			int n = JOptionPane.showOptionDialog(this, rbLocale.getString(b
					+ "Message"), rbLocale.getString(b + "Title"),
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

			if (n == JOptionPane.YES_OPTION) {
				if (tab.getCanvas().getFile() != null)
					saveImage();
				else
					saveImageAs();
			} else if (n == JOptionPane.CANCEL_OPTION)
				return;
		}

		cPtp.remove(tab);
		tab.dispose();

		if (logger.isDebugEnabled()) {
			logger.debug("close() - end");
		}
	}

	/**
	 * Exports the current image to a different file type.
	 * 
	 * @see com.terei.jvector.gui.ImageExporter
	 */
	private void export() {
		if (logger.isInfoEnabled()) {
			logger.debug("export() - start");
		}

		try {
			Component comp = cPtp.getSelectedComponent();
			if (comp instanceof PaintTab) {
				PaintTab tab = (PaintTab) comp;
				ImageFile image = tab.getCanvas().getImage();
				File file = tab.getCanvas().getFile();

				ImageExporter.export(image, file);
			}
		} catch (Exception e) {
			logger.warn("export() - catch", e);

			String b = "Dialog.Error.Export.";
			JOptionPane.showMessageDialog(this, rbLocale.getString(b
					+ "Message"), rbLocale.getString(b + "Title"),
					JOptionPane.ERROR_MESSAGE);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("export() - end");
		}
	}

}
