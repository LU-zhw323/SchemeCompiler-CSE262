import slang_scanner

# [CSE 262] You will probably find it tedious to create a whole class hierarchy
# for your Python parser.  Instead, consider whether each node type could just
# be a hash table.  In that case, you could have a function for "constructing"
# each "type", by putting some values into a hash table.


# A poor-man's enum: each of our token types is just a number
AND, APPLY, BEGIN, BOOL, CHAR, COND, CONS, DBL, DEFINE, IDENTIFIER, IF, INT, LAMBDA, OR, QUOTE, SET, STR, SYMBOL, TICK, VEC = range(0, 20)
class Parser:
    """The parser class is responsible for parsing a stream of tokens to produce
    an AST"""

    def __init__(self, true, false, empty):
        """Construct a parser by caching the environmental constants true,
        false, and empty"""
        self.true = true
        self.false = false
        self.empty = empty


    def parse(self, tokens):
        """parse() is the main routine of the parser.  It parses the token
        stream into an AST."""
        symbol_list = []
        res = []
        index = 0
        while(tokens.hasNext()):
            x = make_datnum(tokens, symbol_list)
            res.append(x)
            print(x)
            tokens.popToken()



def make_datnum(tokens, symbol_list):
    token = tokens.nextToken()
    #Basic datnum
    basic_datnum = {slang_scanner.BOOL:"BOOL", slang_scanner.CHAR:"CHAR", slang_scanner.INT:"INT", slang_scanner.DBL:"Dbl", slang_scanner.STR:"STR", 
    slang_scanner.IDENTIFIER : "IDENTIFIER"}
    if(token.type in basic_datnum.keys()):
        if(token.tokenText in symbol_list):
            return {"SYMBOL": token.tokenText}
        return {basic_datnum[token.type]:token.tokenText}
    #Special datnum
    special_datnum = {slang_scanner.VECTOR:"VECTOR", slang_scanner.ABBREV:"CONS"}
    if(token.type in special_datnum.keys()):
        if(token.type == slang_scanner.VECTOR):
            if tokens.hasNext():
                tokens.popToken()
            else:
                print("Vector error")
                exit()
        else:
            if tokens.hasNext():
                tokens.popToken()
                if(tokens.nextToken().type != slang_scanner.LEFT_PAREN):
                    return None
            else:
                print("Cons or Vector error")
                exit()
            tokens.popToken()
        val = []
        while(tokens.hasNext()):
                if(tokens.nextToken().type == slang_scanner.RIGHT_PAREN):
                    return{special_datnum[token.type]:val}
                else:
                    temp = make_datnum(tokens, symbol_list)
                    if temp == None:
                        print("Cons or Vector error")
                        exit()
                    elif "IDENTIFIER" in temp.keys():
                        print("Cons or Vector error")
                        exit()
                    val.append(temp)
                tokens.popToken()
        print("Cons  or Vector error")
        exit()
            
    return None


