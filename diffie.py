def mod_exp(base, exp, mod):
    """Function to perform modular exponentiation: (base^exp) % mod"""
    result = 1
    while exp > 0:
        if exp % 2 == 1:  
            result = (result * base) % mod
        base = (base * base) % mod  
        exp //= 2
    return result
p = 71  
g = 7   
xA = 5  
xB = 12 
yA = mod_exp(g, xA, p)  
yB = mod_exp(g, xB, p)  
KA = mod_exp(yB, xA, p)
KB = mod_exp(yA, xB, p)  
if KA == KB:
    print(f"Key Exchange Successful! Shared Secret Key: {KA}")
else:
    print("Key Exchange Failed!")
print(f"Alice's Public Key (yA): {yA}")
print(f"Bob's Public Key (yB): {yB}")
print(f"Shared Key Computed by Alice: {KA}")
print(f"Shared Key Computed by Bob: {KB}")
