package com.yuri.hexagonal.adapters.out.client.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.admin.model.ListStubMappingsResult;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class WireMockStandaloneRunner {
    public static void main(String[] args) {
        WireMockServer server = new WireMockServer(
                options()
                        .port(8082)
                        .usingFilesUnderDirectory("./src/main/resources/wiremock")
        );
        server.start();
        ListStubMappingsResult stubMappings = server.listAllStubMappings();
        for (StubMapping stubMapping : stubMappings.getMappings()) {
            System.out.println(stubMapping);
        }
        System.out.println("WireMock rodando em http://localhost:8082");
    }
}
