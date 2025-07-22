package org.typescript.example.bdd;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.nio.file.Paths.get;

public class WireMockSetup {
    private static WireMockServer wireMockServer;

    public static void startIfNotRunning() {
        if (wireMockServer == null || !wireMockServer.isRunning()) {
            wireMockServer = new WireMockServer(8089); // default port
            wireMockServer.start();

            configureFor("localhost", 8089);
            stubFor(WireMock.get(urlEqualTo("/api/test"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "text/plain")
                            .withBody("Hello from WireMock")));
        }
    }

    public static void stop() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }
}
