package com.ehan.lashintbot.bot.command

import com.ehan.lashintbot.bot.ai.AiProviderManager
import com.ehan.lashintbot.bot.baileys.BaileysSocket
import com.ehan.lashintbot.bot.baileys.BotButton
import com.ehan.lashintbot.bot.baileys.BotMessageContent
import com.ehan.lashintbot.bot.baileys.ButtonText
import com.ehan.lashintbot.bot.baileys.SendMessageOptions
import com.ehan.lashintbot.bot.command.commands.AiCommand
import com.ehan.lashintbot.bot.command.commands.AiProviderCommand
import com.ehan.lashintbot.bot.command.commands.AllMenuCommand
import com.ehan.lashintbot.bot.command.commands.CalcCommand
import com.ehan.lashintbot.bot.command.commands.DownloadCommand
import com.ehan.lashintbot.bot.command.commands.EchoCommand
import com.ehan.lashintbot.bot.command.commands.HelpCommand
import com.ehan.lashintbot.bot.command.commands.MenuCommand
import com.ehan.lashintbot.bot.command.commands.PingCommand
import com.ehan.lashintbot.bot.command.commands.StatusCommand
import com.ehan.lashintbot.bot.command.commands.StyleCommand
import com.ehan.lashintbot.bot.command.commands.TikTokCommand
import com.ehan.lashintbot.bot.command.commands.ToolsCommand
import com.ehan.lashintbot.bot.command.commands.YouTubeCommand
import com.ehan.lashintbot.bot.tiktok.TikWmService
import com.ehan.lashintbot.bot.youtube.NazeYoutubeService
import com.ehan.lashintbot.data.ChatMessage

class CommandManager(
    private val tikWmService: TikWmService = TikWmService(),
    private val youtubeService: NazeYoutubeService = NazeYoutubeService()
) {

    private val aiProviderManager =
        AiProviderManager()

    private lateinit var commands: List<BotCommand>

    val allCommands: List<BotCommand>
        get() = commands

    init {
        val basicCommands =
            listOf<BotCommand>(
                TikTokCommand(tikWmService),
                YouTubeCommand(youtubeService, audioOnlyDefault = false),
                YouTubeCommand(youtubeService, audioOnlyDefault = true),
                DownloadCommand(tikWmService, youtubeService),
                ToolsCommand(),
                StyleCommand(),
                AiCommand(aiProviderManager),
                AiProviderCommand(aiProviderManager),
                PingCommand(),
                EchoCommand(),
                CalcCommand(),
                StatusCommand(aiProviderManager)
            )

        commands =
            listOf(
                MenuCommand(basicCommands),
                AllMenuCommand(basicCommands)
            ) +
            basicCommands +
            HelpCommand(basicCommands)
    }

    private val commandMap
        get() = commands.associateBy {
            it.name.lowercase()
        }

    private fun parseCommandAndArgument(input: String): Pair<BotCommand?, String?> {
        val cleanInput = input.trim()
        val content = cleanInput.removePrefix("/")
        val parts = content.split(limit = 2, delimiters = arrayOf(" "))
        val rawCommandName = parts[0].lowercase()
        val rawArgument = parts.getOrNull(1)?.trim()

        if (rawCommandName == "apikey" || rawCommandName == "setkey") {
            val subParts = rawArgument?.split(limit = 2, delimiters = arrayOf(" ")).orEmpty()
            val firstToken = subParts.getOrNull(0)?.lowercase().orEmpty()
            val restToken = subParts.getOrNull(1)?.trim().orEmpty()

            return when (firstToken) {
                "yt", "youtube", "naze" -> {
                    val arg = if (restToken.isEmpty()) null else "key $restToken"
                    commandMap["youtube"] to arg
                }
                "tiktok", "tt", "tikwm" -> {
                    val arg = if (restToken.isEmpty()) null else "key $restToken"
                    commandMap["tiktok"] to arg
                }
                else -> {
                    val arg = if (rawArgument.isNullOrBlank()) null else "key $rawArgument"
                    commandMap["tiktok"] to arg
                }
            }
        }

        if (rawCommandName == "naze") {
            val arg = if (rawArgument.isNullOrBlank()) null else "key $rawArgument"
            return commandMap["youtube"] to arg
        }

        val commandName = when (rawCommandName) {
            "tt", "tikwm" -> "tiktok"
            "yt", "ytmp4" -> "youtube"
            "ytaudio" -> "ytmp3"
            "dl" -> "download"
            else -> rawCommandName
        }

        return commandMap[commandName] to rawArgument
    }

    suspend fun execute(input: String): String {
        val cleanInput = input.trim()
        if (!cleanInput.startsWith("/")) {
            return "Ketik /menu untuk melihat fitur bot."
        }

        val (command, argument) = parseCommandAndArgument(cleanInput)
        if (command == null) {
            return """
                ❌ Command tidak ditemukan.

                Ketik /menu untuk melihat daftar fitur.
            """.trimIndent()
        }

        return command.execute(argument)
    }

    suspend fun execute(
        sock: BaileysSocket,
        jid: String,
        input: String,
        quoted: ChatMessage? = null
    ) {
        val cleanInput = input.trim()
        if (!cleanInput.startsWith("/")) {
            sock.sendMessage(
                jid = jid,
                content = BotMessageContent(
                    text = "Ketik /menu untuk melihat fitur bot.",
                    buttons = listOf(
                        BotButton(
                            buttonId = "/menu",
                            buttonText = ButtonText("📋 Menu Utama"),
                            type = BotButton.TYPE_REPLY
                        )
                    )
                ),
                options = SendMessageOptions(quoted = quoted)
            )
            return
        }

        val (command, argument) = parseCommandAndArgument(cleanInput)
        if (command == null) {
            sock.sendMessage(
                jid = jid,
                content = BotMessageContent(
                    text = """
                        ❌ Command tidak ditemukan.

                        Ketik /menu untuk melihat daftar fitur.
                    """.trimIndent(),
                    buttons = listOf(
                        BotButton(
                            buttonId = "/menu",
                            buttonText = ButtonText("📋 Buka Menu"),
                            type = BotButton.TYPE_REPLY
                        )
                    )
                ),
                options = SendMessageOptions(quoted = quoted)
            )
            return
        }

        command.execute(
            sock = sock,
            jid = jid,
            argument = argument,
            quoted = quoted
        )
    }
}
