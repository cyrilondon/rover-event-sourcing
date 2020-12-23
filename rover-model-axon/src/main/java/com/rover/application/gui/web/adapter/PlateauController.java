package com.rover.application.gui.web.adapter;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rover.application.command.dto.PlateauInitializeCmdDto;
import com.rover.domain.api.PlateauDesactivateCmd;
import com.rover.domain.api.PlateauInitializeCmd;
import com.rover.domain.command.model.service.command.PlateauCommandMapper;
import com.rover.domain.command.model.service.plateau.PlateauService;
import com.rover.domain.query.FindAllPlateauSummaryQuery;
import com.rover.domain.query.FindPlateauSummaryQuery;
import com.rover.domain.query.PlateauSummary;
import com.rover.domain.query.PlateauSummaryFilter;

import io.swagger.annotations.Api;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/plateau")
@Api(value = "Plateau Commands", tags = "Plateau Commands")
public class PlateauController {

	private final PlateauService plateauCommandService;

	private final PlateauCommandMapper plateauCommandMapper;

	private final QueryGateway queryGateway;

	private final ReactorQueryGateway reactorQueryGateway;

	public PlateauController(PlateauService plateauCommandService, PlateauCommandMapper plateauCommandMapper,
			QueryGateway queryGateway, ReactorQueryGateway reactorQueryGateway) {
		this.plateauCommandService = plateauCommandService;
		this.plateauCommandMapper = plateauCommandMapper;
		this.queryGateway = queryGateway;
		this.reactorQueryGateway = reactorQueryGateway;
	}

	@PostMapping
	public CompletableFuture<UUID> create(@RequestBody PlateauInitializeCmdDto plateauInitializeCmdDto) {
		PlateauInitializeCmd cmd = plateauCommandMapper.toPlateauInitializeCmd(plateauInitializeCmdDto);
		return plateauCommandService.initializePlateau(cmd);
	}

	@PutMapping(value = "/{plateauId}")
	public void desactivate(@PathVariable(value = "plateauId") String plateauUUID) {
		PlateauDesactivateCmd cmd = plateauCommandMapper.toPlateauDesactivateCmd(plateauUUID);
		plateauCommandService.desactivatePlateau(cmd);
	}

	@GetMapping(value = "/{plateauId}")
	public CompletableFuture<PlateauSummary> findById(@PathVariable(value = "plateauId") String plateauUUID) {
		return queryGateway.query(new FindPlateauSummaryQuery(plateauUUID), PlateauSummary.class);
	}

	@GetMapping(value = "/all")
	public Mono<List<PlateauSummary>> findAll() {
		return reactorQueryGateway.query(new FindAllPlateauSummaryQuery(0, 20, new PlateauSummaryFilter("")),
				ResponseTypes.multipleInstancesOf(PlateauSummary.class));
	}

}
