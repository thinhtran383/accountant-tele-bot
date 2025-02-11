package org.example.accountantbot.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.accountantbot.service.BotService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BotConfig implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {


    @Value("${telegrambots.bot.token}")
    private String token;

    private final BotService botService;

    @Bean
    TelegramClient telegramClient() {
        return new OkHttpTelegramClient(getBotToken());
    }


    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            int messageId = update.getMessage().getMessageId();

            log.info("Received message: {} from chatId: {}", messageText, chatId);
            log.info("Received message id: {}", messageId);

            SendChatAction sendChatAction = SendChatAction.builder()
                    .chatId(chatId)
                    .action(ActionType.TYPING.toString())
                    .build();

            try {
                telegramClient().execute(botService.processMessage(messageText, chatId, messageId));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
//        } else if (update.hasCallbackQuery()) {
//            String callbackData = update.getCallbackQuery().getData();
//            long chatId = update.getCallbackQuery().getMessage().getChatId();
//
//            log.info("Received callback data: {} from chatId: {}", callbackData, chatId);
//
//            if (callbackData.equals("update_msg_text")) {
//                try {
//                    telegramClient().execute(SendMessage.builder()
//                            .chatId(chatId)
//                            .text("Updated message text")
//                            .build()
//                    );
//                } catch (TelegramApiException e) {
//                    throw new RuntimeException(e);
//                }
//            }
        }
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        log.info("Registered bot running state is: {} ", botSession.isRunning());
    }
}
