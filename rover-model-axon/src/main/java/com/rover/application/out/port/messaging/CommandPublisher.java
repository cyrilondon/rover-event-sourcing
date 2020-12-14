package com.rover.application.out.port.messaging;

import java.util.concurrent.CompletableFuture;

import com.rover.domain.api.DomainCommand;

public interface CommandPublisher {
	
	 <R> CompletableFuture<R> send(DomainCommand command);

}
