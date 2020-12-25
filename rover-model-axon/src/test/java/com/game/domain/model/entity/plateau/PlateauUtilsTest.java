package com.game.domain.model.entity.plateau;

import static org.testng.Assert.assertEquals;

import javax.xml.xpath.XPathExpressionException;

import org.testng.annotations.Test;

import com.rover.domain.command.model.entity.plateau.PlateauDto;
import com.rover.domain.command.model.entity.plateau.PlateauUtils;

public class PlateauUtilsTest {
	
	@Test
	public void testPayLoad() throws XPathExpressionException {
		String payload = "<com.rover.domain.api.PlateauInitializedEvt><id>d034dc23-d01a-4d50-98d7-0afbbb287563</id><width>3</width><height>5</height></com.rover.domain.api.PlateauInitializedEvt>";
		PlateauDto dto = PlateauUtils.readFromEvent(payload);
		    assertEquals(dto.getId(), "d034dc23-d01a-4d50-98d7-0afbbb287563");
		    assertEquals(dto.getWidth(), 3);
		    assertEquals(dto.getHeight(), 5);
	}

}
