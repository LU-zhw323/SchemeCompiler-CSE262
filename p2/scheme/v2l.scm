;; vector2list takes a vector and returns a list, without using `vector->list`
(define (vector2list vec) 
;;An inner function to perform recursion
;;Similar to cv2s
    (let f((acc '()) 
            (vec vec))
        ;;Basecase where there is no element in vector
        (if (equal? 0 (vector-length vec))
            acc
            ;;Append the 1st element in the subvector to list, and take a subvector from the next index
            (f (append acc (list (vector-ref vec 0))) (subvector vec 1 (vector-length vec)))
        ) 
    )
)