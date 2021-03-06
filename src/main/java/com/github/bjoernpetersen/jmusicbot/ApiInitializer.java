package com.github.bjoernpetersen.jmusicbot;

import java.io.Closeable;
import javax.annotation.Nonnull;

@FunctionalInterface
public interface ApiInitializer {

  Closeable initialize(@Nonnull MusicBot bot, int port) throws InterruptedException, InitializationException;
}
