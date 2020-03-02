import socket

PORT = 9070;
HOST = "localhost";
userName = "noName";

import struct

class DataInputStream:
	def __init__(self, stream):
        self.stream = stream

    def read_boolean(self):
        return struct.unpack('?', self.stream.read(1))[0]

    def read_byte(self):
        return struct.unpack('b', self.stream.read(1))[0]

    def read_unsigned_byte(self):
        return struct.unpack('B', self.stream.read(1))[0]

    def read_char(self):
        return chr(struct.unpack('>H', self.stream.read(2))[0])

    def read_double(self):
        return struct.unpack('>d', self.stream.read(8))[0]

    def read_float(self):
        return struct.unpack('>f', self.stream.read(4))[0]

    def read_short(self):
        return struct.unpack('>h', self.stream.read(2))[0]

    def read_unsigned_short(self):
        return struct.unpack('>H', self.stream.read(2))[0]

    def read_long(self):
        return struct.unpack('>q', self.stream.read(8))[0]

    def read_utf(self):
        utf_length = struct.unpack('>H', self.stream.read(2))[0]
        return self.stream.read(utf_length)

    def read_int(self):
        return struct.unpack('>i', self.stream.read(4))[0]

sock = socket.socket()
print("Hello, type your name \n");
input(userName)


try(sock.connect((userName, 9070)))
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
    DataInputStream ois = new DataInputStream(socket.getInputStream());){












sock.send(b'Hello!\n')

data = sock.recv(1024)
udata = data.decode("utf-8")
print("Server > " + udata)

sock.close()