;; charvec2string takes a vector of characters and returns a string
(define (charvec2string cv)
    (let f((cv cv)
            (acc ""));f
        (if(equal? 0 (vector-length cv))
            acc
            (f (subvector cv 0 (- (vector-length cv) 1)) (string-append acc (make-string 1 (vector-ref x 0))))
        )
            
    );let


);function