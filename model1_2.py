#!/usr/bin/env python3

import os
from Crypto.PublicKey import DSA
from Crypto.Hash import SHA256
from Crypto.Signature import DSS
import base64

def generate_keys():
    key = DSA.generate(2048)
    priv_path = input("Save private key as (e.g. candidate_priv.pem): ").strip()
    pub_path  = input("Save public key  as (e.g. candidate_pub.pem):  ").strip()

    with open(priv_path, 'wb') as f:
        f.write(key.export_key('PEM'))
    with open(pub_path, 'wb') as f:
        f.write(key.publickey().export_key('PEM'))

    print(f"\nPrivate key saved to {priv_path}")
    print(f"Public  key saved to {pub_path}")

def sign_resume():
    resume_path = input("Resume file to sign (e.g. resume.txt): ").strip()
    priv_path   = input("Candidate's private key (e.g. candidate_priv.pem): ").strip()
    sig_path    = input("Save signature as (e.g. resume.sig): ").strip()

    if not os.path.exists(resume_path) or not os.path.exists(priv_path):
        print("\nFile not found.")
        return

    data = open(resume_path, 'rb').read()
    h = SHA256.new(data)
    key = DSA.import_key(open(priv_path,'rb').read())
    signer = DSS.new(key, 'fips-186-3')
    signature = signer.sign(h)
    with open(sig_path,'w') as f:
        f.write(base64.b64encode(signature).decode())

    print(f"\nResume signed and signature saved to {sig_path}")

def verify_resume():
    resume_path = input("Resume file to verify (e.g. resume.txt): ").strip()
    sig_path    = input("Signature file        (e.g. resume.sig): ").strip()
    pub_path    = input("Candidate's public key(e.g. candidate_pub.pem): ").strip()

    if not all(os.path.exists(p) for p in (resume_path, sig_path, pub_path)):
        print("\nFile not found.")
        return

    data      = open(resume_path, 'rb').read()
    signature = base64.b64decode(open(sig_path,'r').read())
    h = SHA256.new(data)
    key = DSA.import_key(open(pub_path,'rb').read())
    verifier = DSS.new(key, 'fips-186-3')

    try:
        verifier.verify(h, signature)
        print("\nSignature valid: resume is authentic.")
    except ValueError:
        print("\nSignature invalid: resume may have been altered.")

def main():
    print("Choose action:")
    print("1) Generate DSA key pair")
    print("2) Sign resume")
    print("3) Verify resume signature")
    choice = input("Enter [1-3]: ").strip()

    if choice == '1':
        generate_keys()
    elif choice == '2':
        sign_resume()
    elif choice == '3':
        verify_resume()
    else:
        print("\n Invalid choice.")

if __name__ == "__main__":
    main()
