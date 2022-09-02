package edu.lehigh.cse262.p1;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;

/**
 * App is the entry point into our program. You are allowed to add fields and
 * methods to App. You may also add `import` statements.
 */
public class App {
    public static <T> void main(String[] args) {
        System.out.println("CSE 262 Project 1");
        
        ReadList reader = new ReadList();
        List<T> my_list = new ArrayList<>();
        my_list = reader.read();
        System.out.println(my_list);
        /* 
        MyReverse list = new MyReverse<>();
        System.out.println(list.reverse(my_list));
    
        List<Integer> my_list2 = new ArrayList<>();
        for(int i = 2; i < 10; i++){
            my_list2.add(i);
        }
        Function<Integer, Double> func = a -> a / 2.0;
        MyMap<T> my_map = new MyMap<>();
        System.out.println(my_map.map((List<T>)my_list2, (Function<T,T>)func));
        */
        
    }
}
