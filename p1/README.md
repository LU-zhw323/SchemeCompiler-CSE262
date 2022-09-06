# CSE 262 Assignment 1

The purpose of this assignment is to ensure that you are familiar with the three
programming languages that we will use in this class: Java, Python, and Scheme.
Among the goals of this assignment are:

* To make sure you have a proper development environment for using these
  languages
* To introduce you to these languages, if you haven't used them before
* To introduce you to some features of these languages that you may not have
  seen before
* To get you thinking about how to program idiomatically

## Parts of the Assignment

This assignment has *four* parts, which are contained in three sub-folders:
`java`, `python`, and `scheme`.  Three tasks are similar: in Java, Python, and
Scheme, you will implement five "programs":

* `read_list` -- Read a list of values from stdin and put them in a list
* `reverse` -- Reverse a list, without using any built-in list functions
* `map` -- Apply a function to all elements of a list, without using any
  built-in map functions
* `tree` -- Implement a binary tree
* `prime_divisors`-- Factor an integer into its prime divisors

The README file in each sub-folder has some more information about programming
in each of these languages.

The *fourth* part of the assignment is to answer the questions at the end of
this file.

## Development Environments

I strongly encourage you to use Visual Studio Code as your development
environment.  It has good plug in support for Java and Python, and reasonable
support for Scheme.  This support is not just syntax highlighting, but also code
formatting, refactoring, code completion, and tooltips.  It will help you to
write better code in less time.

VSCode also has two very important features for this assignment: VSCode Remote
and Live Share.  If you do not want to install Java, Python, and Scheme on your
computer, you can use the sunlab, and with VSCode Remote, you can use VSCode to
connect to sunlab.  It's very nice.  If you choose to work in a team of two,
Live Share will make it much easier to pair program.

## Teaming

You may work in teams of two for this assignment.  If you choose to work in a
team, you should **pair program**.  You should not split the assignment.  You
will not be able to succeed in this class if you do not understand everything in
this assignment.  Furthermore, if you split the work, you and your teammate will
wind up having to solve the same hard problems, which means you'll do 100% of
the work for each step.  In contrast, if you pair program, things you figure out
in Java won't need to be re-learned in Python, so you'll do only about 50% of
the work for Python... that savings adds up!

If you wish to work in a team, you must email Prof Spear <spear@lehigh.edu>.
Your email must follow these rules:

1. You must cc your project partner, so that I know that both team members
are aware of the team request.
2. You must tell me which team member's repository you will be working in.

I will change the permissions on that repository, so that both students can
read and write to it.  You will not need to submit the assignment twice.

## Documentation

You are **required** to follow the documentation instructions
that accompany each part of the assignment.  Correct code that does not have
documentation will not receive full points.

**DO NOT FORGET THE QUESTIONS AT THE END OF THIS FILE**

## Deadlines

This assignment is due by 11:59 PM on Friday, September 9th.  You should have
received this assignment by using `git` to `clone` a repository onto the machine
where you like to work.  You can use `git add`, `git commit`, and `git push` to
submit your work.

You are strongly encouraged to proceed *incrementally*: as you finish parts of
the assignment, `commit` and `push` them.

## Start Early

You should not wait until the last minute to start this assignment.  Start
early, and stop often.  This strategy will maximize your learning and minimize
your stress.  I promise.

## Questions

Please be sure to answer all of the following questions by writing responses in
this document.

### Read List

* Did you run into any trouble using `let`?  Why?
  * Actully, I didn't run into any trouble with 'let'
* What happens if the user enters several values on one line?
  1. In scheme, it is okey for user to enter several values on one line, and the code will still insert each read into the list  
  2. In java, it will assume that all the values on one line is a single element that we will put into the list  
    (probably because they are assumed to be a single string instead of seperate values)  
  3. In python, exactly same as it in java  
* What happens if the user enters non-integer values?
  * All three of them, they perform well if user input non-integer values, I convert the input in java from string to T and insert into 
    a list accept T, in python it is allowed that we put different vairbales with different types into a list, in scheme, I don't think there is a regulation on the types of elements in a list(probably like python)  
* Contrast your experience solving this problem in Java, Python, and Scheme.
  * Just like I mentioned, Java requires us to put a certain type of data into a certain type of list, which gave me the worst
    experience of putting different types of variables into a single list(I don't know any solution to that issue instead of hard coding with cating and type check), but for python and scheme, they do support a list with different types of variable, no need for 
    type checking and converting  


### Reverse

* What is tail recursion?
* Is your code tail recursive?
* How would you write a test to see if Scheme is applying tail recursion
  optimizations?
* Contrast your experience solving this problem in Java, Python, and Scheme.

### Map

* What kinds of values can be in `l`?
* What are the arguments to the function `func`?
* Why is this function built into scheme when it's so simple to write?
* Contrast your experience solving this problem in Java, Python, and Scheme.

### Tree

* How do you feel about closures versus objects?  Why?
* How do you feel about defining a tree node as a generic triple?
* Contrast your experience solving this problem in Java, Python, and Scheme.

### Prime Divisors

* Why did you choose the Scheme constructs that you chose in order to solve this
  problem?
* Contrast your experience solving this problem in Java, Python, and Scheme.
