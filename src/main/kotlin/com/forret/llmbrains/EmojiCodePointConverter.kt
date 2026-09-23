package com.forret.llmbrains

import com.intellij.util.xmlb.Converter

/**
 * Persists an emoji as space-separated hex code points (e.g. "1f6e1 fe0f" for 🛡️).
 *
 * Most emoji live outside the Basic Multilingual Plane and are surrogate pairs in Java strings;
 * the IntelliJ XML serializer strips those from attribute values, so a raw emoji is saved as ""
 * and is gone after an IDE restart.
 */
class EmojiCodePointConverter : Converter<String>() {
    override fun toString(value: String): String = encode(value)

    override fun fromString(value: String): String = decode(value)

    companion object {
        fun encode(emoji: String): String =
            emoji.codePoints().toArray().joinToString(" ") { Integer.toHexString(it) }

        fun decode(encoded: String): String {
            val trimmed = encoded.trim()
            if (trimmed.isEmpty()) return DEFAULT_VARIANT_EMOJI
            val codePoints = trimmed.split(" ").map { it.toIntOrNull(16) ?: return DEFAULT_VARIANT_EMOJI }
            return String(codePoints.toIntArray(), 0, codePoints.size)
        }
    }
}
