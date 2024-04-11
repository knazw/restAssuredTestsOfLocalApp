package org.cypress.example;


import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventHandler;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestRunFinished;
import org.slf4j.Logger;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

public class TestEventHandlerPlugin implements ConcurrentEventListener {

    static final Logger log = getLogger(lookup().lookupClass());

    @Override
    public void setEventPublisher(EventPublisher eventPublisher) {
        eventPublisher.registerHandlerFor(TestRunFinished.class, teardown);
    }

    private EventHandler<TestRunFinished> teardown = event -> {
        log.debug("AFTER ALL");
        //run code after all tests
    };
}


