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
                tokenText = str(inline_token.nextToken())
                temp = []
                if tokenText.count('(') != 0:
                    num = tokenText.count('(')
                    for i in range(0 ,len(tokenText)):
                        if(tokenText[i] == '('):
                            temp = ["LeftParen", line, col+i]
                            result.append(temp)
                    col += num
                    tokenText = tokenText.replace('(','')
                elif tokenText.count(')') != 0:
                    num = tokenText.count(')')
                    for i in range(0 ,len(tokenText)):
                        if(tokenText[i] == ')'):
                            temp = ["RightParen", line, col+i]
                            result.append(temp)
                    col += num
                    tokenText = tokenText.replace(')','')
                

                if(tokenText.isdigit()):
                    ININT = True
                elif(tokenText.count('.') == 1 and not tokenText.startswith('.') and not tokenText.endswith('.')):
                    partial_token = tokenText.split('.')
                    if(partial_token[0].isdigit() and partial_token[1].isdigit()):
                        INDOUBLE = True
                    else: Error = True
                
                if ININT:
                    temp = ["Int", line ,col, tokenText]
                    ININT = False
                elif INDOUBLE:
                    temp = ["Double", line, col, tokenText]
                    INDOUBLE = False
                elif Error:
                    temp = ["Error", line, col, tokenText]
                    Error = False
                result.append(temp)
                inline_token.popToken()
                col += len(tokenText)
                col += 1

            line_text.popToken()
            line += 1
            col = 0
        
        return TokenStream(result)


def tokenToXml(token):
    return token
