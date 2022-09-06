;; my_reverse: reverse a list without using the scheme `reverse` function
;;
;; Your implementation of this function can use special forms and standard
;; functions, such as `car`, `cdr`, `list`, `append`, and `if`, but it cannot
;; use the built-in `reverse` function.
;;
;; Your implementation should be recursive.

(define (my-reverse l)
  ;;Contributor：Zhenyu Wu


  ;; basecase where there is only null left in list(last element)
  (if(eq? '() l)
    ;; return '()
    '()
    ;; break the list into two sub-list: 1. list return from the basecase 2. list containing only the 1st element of l
    ;; then use append to join 2 sub-list for each recursion. since we need to reverse the list, so the return list should be in front of the list
    ;; of 1st element, therefore, we can't use cons which will make multiple nested list. General idea is to take out the 1st element from the 
    ;; list passing in, and make it on hold to join the rest of list til each the end of list
    (append (my-reverse (cdr l)) (list (car l)))
  )
)
