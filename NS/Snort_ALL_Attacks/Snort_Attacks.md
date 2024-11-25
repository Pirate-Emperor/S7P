### Target IP for Local Machine
For a local machine, the most common IP addresses are:
- **Loopback IP**: `127.0.0.1` (localhost)
- **Local network IP**: This could be something like `192.168.x.x` if your local machine has a private IP address in your network.

For the sake of simplicity, we will assume `127.0.0.1` (localhost) as the target IP for most of the attacks in the example commands.

### Adjusting Attacks for Local Machine (Target IP: 127.0.0.1)

Here are the necessary configurations:

### 1. **Port Scanning (e.g., using nmap)**
   - **Target IP**: `127.0.0.1`
   - **Snort Rule**:
     ```snort
     alert tcp any any -> any 1:1023 (msg:"Port scan detected"; flags:S; threshold:type threshold, track by_src, count 10, seconds 1; sid:1000001;)
     ```
   - **Command**:
     ```bash
     nmap -p 1-1023 127.0.0.1
     ```

### 2. **SYN Flood (DoS Attack)**
   - **Target IP**: `127.0.0.1`
   - **Snort Rule**:
     ```snort
     alert tcp any any -> any any (msg:"SYN flood attack"; flags:S; threshold:type threshold, track by_src, count 100, seconds 1; sid:1000002;)
     ```
   - **Command**:
     ```bash
     sudo hping3 --syn --flood -p 80 127.0.0.1
     ```

### 3. **ICMP Flood (DoS Attack)**
   - **Target IP**: `127.0.0.1`
   - **Snort Rule**:
     ```snort
     alert icmp any any -> any any (msg:"ICMP flood detected"; threshold:type threshold, track by_src, count 100, seconds 1; sid:1000003;)
     ```
   - **Command**:
     ```bash
     sudo ping -f 127.0.0.1
     ```

### 4. **TCP/UDP Flood**
   - **Target IP**: `127.0.0.1`
   - **Snort Rule**:
     ```snort
     alert ip any any -> any any (msg:"TCP/UDP flood attack"; threshold:type threshold, track by_src, count 1000, seconds 1; sid:1000004;)
     ```
   - **Command**:
     ```bash
     sudo hping3 --flood --rand-source 127.0.0.1
     ```

### 5. **DNS Amplification Attack**
   - **Target IP**: `127.0.0.1`
   - **Snort Rule**:
     ```snort
     alert udp any any -> any 53 (msg:"DNS amplification attack detected"; ip_proto:17; threshold:type threshold, track by_src, count 1000, seconds 1; sid:1000005;)
     ```
   - **Command**:
     ```bash
     sudo hping3 --udp --flood --destport 53 127.0.0.1
     ```

### 6. **SSH Brute Force Attack**
   - **Target IP**: `127.0.0.1`
   - **Snort Rule**:
     ```snort
     alert tcp any any -> any 22 (msg:"SSH brute force detected"; threshold:type threshold, track by_src, count 10, seconds 30; sid:1000006;)
     ```
   - **Command**:
     ```bash
     hydra -l root -P /path/to/passwords.txt ssh://127.0.0.1
     ```

### 7. **XSS (Cross-Site Scripting) Attack**
   - **Target IP**: `127.0.0.1`
   - **Snort Rule**:
     ```snort
     alert tcp any any -> any 80 (msg:"XSS Attack Detected"; content:"<script>"; nocase; sid:1000007;)
     ```
   - **Command**:
     ```bash
     curl -X GET "http://127.0.0.1/page?input=<script>alert('XSS')</script>"
     ```

### 8. **SQL Injection Attack**
   - **Target IP**: `127.0.0.1`
   - **Snort Rule**:
     ```snort
     alert tcp any any -> any 80 (msg:"SQL Injection Attempt"; content:"' OR 1=1 --"; nocase; sid:1000008;)
     ```
   - **Command**:
     ```bash
     curl -X GET "http://127.0.0.1/search?q=' OR 1=1 --"
     ```

### 9. **Privilege Escalation Attempt**
   - **Target IP**: `127.0.0.1`
   - **Snort Rule**:
     ```snort
     alert tcp any any -> any 22 (msg:"Privilege escalation attempt detected"; content:"sudo"; sid:1000009;)
     ```
   - **Command**:
     ```bash
     sudo su
     ```

### 10. **Metasploit Framework Exploit**
   - **Target IP**: `127.0.0.1`
   - **Snort Rule**:
     ```snort
     alert tcp any any -> any any (msg:"Metasploit exploit detected"; content:"metasploit"; sid:1000010;)
     ```
   - **Command**:
     ```bash
     msfconsole
     ```

### 11. **Malicious File Transfer (e.g., using netcat)**
   - **Target IP**: `127.0.0.1`
   - **Snort Rule**:
     ```snort
     alert tcp any any -> any 4444 (msg:"Malicious file transfer detected"; content:"malicious_file"; sid:1000011;)
     ```
   - **Command**:
     ```bash
     nc -lvp 4444 > malicious_file
     ```

---

### **APIs/Applications Required for Certain Attacks:**
For the **SQL Injection** and **XSS** attacks, a basic web application is required to handle these inputs. Below are minimal examples of how to set up a local API/application to trigger these attacks:

#### 1. **SQL Injection (API Example)**

**Python Flask Application (Vulnerable to SQL Injection):**
```python
from flask import Flask, request
import sqlite3

app = Flask(__name__)

# Initialize the database (use SQLite for simplicity)
conn = sqlite3.connect('vulnerable.db')
c = conn.cursor()
c.execute('CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, name TEXT, password TEXT)')
c.execute("INSERT INTO users (name, password) VALUES ('admin', 'admin123')")
conn.commit()
conn.close()

@app.route('/search')
def search():
    user_input = request.args.get('q')
    conn = sqlite3.connect('vulnerable.db')
    c = conn.cursor()
    query = f"SELECT * FROM users WHERE name = '{user_input}'"
    c.execute(query)
    result = c.fetchall()
    conn.close()
    return str(result)

if __name__ == '__main__':
    app.run(host='127.0.0.1', port=80)
```
- Run this app:
  ```bash
  python app.py
  ```
- Now the **SQL Injection** attack can be triggered using:
  ```bash
  curl -X GET "http://127.0.0.1/search?q=' OR 1=1 --"
  ```

#### 2. **XSS (API Example)**

**Python Flask Application (Vulnerable to XSS):**
```python
from flask import Flask, request

app = Flask(__name__)

@app.route('/page')
def page():
    user_input = request.args.get('input', '')
    return f"<html><body><h1>{user_input}</h1></body></html>"

if __name__ == '__main__':
    app.run(host='127.0.0.1', port=80)
```
- Run this app:
  ```bash
  python app.py
  ```
- Now the **XSS** attack can be triggered using:
  ```bash
  curl -X GET "http://127.0.0.1/page?input=<script>alert('XSS')</script>"
  ```

### Conclusion:
- For all attacks, `127.0.0.1` (localhost) is used as the target IP.
- Simple vulnerable applications are created for **SQL Injection** and **XSS** using Flask.
- Snort rules are set up for detecting these attacks.