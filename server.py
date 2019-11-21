import socket
sock = socket.socket()
sock.bind(('127.0.0.1', 8080))
sock.listen(1)
print("server started")
while True:
    conn, addr = sock.accept()
    print("connected:", addr)
    data = conn.recv(1024)
    print(data.decode("utf-8"))
    conn.send(data)
    print("disconnected", addr)
    conn.close()


