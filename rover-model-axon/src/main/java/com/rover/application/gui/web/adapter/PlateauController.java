package com.rover.application.gui.web.adapter;

import java.util.concurrent.CompletableFuture;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rover.application.command.dto.plateau.PlateauInitializeCmdDto;
import com.rover.domain.command.model.service.plateau.PlateauCommandService;
import com.rover.domain.query.FindPlateauSummaryQuery;
import com.rover.domain.query.PlateauSummary;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(value = "/plateau")
@Api(value = "Plateau Commands", tags = "Plateau Commands")
public class PlateauController {

	private final PlateauCommandService plateauCommandService;
	
	private final QueryGateway queryGateway;

	public PlateauController(PlateauCommandService plateauCommandService, QueryGateway queryGateway) {
		this.plateauCommandService = plateauCommandService;
		this.queryGateway = queryGateway;
	}

	@PostMapping
	public CompletableFuture<String> createPlateau(@RequestBody PlateauInitializeCmdDto plateauInitializeCmdDto) {
		return plateauCommandService.initializePlateau(plateauInitializeCmdDto);
	}

	@PutMapping(value = "/{plateauId}")
	public CompletableFuture<String> desactivatePlateau(@PathVariable(value = "plateauId") String plateauUUID) {
		return plateauCommandService.desactivatePlateau(plateauUUID);
	}
	
	@GetMapping(value = "/{plateauId}")
	public CompletableFuture<PlateauSummary> handle(@PathVariable(value = "plateauId") String plateauUUID) {
		return queryGateway.query(new FindPlateauSummaryQuery(plateauUUID), ResponseTypes.instanceOf(PlateauSummary.class));
	}

}
