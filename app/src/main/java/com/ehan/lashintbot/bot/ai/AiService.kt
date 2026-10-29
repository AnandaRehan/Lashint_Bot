package com.ehan.lashintbot.bot.ai

interface AiService {

    suspend fun ask(
        prompt: String
    ): String
}