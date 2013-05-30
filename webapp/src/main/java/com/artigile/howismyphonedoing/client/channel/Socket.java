package com.artigile.howismyphonedoing.client.channel;
/**
 * App Engine Channel API socket object.
 */
public interface Socket {

  /**
   * Close the socket. The socket cannot be used again after calling close; the server must create
   * a new socket.
   */
  void close();

}