;; charvec2string takes a vector of characters and returns a string
(define (charvec2string cv)
    ;;A inner function to accumulate string to do tail recursion
    ;;acc-> accumulate string
    ;;cv-> vector after modify
    (let f((acc "")
            (cv cv))
        ;;Base case where we return accumulate string
        (if(equal? 0 (vector-length cv))
            acc
            ;;Recursion, each time we add the head of current vector to string and pop out the head from the current string
            (f (string-append acc (make-string 1 (vector-ref cv 0))) (subvector cv 1 (vector-length cv)))
        )
            
    )
)