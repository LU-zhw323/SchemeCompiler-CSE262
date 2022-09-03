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
    List<Integer> list = new ArrayList<>();
    if(value < 2){
      list.add(value);
      return list;
    }
    for(int i = 2 ; i <= value/2; i++){
      while(value % i == 0){
        if(list.contains(i) == false){
          list.add(i);
        }
        value/=i;
      }
    }

    if(value > 1){
      list.add(value);
    }
    

    return list;
  }
}
