;; list2vector takes a list and returns a vector, without using `list->vector`
(define (list2vector l) 
    (let f((acc (make-vector (length l) #\a)) 
            (l l) 
            (pos 0))
        (vector-set! acc pos (list-ref l pos))
        (if (eq? pos (length l))
            acc
            (f acc l (+ pos 1))
        )
    
    
    
    
    
    
    
    
    )
)