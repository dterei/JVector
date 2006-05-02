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
package com.terei.jvector.util;
/**
 * This class stores an array of ints, but provides dynamic functions
 * to add, and remove values from the array. The array can also grow as
 * nesseccary.
 * 
 * @author David Terei
 * @since 16/05/2004
 * @version 1
 */
public class DynamicIntArray {

    /**
     * An Array of Ints to hold the integers.
     */
    private int[] data;

    /**
     * Create a new DynamicIntArray.
     */
    public DynamicIntArray() {
        data = new int[1];
    }

    /**
     * Returns all of the array.
     *
     * @return The int Array
     */
    public int[] getAll() {
        return data;
    }

    /**
     * Returns the length of the array.
     *
     * @return The length of the array
     */
    public int length() {
        return data.length;
    }

    /**
     * Returns the int at the posistion given in the array, if it is outside
     * the arrays boundary, then -99 is returned.
     *
     * @param position The Posistion in the array of the int you want returned.
     *                  If it doesnt exist, -99 is returned.
     * @return The int requested.
     */
    public int get(int position) {
        if (position >= data.length)
            return -99;
        else
            return data[position];
    }

    /**
     * Increase the array length by one, and place the value
     * Specified in this posistion
     *
     * @param value An int which you want to store in the array.
     */
    public void add(int value) {
        int[] newData = new int[data.length+1];
        System.arraycopy(data, 0, newData, 0, data.length);
        data = newData;
        data[data.length-1] = value;
    }

    /**
     * Store the value in the specified position in the array.
     * The data array will increase in size to include this
     * position, if necessary.
     *
     * @param position The posistion in the array you want to value.
     * @param value An int which you want to store in the array.
     */
    public void add(int value,int position) {
        if (position >= data.length) {
            int[] newData = new int[position+1];
            System.arraycopy(data, 0, newData, 0, data.length);
            data = newData;
        }
        //Now we know the array is big enough, so we store the value given.
        data[position] = value;
    }

    /**
     * Delete the last value in the array, by reducing the array length
     *
     * @return If the operation was sucessful or not.
     */
     public boolean delete() {
        int[] newData = new int[data.length-1];
        System.arraycopy(data,0,newData,0,newData.length);
        data = newData;
        return true;
     }

    /**
     * Delete the value at the posistion specified, reducing the array appropriatley.
     *
     * @param posistion The posistion of the array element to delete.
     * @return If the operation was sucessful or not.
     */
     public boolean delete(int posistion) {
        if (posistion > data.length)
            return false;
        int[] newData = new int[data.length-1];
        System.arraycopy(data,0,newData,0,posistion-1);
        System.arraycopy(data,posistion-1,newData,posistion,newData.length);
        data = newData;
        return true;
     }

     /**
      * Sets this array to equal the speccified array, exactly.
      *
      * @param ints The array you want to set this array to be equal to.
      */
     public void equals(int[] ints) {
        data = null;
        data = new int[ints.length];
        data = ints;
     }
}