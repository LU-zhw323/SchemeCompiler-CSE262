# [CSE 262] This file is a minimal skeleton for a Scheme scanner in Python.  It
# provides a transliteration of the TokenStream class, and the shell of a
# Scanner class.  Please see the README.md file for more discussion.
from lib2to3.pgen2 import token
import re

class TokenStream:
    def __init__(self, tokens):
        self.__tokens = tokens
        self.__next = 0

    def reset(self): self.__next = 0

    def nextToken(self):
        return None if not self.hasNext() else self.__tokens[self.__next]

    def nextNextToken(self):
        return None if not self.hasNextNext() else self.__tokens[self.__next + 1]

    def popToken(self): self.__next += 1

    def hasNext(self): return self.__next < len(self.__tokens)

    def hasNextNext(self): return (self.__next + 1) < len(self.__tokens)


class Scanner:
    def __init__(self):
        pass

    

    def scanTokens(self, source):
        #The source file that we read
        ori_text = TokenStream(source)
        #boolean flag to determine state
        PM = False
        ININT = False
        INID = False
        INSTR = False
        INDOUBLE = False
        Error = False

        #Final TokenStream that we will return
        result = []
        #line and col to record
        line = 1
        col = 0
        #Split the original file by \n to get tokens on different line
        line_s = source.split('\n')
        line_text = TokenStream(line_s)
        #Reading input line by line
        while(line_text.hasNext()):
            #Split tokens in each line by \t\r
            inline_token = TokenStream(line_text.nextToken().split())
            inline = str(line_text.nextNextToken())
            line_size = 0
            while(inline_token.hasNext()):
                #Take out tokentext from line of tokens
                tokent = ""
                tokenText = str(inline_token.nextToken())
                temp = []
                #Handle special inline '()'
                if(tokenText.count('(') != 0 or tokenText.count(')') != 0):
                    for i in range(0, len(tokenText)):
                        if tokenText[i] == '(':
                            if len(tokent)!=0:
                                temp.append(deter_token(tokent,line,col))
                    
                                col += len(tokent)
                                temp.append(["LPRE", line, col])
                                col += 1
                            else:
                                temp.append(["LPRE", line, col])
                                col += 1
                        elif tokenText[i] == ')':
                            if len(tokent)!=0:
                                temp.append(deter_token(tokent,line,col))
                                #tokent = ""
                                col += len(tokent)
                                print(col)
                                temp.append(["RPRE", line, col])
                                col += 1
                            else:
                                temp.append(["RPRE", line, col])
                                col += 1
                        else: tokent += tokenText[i]
                    col += 1
                       
                else: 
                    temp = deter_token(tokenText, line, col)
                    col += len(tokenText)
                    col += 1
                result.append(temp)
                inline_token.popToken()
                

            line_text.popToken()
            line += 1
            col = 0
        
        return TokenStream(result)

def deter_token(tokenText, line, col):
        temp = []
        if(tokenText.isdigit()):
            temp = ["Int", line, col, tokenText]
        elif(tokenText.count('.') == 1 and not tokenText.startswith('.') and not tokenText.endswith('.')):
                    partial_token = tokenText.split('.')
                    if(partial_token[0].isdigit() and partial_token[1].isdigit()):
                        temp = ["Int", line, col, tokenText]
                    else:  temp = ["Error", line, col, tokenText]
        return temp


def tokenToXml(token):
    return token
