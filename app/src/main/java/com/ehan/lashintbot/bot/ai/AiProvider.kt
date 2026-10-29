package com.ehan.lashintbot.bot.ai

interface AiProvider {

    val name: String

    suspend fun ask(prompt: String): String
}