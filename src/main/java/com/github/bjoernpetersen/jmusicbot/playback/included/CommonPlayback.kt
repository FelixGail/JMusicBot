/**
 * Marker interfaces for PlaybackFactory implementations capable of playing common audio file formats.
 */
package com.github.bjoernpetersen.jmusicbot.playback.included

import com.github.bjoernpetersen.jmusicbot.playback.FilePlaybackFactory

/**
 * PlaybackFactory capable of playing .aac and .m4a files.
 */
interface AacPlabackFactory : FilePlaybackFactory

/**
 * PlaybackFactory capable of playing .flac files.
 */
interface FlacPlaybackFactory : FilePlaybackFactory

/**
 * PlaybackFactory capable of playing .mp3 files.
 */
interface Mp3PlaybackFactory : FilePlaybackFactory

/**
 * PlaybackFactory capable of playing .opus files.
 */
interface OpusPlaybackFactory : FilePlaybackFactory

/**
 * PlaybackFactory capable of playing .ogg and .oga files.
 */
interface VorbisPlaybackFactory : FilePlaybackFactory

/**
 * PlaybackFactory capable of playing .wav and .wave files.
 */
interface WavePlaybackFactory : FilePlaybackFactory

/**
 * PlaybackFactory capable of playing .wma files.
 */
interface WmaPlaybackFactory : FilePlaybackFactory
