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
                        #f
                    )
                    ;;
                    ;;check if the 1st chararcter of the substring after reducing string is equal
                    ;;
                    (if (equal? (string-ref source 0) (string-ref pattern 0))
                        ;;
                        ;;if so, reduce both string and starting accumulate acc
                        ;;
                        (f (string-append acc (substring source 0 1)) (substring source 1 (string-length source)) (substring pattern 1 (string-length pattern)) #t)
                        ;;
                        ;;if not, check if we are currently in the building process
                        ;;
                        (if (equal? flag #t)
                            ;;
                            ;;if building and the chararcter if not matching, abandon current acc and reform the pattern string
                            ;;
                            (f "" (substring source 1 (string-length source)) (string-append acc pattern) #f)
                            ;;
                            ;;if not building, just reduce the source string
                            ;;
                            (f acc (substring source 1 (string-length source)) pattern #f)
                        )
                    )
                )     
            )
        )

    )
)