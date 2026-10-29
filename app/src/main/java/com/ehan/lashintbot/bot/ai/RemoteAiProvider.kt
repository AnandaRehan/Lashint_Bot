package com.ehan.lashintbot.bot.ai

interface RemoteAiProvider : AiProvider {

    val endpoint: String
}