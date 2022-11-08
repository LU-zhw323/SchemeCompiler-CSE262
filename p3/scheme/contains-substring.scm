;; contains-substring checks if a string contains the given substring.  It does
;; not count how many times: it merely returns true or false.
;;
;; The first argument to contains-substring is the string to search
;; The second argument to contains-substring is the substring to try and fine
;;
;; Here's an example execution: 
;; (contains-substring "hello" "ello") <-- returns true
;; (contains-substring "hello" "yell") <-- returns false
;; (contains-substring "The quick brown fox jumps over lazy dogs" "ox") <-- returns true
;;
;; You should implement this on your own, by comparing one character at a time,
;; and should not use any string comparison functions that are provided by gsi.

;; TODO: implement this function
(define (contains-substring source pattern)
    (let f((acc "")
            (source source)
            (pattern pattern)
            (flag #f))
        (if (or (equal? 0 (string-length source)) (equal? 0 (string-length pattern)))
            acc
            (if (equal? (string-ref source 0) (string-ref pattern 0))
                (f (string-append acc (substring source 0 1)) (substring source 1 (string-length source)) (substring pattern 1 (string-length pattern)) #t)
                (if (equal? flag #t)
                    (f "" (substring source 1 (string-length source)) (string-append acc pattern) #f)
                    (f acc (substring source 1 (string-length source)) pattern #f)
                )
            )
        )     
    )
)