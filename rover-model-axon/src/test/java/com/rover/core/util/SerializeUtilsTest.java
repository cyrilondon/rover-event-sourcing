package com.rover.core.util;

import static org.testng.Assert.assertEquals;

import javax.xml.xpath.XPathExpressionException;

import org.testng.annotations.Test;

import com.rover.core.util.SerializeUtils;
import com.rover.domain.command.model.entity.plateau.PlateauDto;

public class SerializeUtilsTest {
	
	@Test
	public void testPayLoad() throws XPathExpressionException {
		String payload = "<com.rover.domain.api.PlateauInitializedEvt><id>d034dc23-d01a-4d50-98d7-0afbbb287563</id><width>3</width><height>5</height></com.rover.domain.api.PlateauInitializedEvt>";
		PlateauDto dto = SerializeUtils.readFromEvent(payload);
		    assertEquals(dto.getId(), "d034dc23-d01a-4d50-98d7-0afbbb287563");
		    assertEquals(dto.getWidth(), 3);
		    assertEquals(dto.getHeight(), 5);
	}
	
	@Test
	public void testRoverPayLoad() {
		String payload = "<BroadCastEvt><name>rover1</name><abscissa>3</abscissa><ordinate>5</ordinate></BroadCastEvt>";
	}

}
