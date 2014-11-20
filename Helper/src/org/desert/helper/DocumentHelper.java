/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.desert.helper;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.openide.util.Exceptions;

/**
 *
 * @author Christian
 */
public class DocumentHelper {

    public static String getWordBeforeCaret(Document doc, int caretOffset) {
        String word = "";
        if (caretOffset > 0) {
            int i = 0;
            try {
                do {
                    i++;
                    word = doc.getText(caretOffset - i, i);
                } while (Character.isLetter(word.charAt(0)) && i < doc.getLength());
                if (i < doc.getLength()) {
                    word = word.substring(1);
                }
            } catch (BadLocationException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return word;
    }

}
