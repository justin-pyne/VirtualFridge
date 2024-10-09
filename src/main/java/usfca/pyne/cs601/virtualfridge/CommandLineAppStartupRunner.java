package usfca.pyne.cs601.virtualfridge;

import usfca.pyne.cs601.virtualfridge.Service.ChatModelTestService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    private final ChatModelTestService openAITestService;

    public CommandLineAppStartupRunner(ChatModelTestService chatModelTestService) {
        this.openAITestService = chatModelTestService;
    }

    @Override
    public void run(String... args) throws Exception {
        String prompt = "Hello, how are you?";
        String response = openAITestService.getOpenAIResponse(prompt);
        System.out.println("AI Response: " + response);
    }
}
