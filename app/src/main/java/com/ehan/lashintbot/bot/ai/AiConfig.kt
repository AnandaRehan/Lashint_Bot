package com.ehan.lashintbot.bot.ai

data class AiConfig(
    val model: String = "gpt-5.6-luna",
    val endpoint: String = "https://api.openai.com/v1/"
)