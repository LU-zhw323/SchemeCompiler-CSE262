import slang_scanner

# [CSE 262] You will probably find it tedious to create a whole class hierarchy
# for your Python parser.  Instead, consider whether each node type could just
# be a hash table.  In that case, you could have a function for "constructing"
# each "type", by putting some values into a hash table.


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

        raise Exception("parse() is not implemented yet! (slang_parser.py)")
