package edu.lehigh.cse262.slang.Env;

import java.util.HashMap;
import java.util.List;

import edu.lehigh.cse262.slang.Parser.IValue;
import edu.lehigh.cse262.slang.Parser.Nodes;
import java.util.ArrayList;
import java.lang.Math;

/**
 * The purpose of LibMath is to implement all of the standard library functions
 * that we can do on numbers (Integer or Double)
 */
public class LibMath {
    /**
     * Populate the provided `map` with a standard set of mathematical functions
     */
    public static void populate(HashMap<String, IValue> map, Nodes.Bool poundT, Nodes.Bool poundF) {
        // As a starting point, let's go ahead and put the addition function
        // into the map. This will make it **much** easier to test `apply`, and
        // should provide some useful guidance for making other functions.
        //
        // Note that this code is **very** tedious. Making some helper
        // functions would probably be wise, but it's up to you to figure out
        // how.
        var add = new Nodes.BuiltInFunc("+", (List<IValue> args) -> {
            // Type checking: make sure we only have int and dbl arguments. We also will use
            // this to know if we should be returning an Int or a Dbl
            int intCount = 0;
            int dblCount = 0;
            for (var arg : args) {
                if (arg instanceof Nodes.Int)
                    intCount++;
                if (arg instanceof Nodes.Dbl)
                    dblCount++;
            }
            if (args.size() > (intCount + dblCount))
                throw new Exception("+ can only handle Int and Dbl arguments");
            // Semantic analysis: make sure there are arguments!
            if (args.size() == 0)
                throw new Exception("+ expects at least one argument");
            // Compute, making sure to know the return type
            if (dblCount > 0) {
                double result = 0;
                for (var arg : args) {
                    if (arg instanceof Nodes.Int)
                        result += ((Nodes.Int) arg).val;
                    else
                        result += ((Nodes.Dbl) arg).val;
                }
                return new Nodes.Dbl(result);
            } else {
                int result = 0;
                for (var arg : args) {
                    result += ((Nodes.Int) arg).val;
                }
                return new Nodes.Int(result);
            }
        });

        map.put(add.name, add);

        /**
         * -
         */ 
        var minus = new Nodes.BuiltInFunc("-", (List<IValue> args)->{
            if (args.size() == 0)
                throw new Exception("- expects at least one argument");
            int type = type_check(args);
            //case for not purely int and double
            if(type == -1){
                throw new Exception("- can only handle int and double");
            }
            //case for return double
            else if(type == 1){
                double res = 0;
                if(args.get(0) instanceof Nodes.Int)
                    res = (double)(((Nodes.Int) args.get(0)).val);
                else
                    res = ((Nodes.Dbl) args.get(0)).val;
                if(args.size() == 1){
                    double Res = 0 - res;
                    return new Nodes.Dbl(Res);
                }
                args.remove(0);
                for(var arg:args){
                    if (arg instanceof Nodes.Int)
                        res -= ((Nodes.Int) arg).val;
                    else
                        res -= ((Nodes.Dbl) arg).val;
                }
                return new Nodes.Dbl(res);
            }
            //case for return int
            else{
                int res = ((Nodes.Int) args.get(0)).val;
                if(args.size() == 1){
                    int Res = 0 - res;
                    return new Nodes.Int(Res);
                }
                args.remove(0);
                for(var arg:args){
                    res -= ((Nodes.Int) arg).val;
                }
                return new Nodes.Int(res);
            }
        });
        map.put(minus.name, minus);

        /**
         * *
         */
        var multiply = new Nodes.BuiltInFunc("*", (List<IValue> args)->{
            if (args.size() == 0)
                throw new Exception("* expects at least one argument");
            int type = type_check(args);
            //case for not purely int and double
            if(type == -1){
                throw new Exception("* can only handle int and double");
            }
            //case for return double
            else if(type == 1){
                double res = 0;
                if(args.get(0) instanceof Nodes.Int)
                    res = (double)(((Nodes.Int) args.get(0)).val);
                else
                    res = ((Nodes.Dbl) args.get(0)).val;
                args.remove(0);
                for(var arg:args){
                    if (arg instanceof Nodes.Int)
                        res *= ((Nodes.Int) arg).val;
                    else
                        res *= ((Nodes.Dbl) arg).val;
                }
                return new Nodes.Dbl(res);
            }
            //case for return int
            else{
                int res = ((Nodes.Int) args.get(0)).val;
                args.remove(0);
                for(var arg:args){
                    res *= ((Nodes.Int) arg).val;
                }
                return new Nodes.Int(res);
            }
        });
        map.put(multiply.name, multiply);

        /*
         * /
         */
        var devide = new Nodes.BuiltInFunc("/", (List<IValue> args)->{
            if (args.size() == 0)
                throw new Exception("/ expects at least one argument");
            int type = type_check(args);
            //case for not purely int and double
            if(type == -1){
                throw new Exception("/ can only handle int and double");
            }
            //case for return double(devide should return double)
            else{
                double res = 0;
                if(args.get(0) instanceof Nodes.Int)
                    res = (double)(((Nodes.Int) args.get(0)).val);
                else
                    res = ((Nodes.Dbl) args.get(0)).val;
                if(args.size() == 1){
                    double Res = 1 / res;
                    return new Nodes.Dbl(Res);
                }
                args.remove(0);
                for(var arg:args){
                    if (arg instanceof Nodes.Int)
                        res /= ((Nodes.Int) arg).val;
                    else
                        res /= ((Nodes.Dbl) arg).val;
                }
                return new Nodes.Dbl(res);
            }
        });
        map.put(devide.name, devide);


        /*
         * %
         */
        var modulo = new Nodes.BuiltInFunc("%", (List<IValue> args)->{
            if (args.size() == 0)
                throw new Exception("% expects at least one argument");
            else if(args.size() != 2)
                throw new Exception("% only handle 2 arguments");
            int type = type_check(args);
            //case for not int (I restrick the parameter to be int only(mentioned on piazza))
            if(type == -1 || type == 1){
                throw new Exception("% can only handle int");
            }
            //case for return int
            else{
                int res = ((Nodes.Int) args.get(0)).val % ((Nodes.Int) args.get(1)).val;
                return new Nodes.Int(res);
            }
        });
        map.put(modulo.name, modulo);


        /*
         * ==
         */
        var equal = new Nodes.BuiltInFunc("==", (List<IValue> args)->{
            if (args.size() == 0)
                throw new Exception("== expects at least one argument");
            int type = type_check(args);
            //case for not int or double
            if(type == -1){
                throw new Exception("== can only handle int or double");
            }
            //case for return int
            else{
                return test_list("==", args);
            }
        });
        map.put(equal.name, equal);

        /*
         * <
         */
        var less = new Nodes.BuiltInFunc("<", (List<IValue> args)->{
            if (args.size() == 0)
                throw new Exception("< expects at least one argument");
            int type = type_check(args);
            //case for not int or double
            if(type == -1){
                throw new Exception("< can only handle int or double");
            }
            //case for return int
            else{
                return test_list("<", args);
            }
        });
        map.put(less.name, less);


        /*
         * >
         */
        var greater = new Nodes.BuiltInFunc(">", (List<IValue> args)->{
            if (args.size() == 0)
                throw new Exception("> expects at least one argument");
            int type = type_check(args);
            //case for not int or double
            if(type == -1){
                throw new Exception("> can only handle int or double");
            }
            //case for return int
            else{
                return test_list(">", args);
            }
        });
        map.put(greater.name, greater);


        /*
         * >=
         */
        var greater_equal = new Nodes.BuiltInFunc(">=", (List<IValue> args)->{
            if (args.size() == 0)
                throw new Exception(">= expects at least one argument");
            int type = type_check(args);
            //case for not int or double
            if(type == -1){
                throw new Exception(">= can only handle int or double");
            }
            //case for return int
            else{
                return test_list(">=", args);
            }
        });
        map.put(greater_equal.name, greater_equal);

        /*
         * <=
         */
        var less_equal = new Nodes.BuiltInFunc("<=", (List<IValue> args)->{
            if (args.size() == 0)
                throw new Exception("<= expects at least one argument");
            int type = type_check(args);
            //case for not int or double
            if(type == -1){
                throw new Exception("<= can only handle int or double");
            }
            //case for return int
            else{
                return test_list("<=", args);
            }
        });
        map.put(less_equal.name, less_equal);
        

        /*
         * abs
         */
        var abs = new Nodes.BuiltInFunc("abs", (List<IValue> args)->{
            if (args.size() != 1)
                throw new Exception("abs expects one argument");
            int type = type_check(args);
            //case for not int or double
            if(type == -1){
                throw new Exception("abs can only handle int or double");
            }
            //case for return int or double
            else{
                if(type == 0){
                    return new Nodes.Int((int)perform_math("abs",args));
                }
                else{
                    return new Nodes.Dbl(perform_math("abs",args));
                }
            }
        });
        map.put(abs.name, abs);


        /*
         * sqrt
         */
        var sqrt = new Nodes.BuiltInFunc("sqrt", (List<IValue> args)->{
            if (args.size() != 1)
                throw new Exception("sqrt expects one argument");
            int type = type_check(args);
            //case for not int or double
            if(type == -1){
                throw new Exception("sqrt can only handle int or double");
            }
            //case for return double
            else{
                return new Nodes.Dbl(perform_math("sqrt",args));
            }
        });
        map.put(sqrt.name, sqrt);

        /*
         * acos
         * For some of the math opertaion, it will return NaN, infinity, -infinity.
         * Initially, I add several handle process(throw expception)
         * Since those are math constants included in our lib, so I deleted the handle process
         * just return the result.
         */
        var acos = new Nodes.BuiltInFunc("acos", (List<IValue> args)->{
            if (args.size() != 1)
                throw new Exception("acos expects one argument");
            int type = type_check(args);
            //case for not int or double
            if(type == -1){
                throw new Exception("acos can only handle int or double");
            }
            //case for return double
            else{
                return new Nodes.Dbl(perform_math("acos",args));
            }
        });
        map.put(acos.name, acos);


        /*
         * asin
         */
        var asin = new Nodes.BuiltInFunc("asin", (List<IValue> args)->{
            if (args.size() != 1)
                throw new Exception("asin expects one argument");
            int type = type_check(args);
            //case for not int or double
            if(type == -1){
                throw new Exception("asin can only handle int or double");
            }
            //case for return double
            else{
                return new Nodes.Dbl(perform_math("asin",args));
            }
        });
        map.put(asin.name, asin);


        /*
         * atan
         */
        var atan = new Nodes.BuiltInFunc("atan", (List<IValue> args)->{
            if (args.size() != 1)
                throw new Exception("atan expects one argument");
            int type = type_check(args);
            //case for not int or double
            if(type == -1){
                throw new Exception("atan can only handle int or double");
            }
            //case for return double
            else{
                return new Nodes.Dbl(perform_math("atan",args));
            }
        });
        map.put(atan.name, atan);


        /*
         * cos
         */
        var cos = new Nodes.BuiltInFunc("cos", (List<IValue> args)->{
            if (args.size() != 1)
                throw new Exception("cos expects one argument");
            int type = type_check(args);
            //case for not int or double
            if(type == -1){
                throw new Exception("cos can only handle int or double");
            }
            //case for return double
            else{
                return new Nodes.Dbl(perform_math("cos",args));
            }
        });
        map.put(cos.name, cos);


        /*
         * cosh
         */
        var cosh = new Nodes.BuiltInFunc("cosh", (List<IValue> args)->{
            if (args.size() != 1)
                throw new Exception("cosh expects one argument");
            int type = type_check(args);
            //case for not int or double
            if(type == -1){
                throw new Exception("cosh can only handle int or double");
            }
            //case for return double
            else{
                return new Nodes.Dbl(perform_math("cosh",args));
            }
        });
        map.put(cosh.name, cosh);


        /*
         * sin
         */
        var sin = new Nodes.BuiltInFunc("sin", (List<IValue> args)->{
            if (args.size() != 1)
                throw new Exception("sin expects one argument");
            int type = type_check(args);
            //case for not int or double
            if(type == -1){
                throw new Exception("sin can only handle int or double");
            }
            //case for return double
            else{
                return new Nodes.Dbl(perform_math("sin",args));
            }
        });
        map.put(sin.name, sin);


        /*
         * sinh
         */
        var sinh = new Nodes.BuiltInFunc("sinh", (List<IValue> args)->{
            if (args.size() != 1)
                throw new Exception("sinh expects one argument");
            int type = type_check(args);
            //case for not int or double
            if(type == -1){
                throw new Exception("sinh can only handle int or double");
            }
            //case for return double
            else{
                return new Nodes.Dbl(perform_math("sinh",args));
            }
        });
        map.put(sinh.name, sinh);


        /*
         * tan
         */
        var tan = new Nodes.BuiltInFunc("tan", (List<IValue> args)->{
            if (args.size() != 1)
                throw new Exception("tan expects one argument");
            int type = type_check(args);
            //case for not int or double
            if(type == -1){
                throw new Exception("tan can only handle int or double");
            }
            //case for return double
            else{
                return new Nodes.Dbl(perform_math("tan",args));
            }
        });
        map.put(tan.name, tan);

        /*
         * tanh
         */
        var tanh = new Nodes.BuiltInFunc("tanh", (List<IValue> args)->{
            if (args.size() != 1)
                throw new Exception("tanh expects one argument");
            int type = type_check(args);
            //case for not int or double
            if(type == -1){
                throw new Exception("tanh can only handle int or double");
            }
            //case for return double
            else{
                return new Nodes.Dbl(perform_math("tanh",args));
            }
        });
        map.put(tanh.name, tanh);


        /*
         * integer?
         */
        var is_int = new Nodes.BuiltInFunc("integer?", (List<IValue> args)->{
            if (args.size() != 1)
                throw new Exception("integer? expects one argument");
            int type = type_check(args);
            //case for not int or double
            if(type != 0){
                return new Nodes.Bool(false);
            }
            return new Nodes.Bool(true);
        });
        map.put(is_int.name, is_int);


        /*
         * double?
         */
        var is_dbl = new Nodes.BuiltInFunc("double?", (List<IValue> args)->{
            if (args.size() != 1)
                throw new Exception("double? expects one argument");
            int type = type_check(args);
            //case for not int or double
            if(type != 1){
                return new Nodes.Bool(false);
            }
            return new Nodes.Bool(true);
        });
        map.put(is_dbl.name, is_dbl);


        /*
         * number?
         */
        var is_number = new Nodes.BuiltInFunc("number?", (List<IValue> args)->{
            if (args.size() != 1)
                throw new Exception("number? expects one argument");
            int type = type_check(args);
            //case for not int or double
            if(type == -1){
                return new Nodes.Bool(false);
            }
            return new Nodes.Bool(true);
        });
        map.put(is_number.name, is_number);


        /*
         * symbol?
         */
        var is_symbol = new Nodes.BuiltInFunc("symbol?", (List<IValue> args)->{
            if (args.size() != 1)
                throw new Exception("symbol? expects one argument");
            if(args.get(0) instanceof Nodes.Symbol == false){
                return new Nodes.Bool(false);
            }
            return new Nodes.Bool(true);
        });
        map.put(is_symbol.name, is_symbol);

        /*
         * procedure?
         */
        var is_procedure = new Nodes.BuiltInFunc("procedure?", (List<IValue> args)->{
            if (args.size() != 1)
                throw new Exception("procedure? expects one argument");
            //I use gsi to test all the expresions, and it only return true with lambda
            if(args.get(0) instanceof Nodes.LambdaVal == false){
                return new Nodes.Bool(false);
            }
            return new Nodes.Bool(true);
        });
        map.put(is_procedure.name, is_procedure);


        /*
         * log10
         */
        var log10 = new Nodes.BuiltInFunc("log10", (List<IValue> args)->{
            if (args.size() != 1)
                throw new Exception("log10 expects one argument");
            int type = type_check(args);
            //case for not int or double
            if(type == -1){
                throw new Exception("log10 can only handle int or double");
            }
            //case for return double
            else{
                return new Nodes.Dbl(perform_math("log10",args));
            }
        });
        map.put(log10.name, log10);


        /*
         * loge
         */
        var loge = new Nodes.BuiltInFunc("loge", (List<IValue> args)->{
            if (args.size() != 1)
                throw new Exception("loge expects one argument");
            int type = type_check(args);
            //case for not int or double
            if(type == -1){
                throw new Exception("loge can only handle int or double");
            }
            //case for return double
            else{
                return new Nodes.Dbl(perform_math("loge",args));
            }
        });
        map.put(loge.name, loge);


        /*
         * pow
         */
        var pow = new Nodes.BuiltInFunc("pow", (List<IValue> args)->{
            if (args.size() != 2)
                throw new Exception("pow expects two arguments");
            int type = type_check(args);
            //case for not int or double
            if(type == -1){
                throw new Exception("pow can only handle int or double");
            }
            //case for return double
            else{
                return new Nodes.Dbl(perform_math("pow",args));
            }
        });
        map.put(pow.name, pow);


        /*
         * not
         */
        var not = new Nodes.BuiltInFunc("not", (List<IValue> args)->{
            if (args.size() != 1)
                throw new Exception("not expects one arguments");
            var temp = args.get(0);
            if(temp instanceof Nodes.Bool){
                if(((Nodes.Bool)temp).val == false){
                    return new Nodes.Bool(true);
                }
            }
            return new Nodes.Bool(false);
        });
        map.put(not.name, not);

        /*
         * and
         * I am confused about why we need to implement 'and' as a built-in func
         * since it is already a special form, and scanner will scan (and ....) 
         * as an 'AND token', and parsed as AND node. But I still implement it
         */
        var and = new Nodes.BuiltInFunc("and", (List<IValue> args)->{
            if (args.size() < 1)
                throw new Exception("and expects at least one arguments");
            for(int i = 0; i < args.size(); i++){
                IValue res = args.get(i);
                //if any of it is false, return false
                if(res instanceof Nodes.Bool){
                    if(((Nodes.Bool) res).val == false){
                        return res;
                    }
                }
                //return last element
                if(i == args.size() - 1){
                    return res;
                }
            }
            return new Nodes.Bool(true);
        });
        map.put(and.name, and);

        /*
         * or
         * same as and
         */
        var or = new Nodes.BuiltInFunc("or", (List<IValue> args)->{
            if (args.size() < 1)
                throw new Exception("or expects at least one arguments");
            for(int i = 0; i < args.size(); i++){
                IValue res = args.get(i);
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
            return new Nodes.Bool(false);
        });
        map.put(or.name, or);


        /*
         * integer->double
         */
        var int_double = new Nodes.BuiltInFunc("integer->double", (List<IValue> args)->{
            if (args.size() != 1)
                throw new Exception("integer->double expects one arguments");
            int type = type_check(args);
            //case for not int or double
            if(type != 0){
                throw new Exception("integer->double can only handle int");
            }
            int val = ((Nodes.Int)args.get(0)).val;
            return new Nodes.Dbl((double)val);
        });
        map.put(int_double.name, int_double);


        /*
         * double->integer
         */
        var double_int = new Nodes.BuiltInFunc("double->integer", (List<IValue> args)->{
            if (args.size() != 1)
                throw new Exception("double->integer expects one arguments");
            int type = type_check(args);
            //case for not int or double
            if(type != 1){
                throw new Exception("double->integer can only handle int");
            }
            double val = ((Nodes.Dbl)args.get(0)).val;
            return new Nodes.Int((int)val);
        });
        map.put(double_int.name, double_int);
        

        /*
         * null?
         */
        var Null = new Nodes.BuiltInFunc("null?", (List<IValue> args)->{
            if (args.size() != 1)
                throw new Exception("null? expects one arguments");
            var temp = args.get(0);
            //I just followed the explaination on piazza that return true if it is an empty list
            if(temp instanceof Nodes.Cons){
                if(((Nodes.Cons)temp).car == null && ((Nodes.Cons)temp).cdr == null){
                    return new Nodes.Bool(true);
                }
            }
            return new Nodes.Bool(false);
        });
        map.put(Null.name, Null);

        map.put("pi", new Nodes.Dbl(Math.PI));
        map.put("e", new Nodes.Dbl(Math.E));
        map.put("tau", new Nodes.Dbl(2*Math.PI));
        map.put("inf+", new Nodes.Dbl(Double.POSITIVE_INFINITY));
        map.put("inf-", new Nodes.Dbl(Double.NEGATIVE_INFINITY));
        map.put("nan", new Nodes.Dbl(Float.NaN));





    }
    /**
     * Helper function for type checking
     * @param args the list of argument
     * 
     * @return integer to suggest the typing situation(0 for all int; 1 for has double; -1 for has something else)
     */
    public static int type_check(List<IValue> args){
        for(var arg:args){
            if(arg instanceof Nodes.Int){
                continue;
            }
            else if(arg instanceof Nodes.Dbl){
                return 1;
            }
            else{
                return -1;
            }
        }
        return 0;
    }
    /**
     * Helper function for <, >, <= , >= , ==
     * @param operation the operation to perform
     * @param args the list of argument
     * 
     * @return a Nodes.Bool which sould be either true or false
     */
    public static Nodes.Bool test_list(String operation, List<IValue> args){
        double res = 0;
        if(args.get(0) instanceof Nodes.Int)
            res = (double)(((Nodes.Int) args.get(0)).val);
        else
            res = ((Nodes.Dbl) args.get(0)).val;
        args.remove(0);
        for(int i = 0; i < args.size(); i++){
            double temp = 0;
            if(args.get(i) instanceof Nodes.Int)
                temp = (double)(((Nodes.Int) args.get(i)).val);
            else
                temp = ((Nodes.Dbl) args.get(i)).val;
            if(operation.equals("<")){
                if(!(res < temp))
                    return new Nodes.Bool(false);
            }
            else if(operation.equals(">")){
                if(!(res > temp))
                    return new Nodes.Bool(false);
            }
            else if(operation.equals(">=")){
                if(!(res >= temp))
                    return new Nodes.Bool(false);
            }
            else if(operation.equals("<=")){
                if(!(res <= temp))
                    return new Nodes.Bool(false);
            }
            else{
                if(res != temp)
                    return new Nodes.Bool(false);
            }
            res = temp;
        }
        return new Nodes.Bool(true);
    }
    /**
     * Helper function for math operation
     * @param operation the operation to perform
     * @param args the list of argument
     * 
     * @return a double after performing operation
     */
    public static double perform_math(String operation, List<IValue> args) throws Exception{
        double temp = 0;
        if(args.get(0) instanceof Nodes.Int)
            temp = (double)(((Nodes.Int) args.get(0)).val);
        else
            temp = ((Nodes.Dbl) args.get(0)).val;
        if(operation.equals("abs")){
            return Math.abs(temp);
        }
        else if(operation.equals("sqrt")){
            return Math.sqrt(temp);
        }
        else if(operation.equals("acos")){
            return Math.acos(temp);
        }
        else if(operation.equals("asin")){
            return Math.asin(temp);
        }
        else if(operation.equals("atan")){
            return Math.atan(temp);
        }
        else if(operation.equals("cos")){
            return Math.cos(temp);
        }
        else if(operation.equals("cosh")){
            return Math.cosh(temp);
        }
        else if(operation.equals("sin")){
            return Math.sin(temp);
        }
        else if(operation.equals("sinh")){
            return Math.sinh(temp);
        }
        else if(operation.equals("tan")){
            return Math.tan(temp);
        }
        else if(operation.equals("tanh")){
            return Math.tanh(temp);
        }
        else if(operation.equals("log10")){
            return Math.log10(temp);
        }
        else if (operation.equals("loge")){
            return Math.log(temp);
        }
        else if(operation.equals("pow")){
            double Temp = 0;
            if(args.get(1) instanceof Nodes.Int)
                Temp = (double)(((Nodes.Int) args.get(1)).val);
            else
                Temp = ((Nodes.Dbl) args.get(1)).val;
            return Math.pow(temp, Temp);
        }
        return temp;
    }

}
