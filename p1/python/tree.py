# tree: A binary tree, implemented as a class
#
# The tree should support the following methods:
# - ins(x)      - Insert the value x into the tree
# - clear()     - Reset the tree to empty
# - inslist(l)  - Insert all the elements from list `l` into the tree
# - display()   - Use `display` to print the tree
# - inorder(f)  - Traverse the tree using an in-order traversal, applying
#                 function `f` to the value in each non-null position
# - preorder(f) - Traverse the tree using a pre-order traversal, applying
#                 function `f` to the value in each non-null position







class tree:

    #Contributor: Zhenyu Wu

    size = 0
    
    # Use the __init__ fuction to initialize the tree by passing the value as the value of root
    #, and the it will initialize all other variables within the class(eg. left, right)
    def __init__(self, value):
        self.left = None
        self.right = None
        self.val = value
        self.size += 1
        self.root = True
        
    

    def ins(self, x):
        self.size += 1
        # where there is no node, then we create one and return
        if self is None:
            tree(x)
            return
        else:
            # Prevent duplicate node
            if self.val == x:
                return
            # Going right
            elif self.val < x:
                # If there is already a right node, call it self again and going right
                if self.right:
                    self.right.ins(x)
                    return
                # Where there is no right node, we create one and return
                else:
                    self.right = tree(x)
                    self.right.root = False
                    return
            # Going left
            else:
                # If there is already a left node, call it self again and going left
                if self.left:
                    self.left.ins(x)
                    return
                # Where there is no left node, we create one and return
                else:
                    self.left = tree(x)
                    self.left.root = False
                    return
        return
    
    def clean(self):
        #check if we hit the left most node
        if self.left is not None:
            #if not, going left
            self.left.clean()
        #check if we hit the right most node
        if self.right is not None:
            #if not, going right
            self.right.clean()
        #hit the deepest node, clean the tree
        self.val = None
        size -= 1

    #apply the ins()for all element
    def inslist(self, l):
        for element in l:
            self.ins(element)
        return
    
    
    #print list in postorder
    def display(self):
        list = []
        if self.left is not None:
            self.left.display()
        if self.right is not None:
            self.right.display()
        if self.val is not None:
            list.append(self.val)

        print(list)
        #if(self.root == False):
            #print("NO")
            #return list
        #else:
            #print(list)
        
    
    def inorder(self, func):
        if self.left is not None:
            self.left.inorder(func)
        #Perform the func in base case
        if self.val is not None:
            self.val = func(self.val)
        if self.right is not None:
            self.right.inorder(func)

    
    def preorder(self, func):
        #Perform the func in base case
        if self.val is not None:
            self.val = func(self.val)
        if self.left is not None:
            self.left.preorder(func)
        if self.right is not None:
            self.right.preorder(func)
        
        