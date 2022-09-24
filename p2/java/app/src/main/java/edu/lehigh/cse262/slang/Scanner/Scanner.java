package edu.lehigh.cse262.slang.Scanner;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Source;

import edu.lehigh.cse262.slang.Scanner.Tokens.Char;

/**
 * Scanner is responsible for taking a string that is the source code of a
 * program, and transforming it into a stream of tokens.
 *
 * [CSE 262] It is tempting to think "if my code doesn't crash when I give it
 * good input, then I have done a good job". However, a good scanner needs to be
 * able to handle incorrect programs. The bare minimum is that the scanner
 * should not crash if the input is invalid. Even better is if the scanner can
 * print a useful diagnostic message about the point in the source code that was
 * incorrect. Best, of course, is if the scanner can somehow "recover" and keep
 * on scanning, so that it can report additional syntax errors.
 *
 * [CSE 262] **In this class, "even better" is good enough for full credit**
 *
 * [CSE 262] With that said, if you make a scanner that can report multiple
 * errors, I'll give you extra credit. But you'll have to let me know that
 * you're doing this... I won't check for it on my own.
 *
 * [CSE 262] This is the only java file that you need to edit in p1. The
 * reference solution has ~240 lines of code (plus 43 blank lines and ~210 lines
 * of comments). Your code may be longer or shorter... the line-of-code count is
 * just a reference.
 *
 * [CSE 262] You are allowed to add private methods and fields to this class.
 * You may also add imports.
 */
public class Scanner {
    /** Construct a scanner */
    public Scanner() {
    }

    //Below are tags that I used to identify each state, according to specification folder
    boolean PM = false;
    boolean ININT = false;
    boolean INT_S = false;
    boolean PREDOUBLE = false;
    boolean INDOUBLE = false;
    boolean INID = false;
    boolean INCOMMENT = false;
    boolean INSTR = false;
    boolean INSTR_p = false;
    boolean VEC = false;
    boolean PRECHAR = false;
    String keyword = "";
    boolean ABBREV = false;
    boolean START = true;
    boolean CLEANBREAK = false;
    boolean IDENTIFIER_S = false;
    boolean Error = false;
    

    



    /**
     * scanTokens works through the `source` and transforms it into a list of
     * tokens. It adds an EOF token at the end, unless there is an error.
     *
     * @param source The source code of the program, as one big string, or a
     *               line of code from the REPL.
     *
     * @return A list of tokens
     */
    public TokenStream scanTokens(String source) {
        // [CSE 262] Right now, this function is not implemented, so we are returning an
        // error token.
        var tokens = new ArrayList<Tokens.BaseToken>();

        //Check if the input is empty
        if(source.isBlank()){
            var error = new Tokens.Error("Empty file", 0, 0);
            tokens.add(error);
            return new TokenStream(tokens);
        }
        //Literals
        String tokenText = "";


        //Record the position in string
        int counter = 0;
        //Check if we are in one line
        boolean inLine = true;
        int line = 1;
        int col = 0;

        int begin;
        int end;
        while(counter < source.length()){
            //Check if this token is in the new line
            if(!inLine){
                col = 0;
                inLine = true;
            }
            //Get current token from the string
            String current ="";
            current += source.charAt(counter);
            

            //Check state transition in START state
            if(START){
                Start(current);
            }



            //Check if input will bring us to PM state
            if(PM){
                if(current.matches("[\\s]")){
                    PM = false;
                    if(ININT){
                        ININT = false;
                        INT_S = true;
                    }
                    else if(INID){
                        IDENTIFIER_S = true;
                        INID = false;
                    }
                }
                else{
                    if(current.matches("[^\\d[+-]]")){
                        tokenText += current;
                        Error = true;
                        ININT = false;
                        IDENTIFIER_S = false;
                    }
                    else if(current.matches("[\\d]")){
                        tokenText += current;
                        ININT = true;
                        IDENTIFIER_S = false;
                    }
                    else if(current.matches("[+-]")){
                        tokenText += current;
                        IDENTIFIER_S = true;
                        col = counter;
                    }
                }
            }
            //Once we have['',\t,\n,\r] or reach the last token in file, we should return current token
            if(CLEANBREAK | counter == source.length()-1){
                if(ININT){
                    int int_l = Integer.parseInt(tokenText);
                    var INT = new Tokens.Int(tokenText, line, col, int_l);
                    tokens.add(INT);
                    ININT = false;
                    PM = false;
                }
                else if(IDENTIFIER_S){
                    var ID = new Tokens.Identifier(tokenText, line, col);
                    tokens.add(ID);
                    IDENTIFIER_S = false;
                    PM = false;
                }
                else if(Error){
                    var error = new Tokens.Error(tokenText, line, col);
                    tokens.add(error);
                    Error = false;
                    PM = false;
                }
                tokenText = "";
                CLEANBREAK = false;
            }

            counter ++;
            if(current.matches("\n")){
                line += 1;
                inLine = false;
            }
            
            
        }
        return new TokenStream(tokens);
    }

    /**Helper function Start() descriping the state transition in Start state
    *@param current Input character
    *
    *
    **/
    public void Start(String current){
        if(current.matches("[+-]")){
            PM = true;
        }
        else if(current.matches("[\\d]")){
            ININT = true;
        }
        else if(current.matches("([\\p{P}]|[a-z]|[A-Z])")){
            INID = true;
        }
        else if(current.matches("#")){
            VEC = true;
        }
        else if(current.matches("\"")){
            INSTR = true;
        }
        else if(current.matches("\'")){
            ABBREV = true;
        }
        else if(current.matches("[\\s]")){
            CLEANBREAK = true;
        }
    }

    
    /* 
    public Tokens.Int PM_INT(String current){
        liter += current;
        
    }
    */
}