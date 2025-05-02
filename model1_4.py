import hashlib
stored_hashes = {
    "5c97a12eae8f6747c5d1c9d2a9f89e31421bdc45",  
    "a5d6f1c60c4be4b755f4b99b7c3a4ff219f79939"
}

paper_text = input("Enter the student's research paper content:\n")

hash_object = hashlib.sha1(paper_text.encode())
paper_hash = hash_object.hexdigest()

print("\nSHA-1 hash of the research paper:")
print(paper_hash)

if paper_hash in stored_hashes:
    print("\n This research paper has already been submitted. Possible duplication detected.")
else:
    print("\n This research paper is original. No duplicates found.")
