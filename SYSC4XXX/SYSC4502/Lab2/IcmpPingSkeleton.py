from socket import *
import os
import sys
import struct
import time
import select
import binascii
import array

ICMP_ECHO_REQUEST = 8


def chksum(packet):
    if len(packet) % 2 != 0:
        packet += b'\0'
    res = sum(array.array("H", packet))
    res = (res >> 16) + (res & 0xffff)
    res += res >> 16
    return (~res) & 0xffff


def receiveOnePing(mySocket, ID, timeout, destAddr):
    timeLeft = timeout

    while 1:
        startedSelect = time.time()
        whatReady = select.select([mySocket], [], [], timeLeft)
        howLongInSelect = (time.time() - startedSelect)
        if whatReady[0] == []:  # Timeout
            return "Request timed out."

        timeReceived = time.time()
        recPacket, addr = mySocket.recvfrom(1024)

        # Fill in start
        if (recPacket.__len__() >= 36):
            length = recPacket[2] * 256 + recPacket[3]
            TTL = recPacket[8]
            sourceIP = recPacket[12:16]
            sourceIP = "" + sourceIP[0].__str__() + "." + sourceIP[1].__str__()  + "." + sourceIP[2].__str__()  + "." + sourceIP[3].__str__()
            # receiveIP = recPacket[16:20]
            data = recPacket[28:36]
            full_path_time = (time.time() - struct.unpack("d", data)[0])
            full_path_time *= 1000
            # full_path_time = full_path_time.__str__() + "ms"
            text = "Reply from {0}: bytes={1} time={2: .7f}ms TTL={3}"
            return text.format(sourceIP, length, full_path_time, TTL)
        # Fill in end

        timeLeft = timeLeft - howLongInSelect
        if timeLeft <= 0:
            return "Request timed out. (2)"


def sendOnePing(mySocket, destAddr, ID):
    # Header is type (8), code (8), checksum (16), id (16), sequence (16)

    myChecksum = 0
    # Make a dummy header with a 0 checksum
    # struct -- Interpret strings as packed binary data
    header = struct.pack("bbHHh", ICMP_ECHO_REQUEST, 0, myChecksum, ID, 1)
    data = struct.pack("d", time.time())
    # Calculate the checksum on the data and the dummy header.

    myChecksum = chksum(header + data)

    header = struct.pack("bbHHh", ICMP_ECHO_REQUEST, 0, myChecksum, ID, 1)
    packet = header + data

    mySocket.sendto(packet, (destAddr, 1))  # AF_INET address must be tuple, not str


# Both LISTS and TUPLES consist of a number of objects
# which can be referenced by their position number within the object.

def doOnePing(destAddr, timeout):
    icmp = getprotobyname("icmp")

    # SOCK_RAW is a powerful socket type. For more details:   http://sock-raw.org/papers/sock_raw
    mySocket = socket(AF_INET, SOCK_RAW, icmp)

    myID = os.getpid() & 0xFFFF  # Return the current process i
    sendOnePing(mySocket, destAddr, myID)
    delay = receiveOnePing(mySocket, myID, timeout, destAddr)

    mySocket.close()
    return delay


def ping(host, timeout=1):
    # timeout=1 means: If one second goes by without a reply from the server,
    # the client assumes that either the client's ping or the server's pong is lost
    dest = gethostbyname(host)
    print("Pinging " + dest + " using Python:")
    print("")
    # Send ping requests to a server separated by approximately one second
    for x in range(10):
        delay = doOnePing(dest, timeout)
        print(delay)
        time.sleep(1)  # one second
    return delay

ping("127.0.0.1")
ping("google.com")
ping("193.99.144.80")
