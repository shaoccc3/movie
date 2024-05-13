package tw.com.ispan.datainjection.service;


import com.github.houbb.opencc4j.util.ZhConverterUtil;
import okhttp3.*;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import tw.com.ispan.datainjection.dto.ActorData;

@Service
public class ActorService {
    private static final int MAX_REQUESTS_PER_PERIOD = 40;
    private static final long PERIOD = TimeUnit.SECONDS.toMillis(10); // 10 seconds

    private OkHttpClient client = new OkHttpClient();
    private ObjectMapper mapper = new ObjectMapper();

    private long periodStart = System.currentTimeMillis();
    private int requestCount = 0;

    public ActorData fetchAndPrepareActorData(int actorId) {
        try {
            rateLimitRequests();
            String url = "https://api.themoviedb.org/3/person/" + actorId + "?language=en-US&api_key=your_api_key";
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyMDQwOTlkMTc4Mzk0YzI0Y2IxZmVmNmJjYzY0ZmM2ZSIsInN1YiI6IjY2M2I4ZGMxMmNiMzNmZGUzNWI1YjkwYSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.xAabA20GxwaFB7Zpbgj60XCv5au3a5nBm71JSskf0Vs")
                    .build();
            try (Response response = client.newCall(request).execute()) {

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                String responseBody = response.body().string(); // 保存响应体数据到字符串
                JsonNode rootNode = mapper.readTree(responseBody); // 使用字符串创建 JsonNode
                int id = rootNode.get("id").asInt();
                JsonNode names = rootNode.get("also_known_as");
                if (names == null || names.isEmpty()) {
                    System.out.println("No names available for actor ID: " + actorId);
                    return null;
                }
                String name = extractName(names,rootNode.get("name"));
                String photo = fetchActorPhoto(actorId);
                ActorData actordata = new ActorData(id,name);
                if (photo != null) {
                    actordata.setPhoto(photo);
                }
                return actordata;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private String fetchActorPhoto(int actorId) throws IOException {
        rateLimitRequests();
        String url = "https://api.themoviedb.org/3/person/" + actorId + "/images?language=en-US&api_key=your_api_key";
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyMDQwOTlkMTc4Mzk0YzI0Y2IxZmVmNmJjYzY0ZmM2ZSIsInN1YiI6IjY2M2I4ZGMxMmNiMzNmZGUzNWI1YjkwYSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.xAabA20GxwaFB7Zpbgj60XCv5au3a5nBm71JSskf0Vs")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            JsonNode rootNode = mapper.readTree(response.body().string());
            JsonNode profiles = rootNode.get("profiles");
            if (profiles != null && profiles.isArray() && profiles.size() > 0) {
                JsonNode firstProfile = profiles.get(0);
                if (firstProfile != null && firstProfile.has("file_path")) {
                    return firstProfile.get("file_path").asText();
                }
            }
        }
        return null;
    }

    private void rateLimitRequests() {
        long now = System.currentTimeMillis();
        synchronized (this) {
            if (now - periodStart < PERIOD) {
                if (requestCount >= MAX_REQUESTS_PER_PERIOD) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(PERIOD - (now - periodStart));
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                    periodStart = System.currentTimeMillis();
                    requestCount = 0;
                }
            } else {
                periodStart = now;
                requestCount = 0;
            }
            requestCount++;
        }
    }
    private String extractName(JsonNode nameList, JsonNode defaultName) {
        if (nameList != null && !nameList.isEmpty()) {
            for (JsonNode name : nameList) {
                String nameStr = name.asText();
                if (nameStr.matches(".*[\u4e00-\u9fa5]+.*")) {//中文
                    nameStr = convertSimplifiedToTraditional(nameStr);//簡轉繁
                    return nameStr;
                }
            }
        }
        if (defaultName != null && !defaultName.asText().isEmpty()) {
            return defaultName.asText();
        }
        return null;

    }

    public void sendActorDataToAnotherServer(ActorData actorData) {
        String json;
        try {
            json = mapper.writeValueAsString(actorData);
            MediaType JSON = MediaType.get("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(json, JSON);
            Request request = new Request.Builder()
                    .url("http://localhost:8082/backstage/actor")
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String convertSimplifiedToTraditional(String simplifiedChinese) {
        try {
            //UTF-8
            String result = ZhConverterUtil.toTraditional(simplifiedChinese);
            return new String(result.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return "轉換錯誤";
        }
    }
}

