package com.ehan.lashintbot.bot.command.style.styles

import com.ehan.lashintbot.bot.command.style.TextStyle

class MonoStyle : TextStyle {

    override val name = "mono"

    override val description =
        "Teks monospace"

    override fun format(
        text: String
    ): String {
        return "```$text```"
    }
}