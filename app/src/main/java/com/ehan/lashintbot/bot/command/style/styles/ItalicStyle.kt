package com.ehan.lashintbot.bot.command.style.styles

import com.ehan.lashintbot.bot.command.style.TextStyle

class ItalicStyle : TextStyle {

    override val name = "italic"

    override val description =
        "Teks miring"

    override fun format(
        text: String
    ): String {
        return "_${text}_"
    }
}