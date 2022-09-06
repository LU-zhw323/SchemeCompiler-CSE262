package edu.lehigh.cse262.p1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.io.*;

/**
 * ReadList is a wrapper class around the function `read`
 */
public class ReadList<T> {
  /**
   * Read from stdin until EOF is encountered, and put all of the values into a
   * list. The order in the list should be the reverse of the order in which the
   * elements were added.
   * 
   * @return A list with the values that were read
   */
  List<T> read() {
    //Contributor:Zhenyu Wu


    //Create a list with type string to use the scanner to read stdin
    List<T> my_list = new ArrayList<>();
    //Create scanner object
    Scanner sc = new Scanner(System.in);
    //A Function to convert string to T
    Function<String, T> convert = a -> (T)a;
    //Start reading from stdin
    while(sc.hasNext()){
      //Use nextline()function to grab entire line of input
      String read = sc.nextLine();
      //Use add()function to add each reading at the index 0 of the list after converting
      my_list.add(0,convert.apply(read));
    }
    sc.close();




    

    //Use casting to return a T type of list
    return my_list;
  }

}