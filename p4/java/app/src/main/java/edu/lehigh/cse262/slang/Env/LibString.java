package edu.lehigh.cse262.slang.Env;

import java.util.HashMap;

import edu.lehigh.cse262.slang.Parser.IValue;
import edu.lehigh.cse262.slang.Parser.Nodes;
import java.util.List;
import java.util.ArrayList;

/**
 * The purpose of LibString is to implement all of the standard library
 * functions that we can do on Strings
 */
public class LibString {
    /**
     * Populate the provided `map` with a standard set of string functions
     */
    public static void populate(HashMap<String, IValue> map, Nodes.Bool poundT, Nodes.Bool poundF) {
        /*
         * String lib is also simple, basically just 1.check argument 2.perform string operation
         */
        var string_append = new Nodes.BuiltInFunc("string-append", (List<IValue> args)->{
            if(args.size() != 2){
                throw new Exception("string-append expects two argument");
            }
            if(args.get(0) instanceof Nodes.Str == false || args.get(1) instanceof Nodes.Str == false){
                throw new Exception("string-append expect string");
            }
            String res = ((Nodes.Str)args.get(0)).val + ((Nodes.Str)args.get(1)).val;
            return new Nodes.Str(res);
        });
        map.put(string_append.name, string_append);

        var string_length = new Nodes.BuiltInFunc("string-length", (List<IValue> args)->{
            if(args.size() != 1){
                throw new Exception("string-length expects one argument");
            }
            if(args.get(0) instanceof Nodes.Str == false){
                throw new Exception("string-length expect string");
            }
            int len = ((Nodes.Str)args.get(0)).val.length();
            return new Nodes.Int(len);
        });
        map.put(string_length.name, string_length);

        var string_check = new Nodes.BuiltInFunc("string?", (List<IValue> args)->{
            if(args.size() != 1){
                throw new Exception("string? expects one argument");
            }
            if(args.get(0) instanceof Nodes.Str == false){
                return poundF;
            }
            return poundT;
        });
        map.put(string_check.name, string_check);

        var string_ref = new Nodes.BuiltInFunc("string-ref", (List<IValue> args)->{
            if(args.size() != 2){
                throw new Exception("string-ref expects one argument");
            }
            if(args.get(0) instanceof Nodes.Str == false){
                throw new Exception("string-ref expect string");
            }
            if(args.get(1) instanceof Nodes.Int == false){
                throw new Exception("string-ref expect int as index");
            }
            String val = ((Nodes.Str)args.get(0)).val;
            int index = ((Nodes.Int)args.get(1)).val;
            if(index < 0 || index > val.length()-1){
                throw new Exception("string-ref Index out of bound");
            }
            return new Nodes.Char(val.charAt(index));
        });
        map.put(string_ref.name, string_ref);

        var string_equal = new Nodes.BuiltInFunc("string-equal?", (List<IValue> args)->{
            if(args.size() != 2){
                throw new Exception("string-equal? expects one argument");
            }
            if(args.get(0) instanceof Nodes.Str == false){
                throw new Exception("string-equal? expect string");
            }
            if(args.get(1) instanceof Nodes.Str == false){
                throw new Exception("string-equal? expect string");
            }
            String val = ((Nodes.Str)args.get(0)).val;
            String val2 = ((Nodes.Str)args.get(1)).val;
            if(val.equals(val2)){
                return poundT;
            }
            return poundF;
        });
        map.put(string_equal.name, string_equal);

        var string_substring = new Nodes.BuiltInFunc("string-substring", (List<IValue> args)->{
            if(args.size() != 3){
                throw new Exception("string-substring expects three argument");
            }
            if(args.get(0) instanceof Nodes.Str == false){
                throw new Exception("string-substring expect string");
            }
            if(args.get(1) instanceof Nodes.Int == false){
                throw new Exception("string-substring expects int as index");
            }
            if(args.get(2) instanceof Nodes.Int == false){
                throw new Exception("string-substring expects int as index");
            }
            String temp = ((Nodes.Str)args.get(0)).val;
            int start = ((Nodes.Int)args.get(1)).val;
            if(start < 0 || start > temp.length()-1){
                throw new Exception("string-substring Index out of bound");
            }
            int end = ((Nodes.Int)args.get(2)).val;
            if(end < 0 || end > temp.length()-1){
                throw new Exception("string-substring Index out of bound");
            }
            return new Nodes.Str(temp.substring(start,end));
            
        });

        var string_make = new Nodes.BuiltInFunc("string", (List<IValue> args)->{
            if(args.size() < 1)
                throw new Exception("string expects at least one argument");
            String res = "";
            for(var arg:args){
                if(arg instanceof Nodes.Char == false)
                    throw new Exception("string expects character");
                char temp = ((Nodes.Char)arg).val;
                res += temp;
            }
            return new Nodes.Str(res);
            
        });
        map.put(string_make.name, string_make);
    }
}
