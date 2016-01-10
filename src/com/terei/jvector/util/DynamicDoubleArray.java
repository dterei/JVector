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
import java.io.Serializable;

/**
 * This class stores an array of doubles, but provides dynamic functions
 * to add, and remove values from the array. The array can also grow as
 * nesseccary.
 * 
 * @author David Terei
 * @since 16/05/2004
 * @version 1
 */
@SuppressWarnings("serial")
public class DynamicDoubleArray implements Serializable {

    /**
     * An Array of Double's to hold the double values.
     */
    private double[] data;

    /**
     * Create a new DynamicDoubleArray.
     */
    public DynamicDoubleArray() {
        //Initialize the array to hold 1 double, will grow as nessecary.
        data = new double[1];
    }

    /**
     * Returns all of the array.
     *
     * @return The double Array
     */
    public double[] getAll() {
        return data;
    }

    /**
     * Returns all of the array, as ints.
     *
     * @return The double Array as an Int array
     */
    public int[] getAllInts() {
        int[] back = new int[data.length];
        for (int i=0;i<data.length;i++)
            back[i]= (int)data[i];
        return back;
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
     * Returns the double at the posistion given in the array, if it is outside
     * the arrays boundary, then -99.99 is returned.
     *
     * @param position The Posistion in the array of the double you want returned.
     *                  If it doesnt exist, -99.99 is returned.
     * @return The double requested.
     */
    public double get(int position) {
        if (position >= data.length)
            return -99.99;
        else
            return data[position];
    }

    /**
     * Increase the array length by one, and place the value
     * Specified in this posistion
     *
     * @param value A double which you want to store in the array.
     */
    public void add(double value) {
        //Create A new Array, 1 size bigger.
        double[] newData = new double[data.length+1];
        //Copy the old array into the new array.
        System.arraycopy(data, 0, newData, 0, data.length);
        //Reference the old array to the new array.
        data = newData;
        //Now add in the value
        data[data.length-1] = value;
    }

    /**
     * Store the value in the specified position in the array.
     * The data array will increase in size to include this
     * position, if necessary.
     *
     * @param position The posistion in the array you want to value.
     * @param value A double which you want to store in the array.
     */
    public void add(double value,int position) {
        if (position >= data.length) {
            // The specified position is outside the actual size of
            // the data array. Set the array Size to be the size of
            // posistion.
            double[] newData = new double[position+1];
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
        //First, create a new array one length less to hold the new array.
        double[] newData = new double[data.length-1];
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
        //Check that the posistion given is within the bounds of the array.
        if (posistion > data.length)
            //Return that the operation didnt take place.
            return false;
        
        //Now we know that the posistion given must be within the bounds of data
        //First, create a new array one length less to hold the new array.
        double[] newData = new double[data.length-1];
        System.arraycopy(data,0,newData,0,posistion);
        System.arraycopy(data,posistion+1,newData,posistion,newData.length-posistion);
        data = newData;
        return true;
     }

     /**
      * Sets this array to equal the speccified array, exactly.
      *
      * @param doubles The array you want to set this array to be equal to.
      */
     public void equals(double[] doubles) {
        data = null;
        data = new double[doubles.length];
        data = doubles;
     }

     /**
      * Sets this array to equal the speccified array, exactly,
      * converting the int array to double values.
      *
      * @param ints The array you want to set this array to be equal to.
      */
     public void equals(int[] ints) {
        data = null;
        data = new double[ints.length];
        for (int i=0;i<ints.length;i++)
            data[i]  = (double)ints[i];
     }
}