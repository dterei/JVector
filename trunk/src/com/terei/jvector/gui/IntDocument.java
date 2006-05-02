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

import javax.swing.text.*;

/**
 * This class extends PlainDocument, to override the documents insertString
 * method, to restrict the string to insert to only containing ints, and not
 * letting the input field above it, exceed the length specified.
 * 
 * @see javax.swing.text.PlainDocument
 * 
 * @author David Terei
 * @since 16/12/03
 * @version 1
 */
@SuppressWarnings("serial")
public class IntDocument extends PlainDocument {
    
    /**
     * The maximun amount of characters allowed in the input field above this
     * document.
     */
    private int max;
    
    /**
     * Create a new IntDocument of the length specified.
     * 
     * @param MaxLength
     *            The maximun length of characters that the document will
     *            accept.
     */
    public IntDocument(int MaxLength) {
        max = MaxLength;
    }
    
    /**
     * Overrides PlainDocument's insertString method to restrict only string
     * containing ints to be inserted into the text document.
     * 
     * @param offs the starting offset >= 0
     * @param str the string to insert; does nothing with null/empty strings
     * @param a the attributes for the inserted content
     * @exception BadLocationException the given insert position is not a 
     * 									valid position within the document
     * @see javax.swing.text.PlainDocument#insertString(int,String,AttributeSet)
     */
    public void insertString(int offs, String str, AttributeSet a)
    									throws BadLocationException {
    	if (str == null) return;
        if (max < getLength() + str.length()) return;
        try {
        	Integer.parseInt(str);
        } catch(Exception e) {
        	return;
        }
  
        super.insertString(offs, new String(str), a);
    }
    
}