/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.desert.parser;

import de.uniluebeck.iti.rteasy.frontend.RTSim_Parser;
import java.io.*;
import javax.swing.event.ChangeListener;
import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.api.Task;
import org.netbeans.modules.parsing.spi.Parser;
import org.netbeans.modules.parsing.spi.SourceModificationEvent;
//import org.desert.parser.jcc.RTSim_Parser;

/**
 *
 * @author Christian
 */
public class RTParser extends Parser {

    private Snapshot snapshot;
    private RTSim_Parser rtSIMParser;

    @Override
    public void parse (Snapshot snapshot, Task task, SourceModificationEvent event) {
        this.snapshot = snapshot;
        Reader reader = new StringReader (snapshot.getText ().toString ());
        rtSIMParser = new RTSim_Parser (reader);
        rtSIMParser.parseRTProgram();
        /*try {
            rtSIMParser.rt_program();
        } catch (org.rteasy.jccparser.ParseException ex) {
            Logger.getLogger (RTSim_Parser.class.getName()).log (Level.WARNING, null, ex);
        }*/
    }

    @Override
    public Result getResult (Task task) {
        return new RTParserResult (snapshot, rtSIMParser);
    }

    @Override
    public void cancel () {
    }

    @Override
    public void addChangeListener (ChangeListener changeListener) {
    }

    @Override
    public void removeChangeListener (ChangeListener changeListener) {
    }

    
    public static class RTParserResult extends Result {

        private final RTSim_Parser rtSimParser;
        private boolean valid = true;

        RTParserResult (Snapshot snapshot, RTSim_Parser rtSimParser) {
            super (snapshot);
            this.rtSimParser = rtSimParser;
        }

        public RTSim_Parser getRTParser () throws org.netbeans.modules.parsing.spi.ParseException {
            if (!valid) throw new org.netbeans.modules.parsing.spi.ParseException ();
            return rtSimParser;
        }

        @Override
        protected void invalidate () {
            valid = false;
        }
    }
}