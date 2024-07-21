package com.softkour.qrsta_server.payload.request;

import com.softkour.qrsta_server.entity.quiz.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OptionCreationRequest {
    private String title;
    private Boolean isCorrectAnswer;

    public Option toOption(){
        Option option=new Option();
        option.setTitle(getTitle());
        option.setIsCorrectAnswer(getIsCorrectAnswer());

        return option;
    }
}
