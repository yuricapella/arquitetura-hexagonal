package com.yuri.hexagonal.adapters.out.client.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class WireMockStandaloneRunner {
    public static void main(String[] args) {
        WireMockServer server = new WireMockServer(
                options()
                        .port(8082)
                        .usingFilesUnderDirectory("src/main/java/com/yuri/hexagonal/adapters/out/client/wiremock")
        );
        server.start();
        System.out.println("WireMock rodando em http://localhost:8082");
    }
}
