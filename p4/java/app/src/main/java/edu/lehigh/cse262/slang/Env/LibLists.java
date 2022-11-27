package edu.lehigh.cse262.slang.Env;

import java.util.HashMap;

import edu.lehigh.cse262.slang.Parser.IValue;
import edu.lehigh.cse262.slang.Parser.Nodes;
import java.util.List;
import java.util.ArrayList;

/**
 * The purpose of LibLists is to implement all of the standard library functions
 * that we can do on Cons nodes
 */
public class LibLists {
    /**
     * Populate the provided `map` with a standard set of list functions
     */
    public static void populate(HashMap<String, IValue> map, Nodes.Bool poundT, Nodes.Bool poundF, Nodes.Cons empty) {
        var list_car = new Nodes.BuiltInFunc("car", (List<IValue> args)->{
            if(args.size() != 1)
                throw new Exception("car expects one argument");
            if(args.get(0) instanceof Nodes.Cons == false){
                throw new Exception("car expect cons cell");
            }
            return ((Nodes.Cons)args.get(0)).car;
        });
        map.put(list_car.name, list_car);

        var list_cdr = new Nodes.BuiltInFunc("cdr", (List<IValue> args)->{
            if(args.size() != 1)
                throw new Exception("cdr expects one argument");
            if(args.get(0) instanceof Nodes.Cons == false){
                throw new Exception("cdr expect cons cell");
            }
            return ((Nodes.Cons)args.get(0)).cdr;
        });
        map.put(list_cdr.name, list_cdr);

        var list_cons = new Nodes.BuiltInFunc("cons", (List<IValue> args)->{
            if(args.size() != 2)
                throw new Exception("cons expects two argument");
            return new Nodes.Cons(args.get(0), args.get(1));
        });
        map.put(list_cons.name, list_cons);

        var list_make = new Nodes.BuiltInFunc("list", (List<IValue> args)->{
            if(args.size() < 1)
                throw new Exception("list expects at least one argument");
            return new Nodes.Cons(args, empty);
        });
        map.put(list_make.name, list_make);

        var list_check = new Nodes.BuiltInFunc("list?", (List<IValue> args)->{
            if(args.size() != 1)
                throw new Exception("list? expects one argument");
            if(args.get(0) instanceof Nodes.Cons){
                return poundT;
            }
            return poundF;
        });
        map.put(list_check.name, list_check);

        var list_set_car = new Nodes.BuiltInFunc("set-car!", (List<IValue> args)->{
            if(args.size() != 2)
                throw new Exception("set-car expects two argument");
            if(args.get(0) instanceof Nodes.Cons == false){
                throw new Exception("set-car! expects cons");
            }
            ((Nodes.Cons)args.get(0)).car = args.get(1);
            //I guess it should return null just like set! and define
            return null;
        });
        map.put(list_set_car.name, list_set_car);

        var list_set_cdr = new Nodes.BuiltInFunc("set-cdr!", (List<IValue> args)->{
            if(args.size() != 2)
                throw new Exception("set-cdr expects two argument");
            if(args.get(0) instanceof Nodes.Cons == false){
                throw new Exception("set-cdr! expects cons");
            }
            ((Nodes.Cons)args.get(0)).cdr = args.get(1);
            //I guess it should return null just like set! and define
            return null;
        });
        map.put(list_set_cdr.name, list_set_cdr);

    }
}
