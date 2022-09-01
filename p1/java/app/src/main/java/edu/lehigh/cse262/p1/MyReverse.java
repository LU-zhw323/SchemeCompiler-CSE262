package edu.lehigh.cse262.p1;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;


/** MyReverse is a wrapper class around the function `reverse` */
public class MyReverse<T> {
  /**
   * Return a list that has all of the elements of `in`, but in reverse order
   * 
   * @param in The list to reverse
   * @return A list that is the reverse of `in`
   */
  List<T> reverse(List<T> in) {
    
    //Use a for loop to go through and reverse the list
    for(int i = 0, j = in.size() - 1; i < j ; i++){
      //By adding the last element at the head of list and removing the last element at the same time
      //which would hold the size the list and reverse the order
      in.add(i, in.remove(j));
    }


    return in;
  }
}
