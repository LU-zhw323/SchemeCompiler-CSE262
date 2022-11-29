import slang_parser
import operator
import math


def addMathFuncs(env):
    def add(list):
        return list[0]
    env.put('+', add)



def addListFuncs(env):
    """Add standard list functions to the given environment"""
    pass


def addStringFuncs(env):
    """Add standard string functions to the given environment"""
    pass


def addVectorFuncs(env):
    """Add standard vector functions to the given environment"""
    pass
