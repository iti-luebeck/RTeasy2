/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.desert.codecompletion;

import java.util.Arrays;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.desert.helper.DocumentHelper;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;

@MimeRegistration(mimeType = "text/x-rt", service = CompletionProvider.class)
/**
 *
 * @author Christian
 */
public class RTCompletionProvider implements CompletionProvider {

    @Override
    public CompletionTask createTask(int queryType, JTextComponent component) {
        if (queryType != CompletionProvider.COMPLETION_QUERY_TYPE) {
            return null;
        }
        return new AsyncCompletionTask(new AsyncCompletionQuery() {

            @Override
            protected void query(CompletionResultSet resultSet, Document doc, int caretOffset) {
                //Iterate through the available locales
                //and assign each country display name
                //to a CompletionResultSet:
                String text = DocumentHelper.getWordBeforeCaret(doc, caretOffset);
                CompletionProposals[] words = CompletionProposals.values();
                Arrays.sort(words);
                for (CompletionProposals word : words) {
                    final String name = word.name().toLowerCase();
                    if (name.startsWith(text)) {
                        resultSet.addItem(new RTCompletionItem(name, caretOffset));
                    }
                }
                resultSet.finish();
            }
        }, component);
    }

    @Override
    public int getAutoQueryTypes(JTextComponent component, String typedText) {
        return 0;
    }

}
