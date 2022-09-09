;; tree: A binary tree, implemented as a "closure"
;;
;; The tree should support the following methods:
;; - 'ins x      - Insert the value x into the tree
;; - 'clear      - Reset the tree to empty
;; - 'inslist l  - Insert all the elements from list `l` into the tree
;; - 'display    - Use `display` to print the tree
;; - 'inorder f  - Traverse the tree using an in-order traversal, applying
;;                 function `f` to the value in each non-null position
;; - 'preorder f - Traverse the tree using a pre-order traversal, applying
;;                 function `f` to the value in each non-null position
;;
;; Note: every method should take two arguments (the method name and a
;; parameter).  If a method is defined as not using any parameters, you
;; should still require a parameter, but your code can ignore it.
;;
;; Note: You should implement the tree as a closure.  One of the simplest
;; examples of a closure that acts like an object is the following:
;;
;; (define (make-my-ds)
;;   (let ((x '())) (lambda (msg arg)
;;       (cond ((eq? msg 'set) (set! x arg) 'ok) ((eq? msg 'get) x) (else 'error)))))
;;
;; In that example, I have intentionally *not* commented anything.  You will
;; need to figure out what is going on there.  If it helps, consider the
;; following sequence:
;;
;; (define ds (make-my-ds)) ; returns nothing
;; (ds 'get 'empty)         ; returns '()
;; (ds 'set 0)              ; returns 'ok
;; (ds 'get 'empty)         ; returns 0
;; (ds 'do 3)               ; returns 'error
;;
;; For full points, your implementation should be *clean*.  That is, the only
;; global symbol exported by this file should be the `make-bst` function.

;; Questions:
;;   - How do you feel about closures versus objects?  Why?
;;   - How do you feel about defining a tree node as a generic triple?
;;   - Contrast your experience solving this problem in Java, Python, and
;;     Scheme.

(define (make-bst)
  

  (let ((tree '()) (size 0))
    ;;The value of tree
    (define (value tree) (cadr tree))

    ;;The left branch
    (define (left tree) (car tree))

    ;;The right branch
    (define (right tree) (caddr tree))

    ;;Method that make a node with value left right which return a (value left right) list
    (define (make-tree value left right) (list left value right))

    
    (define (ins x tree)
      (cond
        ;;No node, make one(basecase of recursion)
        ((eq? tree '())
          (make-tree x '() '())
        )
        
       ;;equal value, return
        ((= x (value tree)) tree)

        ;;go to left, make a new tree with new left branch
        ((< x (value tree)) (make-tree (value tree) (ins x (car tree)) (right tree)))

        ;;go to right, same mechanism as left
        ((> x (value tree)) (make-tree (value tree) (left tree) (ins x (right tree))))
      )
    )

    (define (clean)
      ;;since the whole tree is based on a list, just make that list null
      (set! tree '())
    )

    (define (inslist lst)
      ;;basecase
      (if (eq? lst '())
      '()
      ;;recursively call the ins function and inslist, break the list into
      ;;2 parts: 1st element and rest of the list, each time we add the element we take
      ;;out from the list recursively to the tree that formed by previous element that has been
      ;;added to the tree, similar method in my_reverse
      (ins (car lst) (inslist (cdr lst))))
    )
   
    

    (define (inorder f tree)
      ;;basecase
      (if (eq? tree '())
        '()
        (append (inorder (left tree))
                ;;Since the value of node is only stored in the (value tree) which is the 
                ;;cadr of tree node, we just apply f on (value tree)
                (list (f (value tree)))
                (inorder (right tree))))
      )


    (define (preorder f tree)
      ;;same idea with inorder but different traversal order
      (if (eq? tree '())
        '()
        (append (list (f (value tree)))
                (preorder (left tree))
                (preorder (right tree))))
    )


    (define (display)
      tree
    )


    ;;(define (dis) (set! size (+ size 1)))

    ;;lambda expression to call method
    (lambda(msg arg)
      (cond
        ((eq? msg 'display) (display))
        ((eq? msg 'ins) (set! tree (ins arg tree)) tree)
        ((eq? msg 'inslist) (set! tree (inslist (list 1 2 3 4))) tree)
        ((eq? msg 'clean) (clean))
        ((eq? msg 'inorder) (inorder f tree))
        ((eq? msg 'preorder) (preorder f tree))
      )
    )
  
  
  
  
  
  )
)

