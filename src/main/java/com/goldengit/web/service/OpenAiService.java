package com.goldengit.web.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class OpenAiService {

    private final OpenAiChatModel chatModel;

    public String generateProjectSummaryFromPullRequests(String projectName, List<String> pullRequestTitles) {
        StringBuilder message = new StringBuilder();
        message.append("Write a 100-words summary about the changes happening on the %s project on GitHub, ".formatted(projectName));
        message.append("do not use technical terms or abbreviations, ");
        message.append("consider the following pull request titles: \n");

        for (String title : pullRequestTitles.subList(0, Math.min(15, pullRequestTitles.size()))) {
            message.append(title, 0, Math.min(title.length(), 100)).append("\n");
        }

        ChatResponse response = chatModel.call(new Prompt(new UserMessage(message.toString())));
        return response.getResult().getOutput().getContent();
    }

    public String generateProjectDescription(String projectName) {
        StringBuilder message = new StringBuilder();
        message.append("Write a 80-words description about the %s project on GitHub, ".formatted(projectName));
        message.append("do not use technical terms or abbreviations ");
        ChatResponse response = chatModel.call(new Prompt(new UserMessage(message.toString())));
        return response.getResult().getOutput().getContent();
    }
}
