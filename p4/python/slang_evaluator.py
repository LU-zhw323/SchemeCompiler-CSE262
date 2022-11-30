import slang_parser
import slang_env
# A poor-man's enum for the AST node types
AND, APPLY, BEGIN, BOOL, BUILTIN, CHAR, COND, CONS, DBL, DEFINE, IDENTIFIER, IF, INT, LAMBDADEF, LAMBDAVAL, OR, QUOTE, SET, STR, SYMBOL, TICK, VEC = range(
    0, 22)
def evaluate(expr, env):
    #All the code for python part is the same as java part

    #Basic IValue that can just return
    Basic_IValue = [BOOL, BUILTIN, CHAR, CONS, DBL, INT, STR, SYMBOL, VEC]
    if expr["type"] in Basic_IValue:
        return expr
    #For tick and quote that we return the datum
    Datnum_IValue = [TICK, QUOTE]
    if expr["type"] in Datnum_IValue:
        return expr["datum"]
    
    #AND
    if expr["type"] == AND:
        for index, node in enumerate(expr["exprs"]):
            IValue = evaluate(node, env)
            if index == len(expr['exprs']) - 1:
                return IValue
            if IValue["type"] == BOOL:
                if IValue["val"] == False:
                    return env.poundF
    
    #IDENTIFIER
    if expr["type"] == IDENTIFIER:
        #I modified the env.get so it will not cause Attribute Error
        return env.get(expr["name"])
    
    #APPLY
    if expr["type"] == APPLY:
        #evaluate identifier to get the built-in func back
        func = evaluate(expr["exprs"][0], env)
        operation = expr["exprs"][0]['name']
        expr["exprs"].pop(0)
        list = []
        #evaluate the rest of the list
        for node in expr["exprs"]:
            list.append(evaluate(node, env))
        #perform built-in func
        return func(operation, list)

    #BEGIN
    if expr["type"] == BEGIN:
        for index, node in enumerate(expr["exprs"]):
            IValue = evaluate(node, env)
            if index == len(expr['exprs']) - 1:
                return IValue

    #COND
    if expr["type"] == COND:
        conditions = expr["conditions"]
        for index,condition in enumerate(conditions):
            test = condition["test"]
            test_result = evaluate(test,env)
            if(test_result["type"]) != BOOL:
                raise Exception("Not a testable test")
            if test_result["val"] == True or index == len(conditions)-1:
                actions = condition["exprs"]
                for j, node in enumerate(actions):
                    IValue = evaluate(node, env)
                    if(j == len(actions) - 1): return IValue

    #Define
    if expr["type"] == DEFINE:
        name = expr["id"]["name"]
        val = evaluate(expr["expr"], env)
        env.put(name, val)
        return None

    #SET!
    if expr["type"] == SET:
        name = expr["id"]["name"]
        val = evaluate(expr["expr"], env)
        env.update(name, val)
        return None    
    
    #IF
    if expr["type"] == IF:
        test_res = evaluate(expr["cond"], env)
        if test_res["type"] != BOOL:
            raise Exception("not a testable condition")
        if(test_res["val"] == True):
            return evaluate(expr["true"],env)
        else:
            return evaluate(expr["false"], env)
    
    #OR
    if expr["type"] == OR:
        for node in expr["exprs"]:
            res = evaluate(node, env)
            if(res["type"] == BOOL):
                if(res["val"] == True):
                    return env.poundT
            else:return res
        return env.poundF
    
    #LambdaDef
    if expr["type"] == LAMBDADEF:
        inner = slang_env.makeInnerEnv(env)
        lambda_val = {"type": LAMBDAVAL, "def":expr}
        return evaluate(lambda_val, inner)
    
    #lambdaVal
    if expr["type"] == LAMBDAVAL:
        expression = expr['def']
        for id in expression["formals"]:
            id_check = evaluate(id, env)
        for index, action in enumerate(expression["exprs"]):
            res = evaluate(action, env)
            if index == len(expression["exprs"])-1:
                return res
