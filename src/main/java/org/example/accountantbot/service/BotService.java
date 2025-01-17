package org.example.accountantbot.service;

import lombok.RequiredArgsConstructor;
import org.example.accountantbot.chatmodel.Assistant;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
@RequiredArgsConstructor
public class BotService {
    private final Assistant assistant;
    public SendMessage processMessage(String message, long chatId) {
        if (message.equals("/start")) {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Xin chào, em là nhân viên kết toán, có gì thắc mắc hỏi em nhé :)")
                    .build();
        }
        return SendMessage.builder()
                .chatId(chatId)
                .text(assistant.accountantsHelp(message, chatId))
                .build();
    }
}
