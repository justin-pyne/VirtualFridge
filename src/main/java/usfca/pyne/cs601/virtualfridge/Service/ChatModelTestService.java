package usfca.pyne.cs601.virtualfridge.Service;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import org.springframework.stereotype.Service;

@Service
public class ChatModelTestService {
    private final ChatLanguageModel chatLanguageModel;

    public ChatModelTestService(ChatLanguageModel chatLanguageModel) {
        this.chatLanguageModel = chatLanguageModel;
    }

    public String getOpenAIResponse(String text) {
        Prompt prompt = new Prompt(text);
        AiMessage aiMessage = chatLanguageModel.generate(prompt.toUserMessage()).content();
        return aiMessage.text();
    }
}
