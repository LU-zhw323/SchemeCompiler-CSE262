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
        operation = ""
        if(expr["exprs"][0]['type'] == IDENTIFIER):
            operation = expr["exprs"][0]['name']
        expr["exprs"].pop(0)
        list = []
        #evaluate the rest of the list
        for node in expr["exprs"]:
            list.append(evaluate(node, env))
        #perform built-in func
        if func['type'] == BUILTIN:
            built_in = func['func']
            return built_in(operation, list)
        #perform lambda
        if func['type'] == LAMBDAVAL:
            lambda_def = func['def']
            lambda_formal = lambda_def['formals']
            if(len(lambda_formal) != len(list)):
                raise Exception("Wrong number of argument")
            for count in range(0, len(lambda_formal)):
                formal = lambda_formal[count]
                arg_name = formal['name']
                arg_val = list[count]
                func['env'].put(arg_name, arg_val)
            return evaluate(func, func['env'])
        else:
            raise Exception("Application not found")



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
        lambda_val = {"type": LAMBDAVAL, 'env': inner,  "def":expr}
        return lambda_val
    
    #lambdaVal
    if expr["type"] == LAMBDAVAL:
        local = expr['env']
        expression = expr['def']
        for id in expression["formals"]:
            id_check = evaluate(id, local)
        for index, action in enumerate(expression["exprs"]):
            res = evaluate(action, local)
            if index == len(expression["exprs"])-1:
                return res
