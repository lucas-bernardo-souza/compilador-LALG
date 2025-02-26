/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Bernardo
 */
public class AnalisadorLexico {
       
    private static final String KEYWORDS = "\\b(if|else|while|for|return|int|float|char)\\b";
    private static final String IDENTIFIER = "[a-zA-Z_][a-zA-Z0-9_]*";
    private static final String NUMBER = "\\d+(\\.\\d+)?";
    private static final String OPERATOR = "[+\\-*/=<>!]";
    private static final String DELIMITER = "[(){};]";
    
    private static final Pattern PATTERN = Pattern.compile(
        KEYWORDS + "|" + IDENTIFIER + "|" + NUMBER + "|" + OPERATOR + "|" + DELIMITER
    );

    public static List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        Matcher matcher = PATTERN.matcher(input);

        while (matcher.find()) {
            String token = matcher.group();
            if (token.matches(KEYWORDS)) {
                tokens.add(new Token("KEYWORD", token));
            } else if (token.matches(IDENTIFIER)) {
                tokens.add(new Token("IDENTIFIER", token));
            } else if (token.matches(NUMBER)) {
                tokens.add(new Token("NUMBER", token));
            } else if (token.matches(OPERATOR)) {
                tokens.add(new Token("OPERATOR", token));
            } else if (token.matches(DELIMITER)) {
                tokens.add(new Token("DELIMITER", token));
            }
        }
        tokens.forEach(System.out::println);
        return tokens;
    }
}
