package com.example.demoAI;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.StreamingChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImageOptionsBuilder;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.ai.openai.api.OpenAiImageApi.ImageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;

@Service
public class ChatService {
    @Autowired
    private ChatModel chatModel;

    @Autowired
    private org.springframework.ai.image.ImageModel imageModel;

    @Autowired
    private StreamingChatModel streamingChatModel;

   // @Autowired
  //  private ImageModel imageModel; 

    public String generateResponse(String inputText) {
        //You are using AI
        String response = chatModel.call(inputText);
        return response;
     }

     public Flux<String> streamResponse(String inputText) {
        // Calls the stream method in ChatModel
        Flux<String> response = chatModel.stream(inputText);
        return response;  // Return the Flux response
    }

     public CricketResponse generateCricketResponse(String inputText) throws JsonProcessingException{
        String promptString = "";

        ChatResponse crickResponse= chatModel.call(
            new Prompt(promptString)
            
        );

        //get content as string
        String responseString = crickResponse.getResult().getOutput().getContent();
        ObjectMapper mapper = new ObjectMapper();
        CricketResponse cricketResponse1= mapper.readValue(responseString, CricketResponse.class);
        return cricketResponse1;

     }

//load prompt from classpath
     
    public String generateImages(String imageDesc, String size, int numbers) throws IOException {
         String template = this.loadPromptTemplate("prompts/image_bot.txt");
        String promptString = this.putValuesInPromptTemplate(template, Map.of("description", imageDesc));

        ImageResponse imageResponse = imageModel.call(new ImagePrompt(
            promptString, OpenAiImageOptions.builder()
            .withModel("dall-e-2")
            .withN(1) // Use the `numbers` parameter
             .withHeight(1024)
             .withWidth(1024)
             .withQuality("standard")
             .build()
         ));

    
        List<String> imageUrls = imageResponse.getResult().stream()
         .map(generation -> generation.getOutput().getUrl()) 
         .collect(Collectors.toList());

     return imageUrls;

         }


    public String loadPromptTemplate(String fileName) throws IOException{
    Path filePath = new ClassPathResource(fileName).getFile().toPath();
    return Files.readString(filePath);
    }


//put values to prompt

    public String putValuesInPromptTemplate(String template, Map<String, String> variables){

        for(Map.Entry<String, String> entry : variables.entrySet()){
            template = template.replace("{" + entry.getKey() + "}", entry.getValue());
        }
     return template;


    }
}


