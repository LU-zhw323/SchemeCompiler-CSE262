;; simple-hash creates a basic hash *set* of strings.  It uses the "method
;; receiver" style that you have seen previously: `make-hash` returns a function
;; that closes over some state; that function takes two arguments (a symbol and
;; a string).
;;
;; The argument to make-hash is a number: it should be positive.  It will be the
;; size of the "bucket vector" for your hash table.
;;
;; Your hash table should be a vector of lists.  Three operations can be
;; requested:
;; - 'contains string - Returns true if the string is in the hash set, false
;;   otherwise
;; - 'insert string - Returns true if the string was inserted into the hash set,
;;   false if it was alread there.
;; - 'remove string - Returns true if the string was removed from the hash set,
;;   false if it was not present to begin with.
;;
;; Here's an example execution:
;; (define my-hash (make-hash 32))
;; (my-hash 'insert "hello") <-- returns true
;; (my-hash 'contains "world") <-- returns false
;; (my-hash 'contains "hello") <-- returns true
;; (my-hash 'insert "hello") <-- returns false
;; (my-hash 'remove "world") <-- returns false
;; (my-hash 'remove "hello") <-- returns true
;; (my-hash 'remove "hello") <-- returns false
;; (my-hash 'contains "hello") <-- returns false
;;
;; To "hash" input strings, you should use the (very simple) djb2 function from
;; <http://www.cse.yorku.ca/~oz/hash.html>


;; TODO: implement this function
(define (make-hash size) 
    (let ((hash (make-vector size '())))


        ;;Hash function perfrom djb2
        (define (prehash hash str)
            (let ((hash_val 5831))
                (let f((hash_val hash_val)
                    (hash hash)
                    (str str))
                    (if (equal? 0 (string-length str))
                        (modulo hash_val (vector-length hash))
                        (f (+ (* 33 hash_val) (char->integer (string-ref str 0))) hash (substring str 1 (string-length str)))
                    )
                ) 
            )
            
        )



        ;;Contain function
        (define (contain hash str)
            (let ((hash_val (prehash hash str)))
                (let f((bucket (vector-ref hash hash_val))
                    (str str))
                    (if (equal? 0 (length bucket))
                        #f
                        (if (equal? str (list-ref bucket 0))
                            #t
                            (f (sublist 1 (length bucket)) str)
                        )
                    )
                )
            )
        )

        ;;Insert function
        (define (insert hash str)
            (let ((hash_val (prehash hash str)))
                (if (equal? #f (contain hash str))
                    (begin
                        (vector-set! hash hash_val (append (vector-ref hash hash_val) (list str)))
                        (contain hash str)
                    )
                    #f
                )  
            )
        )

         ;;Remove function
        (define (removes hash str)
            (let ((hash_val (prehash hash str)))
                (if (equal? #t (contain hash str))
                    (begin
                        (vector-set! hash hash_val (deleteItem (vector-ref hash hash_val) str))
                        #t
                    )
                    #f
                )  
            )
        )

    
        ;;Helper function to remove item from a list
        (define (deleteItem lst item)
            (cond ((null? lst)
                    '())
                    ((equal? item (car lst))
                        (cdr lst))
                    (else
                        (cons (car lst) 
                            (deleteItem (cdr lst) item)))))
    
    
    
    
    
    
    
    
    
    
        ;;lambda expression to call method
        (lambda(msg arg)
        (cond
            ((eq? msg 'insert) (insert hash arg))
            ((eq? msg 'contains) (contain hash arg))
            ((eq? msg 'remove) (removes hash arg))
        )
        )
    )


)