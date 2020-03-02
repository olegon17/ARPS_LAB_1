# -*- coding: utf-8 -*- 

import socket
from tkinter import *

tk=Tk()

s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
s.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
s.bind(('0.0.0.0',11719))

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sock.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST,1)
#if (sock.done)

text=StringVar()
name=StringVar()
name.set('ВВЕДИ ИМЯ')
text.set('')
tk.title('MegaChat')
tk.geometry('666x666')

log = Text(tk)
nickname = Entry(tk, textvariable=name)
msg = Entry(tk, textvariable=text)
msg.pack(side='bottom', fill='x', expand='true')
nickname.pack(side='bottom', fill='x', expand='true')
log.pack(side='top', fill='both',expand='true')

def close_window():
	msg_text=name.get() + " вышел из чата"
	sock.sendto (msg_text.encode(),('255.255.255.255',11719))
	text.set('')
	sock.close()
	tk.close()

tk.protocol("WM_DELETE_WINDOW", close_window)

def loopproc():
	log.see(END)
	s.setblocking(False)
	try:
		message = s.recv(128)
		log.insert(END,message.decode("utf-8")+'\n')
	except:
		tk.after(1,loopproc)
		return
	tk.after(1,loopproc)
	return

def sendproc(event):
	msg_text=name.get()+':'+text.get()
	sock.sendto (msg_text.encode(),('255.255.255.255',11719))
	text.set('')

msg.bind('<Return>',sendproc)

msg.focus_set()

tk.after(1,loopproc)
tk.mainloop()