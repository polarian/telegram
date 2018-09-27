package io.polarian.bot.telegram

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import javax.annotation.PostConstruct

@Component
class TelegramBot : TelegramLongPollingBot() {
    override fun getBotUsername(): String {
        return userName
    }

    override fun getBotToken(): String {
        return token
    }

    @Value("\${bot.token}")
    private lateinit var token: String

    @Value("\${bot.username}")
    private lateinit var userName: String

    override fun onUpdateReceived(update: Update?) {
        if (update?.hasMessage()!!) {
            val message = update.message
            val response = SendMessage()

            val chatId = message.chatId
            response.chatId = chatId.toString()

            val text = message.text
            response.text = text

            try {
                execute(response)
                println("Sent message $text to $chatId")
            } catch (e: TelegramApiException) {
                println("Failed to send message $text to $chatId due to error: ${e.message}")
            }
        }
    }

    @PostConstruct
    fun start() {
        println("username: $userName, token: $token")
    }
}