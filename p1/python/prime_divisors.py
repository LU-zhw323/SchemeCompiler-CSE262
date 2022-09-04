# prime_divisors: compute the prime factorization of a number

def prime_divisors(n):
    list=[]
    #For input less than 2, we just add it to the list
    if(n < 2):
        list.append(n)
        return list
    
    for x in range(2, int(n/2 + 1)):
        #if we can still factorize n, we will continue
        while(n % x == 0):
            #if we dont have duplicate item in list, just add it
            if(x not in list):
                list.append(x)
            n /= x
    # check if we still have remainder
    if(n > 1):
        list.append(n)

    return list



    