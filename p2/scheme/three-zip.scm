;; three-zip takes three lists, and returns a single list, where each entry in
;; the returned list is a list with three elements.
;;
;; The nth element of the returned list is a list containing the nth element of
;; the first list, the nth element of the second list, and the nth element of
;; the third list.
;;
;; Your implementation should be tail recursive.
;;
;; If the three lists do not have the same length, then your code should behave
;; as if all of the lists were as long as the longest list, by replicating the
;; last element of each of the short lists.
;;
;; Example: (three-zip '(1 2 3) '("hi" "bye" "hello") '(a b c))
;;          -> ('(1 "hi" a) '(2 "bye" b) '(3 "hello" c))
;;
;; Example: (three-zip '(1 2 3 4) '("hi" "bye" "hello") '(a b c))
;;          -> ('(1 "hi" a) '(2 "bye" b) '(3 "hello" c) '(4 "hello" c))
(define (three-zip l1 l2 l3) 
    (define (find-cdr l)
        (if (null? (cdr l))
            l
            (cdr l)
        )
    )

    (let f((l1 l1)
            (l2 l2)
            (l3 l3)
            (acc '()))
        (if (and (and (null? (cdr l1)) (null? (cdr l2))) (null? (cdr l3)))
            (reverse (cons (list (car l1) (car l2) (car l3)) acc))
            (f (find-cdr l1) (find-cdr l2) (find-cdr l3) (cons (list (car l1) (car l2) (car l3)) acc))
        )
      
    );let

)