;; substring-wildcard is like contains-substring, but it understands
;; single-character wildcards in the pattern string.  Wildcards are represented
;; by the ? character.  Note that this is a slightly broken way of doing
;; wildcards: the '?' character cannot be matched exactly.
;;
;; Here's an example execution: 
;; (contains-substring "hello" "e?lo") <-- returns true
;; (contains-substring "hello" "yell") <-- returns false
;; (contains-substring "The quick brown fox jumps over lazy dogs" "q?ick") <-- returns true
;;
;; You should implement this on your own, by comparing one character at a time,
;; and should not use any string comparison functions that are provided by gsi.

;; TODO: implement this function
(define (contains-substring source pattern) 
    ;;create local variable
    ;;acc -> accumulate string
    ;;query-> query string
    (let ((acc "") (query pattern))
        (begin
            ;;
            ;;Inner function that do the recursion
            ;;it includes flag which used to indicate if we are accumulating the acc or not
            ;;
            (let f((acc acc)
                (source source)
                (pattern pattern)
                (flag #f))
                ;;
                ;;Basecase where either one of them are "" and we check the content of acc
                ;;
                (if (or (equal? 0 (string-length source)) (equal? 0 (string-length pattern)))
                    (if (equal? acc query)
                        #t
                        acc
                    )
                    (if (equal? (string-ref source 0) (string-ref pattern 0))
                        (f (string-append acc (substring source 0 1)) (substring source 1 (string-length source)) (substring pattern 1 (string-length pattern)) #t)
                        (if (equal? flag #t)
                            (if (equal? #\? (string-ref pattern 0))
                                (f (string-append acc (substring pattern 0 1)) (substring source 1 (string-length source)) (substring pattern 1 (string-length pattern)) #t)
                                (f "" (substring source 1 (string-length source)) (string-append acc pattern) #f)
                            )
                            (f acc (substring source 1 (string-length source)) pattern #f)
                        )
                    )
                )     
            )
        )

    )
)