package com.opwriter.services;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


@Service
public class ChatGptServiceImp implements ChatGptService {

    @Value("${cohere.api.key:}")
    private String apiKey;

    private static final String API_URL = "https://api.cohere.ai/v1/chat";
    private static final MediaType JSON = MediaType.parse("application/json");

    public String getAnswer(String topic,Integer words,String format) {
    	
    	
        OkHttpClient client = new OkHttpClient();

        String prompt = "Write a detailed "+format+" on the topic: " + topic+"with only"+words+" words";

        System.out.println(prompt);
        String jsonRequest = "{"
                + "\"model\": \"command-r\","
                + "\"message\": \"" + prompt + "\","
                + "\"temperature\": 0.7,"
                + "\"max_tokens\": 500"
                + "}";

        RequestBody body = RequestBody.create(jsonRequest, JSON);

        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Request failed: " + response.code() + " " + response.message());
                return "Error generating essay.";
            }

            String responseBody = response.body().string();
            System.out.println("Cohere Response:\n" + responseBody);

          
            String result = responseBody.split("\"text\":\"")[1].split("\"")[0];
            return result.replace("\\n", "\n");

        } catch (IOException e) {
            e.printStackTrace();
            return "Exception occurred: " + e.getMessage();
        }
    }
}
