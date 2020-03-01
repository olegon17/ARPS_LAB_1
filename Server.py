import socket
from tkinter import *
root = Tk()
sock = socket.socket()
sock.bind( ('', 9090) )
sock.listen(1)
print('waiting for connection...')
conn, addr = sock.accept()
print('connected: ', addr)
data = conn.recv(1024)
uData = data.decode("utf-8")
print("Client.py > " + uData)
conn.send(uData.encode("utf-8"))
conn.close()