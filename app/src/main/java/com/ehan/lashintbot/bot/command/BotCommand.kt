package com.ehan.lashintbot.bot.command

import com.ehan.lashintbot.bot.baileys.BaileysSocket
import com.ehan.lashintbot.bot.baileys.BotMessageContent
import com.ehan.lashintbot.bot.baileys.SendMessageOptions
import com.ehan.lashintbot.data.ChatMessage

interface BotCommand {

    val name: String

    val description: String

    val category: String

    suspend fun execute(argument: String?): String

    /**
     * Eksekusi command menggunakan sistem mirip @WhiskeySockets/baileys (`sock.sendMessage`).
     * Command dapat meng-override fungsi ini untuk mengirim video, foto, slide carousel,
     * audio, maupun pesan tombol (buttons).
     */
    suspend fun execute(
        sock: BaileysSocket,
        jid: String,
        argument: String?,
        quoted: ChatMessage? = null
    ) {
        val textReply = execute(argument)
        sock.sendMessage(
            jid = jid,
            content = BotMessageContent(text = textReply),
            options = SendMessageOptions(quoted = quoted)
        )
    }
}
