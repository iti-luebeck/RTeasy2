package org.desert.core.highlighters;

import java.awt.Color;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.StyleConstants;
import org.desert.core.BreakPointManager;
import org.netbeans.api.editor.settings.AttributesUtilities;
import org.netbeans.spi.editor.highlighting.support.OffsetsBag;

public class BreakPointHighlighter {

    private final OffsetsBag bag;

    public BreakPointHighlighter(Document doc) {
        bag = new OffsetsBag(doc);
        BreakPointManager.getDefault().setBreakPointHighlighter(this);
    }

    public OffsetsBag getHighlightsBag() {
        return bag;
    }

    /**
     * This function clears old step mark and it sets the new one.
     * 
     * @param startOffset Start of new mark
     * @param endOffset End of new mark
     * @param color Color of new mark
     */
    public void setMark(int startOffset, int endOffset, Color color) {
        bag.clear();
        AttributeSet attrSet = AttributesUtilities.createImmutable(StyleConstants.Background, color);
        bag.addHighlight(startOffset, endOffset, attrSet);
    }
    
    /**
     * This removes all step marks.
     */
    public void removeAllMarks() {
        bag.clear();
    }

}
