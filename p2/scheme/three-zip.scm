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
    ;;A helper function to determine if the current list only has one element left
    ;;if so return list itself(with only 1 element)
    ;;if not return the rest of the list by cdr
    (define (find-cdr l)
        (if (null? (cdr l))
            l
            (cdr l)
        )
    )
    ;;Inner function that do the recursion
    (let f((l1 l1)
            (l2 l2)
            (l3 l3)
            (acc '()))
        ;;Basecase where three lists all have only 1 element left-->return the accumulated list after cons the last elements in each list
        ;;to the accumulated list, also reverse the order
        (if (and (and (null? (cdr l1)) (null? (cdr l2))) (null? (cdr l3)))
            (reverse (cons (list (car l1) (car l2) (car l3)) acc))
            ;;Recursion where we firstly call the 'find-cdr' to slice the list until all of them only have 1 element left
            ;;Then, we cons the first element in the sliced list to the accumulated list. If one of them still has more than one elements
            ;;left, it will cons the last elements from two who have already reach the last one to the 1st element from whom haven't reach 
            ;;the end
            (f (find-cdr l1) (find-cdr l2) (find-cdr l3) (cons (list (car l1) (car l2) (car l3)) acc))
        )
      
    );let

)