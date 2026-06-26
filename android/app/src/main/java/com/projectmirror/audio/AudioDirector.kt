package com.projectmirror.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

@Singleton
class AudioDirector @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var ambientTrack: AudioTrack? = null
    private var ambientJob: Job? = null
    private var enabled = true
    private var currentAmbient: AmbientType = AmbientType.NONE

    fun setEnabled(enabled: Boolean) {
        this.enabled = enabled
        if (!enabled) stopAll()
    }

    fun onSceneEnter(profile: SceneAudioProfile) {
        if (!enabled) return
        ensureMusicAudible()
        profile.oneShot?.let(::playCue)
        if (profile.ambient != AmbientType.NONE) {
            stopAmbient()
            startAmbient(profile.ambient, profile.padLayer, profile.ambientVolumeScale)
            currentAmbient = profile.ambient
        } else {
            stopAmbient()
            currentAmbient = AmbientType.NONE
        }
    }

    fun playCue(cue: AudioCue) {
        if (!enabled) return
        ensureMusicAudible()
        when (cue) {
            AudioCue.DOOR_WARM -> playOneShot(frequencyHz = 520.0, durationMs = 220, volume = 0.45f)
            AudioCue.DOOR_COLD -> playOneShot(frequencyHz = 165.0, durationMs = 320, volume = 0.45f)
            AudioCue.BIRD_DISTANT -> playOneShot(frequencyHz = 1_760.0, durationMs = 140, volume = 0.25f)
            else -> Unit
        }
    }

    private fun ensureMusicAudible() {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val stream = AudioManager.STREAM_MUSIC
        val max = audioManager.getStreamMaxVolume(stream)
        val current = audioManager.getStreamVolume(stream)
        if (current < max / 2) {
            audioManager.setStreamVolume(stream, (max * 0.7f).toInt().coerceAtLeast(1), 0)
        }
    }

    private fun playOneShot(frequencyHz: Double, durationMs: Int, volume: Float) {
        scope.launch {
            val sampleRate = 44_100
            val sampleCount = sampleRate * durationMs / 1_000
            val buffer = ShortArray(sampleCount)
            val attack = (sampleRate * 0.015).toInt().coerceAtLeast(1)
            val release = (sampleRate * 0.08).toInt().coerceAtLeast(1)
            for (i in buffer.indices) {
                val t = i.toDouble() / sampleRate
                val attackGain = (i.toDouble() / attack).coerceAtMost(1.0)
                val releaseGain = ((sampleCount - i).toDouble() / release).coerceAtMost(1.0)
                val envelope = minOf(attackGain, releaseGain)
                val sample = sin(2.0 * PI * frequencyHz * t) * envelope * volume
                buffer[i] = (sample * Short.MAX_VALUE).toInt().toShort()
            }
            playBuffer(buffer, sampleRate)
        }
    }

    private fun playBuffer(buffer: ShortArray, sampleRate: Int) {
        val minBuffer = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
        )
        val track = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build(),
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build(),
            )
            .setBufferSizeInBytes(maxOf(minBuffer, buffer.size * 2))
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()
        track.write(buffer, 0, buffer.size)
        track.play()
        scope.launch {
            kotlinx.coroutines.delay(buffer.size * 1_000L / sampleRate + 80)
            runCatching {
                track.stop()
                track.release()
            }
        }
    }

    private fun startAmbient(type: AmbientType, padLayer: Boolean, volumeScale: Float = 1f) {
        val profile = when (type) {
            AmbientType.HUM -> AmbientProfile(baseHz = 110.0, volume = 0.14f, noise = 0.02f)
            AmbientType.RAIN -> AmbientProfile(baseHz = 0.0, volume = 0.0f, noise = 0.12f)
            AmbientType.PAD -> AmbientProfile(baseHz = 220.0, volume = 0.12f, noise = 0.03f)
            AmbientType.NONE -> return
        }
        val sampleRate = 44_100
        val bufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
        ).coerceAtLeast(sampleRate / 10)
        val track = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build(),
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build(),
            )
            .setBufferSizeInBytes(bufferSize * 4)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()
        ambientTrack = track
        track.play()
        ambientJob = scope.launch {
            val samples = bufferSize / 2
            val buffer = ShortArray(samples)
            var phase = 0.0
            var phase2 = 0.0
            val scaledVolume = volumeScale.coerceIn(0.1f, 1f)
            while (ambientTrack === track) {
                for (i in buffer.indices) {
                    val tone = if (profile.baseHz > 0.0) {
                        val fundamental = sin(phase)
                        val harmonic = if (padLayer) sin(phase2) * 0.35 else 0.0
                        (fundamental * 0.75 + harmonic) * profile.volume
                    } else {
                        0.0
                    }
                    val noise = (Random.nextDouble() * 2.0 - 1.0) * profile.noise
                    val mixed = (tone + noise).coerceIn(-1.0, 1.0) * scaledVolume
                    buffer[i] = (mixed * Short.MAX_VALUE).toInt().toShort()
                    if (profile.baseHz > 0.0) {
                        phase += 2.0 * PI * profile.baseHz / sampleRate
                        phase2 += 2.0 * PI * (profile.baseHz * 1.5) / sampleRate
                    }
                }
                track.write(buffer, 0, buffer.size)
            }
        }
    }

    private fun stopAmbient() {
        ambientJob?.cancel()
        ambientJob = null
        ambientTrack?.let { track ->
            runCatching {
                track.stop()
                track.release()
            }
        }
        ambientTrack = null
        currentAmbient = AmbientType.NONE
    }

    fun stopAll() {
        stopAmbient()
    }

    private data class AmbientProfile(
        val baseHz: Double,
        val volume: Float,
        val noise: Float,
    )
}
