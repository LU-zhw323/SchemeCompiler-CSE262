# read_list: Read from the keyboard and put the results into a list.  The code
# should keep reading until EOF (control-d) is input by the user.
#
# The order of elements in the list returned by read_list should the reverse of
# the order in which they were entered.
from sys import stdin

def read_list():
    # create a list
    list = []
    # Start iterating til EOF
    while True:
        try:
            # Add sth to list
            list.insert(0, input())
        # Reach EOF
        except EOFError:
            break
    return list
