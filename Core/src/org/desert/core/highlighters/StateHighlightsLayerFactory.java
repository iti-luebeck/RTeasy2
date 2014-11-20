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
public class StateHighlightsLayerFactory implements HighlightsLayerFactory {

    public static StateHighlighter getStateHighlighter(Document doc) {
        StateHighlighter highlighter
                = (StateHighlighter) doc.getProperty(StateHighlighter.class);
        if (highlighter == null) {
            doc.putProperty(StateHighlighter.class,
                    highlighter = new StateHighlighter(doc));
        }
        return highlighter;
    }

    @Override
    public HighlightsLayer[] createLayers(Context context) {
        return new HighlightsLayer[]{
            HighlightsLayer.create(StateHighlighter.class.getName(),
            ZOrder.CARET_RACK.forPosition(2000),
            true,
            getStateHighlighter(context.getDocument()).getHighlightsBag())
        };
    }

}
