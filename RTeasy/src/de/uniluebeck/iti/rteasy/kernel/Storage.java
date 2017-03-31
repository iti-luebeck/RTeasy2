/*
 * Copyright (c) 2003-2013, University of Luebeck, Institute of Computer Engineering
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of Luebeck, the Institute of Computer
 *       Engineering nor the names of its contributors may be used to endorse or
 *       promote products derived from this software without specific prior
 *       written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE UNIVERSITY OF LUEBECK OR THE INSTITUTE OF COMPUTER
 * ENGINEERING BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package de.uniluebeck.iti.rteasy.kernel;

import de.uniluebeck.iti.rteasy.RTSimGlobals;
import de.uniluebeck.iti.rteasy.frontend.ASTStorDecl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jasper Schwinghammer
 */
public class Storage extends SimulationObject {

    private Map<Integer, BitVector> stor;
    private Map<Integer, BitVector> storNew;
    private String name;
    private int width;
    private int length;
    private int addrMaxWidth;
    private int lastWritten = 0;
    private BitRange br;
    private boolean direction = true;
    private int offset = 0;

    public Storage(ASTStorDecl decl) {
        super(decl.getName(), decl.getPositionRange());
        this.br = decl.getBitRange();
        this.name = decl.getName();
        this.width = decl.getWidth();
        this.length = decl.getNumberOfMemCells();
        stor = new HashMap();
        storNew = new HashMap();
        this.direction = direction;
        this.offset = offset;
        this.addrMaxWidth = (int)(Math.ceil(Math.log10(((double)length))/Math.log10(2.)));
    }

    public void set(int address, BitVector bv) {
        if (!checkIfWrittenThisCicle(address)) {
            storNew.put(address, bv);
            lastWritten = address;
        } else {
            //ERROR!
        }
    }

    public BitVector get(int address) {
        if (stor.containsKey(address)) {
            return stor.get(address);
        } else {
            return new BitVector();
        }
    }

    public void commit() {
        stor.putAll(storNew);
        storNew.clear();
    }

    public void clear() {
        stor.clear();
        storNew.clear();
    }

    /**
     * (taken from Memory.java, used to bring the whole memory data to the gui)
     *
     * @return the current state of the memory as array
     */
    public ArrayList getUsedCellsSorted() {
        ArrayList cells = new ArrayList(stor.keySet());
        for (int i = 0; i < cells.size(); i++) {
            cells.set(i, (new BitVector((int)cells.get(i))).toBoolArray(addrMaxWidth));
        }
        Collections.sort(cells, new BoolArrayComparator());
        return cells;
    }

    /**
     * Inner class to use the boolArrayCompare method from RTSimGlobals to
     * compare two numbers represented by bitwise booleans
     */
    class BoolArrayComparator implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            return RTSimGlobals.boolArrayCompare((boolean[]) o1, (boolean[]) o2);
        }
    }

    public void setDataAt(boolean address[], boolean data[]) {
        int dw = width;
        int aw = addrMaxWidth;
        int width = address.length <= aw ? address.length : aw;
        int addressInt = 0;
        for (int i = 0; i < width; i++) {
            addressInt += (address[i] ? 1 : 0) * Math.pow(2, i);
        }
        width = data.length <= dw ? data.length : dw;
        String s = "";
        for (int i = 0; i < width; i++) {
            s = (data[i] ? "1" : "0") + s;
        }
        BitVector dbv = new BitVector(s);
        stor.put(addressInt, dbv);
    }

    public boolean[] getDataAt(boolean address[]) {
        int dw = getWidth();
        boolean back[] = new boolean[dw];
        int addressInt = 0;
        int addressWidth = addrMaxWidth;
        for (int i = 0; i < addressWidth; i++) {
            addressInt += (address[i] ? 1 : 0) * Math.pow(2, i);
        }
        if (stor.containsKey(addressInt)) {
            BitVector bv = (BitVector) stor.get(addressInt);
            for (int i = 0; i < dw; i++) {
                back[i] = bv.get(i);
            }
        } else {
            for (int i = 0; i < dw; i++) {
                back[i] = false;
            }
        }
        return back;
    }
    
    public boolean[] getAddr() {
        BitVector bv = new BitVector(lastWritten);
        return bv.toBoolArray(bv.getWidth());
    }

    @Override
    public String getVHDLName() {
        return "stor_" + getIdStr();
    }

    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }
    
    public int getMaxAddrWidth() {
        return addrMaxWidth;
    }

    public boolean getDirection() {
        return direction;
    }

    /**
     *
     * @param i the address of the cell to be checked
     * @return true if the memory cell was already written this cycle
     */
    public boolean checkIfWrittenThisCicle(int i) {
        if (storNew.containsKey(i)) {
            return true;
        } else {
            return false;
        }
    }

    public int getOffset() {
        return offset;
    }

    public String getPrettyDecl() {
        return name;
    }

    public boolean checkBitRange(BitRange br) {
        if (direction) {
            return br.begin >= br.end && br.begin <= (offset + width - 1)
                    && br.end >= offset;
        } else {
            return br.begin <= br.end && br.begin >= offset
                    && br.end <= (offset + width - 1);
        }
    }

}
