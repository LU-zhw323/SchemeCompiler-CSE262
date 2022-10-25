package edu.lehigh.cse262.slang.Parser;

import java.util.List;
import java.util.ArrayList;

import edu.lehigh.cse262.slang.Scanner.TokenStream;
import edu.lehigh.cse262.slang.Scanner.Tokens;

/**
 * Parser is the second step in our interpreter. It is responsible for turning a
 * sequence of tokens into an abstract syntax tree.
 */
public class Parser {
    private final Nodes.Bool _true;
    private final Nodes.Bool _false;
    private final Nodes.Cons _empty;

    public Parser(Nodes.Bool _true, Nodes.Bool _false, Nodes.Cons _empty) {
        this._true = _true;
        this._false = _false;
        this._empty = _empty;
    }

    /**
     * Transform a stream of tokens into an AST
     *
     * @param tokens a stream of tokens
     *
     * @return A list of AstNodes, because a Scheme program may have multiple
     *         top-level expressions.
     */
    public List<Nodes.BaseNode> parse(TokenStream tokens) throws Exception {
        //Return result
        List<Nodes.BaseNode> res = new ArrayList<>();
        //Value list for vector
        List<IValue> datnum = new ArrayList<>();
        //Symbol list
        List<String> symbol_list = new ArrayList<>();
        int ini_line = 0;
        int out_line = ini_line;
        boolean VEC = false;
        while(tokens.hasNext()){
            Tokens.BaseToken current = tokens.nextToken();
            ini_line = current.line;
            //Where this is a new line
            if(ini_line != out_line){
                if(VEC){
                    VEC = false;
                    throw new Exception("Vector error");
                }
            }
            //Int <datum>
            if(current instanceof Tokens.Int){
                int literal = ((Tokens.Int)current).literal;
                var node = new Nodes.Int(literal);
                if(VEC){
                    IValue temp = node;
                    datnum.add(temp);
                }
                else{
                    res.add(node);
                }
            }
            //Bool <datum>
            else if(current instanceof Tokens.Bool){
                boolean literal = ((Tokens.Bool)current).literal;
                var node = new Nodes.Bool(literal);
                if(VEC){
                    IValue temp = node;
                    datnum.add(temp);
                }
                res.add(node);
            }
            //character <datum>
            else if(current instanceof Tokens.Char){
                char literal = ((Tokens.Char) current).literal;
                var node = new Nodes.Char(literal);
                if(VEC){
                    IValue temp = node;
                    datnum.add(temp);
                    
                }
                else{
                    res.add(node);
                }
            }
            //String <datum>
            else if(current instanceof Tokens.Str){
                String literal = ((Tokens.Str) current).literal;
                var node = new Nodes.Str(literal);
                if(VEC){
                    IValue temp = node;
                    datnum.add(temp);
                }
                else{
                    res.add(node);
                }
            }
            //Double <datum>
            else if(current instanceof Tokens.Dbl){
                double literal = ((Tokens.Dbl) current).literal;
                var node = new Nodes.Dbl(literal);
                if(VEC){
                    IValue temp = node;
                    datnum.add(temp);
                }
                else{
                    res.add(node);
                }
            }
            //Indentifier & Symbol
            else if(current instanceof Tokens.Identifier){
                String name = ((Tokens.Identifier)current).tokenText;
                //Which indicate that this identifier has been defined as symbol
                if(symbol_list.contains(name)){
                    var node = new Nodes.Symbol(name);
                    //Symbol is a datnum which can be include in vector
                    if(VEC){
                        IValue temp = node;
                        datnum.add(temp);
                    }
                    else{
                        res.add(node);
                    }
                }
                else{
                    var node = new Nodes.Identifier(name);
                    //Identifier can't be included in a vector
                    if(VEC){
                        throw new Exception("Vector error");
                    }
                    else{
                        res.add(node);
                    }
                }
                
            }
            //Vector <datnum>
            else if(current instanceof Tokens.Vec){
                VEC = true;
            }
            else if(current instanceof Tokens.RightParen){
                if(VEC){
                    var node = new Nodes.Vec(datnum);
                    res.add(node);
                    VEC = false;
                }
            }



            out_line = ini_line;
            tokens.popToken();
        }
        //Check error
        if(VEC){
            VEC = false;
            throw new Exception("Vector error");
        }
        return res;

    }
}