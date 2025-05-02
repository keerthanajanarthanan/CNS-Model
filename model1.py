import hashlib
document = input("Enter the document to be signed:\n")

encoded_doc = document.encode()

sha1_hash = hashlib.sha1(encoded_doc)
signature = sha1_hash.hexdigest()

print("\n🔐 SHA-1 Digital Signature (Message Digest):")
print(signature)
