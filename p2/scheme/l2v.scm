;; list2vector takes a list and returns a vector, without using `list->vector`
(define (list2vector l) 
    ;;An inner function to perform recursion
    (let f((acc (make-vector (length l) '())) 
            (l l) 
            (pos 0))
        ;;Each time I increment the position index until it hits the last element
        (if (eq? pos (length l))
            acc
            ;;Set the corresponding value in the list to vector, then do recursion
            (begin
                (vector-set! acc pos (list-ref l pos))
                (f acc l (+ pos 1))
            )
        ) 
    )
)