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
;;90% is exactly the same as contains-substring
(define (contains-substring source pattern)
    (let ((acc "") (query pattern))
        (begin
            (let f((acc acc)
                (source source)
                (pattern pattern)
                (flag #f))
                (if (or (equal? 0 (string-length source)) (equal? 0 (string-length pattern)))
                    (if (equal? acc query)
                        #t
                        #f
                    )
                    (if (equal? (string-ref source 0) (string-ref pattern 0))
                        (f (string-append acc (substring source 0 1)) (substring source 1 (string-length source)) (substring pattern 1 (string-length pattern)) #t)
                        ;;The only change is to determine if we have a wildcard character in pattern
                        ;;if so just accumulate to acc and keep going
                        (if (equal? #\? (string-ref pattern 0))
                            (f (string-append acc (substring pattern 0 1)) (substring source 1 (string-length source)) (substring pattern 1 (string-length pattern)) #t)
                            (if (equal? flag #t)
                                (f "" (substring source 1 (string-length source)) (string-append acc pattern) #f)
                                (f acc (substring source 1 (string-length source)) pattern #f)
                            )
                        )
                    )
                )     
            )
        )

    )
)