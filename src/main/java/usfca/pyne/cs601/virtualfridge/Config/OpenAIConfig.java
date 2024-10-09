package usfca.pyne.cs601.virtualfridge.Config;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfig {

    private Dotenv dotenv = Dotenv.load();
    private String openAiApiKey = dotenv.get("OPENAI_API_KEY");

    @Bean
    public ChatLanguageModel chatLanguageModel() {
        if (openAiApiKey == null || openAiApiKey.isEmpty()){
            throw new IllegalStateException("OPEN_API_KEY env variable not set.");
        }

        return OpenAiChatModel.builder()
                .apiKey(openAiApiKey)
                .modelName(OpenAiChatModelName.GPT_4_O)
                .organizationId("org-N3kLzCDDNnyAV7Ex19NS2GYN")
                .build();
    }


}
