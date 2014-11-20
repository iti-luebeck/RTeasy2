/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.desert.lexer;

import org.netbeans.api.lexer.Language;
import org.netbeans.api.lexer.TokenId;

/**
 *
 * @author Christian
 */
public class RTTokenId implements TokenId {
    private static final Language<RTTokenId> language = new RTLanguageHierarchy ().language ();

    public static final Language<RTTokenId> getLanguage() {
        return language;
    }

    private final String name;
    private final String primaryCategory;
    private final int id;


    RTTokenId (String name, String primaryCategory, int id) {
        this.name = name;
        this.primaryCategory = primaryCategory;
        this.id = id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int ordinal() {
        return id;
    }

    @Override
    public String primaryCategory() {
        return primaryCategory;
    }


}
