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
				tokens.popToken();
				current = tokens.nextToken();
				var expres = Expres_node(current, symbol_list, tokens);
				if(expres != null){
					res.add(expres);
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
	 * @param current token that we uesed to determine the form
	 * @param tokens TokenStream
	 * 
	 * @return The node of expression
	 */
	public Nodes.BaseNode Expres_node(Tokens.BaseToken current, List<String> symbol_list,  TokenStream tokens) throws Exception{
		Nodes.BaseNode res = null;
		//Speical form quote
		if(current instanceof Tokens.Quote){
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
				res = new Nodes.Quote(datnum); 
			}
			else{
				throw new Exception("Quote error");
			}
		}
		//Speical form and, begin, or
		else if(current instanceof Tokens.And || current instanceof Tokens.Begin || current instanceof Tokens.Or){
			List<Nodes.BaseNode> nodes = new ArrayList<>();
			boolean and = false;
			boolean begin = false;
			boolean or = false;
			if(current instanceof Tokens.And){
				and = true;
			}
			else if(current instanceof Tokens.Begin){
				begin = true;
			}
			else{
				or = true;
			}
			while(tokens.hasNext()){
				if(tokens.hasNext()){
					tokens.popToken();
				}
				else{
					throw new Exception("Parsing error");
				}
				current = tokens.nextToken();
				if(current instanceof Tokens.LeftParen){
					tokens.popToken();
					Tokens.BaseToken special = tokens.nextToken();
					nodes.add(Expres_node(special, symbol_list, tokens));
					
				}
				else if(current instanceof Tokens.Identifier || current instanceof Tokens.Int || current instanceof Tokens.Dbl || current instanceof Tokens.Bool || current instanceof Tokens.Char || current instanceof Tokens.Str){
					nodes.add(Data_node(current, symbol_list, tokens));
				}
				else if(current instanceof Tokens.RightParen){
					if(and){
						res = new Nodes.And(nodes);
					}
					else if(begin){
						res = new Nodes.Begin(nodes);
					}
					else if(or){
						res = new Nodes.Or(nodes);
					}
					nodes = null;
					break;
				}
				else{
					throw new Exception("And error");
				}
				//tokens.popToken();
			}
			if(nodes != null){
				throw new Exception("And error");
			}
		}
		//Special form if
		else if(current instanceof Tokens.If){
			Nodes.BaseNode cond = null;
			Nodes.BaseNode ifTure = null;
			Nodes.BaseNode ifFalse = null;
			int expre_count = 1;
			while(tokens.hasNext()){
				Nodes.BaseNode node = null;
				if(tokens.hasNext()){
					tokens.popToken();
				}
				else{
					throw new Exception("Parsing error");
				}
				if(expre_count > 4){
					throw new Exception("If error");
				}
				current = tokens.nextToken();
				if(current instanceof Tokens.LeftParen){
					tokens.popToken();
					Tokens.BaseToken special = tokens.nextToken();
					node = Expres_node(special, symbol_list, tokens);
					
				}
				else if(current instanceof Tokens.Identifier || current instanceof Tokens.Int || current instanceof Tokens.Dbl || current instanceof Tokens.Bool || current instanceof Tokens.Char || current instanceof Tokens.Str){
					node = Data_node(current, symbol_list, tokens);
				}
				else if(current instanceof Tokens.RightParen){
					if(expre_count < 4){
						throw new Exception("If error");
					}
					res = new Nodes.If(cond,ifTure, ifFalse);
					break;
				}
				else{
					throw new Exception("If error");
				}
				switch(expre_count){
					case 1: 
						cond = node;
						break;
					case 2:
						ifTure = node;
						break;
					case 3:
						ifFalse = node;
						break;
				}
				expre_count += 1;
			}
		}
		//Special form set!
		else if(current instanceof Tokens.Set){
			Nodes.Identifier id = null;
			Nodes.BaseNode expr = null;
			if(tokens.hasNext()){
				tokens.popToken();
			}
			else{
				throw new Exception("Parsing error");
			}
			var next = tokens.nextToken();
			if(next instanceof Tokens.Identifier){
				id = (Nodes.Identifier)Data_node(next,symbol_list, tokens);
			}
			else{
				throw new Exception("Set error");
			}
			if(tokens.hasNext()){
				tokens.popToken();
			}
			else{
				throw new Exception("Set error");
			}
			next = tokens.nextToken();
			if(next instanceof Tokens.LeftParen){
				tokens.popToken();
				Tokens.BaseToken special = tokens.nextToken();
				expr = Expres_node(special, symbol_list, tokens);
			}
			else if(next instanceof Tokens.Identifier || next instanceof Tokens.Int || next instanceof Tokens.Dbl || next instanceof Tokens.Bool || next instanceof Tokens.Char || next instanceof Tokens.Str){
				expr = Data_node(next, symbol_list, tokens);
			}
			else{
				throw new Exception("Set error");
			}
			if(tokens.hasNext()){
				tokens.popToken();
			}
			else{
				throw new Exception("Set error");
			}
			next = tokens.nextToken();
			if(next instanceof Tokens.RightParen){
				res = new Nodes.Set(id, expr);
			}
			else{
				throw new Exception("Set error");
			}
		}
		//Basic application(defult form)
		else{
			List<Nodes.BaseNode> nodes = new ArrayList<>();
			while(tokens.hasNext()){
				current = tokens.nextToken();
				if(current instanceof Tokens.LeftParen){
					tokens.popToken();
					Tokens.BaseToken special = tokens.nextToken();
					nodes.add(Expres_node(special, symbol_list, tokens));
				}
				else if(current instanceof Tokens.Identifier || current instanceof Tokens.Int || current instanceof Tokens.Dbl || current instanceof Tokens.Bool || current instanceof Tokens.Char || current instanceof Tokens.Str){
					nodes.add(Data_node(current, symbol_list, tokens));
				}
				else if(current instanceof Tokens.RightParen){
					if(nodes == null){
						throw new Exception("Application error");
					}
					res = new Nodes.Apply(nodes);
					nodes = null;
					break;
				}
				else{
					throw new Exception("Application error");
				}
				tokens.popToken();
			}
			if(nodes != null){
				throw new Exception("Application error");
			}
			
		}
		return res;
	}
}