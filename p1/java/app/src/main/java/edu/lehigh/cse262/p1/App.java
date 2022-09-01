package edu.lehigh.cse262.p1;
import java.util.List;
import java.util.ArrayList;
/**
 * App is the entry point into our program. You are allowed to add fields and
 * methods to App. You may also add `import` statements.
 */
public class App {
    public static void main(String[] args) {
        System.out.println("CSE 262 Project 1");
        //ReadList reader = new ReadList();
        //System.out.println(reader.read());
        
        List<String> test = new ArrayList<>();
        test.add("x");
        test.add("y");
        test.add("z");
        MyReverse list = new MyReverse<>();
        System.out.println(list.reverse(test));

    }
}
