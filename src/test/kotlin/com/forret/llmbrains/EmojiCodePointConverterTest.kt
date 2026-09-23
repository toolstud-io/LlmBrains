package com.forret.llmbrains

import com.intellij.openapi.util.JDOMUtil
import com.intellij.util.xmlb.XmlSerializer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EmojiCodePointConverterTest {
    @Test
    fun `should encode emoji as hex code points`() {
        assertEquals("1f535", EmojiCodePointConverter.encode("🔵"))
        assertEquals("26a1", EmojiCodePointConverter.encode("⚡"))
        assertEquals("1f6e1 fe0f", EmojiCodePointConverter.encode("🛡️"))
    }

    @Test
    fun `should decode hex code points back to emoji`() {
        assertEquals("🔵", EmojiCodePointConverter.decode("1f535"))
        assertEquals("🛡️", EmojiCodePointConverter.decode(" 1f6e1 fe0f "))
    }

    @Test
    fun `should fall back to default emoji for blank or malformed values`() {
        assertEquals(DEFAULT_VARIANT_EMOJI, EmojiCodePointConverter.decode(""))
        assertEquals(DEFAULT_VARIANT_EMOJI, EmojiCodePointConverter.decode("⚡"))
        assertEquals(DEFAULT_VARIANT_EMOJI, EmojiCodePointConverter.decode("zz"))
    }

    @Test
    fun `should survive an XML serializer round trip`() {
        val state = AgentSettingsState.State(
            customVariants = mutableListOf(
                CustomVariantEntry("claude", "Fable", "--model fable", "🔵"),
                CustomVariantEntry("claude", "Shield", "", "🛡️"),
            ),
        )

        val xml = JDOMUtil.write(XmlSerializer.serialize(state))
        val restored = XmlSerializer.deserialize(JDOMUtil.load(xml), AgentSettingsState.State::class.java)

        assertEquals(listOf("🔵", "🛡️"), restored.customVariants.map { it.emoji })
    }
}
