/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.desert.lexer;

import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;
import org.desert.lexer.jcc.RTSim_ParserTokenManager;
import org.desert.lexer.jcc.SimpleCharStream;
import org.desert.lexer.jcc.Token;


/**
 *
 * @author Christian
 */
public class RTLexer implements Lexer<RTTokenId> {

    private final LexerRestartInfo<RTTokenId> info;
    private final RTSim_ParserTokenManager rtsim_ParserTokenManager;

    RTLexer(LexerRestartInfo<RTTokenId> info) {
        this.info = info;
        SimpleCharStream stream = new SimpleCharStream(info.input());
        rtsim_ParserTokenManager = new RTSim_ParserTokenManager(stream);
    }

    @Override
    public org.netbeans.api.lexer.Token<RTTokenId> nextToken() {
        Token token = rtsim_ParserTokenManager.getNextToken();
        if (info.input().readLength() < 1) {
            return null;
        }
        return info.tokenFactory().createToken(RTLanguageHierarchy.getToken(token.kind));
    }

    @Override
    public Object state() {
        return null;
    }

    @Override
    public void release() {
    }

}
