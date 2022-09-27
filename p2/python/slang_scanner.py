# [CSE 262] This file is a minimal skeleton for a Scheme scanner in Python.  It
# provides a transliteration of the TokenStream class, and the shell of a
# Scanner class.  Please see the README.md file for more discussion.

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
            while(inline_token.hasNext()):
                #Take out tokentext from line of tokens
                tokenText = TokenStream(inline_token.nextToken())
                while(tokenText.hasNext):
                    #PM state
                    if tokenText.nextToken() == '+':
                        
                col += 1
                inline_token.popToken()

            line_text.popToken()
            line += 1
            pass


def tokenToXml(token):
    return ""
