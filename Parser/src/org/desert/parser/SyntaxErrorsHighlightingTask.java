/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.desert.parser;

import de.uniluebeck.iti.rteasy.frontend.ParseException;
import de.uniluebeck.iti.rteasy.frontend.Token;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;
import org.desert.parser.RTParser.RTParserResult;
import org.netbeans.modules.parsing.spi.Parser.Result;
import org.netbeans.modules.parsing.spi.ParserResultTask;
import org.netbeans.modules.parsing.spi.Scheduler;
import org.netbeans.modules.parsing.spi.SchedulerEvent;
import org.netbeans.spi.editor.hints.ErrorDescription;
import org.netbeans.spi.editor.hints.ErrorDescriptionFactory;
import org.netbeans.spi.editor.hints.HintsController;
import org.netbeans.spi.editor.hints.Severity;
import org.openide.text.NbDocument;
import org.openide.util.Exceptions;

/**
 *
 * @author Christian
 */
class SyntaxErrorsHighlightingTask extends ParserResultTask {

    public SyntaxErrorsHighlightingTask() {
    }

    @Override
    public void run(Result result, SchedulerEvent event) {
        try {
            RTParserResult rtResult = (RTParserResult) result;
            List<ParseException> syntaxErrors = rtResult.getRTParser().syntaxErrors;
            Document doc = result.getSnapshot().getSource().getDocument(false);
            List<ErrorDescription> errors = new ArrayList<ErrorDescription>();
            for (ParseException syntaxError : syntaxErrors) {
                Token token = syntaxError.currentToken;
                int bL = token.next.beginLine;
                int eL = token.next.endLine;
                if(bL > 0) {
                    bL--;
                }
                if(eL > 0) {
                    eL--;
                }
                int start = NbDocument.findLineOffset((StyledDocument) doc, bL) + token.next.beginColumn;
                int end = NbDocument.findLineOffset((StyledDocument) doc, eL) + token.next.endColumn;
                ErrorDescription errorDescription = ErrorDescriptionFactory.createErrorDescription(
                        Severity.ERROR,
                        syntaxError.getMessage(),
                        doc,
                        doc.createPosition(start),
                        doc.createPosition(end)
                );
                errors.add(errorDescription);
            }
            // TODO prüfen
            HintsController.setErrors(doc, "RT", errors);
        } catch (BadLocationException ex1) {
            Exceptions.printStackTrace(ex1);
        } catch (org.netbeans.modules.parsing.spi.ParseException ex1) {
            Exceptions.printStackTrace(ex1);
        }
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public Class<? extends Scheduler> getSchedulerClass() {
        return Scheduler.EDITOR_SENSITIVE_TASK_SCHEDULER;
    }

    @Override
    public void cancel() {
    }
}
