package edu.lehigh.cse262.slang.Interpreter;

import edu.lehigh.cse262.slang.Env.Env;
import edu.lehigh.cse262.slang.Parser.IAstVisitor;
import edu.lehigh.cse262.slang.Parser.IValue;
import edu.lehigh.cse262.slang.Parser.Nodes;

import java.util.List;
import java.util.ArrayList;

/**
 * ExprEvaluator evaluates an AST node. It is the heart of the evaluation
 * portion of our interpreter.
 */
public class ExprEvaluator implements IAstVisitor<IValue> {
    /** The environment in which to do the evaluation */
    private Env env;

    /** Construct an ExprEvaluator by providing an environment */
    public ExprEvaluator(Env env) {
        this.env = env;
    }

    /** Interpret an Identifier */
    @Override
    public IValue visitIdentifier(Nodes.Identifier expr) throws Exception {
        IValue value = env.get(expr.name);
		/*
		 * The null checking will probably make 'null?' meaningless since it will
		 * just throw an exception if some identifier is null
		 */
        if(value == null){
            throw new Exception("Identifier not found");
        }
        return value;
    }

    /**
     * Interpret a Define special-form
     *
     * NB: it's OK for this to return null, because definitions aren't
     * expressions
     */
    @Override
    public IValue visitDefine(Nodes.Define expr) throws Exception {
        //get the name of identifier to be defined & expression
        String name = expr.identifier.name;
        IValue express = expr.expression.visitValue(this);
        //I tried to do checking before putting to aviod redefining of builtin func
        //but it will throw exception in outerGet() if we are trying to define new val
        // so it didn't work out
        //set binding in env
        env.put(name,express);
        return null;
    }

    /** Interpret a Bool value */
    @Override
    public IValue visitBool(Nodes.Bool expr) throws Exception {
        return expr;
        //throw new Exception("visitBool is not yet implemented");
    }

    /** Interpret an Int value */
    @Override
    public IValue visitInt(Nodes.Int expr) throws Exception {
        return expr;
        //throw new Exception("visitInt is not yet implemented");
    }

    /** Interpret a Dbl value */
    @Override
    public IValue visitDbl(Nodes.Dbl expr) throws Exception {
        return expr;
        //throw new Exception("visitDbl is not yet implemented");
    }

    /** Interpret a Lambda value */
    @Override
    public IValue visitLambdaVal(Nodes.LambdaVal expr) throws Exception {
       //Get the definition
       Nodes.LambdaDef def = expr.lambda;
       //check parameter
       for(int i = 0; i < def.formals.size(); i++){
        //visitIdentifer() will check the existence of parameter for us
        IValue check = def.formals.get(i).visitValue(this);
       }
       //Perform actions
	   for(int i = 0; i < def.body.size(); i++){
		IValue res = def.body.get(i).visitValue(this);
		if(i == def.body.size()-1){
			return res;
		}
	   }
	   return null;
    }

    /**
     * Interpret a Lambda definition by creating a Lambda value from it in the
     * current environment
     */
    @Override
    public IValue visitLambdaDef(Nodes.LambdaDef expr) throws Exception {
        /**
         * make a inner encironment for the lambdaVal
         * makeInner() will make current 'env' as the outer of 'inner'
         * Then once we need to get any value in the enviornment we can just
         * call inner.get() in visitLambdaVal() since it will check the outer
         * environment for us as well. So we do not need to explictily pass
         * every value in the 'env' to inner, which including everything(built-in
         * func and defined identifier)
         */
        Env inner = env.makeInner(env);
        //Then create a new LambdaVal node
        var res = new Nodes.LambdaVal(inner, expr);
		//Create a new evaluator with inner environment
		ExprEvaluator evaluator = new ExprEvaluator(inner);
        //Call visit LambdaVal to get result
        return res.visitValue(evaluator);
    }

    /** Interpret an If expression */
    @Override
    public IValue visitIf(Nodes.If expr) throws Exception {
        //visit value of condition 
        IValue condition = expr.cond.visitValue(this);
        //evaluate condition
        if(condition instanceof Nodes.Bool == false){
            throw new Exception("reult of Condition is not a boolean");
        }
        if(((Nodes.Bool)condition).val){
            return expr.ifTrue.visitValue(this);
        }
        else{
            return expr.ifFalse.visitValue(this);
        }
    }

    /**
     * Interpret a set! special form. As with Define, this isn't an expression,
     * so it can return null
     */
    @Override
    public IValue visitSet(Nodes.Set expr) throws Exception {
        //same to define but use update instead
        String name = expr.identifier.name;
        IValue express = expr.expression.visitValue(this);
        //this can easily goes wrong, if identifier is not defined and outer scope is null.
        //I add a few line in Env to throw an error message
        env.update(name, express);
        return null;
    }
    /**
     * And and Or are based on the result of gsi. AND will return false only if one of the expressions is false
     * Or will return the result of any other expressions before bool and ignore the rest of expression after true
     */
    /** Interpret an And expression */
    @Override
    public IValue visitAnd(Nodes.And expr) throws Exception {
       for(int i = 0; i < expr.expressions.size(); i++){
            Nodes.BaseNode node = expr.expressions.get(i);
            IValue res = node.visitValue(this);
            //if any of it is false, return false
            if(res instanceof Nodes.Bool){
                if(((Nodes.Bool) res).val == false){
                    return res;
                }
            }
            //return last element
            if(i == expr.expressions.size() - 1){
                return res;
            }
       }
       return null;
    }

    /** Interpret an Or expression */
    @Override
    public IValue visitOr(Nodes.Or expr) throws Exception {
        for(int i = 0; i < expr.expressions.size(); i++){
            Nodes.BaseNode node = expr.expressions.get(i);
            IValue res = node.visitValue(this);
            //if any of it is not false, return it
            if(res instanceof Nodes.Bool){
                if(((Nodes.Bool) res).val == true){
                    return res;
                }
            }
            else{
                return res;
            }
       }
       //if all elements are false, return false
       return env.poundF.visitValue(this);
    }

    /** Interpret a Begin expression */
    @Override
    public IValue visitBegin(Nodes.Begin expr) throws Exception {
        IValue last = null;
        for(int i = 0 ; i < expr.expressions.size(); i++){
            //visit every expression
            Nodes.BaseNode node = expr.expressions.get(i);
            IValue res = node.visitValue(this);
            //return the last one
            if(i == expr.expressions.size()-1){
                last = res;
            }
        }
        return last;
    }

    /** Interpret a "not special form" expression */
    @Override
    public IValue visitApply(Nodes.Apply expr) throws Exception {
        //take out the name of function
        Nodes.BaseNode fun = expr.expressions.get(0);
        expr.expressions.remove(0);
        //make a list of IValue
        List<IValue> args = new ArrayList<>();
        for(int i = 0; i < expr.expressions.size(); i ++){
            args.add(expr.expressions.get(i).visitValue(this));
        }
        //get bult-in func from environment
        IValue built_in = fun.visitValue(this);
        if(built_in == null){
            throw new Exception("Not a built in function");
        }
        return ((Nodes.BuiltInFunc) built_in).func.execute(args);
    }

    /** Interpret a Cons value */
    @Override
    public IValue visitCons(Nodes.Cons expr) throws Exception {
        return expr;
        //throw new Exception("visitCons is not yet implemented");
    }

    /** Interpret a Vec value */
    @Override
    public IValue visitVec(Nodes.Vec expr) throws Exception {
        return expr;
        //throw new Exception("visitVec is not yet implemented");
    }

    /** Interpret a Symbol value */
    @Override
    public IValue visitSymbol(Nodes.Symbol expr) throws Exception {
        return expr;
        //throw new Exception("visitSymbol is not yet implemented");
    }

    /** Interpret a Quote expression */
    @Override
    public IValue visitQuote(Nodes.Quote expr) throws Exception {
        return expr.datum;
    }

    /** Interpret a quoted datum expression */
    @Override
    public IValue visitTick(Nodes.Tick expr) throws Exception {
        return expr.datum;
    }

    /** Interpret a Char value */
    @Override
    public IValue visitChar(Nodes.Char expr) throws Exception {
        return expr;
        //throw new Exception("visitChar is not yet implemented");
    }

    /** Interpret a Str value */
    @Override
    public IValue visitStr(Nodes.Str expr) throws Exception {
        return expr;
        //throw new Exception("visitStr is not yet implemented");
    }

    /** Interpret a Built-In Function value */
    @Override
    public IValue visitBuiltInFunc(Nodes.BuiltInFunc expr) throws Exception {
        return expr;
        //throw new Exception("visitBuiltInFunc is not yet implemented");
    }

    /** Interpret a Cons expression */
    @Override
    public IValue visitCond(Nodes.Cond expr) throws Exception {
        for(int i = 0 ; i < expr.conditions.size(); i++){
            var task = expr.conditions.get(i);
            var test = task.test;
            IValue test_res = test.visitValue(this);
            if(test_res instanceof Nodes.Bool == false){
                throw new Exception("Not a testable condition");
            }
            //test true or the last expression
            if(((Nodes.Bool)test_res).val == true || i == expr.conditions.size()-1){
                var actions = task.expressions;
                for(int j = 0; j < actions.size(); j ++){
                    IValue action = actions.get(j).visitValue(this);
                    if(j == actions.size()-1){
                        return action;
                    }
                }
            }
        }
        return null;
    }
}