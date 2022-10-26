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
            
            //Basic datnum
            var dat = Data_node(current, symbol_list, tokens);
            if(dat != null){
                res.add(dat);
            }
            
            if(current instanceof Tokens.LeftParen){
                if(tokens.hasNext()){
                    tokens.popToken();
                }
                else{
                    throw new Exception("Parsing error");
                }
				Tokens.BaseToken special = tokens.nextToken();
				//Speical form quote
				if(special instanceof Tokens.Quote){
					IValue datnum = null;
					if(tokens.hasNext()){
						tokens.popToken();
					}
					else{
						throw new Exception("Parsing error");
					}
					Tokens.BaseToken next = tokens.nextToken();
					if(next instanceof Tokens.LeftParen || next instanceof Tokens.RightParen){
						throw new Exception("Quote error");
					}
					var node = Data_node(next, symbol_list, tokens);
					if(tokens.hasNext()){
						tokens.popToken();
					}
					else{
						throw new Exception("Quote error");
					}
					Tokens.BaseToken end = tokens.nextToken();
					if(end instanceof Tokens.RightParen){
						datnum = (IValue) node;
						res.add(new Nodes.Quote(datnum)); 
					}
					else{
						throw new Exception("Quote error");
					}
				}
				//Basic application(defult form)
				else{

				}
			}
            



            tokens.popToken();
        }
        
        return res;

    }

    /**
     * Helper function to determine the datnum 
     * @param current token to passin
     * @param symbol_list list of name of symbol
	 * @param tokens TokenStream that we are workding on
	 * 
	 * 
	 * 
     * @return The corresponding node for that token
     * 
     */
    public Nodes.BaseNode Data_node(Tokens.BaseToken current, List<String> symbol_list, TokenStream tokens) throws Exception{
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
		//Vector <datnum>
		else if(current instanceof Tokens.Vec){
			tokens.popToken();
			//Value list for vector
			List<IValue> datnum = new ArrayList<>();
			while(tokens.hasNext()){
				Tokens.BaseToken next = tokens.nextToken();
				if(next instanceof Tokens.LeftParen){
					throw new Exception("Vector error");
				}
				else if(next instanceof Tokens.RightParen){
					res = new Nodes.Vec(datnum);
					datnum = null;
					break;
				}
				var temp = Data_node(next,symbol_list, tokens);
				if(temp instanceof Nodes.Identifier){
					throw new Exception("vector error");
				}
				IValue x = (IValue)temp;
				datnum.add(x);
				tokens.popToken();	
			}
			if(datnum != null){
				throw new Exception("Vector error");
			}
		}
        return res;
    }

	/**
	 * Helper function to build expression
	 * @param 
	 */
}