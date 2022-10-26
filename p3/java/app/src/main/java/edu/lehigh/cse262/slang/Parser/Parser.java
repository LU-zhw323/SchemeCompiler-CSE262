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
        //Symbol list
        List<String> symbol_list = new ArrayList<>();
        boolean VEC = false;
        boolean express = false;
        boolean quote = false;
		int pop_count = 0;
        while(tokens.hasNext()){
            Tokens.BaseToken current = tokens.nextToken();
            //Where this is a new line
            if(ini_line != out_line){
                if(VEC){
                    VEC = false;
                    throw new Exception("Vector error");
                }
            }
            //Basic datnum
            var dat = Data_node(current, symbol_list);
            if(dat != null){
                res.add(dat);
            }
            
            //Vector <datnum>
            if(current instanceof Tokens.Vec){
				tokens.popToken();
				int ini_line = 0;
        		int out_line = ini_line;
				//Value list for vector
				List<IValue> datnum = new ArrayList<>();
				while(tokens.hasNext()){
					Tokens.BaseToken next = tokens.nextToken();
					ini_line = current.line;
					if(ini_line != out_line){
						throw new Exception("Vector error");
					}
					if(next instanceof Tokens.LeftParen){
						throw new Exception("Vector error");
					}
					else if(next instanceof Tokens.RightParen){
						var node = new Nodes.Vec(datnum);
						res.add(node);
						datnum = null;
						break;
					}
					var temp = Data_node(next,symbol_list);
					if(temp instanceof Nodes.Identifier){
						throw new Exception("vector error");
					}
					IValue x = (IValue)temp;
					datnum.add(x);
					tokens.popToken();
					out_line = ini_line;	
				}
				if(datnum != null){
					throw new Exception("Vector error");
				}
            }
            else if(current instanceof Tokens.LeftParen){
                Tokens.BaseToken next = null;
                if(tokens.hasNextNext()){
                    next = tokens.nextNextToken();
                }
                else{
                    throw new Exception("Parsing error");
                }
                if(next instanceof Tokens.Quote){
                    quote = true;
                }
            }
            



            tokens.popToken();
        }
        
        return res;

    }

    /**
     * Helper function to determine the datnum 
     * @param token token to passin
     * 
     * @return The corresponding node for that token
     * 
     */
    public Nodes.BaseNode Data_node(Tokens.BaseToken current, List<String> symbol_list){
        Nodes.BaseNode res = null;
        //Int <datum>
        if(current instanceof Tokens.Int){
            int literal = ((Tokens.Int)current).literal;
            res = new Nodes.Int(literal);
        }
        //Bool <datum>
        else if(current instanceof Tokens.Bool){
            boolean literal = ((Tokens.Bool)current).literal;
            res = new Nodes.Bool(literal);
        }
        //character <datum>
        else if(current instanceof Tokens.Char){
            char literal = ((Tokens.Char) current).literal;
            res = new Nodes.Char(literal);
        }
        //String <datum>
        else if(current instanceof Tokens.Str){
            String literal = ((Tokens.Str) current).literal;
            res = new Nodes.Str(literal);
        }
        //Double <datum>
        else if(current instanceof Tokens.Dbl){
            double literal = ((Tokens.Dbl) current).literal;
            res = new Nodes.Dbl(literal);
        }
        //Indentifier & Symbol
        else if(current instanceof Tokens.Identifier){
            String name = ((Tokens.Identifier)current).tokenText;
            //Which indicate that this identifier has been defined as symbol
            if(symbol_list.contains(name)){
                res = new Nodes.Symbol(name);
            }
            else{
                res = new Nodes.Identifier(name);
            }
            
        }
        return res;

    }
}