package com.github.cli;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final String GITHUB_API_URL = "https://api.github.com/users/%s/events";

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java GitHubActivityCLI <GitHub-username>");
            return;
        }

        String username = args[0];
        fetchGitHubActivity(username);
    }

    private static void fetchGitHubActivity(String username) {
        try {
            String apiUrl = String.format(GITHUB_API_URL, username);
            URL url = new URL(apiUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                System.out.println("Error: Unable to fetch data. HTTP response code: " + responseCode);
                return;
            }

            String content;
            try (InputStream inputStream = connection.getInputStream()) {
                content = new String(inputStream.readAllBytes());
            }

            JsonArray events = JsonParser.parseString(content).getAsJsonArray();

            if(events.isEmpty()) {
                System.out.println("No recent activity found for user: " + username);
                return;
            }

            System.out.println(formatAllEvents(events));

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static String formatAllEvents(JsonArray events) {
        Map<String, Integer> pushEventCounts = new HashMap<>();
        StringBuilder formattedOutput = new StringBuilder();

        for(JsonElement jsonElement : events) {
            JsonObject event = jsonElement.getAsJsonObject();
            String eventType = event.get("type").getAsString();
            String repoName = event.get("repo").getAsJsonObject().get("name").getAsString();

            switch (eventType) {
                case "PushEvent" -> {
                    int commitCount = event.get("payload").getAsJsonObject().get("commits").getAsJsonArray().size();
                    pushEventCounts.put(repoName, pushEventCounts.getOrDefault(repoName, 0) + commitCount);
                    break;
                }
                case "WatchEvent" -> {
                    formattedOutput.append("Starred ").append(repoName).append("\n");
                    break;
                }
                case "ForkEvent" -> {
                    formattedOutput.append("Forked ").append(repoName).append("\n");
                    break;
                }
                case "PullRequestEvent" -> {
                    formattedOutput.append("Pull Requested ").append(repoName).append("\n");
                    break;
                }
                case "IssueEvent" -> {
                    formattedOutput.append("Issued ").append(repoName).append("\n");
                    break;
                }
                case "CreateEvent" -> {
                    formattedOutput.append("Created ").append(repoName).append("\n");
                    break;
                }
                default -> {
                    formattedOutput.append(eventType).append(" ").append(eventType).append("\n");
                    break;
                }
            }
        }

        for (Map.Entry<String, Integer> entry : pushEventCounts.entrySet()) {
            formattedOutput.append("Pushed ").append(entry.getValue()).append(" commit(s) to ").append(entry.getKey()).append("\n");
        }

        return formattedOutput.toString();

    }
}