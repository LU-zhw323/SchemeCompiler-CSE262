package edu.lehigh.cse262.slang.Scanner;

import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.synth.SynthEditorPaneUI;
import javax.swing.text.AbstractDocument.Content;
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
    boolean Error = false;
    boolean LPRE = false;
    boolean RPRE = false;
    boolean EOF = false;
    //tags for keywords
    boolean AND = false;
    boolean COND = false;
    boolean IF = false;
    boolean LAMBDA = false;
    boolean SET = false;
    boolean QUOTE = false;
    boolean BEGIN = false;
    boolean DEFINE = false;
    boolean OR = false;


    

    
    //Literals
    String tokenText = "";

    //Record the position in string
    int counter = 0;
    //Check if we are in one line
    boolean inLine = true;
    int line = 1;
    int col = 0;

    //Length of line for each line of input
    int line_l = 0;


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
        
        while(counter < source.length()){
            //Check if this token is in the new line, resest the lenght for this new line
            if(!inLine){
                line_l = 0;
                inLine = true;
            }
            //Get current token from the string
            String current ="";
            current += source.charAt(counter);

            //Handle left and right parenthesis first, if we have a ( or ), we shall
            //go to the cleanbreak and print what we have now as well as LP or RP
            if(current.matches("\\(")){
                LPRE = true;
                CLEANBREAK = true;
                START = false;
                
                //col = line_l;
            }
            else if(current.matches("\\)")){
                RPRE = true;
                CLEANBREAK = true;
                START = false;
                //col = line_l;
            }
            //Same as ' which we should handle first
            else if(current.matches("\\'")){
                ABBREV = true;
                CLEANBREAK = true;
                START = false;
            }
            
            if(counter == source.length()-1){
                EOF = true;
                CLEANBREAK = true;
                START = false;
            }
            

            //Check state transition in START state
            if(START){
                Start(current);
                START = false;
            }
            if(current.matches("[\\s]")){
                CLEANBREAK = true;
            }
            //Check if we have error right now, if so we dont need to figure out what it is
            if(Error){
                if(!CLEANBREAK){
                    tokenText += current;
                    line_l ++;
                    counter ++;
                    //if there is a newline character, and a new line
                    if(current.matches("\n")){
                        line += 1;
                        inLine = false;
                    }
                    continue;
                }
            }
            
            
            //Check if input will bring us to PM state
            if(PM){
                if(INDOUBLE){
                    ININT = false;
                }
                if(CLEANBREAK){
                    //The problem here is if input is 3. a number follow by '.', since there is no input at PREDOUBLE
                    //state, it will be a error. However, I haven't figure out a better way to solve it
                    if(tokenText.charAt(tokenText.length()-1) == '.'){
                        Error = true;
                    }
                }
                else{
                    From_PM(current);
                }
            }
            //Check if input will bring us straight to ININT state
            else if(ININT){
                if(CLEANBREAK){
                    //TO check which state we are in
                    if(INDOUBLE){
                        ININT = false;
                    }
                    if(tokenText.charAt(tokenText.length()-1) == '.'){
                        Error = true;
                    }
                }
                else{
                    FORM_INT_DOUBLE(current);
                }
            }
            //Check if input will bring us straight to INID state
            else if(INID){
                if(!CLEANBREAK){
                    //I didn't cover [.+-] for the reason that one of the fail test file suggests that a+ should fail
                    if(current.matches("[[!$%&*/:<=>?-_^ && [^\\[\\]]][a-z][A-Z][0-9]]")){
                        
                        tokenText += current;
                    }
                    else{
                        tokenText += current;
                        Error = true;
                    }
                }
                //When there is a \t\r\n, we should check if input is a special form
                else{
                    keyword = tokenText.substring(0, tokenText.length());
                    Check_key(keyword);
                }
            }
            

            
            
           
            //Once we have['',\t,\n,\r] or reach the last token in file, we should return current token
            if(CLEANBREAK | counter == source.length()-1){
                
                //increment 1 if we are at the last element of input
                if(counter == source.length()-1){
                    line_l ++;
                }

                col = line_l - tokenText.length();
                
                //Check if we have to handle error
                if(Error){
                    
                    tokens.add(Check_error());
                }
                if(ININT){
                    int int_l = Integer.parseInt(tokenText);
                    var INT = new Tokens.Int(tokenText, line, col, int_l);
                    tokens.add(INT);
                    ININT = false;
                    PM = false;
                    
                }
                else if(INID){
                    var ID = new Tokens.Identifier(tokenText, line, col);
                    tokens.add(ID);
                    INID = false;
                    PM = false;
                    
                }
                else if(INDOUBLE){
                    double dou_l = Double.parseDouble(tokenText);
                    var Dou = new Tokens.Dbl(tokenText, line, col, dou_l);
                    tokens.add(Dou);
                    INDOUBLE = false;
                    PM = false;
                }
                else if(AND){
                    var key_w = new Tokens.And(tokenText, line, col);
                    tokens.add(key_w);
                    AND = false;
                }
                else if(COND){
                    var key_w = new Tokens.Cond(tokenText, line, col);
                    tokens.add(key_w);
                    COND = false;
                }
                else if(IF){
                    var key_w = new Tokens.If(tokenText, line, col);
                    tokens.add(key_w);
                    IF = false;
                }
                else if(OR){
                    var key_w = new Tokens.Or(tokenText, line, col);
                    tokens.add(key_w);
                    OR = false;
                }
                else if(SET){
                    var key_w = new Tokens.Set(tokenText, line, col);
                    tokens.add(key_w);
                    SET = false;
                }
                else if(QUOTE){
                    var key_w = new Tokens.Quote(tokenText, line, col);
                    tokens.add(key_w);
                    QUOTE = false;
                }
                else if(LAMBDA){
                    var key_w = new Tokens.Lambda(tokenText, line, col);
                    tokens.add(key_w);
                    LAMBDA = false;
                }
                else if(BEGIN){
                    var key_w = new Tokens.Begin(tokenText, line, col);
                    tokens.add(key_w);
                    BEGIN = false;
                }
                else if(DEFINE){
                    var key_w = new Tokens.Define(tokenText, line, col);
                    tokens.add(key_w);
                    DEFINE = false;
                }

                if(LPRE){
                    if(counter == source.length()-1){
                        line_l -= 1;
                    }
                    col = line_l;
                    var LP = new Tokens.LeftParen("\\(", line, col);
                    tokens.add(LP);
                    LPRE = false;
                }
                else if(RPRE){
                    if(counter == source.length()-1){
                        line_l -= 1;
                    }
                    
                    col = line_l;
                    var RP = new Tokens.RightParen("\\)", line, col);
                    tokens.add(RP);
                    RPRE = false;
                }
                else if(ABBREV){
                    if(counter == source.length()-1){
                        line_l -= 1;
                    }
                    col = line_l;
                    var abb = new Tokens.Abbrev("\\'", line, col);
                    tokens.add(abb);
                    ABBREV = false;
                }

                if(EOF){
                    if(counter == source.length()-1){
                        line_l -= 1;
                    }
                    col = line_l;
                    var eof = new Tokens.Eof("\0", line, col);
                    tokens.add(eof);
                    EOF = false;
                }

                
                tokenText = "";
                CLEANBREAK = false;
                START = true;
            }
            line_l ++;
            
            counter ++;
            //if there is a newline character, and a new line
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
        else if(current.matches("[[!$%&*/:<=>?-_^]|[a-z]|[A-Z]]")){
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

    
    /**Helper function From_PM which will take a input and scan it 
     * @param current input token
     * 
     */
    public void From_PM(String current){
        //check error token
        if(current.matches("[+-]")){
            tokenText += current;
            INID = true;
        }
        else{
            INID = false;
            FORM_INT_DOUBLE(current);
        }
    }


    /**Helper function FORM_INT_DOUBLE which will take a input and scan it as either double or int
     * @param current input token
     * 
     */
    public void FORM_INT_DOUBLE(String current){
        if(CLEANBREAK==false){
            //Check if we are in PREDOUBLE state
            if(PREDOUBLE){
                //Transition to INDOUBLE only happens when we read a digit
                if(current.matches("[\\d]")){
                    tokenText += current;
                    INDOUBLE = true;
                    PREDOUBLE = false;
                }
                else{
                    tokenText += current;
                    Error = true;
                }
            }
            //Check if we are in INDOUBLE state
            else if(INDOUBLE){
                if(current.matches("[^\\d]")){
                    Error = true;
                }
                tokenText += current;
            }
            //Case we are in ININT state
            else{
                if(current.matches("[\\d]")){
                    tokenText += current;
                }
                //if we read a '.', make a transition to PREDOUBLE
                else if(current.matches("[.]")){
                    tokenText += current;
                    PREDOUBLE = true;
                    
                }
                else{
                    tokenText += current;
                    Error = true;
                }
            }
        }
    }

    /**Helper function Check_key() will check if we have special form in input
     * 
     * 
     * 
     * @param tokenText the tokenText that we should check
     */
    public void Check_key(String keyword){
        switch(keyword){
            case "and":
                AND = true;
                INID = false;
                tokenText = keyword;
                break;
            case "cond":
                COND = true;
                INID = false;
                tokenText = keyword;
                break;
            case "if":
                IF = true;
                INID = false;
                tokenText = keyword;
                break;
            case "lambda":
                LAMBDA = true;
                INID = false;
                tokenText = keyword;
                break;
            case "quote":
                QUOTE = true;
                INID = false;
                tokenText = keyword;
                break;
            case "set!":
                SET = true;
                INID = false;
                tokenText = keyword;
                break;
            case "begin":
                BEGIN = true;
                INID = false;
                tokenText = keyword;
                break;
            case "define":
                DEFINE = true;
                INID = false;
                tokenText = keyword;
                break;
            case "or":
                OR = true;
                INID = false;
                tokenText = keyword;
                break;
        }
    }
    
    
    
    /**Helper function Check_error() which will generate a error token if Error flag is on
     * 
     */
    public Tokens.Error Check_error(){
        var error = new Tokens.Error(tokenText, line, col);
        Error = false;
        ININT = false;
        INID = false;
        PM = false;
        INDOUBLE = false;
        AND = false;
        COND = false;
        IF = false;
        LAMBDA = false;
        SET = false;
        QUOTE = false;
        BEGIN = false;
        DEFINE = false;
        OR = false;
        return error;
    }
}