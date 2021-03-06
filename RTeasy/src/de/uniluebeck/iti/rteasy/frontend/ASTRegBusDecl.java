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


package de.uniluebeck.iti.rteasy.frontend;
import de.uniluebeck.iti.rteasy.frontend.ParseException;
import de.uniluebeck.iti.rteasy.kernel.BitRange;
import de.uniluebeck.iti.rteasy.kernel.Bus;
import de.uniluebeck.iti.rteasy.kernel.Register;

public class ASTRegBusDecl extends RTSimNode {
  private int width = 1;
  private int offset = 0;
  private String name;
  private Register register = null;
  private Bus bus = null;
  private boolean direction = true;
 
  public ASTRegBusDecl(int id) {super(id);}

  public void setName(String s) { name = s; }
  public String getName() { return name; }
  public void setBitRange(BitRange br) throws ParseException {
    if(br.begin < 0 || br.end < 0)
      throw new ParseException("Falscher Bit-Bereich bei Deklaration, beide Grenzen m�ssen positive Ganzzahlen sein.");
    else if(br.begin < br.end) {
      width = br.end-br.begin+1;
      offset = br.begin;
      direction = false;
    }
    else {
      width = br.begin-br.end+1;
      offset = br.end;
      direction = true;
    } 
  }

  public void setWidth(int w) { this.width = w;}
  public boolean getDirection() { return direction; }
  public int getWidth() { return width; }
  public int getOffset() { return offset; }
  public void setRegister(Register r) { register = r; }
  public Register getRegister() { return register; }
  public void setBus(Bus b) { bus = b; }
  public Bus getBus() { return bus; }

  public String toString() {
    if(direction) 
      return name+" ("+Integer.toString(offset+width-1)+":"
        +Integer.toString(offset)+")";
    else
      return name+" ("+Integer.toString(offset)+":"
        +Integer.toString(offset+width-1)+")"; 
  }
} 
