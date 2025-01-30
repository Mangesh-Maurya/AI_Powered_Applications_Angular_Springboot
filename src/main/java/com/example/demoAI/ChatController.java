//package controller;

package com.example.demoAI;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;

import reactor.core.publisher.Flux;

import org.springframework.web.bind.annotation.GetMapping;



@Controller
@RequestMapping("/api/v1/chat")
public class ChatController {
    
    @Autowired
    private ChatService chatService;

    @GetMapping
    public ResponseEntity<String> generateResponse(
        @RequestParam(value = "inputText")String inputText){
        String responseText=chatService.generateResponse(inputText);
                return ResponseEntity.ok(responseText);
                
    }

    
    @GetMapping("/stream")
    public Flux<String> streamResponse(
    @RequestParam(value = "inputText") String inputText) {
    Flux<String> response = chatService.streamResponse(inputText);
    return response; 
}


    @GetMapping("/cricket")
    public ResponseEntity<CricketResponse> getcCricketResponse(
            @RequestParam("inputText") String inputText
      ) throws JsonProcessingException{
        CricketResponse cricketResponse = chatService.generateCricketResponse(inputText);
        return ResponseEntity.ok(cricketResponse);
      }

     
     
      @GetMapping("/images")
      public ResponseEntity<List<String>> generateImages(
        @RequestParam("imageDescription") String imageDesc,
        @RequestParam(value = "size", required = false, defaultValue = "512x512") String size,
        @RequestParam(value = "numberOfImages",required = false,defaultValue = "2") int numbers
      ) throws IOException{
        return ResponseEntity.ok(chatService.generateImages(imageDesc, size, numbers));
      }
    }
  
