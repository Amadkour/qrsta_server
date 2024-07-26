package com.softkour.qrsta_server.controller;

import com.pkslow.ai.GoogleBardClient;
import com.pkslow.ai.domain.Answer;
import com.pkslow.ai.domain.AnswerStatus;
import com.softkour.qrsta_server.config.GenericResponse;
import com.softkour.qrsta_server.exception.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.softkour.qrsta_server.config.AI.ChatGptRequest;
import com.softkour.qrsta_server.config.AI.ChatGptResponse;

/**
 * AIController
 */
@RestController
@RequestMapping("/api/bot")
public class AIController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate template;
   @Autowired
   private GoogleBardClient client;

    @GetMapping("/board")
    public ResponseEntity<GenericResponse<Object>> ask(@RequestHeader("input") String input) {
        Answer answer = client.ask(input);
        if (answer.status() == AnswerStatus.OK) {
            return  GenericResponse.successWithMessageOnly(answer.chosenAnswer());

//            return new BardAnswer(answer.chosenAnswer(), answer.draftAnswers());
        }

        if (answer.status() == AnswerStatus.NO_ANSWER) {
            return  GenericResponse.successWithMessageOnly("no Answer");
        }

        throw new ClientException("ai","Can't access to Google Bard");

    }

    @GetMapping("/chat")
    public String chat(@RequestHeader("input") String input) {
        ChatGptRequest request = new ChatGptRequest(model, input);
        System.out.println(input);
        System.out.println(apiURL);
        System.out.println(model);
        ChatGptResponse chatGptResponse = template.postForObject(apiURL, request, ChatGptResponse.class);
        assert chatGptResponse != null;
        System.out.println(chatGptResponse.getChoices().stream().map(e->e.getMessage()+", "));
        return chatGptResponse.getChoices().get(0).getMessage().getContent();
    }

}