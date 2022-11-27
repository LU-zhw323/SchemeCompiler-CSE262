package edu.lehigh.cse262.slang.Env;

import java.util.HashMap;

import edu.lehigh.cse262.slang.Parser.IValue;
import edu.lehigh.cse262.slang.Parser.Nodes;
import java.util.List;
import java.util.ArrayList;

/**
 * The purpose of LibVector is to implement all of the standard library
 * functions that we can do on vectors
 */
public class LibVector {
    /**
     * Populate the provided `map` with a standard set of vector functions
     */
    public static void populate(HashMap<String, IValue> map, Nodes.Bool poundT, Nodes.Bool poundF) {
        /*
         * All the codes are easy, so I will not comment too much on them
         */
        var vector_length = new Nodes.BuiltInFunc("vector-length", (List<IValue> args)->{
            if(args.size() != 1){
                throw new Exception("vector-length expect one argument");
            }
            if(args.get(0) instanceof Nodes.Vec == false){
                throw new Exception("vector-length expect vector");
            }
            return new Nodes.Int(((Nodes.Vec)args.get(0)).items.length);
        });
        map.put(vector_length.name, vector_length);

        var vector_get = new Nodes.BuiltInFunc("vector-get", (List<IValue> args)->{
            if(args.size() != 2){
                throw new Exception("vector-get expect two argument");
            }
            if(args.get(0) instanceof Nodes.Vec == false){
                throw new Exception("vector-get expect vector");
            }
            if(args.get(1) instanceof Nodes.Int == false){
                throw new Exception("vector-get expect integer as index");
            }
            if(((Nodes.Int)args.get(1)).val > ((Nodes.Vec)args.get(0)).items.length-1 || ((Nodes.Int)args.get(1)).val < 0)
                throw new Exception("vector-get Index out of bound");
            return ((Nodes.Vec)args.get(0)).items[((Nodes.Int)args.get(1)).val];
        });
        map.put(vector_get.name, vector_get);

        var vector_set = new Nodes.BuiltInFunc("vector-set!", (List<IValue> args)->{
            if(args.size() != 3){
                throw new Exception("vector-set! expect three argument");
            }
            if(args.get(0) instanceof Nodes.Vec == false){
                throw new Exception("vector-set! expect vector");
            }
            if(args.get(1) instanceof Nodes.Int == false){
                throw new Exception("vector-set! expect integer as index");
            }
            if(((Nodes.Int)args.get(1)).val > ((Nodes.Vec)args.get(0)).items.length-1 || ((Nodes.Int)args.get(1)).val < 0)
                throw new Exception("vector-get Index out of bound");
            ((Nodes.Vec)args.get(0)).items[((Nodes.Int)args.get(1)).val] = args.get(2);
            //just like set!, it should return null
            return null;
        });
        map.put(vector_set.name, vector_set);

        var vector_make = new Nodes.BuiltInFunc("vector", (List<IValue> args)->{
            if(args.size() < 1){
                throw new Exception("vector expect at least one argument");
            }
            List<IValue> body = new ArrayList<>();
            for(var arg:args){
                body.add(arg);
            }
            return new Nodes.Vec(body);
        });
        map.put(vector_make.name, vector_make);

        var vector_check = new Nodes.BuiltInFunc("set!-car", (List<IValue> args)->{
            if(args.size() != 1){
                throw new Exception("vector? expects one argument");
            }
            if(args.get(0) instanceof Nodes.Vec == false){
                return poundF;
            }
            return poundT;
        });
        map.put(vector_check.name, vector_check);
    }
}
