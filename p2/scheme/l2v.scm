;; list2vector takes a list and returns a vector, without using `list->vector`
(define (list2vector list) 
    (let f((acc (make-vector (length list) '()))
            (list list))
            ;;Base case where we return accumulate string
            (if(equal? 0 (length list))
                acc
                ;;Recursion, each time we add the head of current vector to string and pop out the head from the current string
                (f () (sublist list 1 (length list)))
            )
                
    )
)