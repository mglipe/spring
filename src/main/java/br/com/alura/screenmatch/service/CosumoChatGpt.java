package br.com.alura.screenmatch.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class CosumoChatGpt {

    private final static String API_KEY = System.getenv("OPENAI_KEY");


    public static String obterTraducao(String text){
        OpenAiService service = new OpenAiService(API_KEY);

        CompletionRequest request = CompletionRequest.builder()
            .model("gpt-3.5-turbo-instruct")
            .prompt("Traduza para o português: " + text)
            .maxTokens(1000)
            .temperature(0.7)
            .build();

        var response = service.createCompletion(request);
        return response.getChoices().get(0).getText();
    }
    
}
