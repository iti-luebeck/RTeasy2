package org.desert.core.highlighters;

import javax.swing.text.Document;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.highlighting.HighlightsLayer;
import org.netbeans.spi.editor.highlighting.HighlightsLayerFactory;
import org.netbeans.spi.editor.highlighting.ZOrder;

@MimeRegistration(mimeType = "text/x-rt", service = HighlightsLayerFactory.class)

/**
 *
 * @author Christian
 */
public class BreakPointHighlightsLayerFactory implements HighlightsLayerFactory {

    public static BreakPointHighlighter getBreakPointHighlighter(Document doc) {
        BreakPointHighlighter highlighter
                = (BreakPointHighlighter) doc.getProperty(BreakPointHighlighter.class);
        if (highlighter == null) {
            doc.putProperty(BreakPointHighlighter.class,
                    highlighter = new BreakPointHighlighter(doc));
        }
        return highlighter;
    }

    @Override
    public HighlightsLayer[] createLayers(Context context) {
        return new HighlightsLayer[]{
            HighlightsLayer.create(BreakPointHighlighter.class.getName(),
            ZOrder.CARET_RACK.forPosition(2000),
            true,
            getBreakPointHighlighter(context.getDocument()).getHighlightsBag())
        };
    }

}
