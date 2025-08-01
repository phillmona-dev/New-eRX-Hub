package com.medco.eprescription_out_of_stock.ServiceImp.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AfroMessageService {

    @Value("${afromessage.api.token}")
    private String apiToken;

    @Value("${afromessage.api.url}")
    private String apiUrl;

    @Value("${afromessage.api.identifierId}")
    private String identifierId;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendSms(String phoneNumber, String message) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(apiUrl).newBuilder();
        urlBuilder.addQueryParameter("to", phoneNumber);
        urlBuilder.addQueryParameter("message", message);
        urlBuilder.addQueryParameter("from", identifierId);

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .addHeader("Authorization", "Bearer " + apiToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            // Handle the response as needed
            System.out.println("SMS sent successfully");
        }
    }
}
