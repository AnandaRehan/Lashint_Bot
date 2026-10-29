package com.ehan.lashintbot.bot.command.commands

import com.ehan.lashintbot.bot.ai.AiProviderManager
import com.ehan.lashintbot.bot.command.BotCommand

class AiCommand(
    private val aiProviderManager: AiProviderManager
) : BotCommand {

    override val name = "ai"
    override val description = "Menggunakan fitur AI"
    override val category = "AI"
    override suspend fun execute(argument: String?): String {

        if (argument.isNullOrBlank()) {
            return """
                🤖 AI

                Provider:
                ${aiProviderManager.getActiveProviderName()}

                Gunakan:
                /ai <pertanyaan>

                Contoh:
                /ai jelaskan fotosintesis
            """.trimIndent()
        }

        return aiProviderManager.ask(argument.trim())
    }
}