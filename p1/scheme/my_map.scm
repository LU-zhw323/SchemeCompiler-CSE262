;; my_map: apply a function to every element in a list, and return a list
;; that holds the results.
;;
;; Your implementation of this function is not allowed to use the built-in
;; `map` function.

(define (my-map func l)

  ;;Contributor: Zhenyu Wu


  ;;Same idea with my_reverse, just compute func on each iteam for each recursion
  ;;Basecase where there is only '() left in list, return
  (if(eq? '() l)
      '()
      ;;Recursivly apply the my-map on the 1st element of the list we passed in
      ;;and join the result with the result of list after recursivly applying the func
      ;;on the rest of list. Each time, we will compute the func on the 1st element, and tell
      ;;him to wait for we reach the end. Therefore, the general idea is to take 1 element out from
      ;;the list each time and apply func on it, and make it on hold waiting for a list returned with result in it

      (cons (func (car l)) (my-map func (cdr l)) )
  
  
  
  )  
)
