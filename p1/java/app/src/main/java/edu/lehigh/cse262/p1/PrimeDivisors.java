package edu.lehigh.cse262.p1;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/** PrimeDivisors is a wrapper class around the function `computeDivisors` */
public class PrimeDivisors {
  /**
   * Compute the prime divisors of `value` and return them as a list
   *
   * @param value The value whose prime divisors are to be computed
   * @return A list of the prime divisors of `value`
   */
  List<Integer> computeDivisors(int value) {

    //Contributor:Zhenyu Wu

    
    List<Integer> list = new ArrayList<>();
    //if value is less than 2, just add it
    if(value < 2){
      list.add(value);
      return list;
    }
    //Start from 2 to value/2
    for(int i = 2 ; i <= value/2; i++){
      //loop til we have a remainder of division
      while(value % i == 0){
        //check if it has duplicate element
        if(list.contains(i) == false){
          list.add(i);
        }
        value/=i;
      }
    }
    //check if there is still a remainder after all devision
    if(value > 1){
      list.add(value);
    }
    

    return list;
  }
}
