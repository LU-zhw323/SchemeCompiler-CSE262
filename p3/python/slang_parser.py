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
        #In python part, I use dictionary(a mapping of k/v) to represent each node, the general idea
        #is similar to the java one
        symbol_list = []
        res = []
        index = 0
        while(tokens.hasNext()):
            node = make_datnum(tokens, symbol_list)
            if(node == None):
                node = make_expres(tokens, symbol_list)
            res.append(node)
            print(node)
            tokens.popToken()
        return res

def make_expres(tokens,symbol_list):
    #Tick
    if (tokens.nextToken().type == slang_scanner.ABBREV):
        if not tokens.hasNext():
            print("Parse error")
            exit()
        node = make_datnum(tokens,symbol_list)
        if(node == None or "SYMBOL" in node.keys()):
            print("Tick error")
            exit()
        else: return {"Tick": node}
    tokens.popToken()
    if not tokens.hasNext():
        print("Parse error")
        exit()
    current = tokens.nextToken()
    #Quote
    if(current.type == slang_scanner.QUOTE):
        tokens.popToken()
        if not tokens.hasNext():
            print("Quote error")
            exit()
        val = make_datnum(tokens, symbol_list)
        if val == None or "IDENTIFIER" in val.keys():
            print("Quote error")
            exit()
        tokens.popToken()
        if not tokens.hasNext():
            print("Quote error")
            exit()
        if tokens.nextToken().type == slang_scanner.RIGHT_PAREN:
            return {"QUOTE": val}
        else:
            print("Quote error")
            exit()
    #Begin, and ,or 
    elif(current.type == slang_scanner.AND or current.type == slang_scanner.BEGIN or current.type ==slang_scanner.OR):
        special_tok = {slang_scanner.AND:"AND", slang_scanner.BEGIN:"BEGIN", slang_scanner.OR:"OR"}
        val = []
        while(tokens.hasNext()):
            tokens.popToken()
            if not tokens.hasNext():
                print("Parse error")
                exit()
            node = make_datnum(tokens, symbol_list)
            if(node != None):
                if("SYMBOL" in node.keys()):
                    print("Use symbol as expression")
                    exit()
                val.append(node)
            else:
                if(tokens.nextToken().type == slang_scanner.LEFT_PAREN):
                    node = make_expres(tokens,symbol_list)
                    val.append(node)
                elif(tokens.nextToken().type == slang_scanner.RIGHT_PAREN):
                    return {special_tok[current.type]:val}
                else:
                    print("AND,BEGIN,OR error")
                    exit()
        print("AND,BEGIN,OR error")
        exit()
    #If
    elif(current.type == slang_scanner.IF):
        cond = {}
        ifTrue = {}
        ifFalse = {}
        count = 1
        while(tokens.hasNext()):
            if count > 4:
                print("IF error")
                exit()
            tokens.popToken()
            if not tokens.hasNext():
                print("Parse error")
                exit()
            node = make_datnum(tokens, symbol_list)
            if(node != None):
                if("SYMBOL" in node.keys()):
                    print("Use symbol as expression")
                    exit()
            else:
                if(tokens.nextToken().type == slang_scanner.LEFT_PAREN):
                    node = make_expres(tokens,symbol_list)
                elif(tokens.nextToken().type == slang_scanner.RIGHT_PAREN):
                    if count < 4:
                        print("IF error")
                        exit()
                    return{"IF":{"Con":cond, "True":ifTrue, "False":ifFalse}}
                else:
                    print("IF error")
                    exit()
            if count == 1: cond = node
            elif count == 2: ifTrue = node
            elif count == 3: ifFalse = node
            count += 1
    #Set, define
    elif(current.type == slang_scanner.SET or current.type == slang_scanner.DEFINE):
        special_tok = {slang_scanner.SET:"SET", slang_scanner.DEFINE:"DEFINE"}
        tokens.popToken()
        if not tokens.hasNext():
            print("Parsing error")
            exit()
        id = make_datnum(tokens,symbol_list)
        if(id == None or "IDENTIFIER" not in id.keys()):
            print("Set,define error")
            exit()
        symbol_list.append(id["IDENTIFIER"])
        tokens.popToken()
        if not tokens.hasNext():
            print("Parsing error")
            exit()
        expre = make_datnum(tokens,symbol_list)
        if(expre != None):
                if("SYMBOL" in expre.keys()):
                    print("Use symbol as expression")
                    exit()
        else: expre = make_expres(tokens,symbol_list)
        tokens.popToken()
        if not tokens.hasNext():
            print("Parsing error")
            exit()
        if(tokens.nextToken().type != slang_scanner.RIGHT_PAREN):
            print("Set,define error")
            exit()
        return {special_tok[current.type]:{"ID":id, "EXPRESSION":expre}}
    #cond
    elif(current.type == slang_scanner.COND):
        res = {"COND":[]}
        cond_count = 1
        while tokens.hasNext():
            tokens.popToken()
            if not tokens.hasNext():
                print("Parsing error")
                exit()
            if(tokens.nextToken().type == slang_scanner.LEFT_PAREN):
                each_cond = {"Test":{}, "Expres":[]}
                count = 1
                expre_count = 1
                while(tokens.hasNext()):
                    tokens.popToken()
                    if not tokens.hasNext():
                        print("Parsing error")
                        exit()
                    node = make_datnum(tokens, symbol_list)
                    if(node != None):
                        if("SYMBOL" in node.keys()):
                            print("Use symbol as expression")
                            exit()
                    else:
                        if(tokens.nextToken().type == slang_scanner.LEFT_PAREN):
                            node = make_expres(tokens,symbol_list)
                        elif(tokens.nextToken().type == slang_scanner.RIGHT_PAREN):
                            if(each_cond["Test"] == {} or each_cond["Expres"] == []):
                                print("Cond error")
                                exit()
                            res["COND"].append(each_cond)
                            cond_count += 1
                            break
                        else:
                            print("Cond error")
                            exit()
                    if count == 1: each_cond["Test"].update(node)
                    else: 
                        each_cond["Expres"].append(node)
                        expre_count += 1
                    count += 1
                if cond_count == 1:
                    print("Cond error")
                    exit()
            elif(tokens.nextToken().type == slang_scanner.RIGHT_PAREN):
                if(res["COND"] == []):
                    print("Cond error")
                    exit()
                return res 
            else:
                print("Cond error")
                exit()
        print("Cond error")
        exit()
    elif current.type == slang_scanner.LAMBDA:
        formals = []
        body = []
        tokens.popToken()
        if not tokens.hasNext():
            print("Parsing error")
            exit()
        #Formal
        Form = False
        if tokens.nextToken().type == slang_scanner.LEFT_PAREN:
            while(tokens.hasNext()):
                tokens.popToken()
                if not tokens.hasNext():
                    print("Parsing error")
                    exit()
                node = make_datnum(tokens, symbol_list)
                if node != None:
                    if "IDENTIFIER" in node.keys():
                        formals.append(node)
                        Form = True
                    else:
                        print("Lambda error")
                        exit()
                else:
                    if tokens.nextToken().type == slang_scanner.RIGHT_PAREN:
                        Form = False
                        break
                    else:
                        print("Lambda error")
                        exit()
        if Form:
            print("Lambda error")
            exit()
        tokens.popToken()
        if not tokens.hasNext():
            print("Parsing error")
            exit()
        #Body
        if tokens.nextToken().type == slang_scanner.LEFT_PAREN:
            expre_count = 0
            Define = False
            while(tokens.hasNext()):
                if tokens.nextToken().type == slang_scanner.RIGHT_PAREN:
                    Define = False
                    break
                node = make_expres(tokens, symbol_list)
                Define = True
                if node != None:
                    if "DEFINE" in node.keys():
                        body.append(node)
                    else:
                        if expre_count == 0:
                            body.append(node)
                            expre_count += 1
                        else:
                            print("Lambda error")
                            exit()
                else:
                    print("Lambda error")
                    exit()
                tokens.popToken()
            if Define or body == []:
                print("Lambda error")
                exit()
        else:
            print("Lambda error")
            exit()
        return {"LAMBDA":{"Formals": formals, "Body":body}}
    #apply
    else:
        val = []
        while(tokens.hasNext()):
            node = make_datnum(tokens, symbol_list)
            if(node != None):
                if("SYMBOL" in node.keys()):
                    print("Use symbol as expression")
                    exit()
                val.append(node)
            else:
                if(tokens.nextToken().type == slang_scanner.LEFT_PAREN):
                    node = make_expres(tokens,symbol_list)
                    val.append(node)
                elif(tokens.nextToken().type == slang_scanner.RIGHT_PAREN):
                    if(len(val) == 0):
                        print("Application error")
                        exit()
                    return {"APPLY":val}
                else:
                    print("Application error")
                    exit()
            tokens.popToken()
        print("Application error")
        exit()


def make_datnum(tokens, symbol_list):
    token = tokens.nextToken()
    #Basic datnum
    basic_datnum = {slang_scanner.BOOL:"BOOL", slang_scanner.CHAR:"CHAR", slang_scanner.INT:"INT", slang_scanner.DBL:"Dbl", slang_scanner.STR:"STR", 
    slang_scanner.IDENTIFIER : "IDENTIFIER"}
    if(token.type in basic_datnum.keys()):
        if(token.tokenText in symbol_list):
            return {"SYMBOL": token.tokenText}
        if(token.type == slang_scanner.BOOL):
            return {"BOOL": token.literal}
        return {basic_datnum[token.type]:token.tokenText}
    #Special datnum
    special_datnum = {slang_scanner.VECTOR:"VECTOR", slang_scanner.ABBREV:"CONS"}
    if(token.type in special_datnum.keys()):
        if(token.type == slang_scanner.VECTOR):
            tokens.popToken()
            if not tokens.hasNext():
                print("Vector or Cons error")
                exit()
        else:
            tokens.popToken()
            if not tokens.hasNext():
                print("Vector or Cons error")
                exit()
            if(tokens.nextToken().type != slang_scanner.LEFT_PAREN):
                    return None
            tokens.popToken()
            if not tokens.hasNext():
                print("Vector or Cons error")
                exit()
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


