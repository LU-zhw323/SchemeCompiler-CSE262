;; Compute the exclusive or of two values, using only and, or, and not
;;
;; xor should always return a boolean value
(define (xor a b) 
    (and (not (and a b)) (or a b))
)
;(not (and a b)) is true if a b are different or both false
;(or a b) is true if one of them is true
;combine them with 'and' will return true only if they are different