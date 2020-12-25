package com.rover.application.gui.web.adapter;

import java.util.concurrent.CompletableFuture;

import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rover.application.command.dto.RoverInitializeCmdDto;
import com.rover.domain.api.RoverInitializeCmd;
import com.rover.domain.command.model.entity.rover.RoverIdentifier;
import com.rover.domain.command.model.service.command.RoverCommandMapper;
import com.rover.domain.command.model.service.rover.RoverService;

import io.swagger.annotations.Api;

/**
 * Vaadin by nature does not need REST Service as the 'client code' is on server side.
 * Rest servlet are just here for illustration purpose
 *
 */
@RestController
@RequestMapping(value = "/rover")
@Api(value = "Rover Commands", tags = "Rover Commands")
public class RoverController {

	private final RoverService roverService;

	private final RoverCommandMapper roverCommandMapper;

	private final QueryGateway queryGateway;

	public RoverController(RoverService roverService, RoverCommandMapper roverCommandMapper,
			QueryGateway queryGateway) {
		this.roverService = roverService;
		this.roverCommandMapper = roverCommandMapper;
		this.queryGateway = queryGateway;
	}

	@PostMapping
	public CompletableFuture<RoverIdentifier> create(@RequestBody RoverInitializeCmdDto roverInitializeCmdDto) {
		RoverInitializeCmd cmd = roverCommandMapper.toRoverInitializeCmd(roverInitializeCmdDto);
		return roverService.initializeRover(cmd);
	}

}
