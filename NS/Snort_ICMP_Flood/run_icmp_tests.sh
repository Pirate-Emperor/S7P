#!/bin/bash

# Testcase 2: Testing ICMP ISS Pinger
echo "Executing Testcase 2: Testing ICMP ISS Pinger"
ping -p "495353504e475251" -w 5 127.0.0.0 &

# Testcase 3: Testing ICMP L3retriever Ping
echo "Executing Testcase 3: Testing ICMP L3retriever Ping"
ping -p "4142434445464748494a4b4c4d4e4f5051525354555657584142434445464748494a4b4c4d4e4f50" -w 5 127.0.0.0 &

# Testcase 4: Testing ICMP Nemesis v1.1 Echo
echo "Executing Testcase 4: Testing ICMP Nemesis v1.1 Echo"
ping -p "00000000000000000000000000000000000000000000000000" -w 5 127.0.0.0 &

# Testcase 5: Testing ICMP PING NMAP
echo "Executing Testcase 5: Testing ICMP PING NMAP"
ping -s 0 -w 5 127.0.0.0 &

# Testcase 6: Testing ICMP icmpenum v1.1.1
echo "Executing Testcase 6: Testing ICMP icmpenum v1.1.1"
ping -i 666 -s 0 -w 5 127.0.0.0 &

# Testcase 7: Testing ICMP Redirect Host
echo "Executing Testcase 7: Testing ICMP Redirect Host"
sudo hping3 --icmp --icmp-icode 1 --icmp-type 5 -w 5 127.0.0.0 &

# Testcase 8: Testing ICMP Redirect Net
echo "Executing Testcase 8: Testing ICMP Redirect Net"
sudo hping3 --icmp --icmp-icode 0 --icmp-type 5 -w 5 127.0.0.0 &

# Testcase 9: Testing ICMP Superscan Echo
echo "Executing Testcase 9: Testing ICMP Superscan Echo"
ping -p "0000000000000000" -w 5 127.0.0.0 &

# Testcase 10: Testing ICMP Traceroute IP Options
echo "Executing Testcase 10: Testing ICMP Traceroute IP Options"
sudo traceroute -I 127.0.0.0 -w 5 &

# Testcase 11: Testing ICMP Webtrends Scanner
echo "Executing Testcase 11: Testing ICMP Webtrends Scanner"
ping -p "00000000EEEEEEEEEEEE" -w 5 127.0.0.0 &

# Wait for all background processes to finish
wait

echo "All tests executed."
