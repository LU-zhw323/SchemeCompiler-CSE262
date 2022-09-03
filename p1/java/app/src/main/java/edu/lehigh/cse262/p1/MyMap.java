package edu.lehigh.cse262.p1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/** MyMap is a wrapper class around the function `map` */
public class MyMap<T> {
  /**
   * Apply `func` to every element in `list`, and return a list containing the
   * results
   * 
   * @param list The list of elements that should be passed to func
   * @param func The function to apply to each element in the list
   * @return A list of the results
   */
  List<T> map(List<T> list, Function<T, T> func) {
    //List<T> my_map = new ArrayList<>();
    Iterator<T>it = list.iterator();
    //Dealing with method with no return value like system.out
    if(func.apply(it.next()) == null){
      list.forEach(a -> func.apply(a));
    }
    //Applying func to all element in the list
    else{
      list.replaceAll(a -> func.apply(a));
    }
    return list;
  }
}
