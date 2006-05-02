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

/** These classes all help load and store the correct language file
 * specified by the user.
 */
package com.terei.jvector.language;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

/**
 * This class provides one easy static method to load the currently set language
 * file for the program specified.
 * 
 * @author David Terei
 * @since 26/12/03
 * @version 1
 */
public class LangLoader {
	
    private static final String LANG_PATH = "Languages/jvector";
	private static final String LANG_EXT = ".properties";
	/**
     * Log4J Logger for this class
     */
    private static final Logger logger = Logger.getLogger(LangLoader.class);
    
    /**
     * Dont Allow Contrsuction
     */
    private LangLoader() {
    }

    /**
     * Loads the language bundle specified, looking in the languages program,
     * relative to the program root. The locale to load is specified in the
     * lang.properties file in the program root folder. For example,
     * 
     * <p>
     * <Center><Dir>%PROGRAM_ROOT%\Languages\jvector_en_AU.lang </Dir>
     * </Center>
     * </p>
     * 
     * <p>
     * This is the path of the file it will atempt to load.
     * </p>
     * 
     * @param bundle
     *            the resurce bundle to load. %PROGRAM_ROOT%\Languages\%bundle%
     * @return The resource bundle requested in the locale specified in the
     *         %PROGRAM_root%\language.properties file.
     */
    public static ResourceBundle getLocale(String bundle) {
        PropertyResourceBundle lang = null;

        try {
            //get the language property file
            InputStream in = new FileInputStream("language.properties");
            PropertyResourceBundle props = new PropertyResourceBundle(in);

            //country and language strings
            String LC = "";
            String CC = "";
            //Get the locale Langauge code
            LC = props.getString("Language.Code");
            if (LC.length() > 0)
                LC = "_" + LC;
            //Get the Locale Country code
            CC = props.getString("Country.Code");
            if (CC.length() > 0)
                CC = "_" + CC;

            //oaky now load the actual lnaguage file that we have the
            //required info
            String path = LANG_PATH + LC + CC + LANG_EXT;
            InputStream in2 = new FileInputStream(path);
            lang = new PropertyResourceBundle(in2);

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (MissingResourceException e) {
            logger.fatal("Language File Fauiled to load", e);
            JOptionPane.showMessageDialog(null,
                    	"Failed To load Language File",
                        "Failed Language File",
                        JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        return lang;
    }

}
