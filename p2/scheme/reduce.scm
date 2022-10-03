;; reduce takes a binary function, a list, and an identity value, and computes
;; the result of repeatedly applying that function
;;
;; Example: (reduce + '(1 2 3) 0) ==> 6
;;
;; Example: (reduce * '() 1) ==> 1
(define (reduce op l identity)
    ;;Inner function to do recursion
    (let f((acc identity);;assign accumulate result to be identity
            (op op)
            (l l)
            (identity identity))
        ;;basecase where the list is empty, we return accumulate result
        (if (null? l)
            acc
            ;;Each time perform operation on the 1st element in the list, and pass the rest of the list to perform
            (f (op acc (car l)) op (cdr l) identity)
           
        )
    )


)