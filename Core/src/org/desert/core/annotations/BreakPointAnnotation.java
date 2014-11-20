/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.desert.core.annotations;

import org.openide.text.Annotation;

/**
 *
 * @author Christian
 */
public class BreakPointAnnotation extends Annotation {

    @Override
    public String getAnnotationType() {
        
        return "org-desert-breakpointannotation";
    }

    @Override
    public String getShortDescription() {
        return "Breakpoint";
    }

}
