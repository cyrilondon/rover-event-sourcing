package com.game.domain.model.entity.plateau;

import java.util.UUID;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.game.domain.model.exception.EntityValidationException;
import com.game.domain.rover.api.PlateauInitializeCmd;
import com.game.domain.rover.api.PlateauInitializedEvt;

public class PlateauTest {

	private FixtureConfiguration<Plateau> fixture;

	@BeforeMethod
	public void setUp() {
		fixture = new AggregateTestFixture<>(Plateau.class);
	}

	/*
	 * These four lines define the actual scenario and its expected result. The
	 * first line defines the events that happened in the past. These events define
	 * the state of the aggregate under test. In practical terms, these are the
	 * events that the event store returns when an aggregate is loaded.
	 * 
	 * The second line defines the command that we wish to execute against our
	 * system.
	 * 
	 * Finally, we have two more methods that define expected behavior. In the
	 * example, we use the recommended void return type. The last method defines
	 * that we expect a single event as result of the command execution.
	 */
	@Test
	public void testPlateauInitializeCmdSuccess() {
		UUID plateauUUID = UUID.randomUUID();
		int width = 5;
		int height = width;
		fixture.given().when(new PlateauInitializeCmd(plateauUUID, width, height)).expectSuccessfulHandlerExecution()
				.expectEvents(new PlateauInitializedEvt(plateauUUID, width, height));
	}

	@Test(expectedExceptions = NullPointerException.class, expectedExceptionsMessageRegExp = "Parameter specified as non-null is null:.*")
	public void testPlateauInitializeCmdNullUUID() {
		int width = 5;
		int height = width;
		fixture.given().when(new PlateauInitializeCmd(null, width, height));
	}

	@Test
	public void testPlateauInitializeCmdNegativeWidth() {
		UUID uuid = UUID.randomUUID();
		int width = -2;
		int height = 5;
		fixture.given().when(new PlateauInitializeCmd(uuid, width, height))
				.expectException(EntityValidationException.class)
				.expectExceptionMessage("[ERR-001] Plateau width [-2] should be strictly positive");
	}
	
	@Test
	public void testPlateauInitializeCmdNegativeWidthAndHeight() {
		UUID uuid = UUID.randomUUID();
		int width = -2;
		int height = width;
		String expectedErrorMessage = "[ERR-001] Plateau width [-2] should be strictly positive, Plateau height [-2] should strictly positive";
		fixture.given().when(new PlateauInitializeCmd(uuid, width, height))
				.expectException(EntityValidationException.class)
				.expectExceptionMessage(expectedErrorMessage);
	}

}
