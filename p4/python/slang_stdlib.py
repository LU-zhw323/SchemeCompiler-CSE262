import slang_parser
import operator
import math
AND, APPLY, BEGIN, BOOL, BUILTIN, CHAR, COND, CONS, DBL, DEFINE, IDENTIFIER, IF, INT, LAMBDADEF, LAMBDAVAL, OR, QUOTE, SET, STR, SYMBOL, TICK, VEC = range(
    0, 22)
math_type = [INT, DBL]

def addMathFuncs(env):
    def type_check(args):
        for arg in args:
            if(arg["type"] not in math_type):
                return False
        return True
    #==, <, >, <=, >=
    def test_args(operation, args):
        if(len(args) < 1):
            raise Exception(operation + "expects at least one argument")
        if(type_check(args) == False):
            raise Exception(operation + "can only handle int or double")
        res = args[0]['val']
        args.pop(0)
        for arg in args:
            temp = arg['val']
            if operation == '==':
                if(res != temp):
                    return env.poundF
            if operation == '<':
                if(not res < temp):
                    return env.poundF
            if operation == '>':
                if(not res > temp):
                    return env.poundF
            if operation == '<=':
                if(not res <= temp):
                    return env.poundF
            if operation == '>=':
                if(not res >= temp):
                    return env.poundF
            res = temp
        return env.poundT

    #+
    def add(operation, args):
        if(len(args) < 1):
            raise Exception ("+ expects at least one argument")
        res = 0
        for arg in args:
            if(arg["type"] not in math_type):
                raise Exception("+ can only handle int or double")
            res += arg["val"]
        if(isinstance(res, int)):
            return {"type":INT, "val":res}
        else:
            return {"type":DBL, "val":res}
    env.put('+', add)

    #-
    def minus(operation,args):
        if(len(args) < 1):
            raise Exception ("- expects at least one argument")
        if(type_check(args) == False):
            raise Exception("- can only handle int or double")
        if(len(args) == 1):
            args[0]['val'] = -1 * args[0]['val']
            return args[0]
        res = args[0]['val']
        args.pop(0)
        for arg in args:
            res -= arg["val"]
        if(isinstance(res, int)):
            return {"type":INT, "val":res}
        else:
            return {"type":DBL, "val":res}
    env.put('-', minus)

    #*
    def multiply(operation, args):
        if(len(args) < 1):
            raise Exception("* expects at least one argument")
        if(type_check(args) == False):
            raise Exception("* can only handle int or double")
        res = 1
        for arg in args:
            res *= arg['val']
        if(isinstance(res, int)):
            return {"type":INT, "val":res}
        else:
            return {"type":DBL, "val":res}
    env.put('*', multiply)

    #/
    def devide(operation, args):
        if(len(args) < 1):
            raise Exception("/ expects at least one argument")
        if(type_check(args) == False):
            raise Exception("/ can only handle int or double")
        res = 1
        for arg in args:
            res /= arg['val']
        if(isinstance(res, int)):
            return {"type":INT, "val":res}
        else:
            return {"type":DBL, "val":res}
    env.put('/', devide)

    #%
    def modulo(operation, args):
        if(len(args) != 2):
            raise Exception("modulo expects two argument")
        if(type_check(args) == False or args[0]['type'] != INT or args[1]['type'] != INT ):
            raise Exception("modulo can only handle int")
        res = args[0]['val'] % args[1]['val']
        if(isinstance(res, int)):
            return {"type":INT, "val":res}
        else:
            return {"type":DBL, "val":res}
    env.put('%', modulo)

    env.put('==', test_args)
    
    env.put('<', test_args)
    
    env.put('>', test_args)
    
    env.put('<=', test_args)
    
    env.put('>=', test_args)

    #basic math operation
    #for trigonometric operation, I didn't explictly handle the value error
    #I will let python handle it for me
    def math_operation(operation, args):
        if(len(args) != 1):
            raise Exception(operation + "expects one argument")
        if(type_check(args) == False):
            raise Exception(operation + "can only handle int or double")
        res = 0
        if(operation == 'abs'):
           res = abs(args[0]['val'])
        elif(operation == 'sqrt'):
            res = math.sqrt(args[0]['val'])
        elif(operation == 'acos'):
            res = math.acos(args[0]['val'])
        elif(operation == 'asin'):
            res = math.asin(args[0]['val'])
        elif(operation == 'atan'):
            res = math.atan(args[0]['val'])
        elif(operation == 'sin'):
            res = math.sin(args[0]['val'])
        elif(operation == 'sinh'):
            res = math.sinh(args[0]['val'])
        elif(operation == 'cos'):
            res = math.cos(args[0]['val'])
        elif(operation == 'cosh'):
            res = math.cosh(args[0]['val'])
        elif(operation == 'tan'):
            res = math.tan(args[0]['val'])
        elif(operation == 'tanh'):
            res = math.tanh(args[0]['val'])
        elif(operation == 'log10'):
            res = math.log10(args[0]['val'])
        elif(operation == 'loge'):
            res = math.log(args[0]['val'])
        if(isinstance(res, int)):
            return {"type":INT, "val":res}
        else:
            return {"type":DBL, "val":res} 

    math_operation_list = ['abs','sqrt','acos','asin','atan','sin','sinh','cos','cosh','tan','tanh','log10','loge']
    for op in math_operation_list:
        env.put(op, math_operation)
    
    #number?, integer?, double?, symbol?, procedure?, null?, not
    def type_query(operation, args):
        if(len(args) != 1):
            raise Exception(operation + "expects one argument")
        if(operation == 'number?'):
            if(type_check(args) == False):
                return env.poundF
        if(operation == 'integer?'):
            if(args[0]['type'] != INT):
                return env.poundF
        if(operation == 'double?'):
            if(args[0]['type'] != DBL):
                return env.poundF
        if(operation == 'symbol?'):
            if(args[0]['type'] != SYMBOL):
                return env.poundF
        if(operation == 'procedure?'):
            if(args[0]['type'] != LAMBDAVAL):
                return env.poundF
        if(operation == 'null?'):
            if(args[0] != env.empty):
                return env.poundF
        if(operation == 'not'):
            if(args[0]['type'] != BOOL):
                return env.poundF
            else:
                if(args[0]['val'] == True):
                    return env.poundF
        return env.poundT
    type_query_list=['number?', 'integer?', 'double?', 'symbol?', 'procedure?', 'null?', 'not']
    for op in type_query_list:
        env.put(op, type_query)
    
    #pow
    def pow(operaion, args):
        if(len(args) != 2):
            raise Exception("pow expects two arguments")
        if(type_check(args) == False):
            raise Exception("pow can only handle int or double")
        res = math.pow(args[0]['val'], args[1]['val'])
        if(isinstance(res, int)):
            return {"type":INT, "val":res}
        else:
            return {"type":DBL, "val":res}
    env.put('pow', pow)

    #integer to double, double to integer
    def transfer(operation, args):
        if(len(args) != 1):
            raise Exception(operation + "expects one argument")
        if(type_check(args) == False):
            raise Exception(operation + "can only handle int or double")
        if(operation == 'integer->double'):
            if(args[0]['type'] != INT):
                raise Exception("integer->double expects integer")
            return{"type":DBL, 'val':float(args[0]['val'])}
        elif(operation == 'double->integer'):
            if(args[0]['type'] != DBL):
                raise Exception("double->integer expects double")
            return{'type':INT, 'val': int(args[0]['val'])}
    env.put('integer->double', transfer)
    env.put('double->integer', transfer)

    #and
    def And(operation, args):
        for index, node in enumerate(args):
            IValue = node
            if index == len(args['exprs']) - 1:
                return IValue
            if IValue["type"] == BOOL:
                if IValue["val"] == False:
                    return IValue
        return env.poundT
    env.put('and', And)

    #or
    def Or(operation, args):
        for node in args:
            res = node
            if(res["type"] == BOOL):
                if(res["val"] == True):
                    return env.poundT
            else:return res
        return env.poundF
    env.put('or', Or)






def addListFuncs(env):
    """Add standard list functions to the given environment"""
    pass


def addStringFuncs(env):
    """Add standard string functions to the given environment"""
    pass


def addVectorFuncs(env):
    """Add standard vector functions to the given environment"""
    pass
