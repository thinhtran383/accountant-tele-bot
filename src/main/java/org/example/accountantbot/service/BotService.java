package org.example.accountantbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.accountantbot.chatmodel.Assistant;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Slf4j
@Service
@RequiredArgsConstructor
public class BotService {
    private final Assistant assistant;

    public SendMessage processMessage(String message, long chatId, int messageId) {
        if (message.equals("/start")) {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Xin chào, mình là nhân viên kết toán, có gì thắc mắc hỏi mình nhé :)")
                    .build();
        }

       
        
        
        String reply = assistant.accountantsHelp(message, chatId);
        
        log.info("Replying to chatId: {} with: {}", chatId, reply);

        return SendMessage.builder()
                .chatId(chatId)
                .parseMode("Markdown")
                .text(reply)
                .replyToMessageId(messageId)
//                .replyMarkup(InlineKeyboardMarkup
//                        .builder()
//                        .keyboardRow(
//                                new InlineKeyboardRow(InlineKeyboardButton
//                                        .builder()
//                                        .text("Update message text")
//                                        .callbackData("update_msg_text")
//                                        .build()
//                                )
//                        )
//                        .build())
                .build();
    }


}
