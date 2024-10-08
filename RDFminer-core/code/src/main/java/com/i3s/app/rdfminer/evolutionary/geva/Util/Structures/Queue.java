/*
Grammatical Evolution in Java
Release: GEVA-v2.0.zip
Copyright (C) 2008 Michael O'Neill, Erik Hemberg, Anthony Brabazon, Conor Gilligan 
Contributors Patrick Middleburgh, Eliott Bartley, Jonathan Hugosson, Jeff Wrigh

Separate licences for asm, bsf, antlr, groovy, jscheme, commons-logging, jsci is included in the lib folder. 
Separate licence for rieps is included in src/com folder.

This licence refers to GEVA-v2.0.

This software is distributed under the terms of the GNU General Public License.


This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
/>.
*/

package com.i3s.app.rdfminer.evolutionary.geva.Util.Structures;

import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * A FIFO queue implementation containting a linked list
 */
public class Queue<T> {
    
    private LinkedList<T> items;

    /**
     * Create queue
     */
    public Queue() {
        this.items = new LinkedList<T>();
    }

    /**
     * add element to end of items
     * @param element element to add
     * @return added element
     */
    @SuppressWarnings({"UnusedReturnValue"})
    public T enqueue(T element) {
        items.add(element);
        return element;
    }

    /**
     * Remove first element.
     * @throws NoSuchElementException if size is 0
     * @return first element
     */
    public T dequeue() {
        if (items.size()== 0)
            throw new NoSuchElementException() ;
        return items.removeFirst();
    }

    /**
     * Check if queue is empty
     * @return true if it is empty
     */
    @SuppressWarnings({"BooleanMethodIsAlwaysInverted"})
    public boolean isEmpty() {
        return items.isEmpty();
    }


}