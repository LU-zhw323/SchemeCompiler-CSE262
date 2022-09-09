;; prime_divisors: compute the prime factorization of a number
;;
;; To test this function, open a new `gsi` instance and then type:
;;  (load "prime_divisors.scm")
;; Then you can issue commands such as:
;;  (prime-divisors 60)
;; And you should see results of the form:
;;  (2 2 3 5)

;; This is a skeleton for the prime-divisors function.  For now, it just
;; returns #f (false)
;;
;; Note that you will almost certainly want to write some helper functions,
;; and also that this will probably need to be a recursive function.  You are
;; not required to use good information hiding.  That is, you may `define`
;; other functions in the global namespace and use them from
;; `prime-divisors`.
(define (prime-divisors num)
  ;;Contributor: Zhenyu Wu

  ;;Initially I want to put those prime factors into the list 'div', but then I find out it is unnecessary to do so
  ;;Just ignore the let
  (letrec(
    (div '())
    )
    ;;helper function that actually do the work, it will take in the number and
    ;;factor
    (define (factor n f)
      ;;if num is 1, which means we hit the basecase where we factorize the number to 1
      (if (= n 1)
        '()
        ;;if the result of mod n and f is zero, which means that f is one of the
        ;;prime factor of number, we append f to the list, then call the helper function again
        ;;but pass in number as n/f and current f to see that if we can use the current factor to
        ;;factorize number continously
        (if(= (remainder n f) 0)
          (append (list f) (factor (/ n f) f))

          ;;if not, it means that the current factor is not what we are looking for, we increment it
          ;;and call the helper function again by passing the current number and f+1, we will continue
          ;;this process til we find a prime factor for current number
          (factor n (+ f 1))
        )
      )
      ;;if number is less than 1, just return it
      (if (< n 1)
        (append n '())
      
      )
    )
    ;;call the helper function and start factorize number with 2
    (factor num 2)

  )  

)





  
