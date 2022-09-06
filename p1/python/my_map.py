# my_map: apply a function to every element in a list, and return a list
# that holds the results.
#
# Your implementation of this function is not allowed to use the built-in
# `map` function.

def my_map(func, l):
    #Contributor: Zhenyu Wu
    list = []
    #Iterate through the whole list
    for element in l:
        #Apply the function to each element and append the result to 
        # a new list
        list.append(func(element))
    return list
