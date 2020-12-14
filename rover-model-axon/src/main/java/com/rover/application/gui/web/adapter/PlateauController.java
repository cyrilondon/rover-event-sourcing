package com.rover.application.gui.web.adapter;

import java.util.concurrent.CompletableFuture;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rover.application.command.dto.plateau.PlateauInitializeCmdDto;
import com.rover.domain.model.service.plateau.PlateauCommandService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(value = "/plateau")
@Api(value = "Plateau Commands", tags = "Plateau Commands")
public class PlateauController {

	private final PlateauCommandService plateauCommandService;

	public PlateauController(PlateauCommandService plateauCommandService) {
		this.plateauCommandService = plateauCommandService;
	}

	@PostMapping
	public CompletableFuture<String> createPlateau(@RequestBody PlateauInitializeCmdDto plateauInitializeCmdDto) {
		return plateauCommandService.initializePlateau(plateauInitializeCmdDto);
	}

	@PutMapping(value = "/{plateauId}")
	public CompletableFuture<String> desactivatePlateau(@PathVariable(value = "plateauId") String plateauUUID) {
		return plateauCommandService.desactivatePlateau(plateauUUID);
	}

}
