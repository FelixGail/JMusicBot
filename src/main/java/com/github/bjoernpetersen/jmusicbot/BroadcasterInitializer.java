package com.github.bjoernpetersen.jmusicbot;

import java.io.Closeable;
import javax.annotation.Nonnull;

@FunctionalInterface
public interface BroadcasterInitializer {

  /**
   * Initialized a UDP broadcaster. The broadcaster should broadcast the specified message every few
   * seconds with the specified destination port and group address.
   *
   * The message should be encoded with UTF-8.
   *
   * @param port a port number
   * @param groupAddress a UDP multicast group IPv4 address
   * @param message a message to broadcast
   * @return a broadcaster
   * @throws InterruptedException if the thread is interrupted during initialization
   * @throws InitializationException if the broadcaster can not be started
   */
  @Nonnull
  Closeable initialize(int port, String groupAddress, String message)
      throws InterruptedException, InitializationException;
}
