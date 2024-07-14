package com.softkour.qrsta_server.config.firebase;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FCMInitializer {

    @Value("${app.firebase-configuration-file}")
    private String firebaseConfigPath;

    @PostConstruct
    public void firebaseInit() throws IOException {
        try {
            FileInputStream serviceAccount = new FileInputStream(firebaseConfigPath);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(options);
            // isFirebaseActive = true;
        } catch (Exception e) {
            log.error("Post Construct: {}", e);
        }
    }

    public String sendPushNotificationService(NotificationRequest request) {
        Map<String, String> firebaseMessageBody = new HashMap<>();
        firebaseMessageBody.put("title", request.getTitle());
        firebaseMessageBody.put("body", request.getBody());
        try {
            Message message = Message
                    .builder()
                    .setToken(request.getToken())
                    .putAllData(firebaseMessageBody)
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            return response;
        } catch (FirebaseMessagingException e) {
            log.error("Firebase error sending: {}", e);

            return "Firebase error sending";
        }
    }
}