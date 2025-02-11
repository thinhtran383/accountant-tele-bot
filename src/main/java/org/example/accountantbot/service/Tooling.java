package org.example.accountantbot.service;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class Tooling {
    @Tool("getCurrentTime")
    public LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }
}
