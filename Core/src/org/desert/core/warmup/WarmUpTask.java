/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.desert.core.warmup;

import org.desert.core.AllClosedCookie;
import org.desert.helper.CentralLookup;
import org.netbeans.api.editor.EditorRegistry;

/**
 *
 * @author Christian
 */
public class WarmUpTask implements Runnable {

    @Override
    public void run() {
        // if no editor component is open add cookie to global lookup to allow open of new files.
        if (EditorRegistry.componentList().isEmpty()) {
            CentralLookup.getDefault().add(new AllClosedCookie());
        }
    }

}
