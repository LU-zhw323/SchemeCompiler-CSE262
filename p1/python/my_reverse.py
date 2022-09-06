# my_reverse: reverse a list without using the python `reverse` function

def my_reverse(l):
    #Contributor: Zhenyu Wu


    
    # I tried to use the iterator to go through the whole list, but I found out that using
    # simple for loop is a more quick way of doing that
    for x in range(l.__len__()):
        # Use the pop() to remove the last element and return it, then use the insert() to
        # put it at the head
        l.insert(x , l.pop())
    return l
