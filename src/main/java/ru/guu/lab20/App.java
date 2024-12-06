package ru.guu.lab20;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import okhttp3.*;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        BorderPane borderPane = new BorderPane();
        primaryStage.setTitle("HTTP Request App");

        // Элементы интерфейса
        TextField urlField = new TextField("https://jsonplaceholder.typicode.com/posts/1");
        urlField.setPromptText("Введите URL API");

        ComboBox<String> methodComboBox = new ComboBox<>();
        methodComboBox.getItems().addAll("GET", "POST");
        methodComboBox.setValue("GET");

        Button sendRequestButton = new Button("Отправить запрос");
        sendRequestButton.setId("sendRequest");


        TextArea responseArea = new TextArea();
        responseArea.setEditable(false);

        // Логика обработки кнопки
        sendRequestButton.setOnAction(e -> {
            String urlString = urlField.getText();
            String requestMethod = methodComboBox.getValue();

            try {
                String response;
                if (requestMethod.equals("GET")) {
                    response = sendGetRequest(urlString);
                } else if (requestMethod.equals("POST")) {
                    response = sendPostRequest(urlString);
                } else {
                    throw new Exception("Неверный метод запроса");
                }

                responseArea.setText(response);
            } catch (Exception ex) {
                responseArea.setText("Ошибка: " + ex.getMessage());
            }
        });

        // Компоновка


        borderPane.setTop(urlField);
        borderPane.setLeft(methodComboBox);
        borderPane.setRight(sendRequestButton);
        borderPane.setBottom(responseArea);

        root.getChildren().add(borderPane);
        root.setPadding(new Insets(10));


        Scene scene = new Scene(root, 600, 400);

        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    // Метод для выполнения GET-запроса
    private String sendGetRequest(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine).append("\n");
            }
            in.close();
            return response.toString();
        } else {
            throw new Exception("HTTP ошибка: " + responseCode);
        }
    }

    // Метод для выполнения POST-запроса
    private String sendPostRequest(String urlString) throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("key", "value")
                .build();

        Request request = new Request.Builder()
                .url(urlString)
                .post(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
