import slang_parser


def AstToXml(expr,indent):
    """Convert an AST into its XML-like representation"""
    # [CSE 262] You will need to implement this after you decide on the types
    # for your nodes
    
    xml = ""
    if "INT" in expr.keys():
        xml = indent + "<Int val='" + expr["INT"] + "' />\n"
    elif "Dbl" in expr.keys():
        xml = indent + "<Dbl val='" + expr["Dbl"] + "' />\n"
    elif "CHAR" in expr.keys():
        xml = indent + "<CHAR val='" + expr["CHAR"] + "' />\n"
    elif "IDENTIFIER" in expr.keys():
        xml = indent + "<IDENTIFIER val='" + expr["IDENTIFIER"] + "' />\n"
    elif "STR" in expr.keys():
        xml = indent + "<STR val='" + expr["STR"] + "' />\n"
    elif "SYMBOL" in expr.keys():
        xml = indent + "<SYMBOL val='" + expr["SYMBOL"] + "' />\n"
    elif "BOOL" in expr.keys():
        xml = indent + "<BOOL val='" + str(expr["BOOL"]) + "' />\n"
    elif "VECTOR" in expr.keys():
        xml += indent + "<Vector>\n"
        for e in expr["VECTOR"]:
            xml += AstToXml(e, indent+"  ")
        xml += indent + "<Vector/>\n"
    elif "CONS" in expr.keys():
        xml += indent + "<CONS>\n"
        for e in expr["CONS"]:
            xml += AstToXml(e, indent+'  ')
        xml += indent + '<CONS/>\n'
    elif "TICK" in expr.keys():
        xml = indent + "<TICK val='" + str(expr["TICK"]) + "' />\n"
    elif "QUOTE" in expr.keys():
        xml += indent + "<QUOTE>\n"
        xml += AstToXml(expr["QUOTE"], indent+'  ')
        xml += indent + "<QUOTE/>\n"
    elif "IF" in expr.keys():
        xml += indent + "<IF>\n"
        xml += AstToXml(expr['IF']['Con'], indent+'  ')
        xml += AstToXml(expr["IF"]['True'], indent + '  ')
        xml += AstToXml(expr["IF"]['False'], indent + '  ')
        xml += indent + "<IF/>\n"
    elif 'APPLY' in expr.keys():
        xml += indent + "<APPLY>\n"
        for e in expr["APPLY"]:
            xml +=AstToXml(e, tab(indent))
        xml += indent + "<APPLY/>\n"
    elif 'BEGIN' in expr.keys():
        xml += indent + "<BEGIN>\n"
        for e in expr["BEGIN"]:
            xml += AstToXml(e, tab(indent))
        xml += indent + "<BEGIN/>\n"
    elif 'OR' in expr.keys():
        xml += indent + "<OR>\n"
        for e in expr["OR"]:
            xml += AstToXml(e, tab(indent))
        xml += indent + "<OR/>\n"
    elif 'AND' in expr.keys():
        xml += indent + "<AND>\n"
        for e in expr["AND"]:
            xml += AstToXml(e, tab(indent))
        xml += indent + "<AND/>\n"
    elif 'DEFINE' in expr.keys():
        xml += indent + "<DEFINE>\n"
        xml += AstToXml(expr['DEFINE']['ID'], indent+'  ')
        xml += AstToXml(expr['DEFINE']['EXPRESSION'], indent+'  ')
        xml += indent + "<DEFINE/>\n"
    elif 'SET' in expr.keys():
            xml += indent + "<SET>\n"
            xml += AstToXml(expr['SET']['ID'], indent+'  ')
            xml += AstToXml(expr['SET']['EXPRESSION'], indent+'  ')
            xml += indent + "<SET/>\n"
    elif 'COND' in expr.keys():
            xml += indent + "<COND>\n"
            for cond in expr["COND"]:
                xml += tab(indent) + '<Conditions>\n'
                xml += tab(tab(indent)) + '<Test>\n'
                xml += AstToXml(cond["Test"], tab(tab(tab(indent))))
                xml += tab(tab(indent)) + '<Test/>\n'
                xml += tab(tab(indent)) + '<Actions>\n'
                for action in cond['Expres']:
                    xml += AstToXml(action, tab(tab(tab(indent))))
                xml += tab(tab(indent)) + '<Actions/>\n'
                xml += tab(indent) + '<Conditions/>\n'
            xml += indent + "<COND/>\n"
    return xml

def checkNest(tag):
    special = ["CONS","QUOTE","VECTOR","APPLY", "BEGIN", "IF","OR", "SET", "LAMBDA", "DEFINE", "COND","AND"]
    if tag in special: return True
    return False
def tab(indent):
    return indent + '  '