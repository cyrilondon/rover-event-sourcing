package com.rover.application.out.adapter.messaging;

import java.util.concurrent.CompletableFuture;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Component;

import com.rover.application.out.port.messaging.CommandPublisher;
import com.rover.domain.api.DomainCommand;

@Component
public class AxonMessagePublisher implements CommandPublisher {
	
	private final CommandGateway commandGateway;
	
	public AxonMessagePublisher(CommandGateway commandGateway) {
		this.commandGateway = commandGateway;
	}

	@Override
	public  <R> CompletableFuture<R> send(DomainCommand command) {
		return commandGateway.send(command);
	}

}
