package com.softkour.qrsta_server.payload.request;

import com.google.firebase.database.annotations.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
@Setter
@Getter
public class QuizCorrectionRequest {
    @NotNull
    private Long quizId;
    @NotNull
    private List<Long> questionsId;
    @NotNull
    private List<List<String>> answers;

}
