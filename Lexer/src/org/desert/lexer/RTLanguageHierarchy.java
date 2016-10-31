/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.desert.lexer;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.netbeans.spi.lexer.LanguageHierarchy;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;
import static org.desert.lexer.jcc.RTSim_ParserConstants.*;

/**
 *
 * @author Christian
 */
public class RTLanguageHierarchy extends LanguageHierarchy<RTTokenId> {

    private static List<RTTokenId> tokens;
    private static Map<Integer, RTTokenId> idToToken;

    private static void init() {
        tokens = Arrays.<RTTokenId>asList(new RTTokenId[]{
            new RTTokenId("EOF", "whitespace", EOF),
            new RTTokenId("SINGLE_LINE_COMMENT", "comment", SINGLE_LINE_COMMENT),
            new RTTokenId("BIN_NUM", "black", BIN_NUM),
            new RTTokenId("HEX_NUM", "black", HEX_NUM),
            new RTTokenId("DEC_NUM", "black", DEC_NUM),
            new RTTokenId("LPAREN", "black", LPAREN),
            new RTTokenId("RPAREN", "black", RPAREN),
            new RTTokenId("LBRACKET", "black", LBRACKET),
            new RTTokenId("RBRACKET", "black", RBRACKET),
            new RTTokenId("LBRACE", "black", LBRACE),
            new RTTokenId("RBRACE", "black", RBRACE),
            new RTTokenId("COMMA", "black", COMMA),
            new RTTokenId("SEMICOLON", "black", SEMICOLON),
            new RTTokenId("COLON", "black", COLON),
            new RTTokenId("DOT", "black", DOT),
            new RTTokenId("PIPE", "black", PIPE),
            new RTTokenId("ASSIGN", "black", ASSIGN),
            new RTTokenId("LE", "black", LE),
            new RTTokenId("GE", "black", GE),
            new RTTokenId("NE", "black", NE),
            new RTTokenId("PLUS", "black", PLUS),
            new RTTokenId("MINUS", "black", MINUS),
            new RTTokenId("LT", "black", LT),
            new RTTokenId("GT", "black", GT),
            new RTTokenId("EQ", "black", EQ),
            new RTTokenId("AND", "black", AND),
            new RTTokenId("OR", "black", OR),
            new RTTokenId("NOR", "black", NOR),
            new RTTokenId("XOR", "black", XOR),
            new RTTokenId("NAND", "black", NAND),
            new RTTokenId("IF", "conditional", IF),
            new RTTokenId("THEN", "conditional", THEN),
            new RTTokenId("ELSE", "conditional", ELSE),
            new RTTokenId("FI", "conditional", FI),
            new RTTokenId("SWITCH", "conditional", SWITCH),
            new RTTokenId("CASE", "conditional", CASE),
            new RTTokenId("CASEDEFAULT", "conditional", CASEDEFAULT),
            new RTTokenId("DECLARE", "keyword", DECLARE),
            new RTTokenId("REGISTER", "memory", REGISTER),
            new RTTokenId("BUS", "memory", BUS),
            new RTTokenId("MEMORY", "memory", MEMORY),
            new RTTokenId("STORAGE","storage", STORAGE),
            new RTTokenId("ARRAY", "memory", ARRAY),
            new RTTokenId("READ", "black", READ),
            new RTTokenId("WRITE", "black", WRITE),
            new RTTokenId("GOTO", "black", GOTO),
            new RTTokenId("GO", "black", GO),
            new RTTokenId("TO", "black", TO),
            new RTTokenId("NOT", "black", NOT),
            new RTTokenId("NOP", "black", NOP),
            new RTTokenId("ID", "identifier", ID),
            new RTTokenId("LETTER", "marker", LETTER),
            new RTTokenId("PART_LETTER", "identifier", PART_LETTER),
            new RTTokenId("MARKER", "identifier", MARKER),
            new RTTokenId("IDENTIFIER", "identifier", IDENTIFIER)}
        );
        idToToken = new HashMap<>();
        for (RTTokenId token : tokens) {
            idToToken.put(token.ordinal(), token);
        }
    }

    static synchronized RTTokenId getToken(int id) {
        if (idToToken == null) {
            init();
        }
        return idToToken.get(id);
    }

    @Override
    protected synchronized Collection<RTTokenId> createTokenIds() {
        if (tokens == null) {
            init();
        }
        return tokens;
    }

    @Override
    protected synchronized Lexer<RTTokenId> createLexer(LexerRestartInfo<RTTokenId> info) {
        return new RTLexer(info);
    }

    @Override
    protected String mimeType() {
        return "text/x-rt";
    }

}
