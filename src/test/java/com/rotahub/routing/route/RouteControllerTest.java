package com.rotahub.routing.route;

import com.rotahub.routing.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RouteControllerTest {

    @Value("${local.server.port}")
    private int port;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Test
    void createsAndOptimizesARoute() throws Exception {
        String payload = """
            {"stops":[
              {"orderId":"11111111-1111-1111-1111-111111111111","address":"Congonhas","lat":-23.6266,"lng":-46.6553},
              {"orderId":"22222222-2222-2222-2222-222222222222","address":"Paulista","lat":-23.5613,"lng":-46.6565},
              {"orderId":"33333333-3333-3333-3333-333333333333","address":"Ibirapuera","lat":-23.5874,"lng":-46.6576}
            ]}
            """;

        HttpResponse<String> response = post("/routes", payload);

        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(response.body()).contains("\"status\":\"PLANNED\"", "totalDistanceKm");
    }

    @Test
    void rejectsRouteWithNoStops() throws Exception {
        HttpResponse<String> response = post("/routes", "{\"stops\":[]}");

        assertThat(response.statusCode()).isEqualTo(400);
    }

    @Test
    void getsRouteById() throws Exception {
        String created = post("/routes", """
            {"stops":[
              {"orderId":"11111111-1111-1111-1111-111111111111","address":"A","lat":-23.5,"lng":-46.6},
              {"orderId":"22222222-2222-2222-2222-222222222222","address":"B","lat":-23.55,"lng":-46.65}
            ]}
            """).body();
        String id = extractId(created);

        HttpResponse<String> response = get("/routes/" + id);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains(id);
    }

    @Test
    void returnsNotFoundForUnknownRoute() throws Exception {
        HttpResponse<String> response = get("/routes/00000000-0000-0000-0000-000000000000");

        assertThat(response.statusCode()).isEqualTo(404);
    }

    private HttpResponse<String> post(String path, String body) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl() + path))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> get(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl() + path))
            .GET()
            .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    private String extractId(String body) {
        Matcher matcher = Pattern.compile("\"id\":\"([^\"]+)\"").matcher(body);
        matcher.find();
        return matcher.group(1);
    }
}
