package com.ehan.lashintbot.bot.command.style.styles

import com.ehan.lashintbot.bot.command.style.TextStyle

class BoldItalicStyle : TextStyle {

    override val name = "bolditalic"

    override val description =
        "Teks tebal dan miring"

    override fun format(
        text: String
    ): String {
        return "*_${text}_*"
    }
}