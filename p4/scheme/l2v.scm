;; list2vector takes a list and returns a vector, without using `list->vector`
;;basically just modify the one from p3, replace 'let' with 'lambda'
(define (list2vector l) 
    (define f (lambda (acc arg pos)
            (if (eq? pos (length arg))
                acc
                (begin
                    (vector-set! acc pos (list-ref l pos))
                    (f acc l (+ pos 1)) 
                )
            )
        )
    )
    (f (make-vector (length l) '()) l 0)
)