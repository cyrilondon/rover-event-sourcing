package com.rover.application.gui.web.adapter;

import java.util.UUID;
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
import com.rover.domain.api.PlateauDesactivateCmd;
import com.rover.domain.api.PlateauInitializeCmd;
import com.rover.domain.command.model.service.command.PlateauCommandMapper;
import com.rover.domain.command.model.service.plateau.PlateauCommandService;
import com.rover.domain.query.FindPlateauSummaryQuery;
import com.rover.domain.query.PlateauSummary;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(value = "/plateau")
@Api(value = "Plateau Commands", tags = "Plateau Commands")
public class PlateauController {

	private final PlateauCommandService plateauCommandService;
	
	private final PlateauCommandMapper plateauCommandMapper;
	
	private final QueryGateway queryGateway;
	

	public PlateauController(PlateauCommandService plateauCommandService, PlateauCommandMapper plateauCommandMapper, QueryGateway queryGateway) {
		this.plateauCommandService = plateauCommandService;
		this.plateauCommandMapper = plateauCommandMapper;
		this.queryGateway = queryGateway;
	}

	@PostMapping
	public CompletableFuture<UUID> createPlateau(@RequestBody PlateauInitializeCmdDto plateauInitializeCmdDto) {
		PlateauInitializeCmd cmd = plateauCommandMapper.toPlateauInitializeCmd(plateauInitializeCmdDto);
		return plateauCommandService.initializePlateau(cmd);
	}

	@PutMapping(value = "/{plateauId}")
	public void desactivatePlateau(@PathVariable(value = "plateauId") String plateauUUID) {
		PlateauDesactivateCmd cmd = plateauCommandMapper.toPlateauDesactivateCmd(plateauUUID);
		plateauCommandService.desactivatePlateau(cmd);
	}
	
	@GetMapping(value = "/{plateauId}")
	public CompletableFuture<PlateauSummary> handle(@PathVariable(value = "plateauId") String plateauUUID) {
		return queryGateway.query(new FindPlateauSummaryQuery(plateauUUID), ResponseTypes.instanceOf(PlateauSummary.class));
	}

}
