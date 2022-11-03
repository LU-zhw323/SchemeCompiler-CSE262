import slang_parser


def AstToXml(expr,indent):
    """Convert an AST into its XML-like representation"""
    # [CSE 262] You will need to implement this after you decide on the types
    # for your nodes
    indent += "\t"
    xml = ""
    if "INT" in expr.keys():
        xml = "<Int val='" + expr["INT"] + "' />\n"
    elif "Dbl" in expr.keys():
        xml = "<Dbl val='" + expr["Dbl"] + "' />\n"
    elif "CHAR" in expr.keys():
        xml = "<CHAR val='" + expr["CHAR"] + "' />\n"
    elif "IDENTIFIER" in expr.keys():
        xml = "<IDENTIFIER val='" + expr["IDENTIFIER"] + "' />\n"
    elif "STR" in expr.keys():
        xml = "<STR val='" + expr["STR"] + "' />\n"
    elif "SYMBOL" in expr.keys():
        xml = "<SYMBOL val='" + expr["SYMBOL"] + "' />\n"
    elif "BOOL" in expr.keys():
        xml = "<BOOL val='" + str(expr["BOOL"]) + "' />\n"
    elif "VECTOR" in expr.keys():
        xml += "<Vector>\n"
        for e in expr["VECTOR"]:
            if checkNest(str(e.keys())):
                xml += indent + AstToXml(e, indent)
            else:
                xml += indent + AstToXml(e, "")
        xml += "<Vector/>\n"
    elif "CONS" in expr.keys():
        xml += "<CONS>\n"
        for e in expr["CONS"]:
            if checkNest(str(e.keys())):
                xml += indent + AstToXml(e, indent)
            else:
                xml += indent + AstToXml(e, "")
        xml += '<CONS/>\n'
    elif "TICK" in expr.keys():
        xml = "<TICK val='" + str(expr["TICK"]) + "' />\n"
    elif "QUOTE" in expr.keys():
        xml += "<QUOTE>\n"
        xml += indent + AstToXml(expr["QUOTE"], '')
        xml += "<QUOTE/>\n"
    
    return xml

def checkNest(tag):
    special = ["CONS","QUOTE","VECTOR","APPLY", "BEGIN", "IF","OR", "SET", "LAMBDA", "DEFINE", "COND","AND"]
    if tag in special: return True
    return False
