package com.rover.core.util;

import java.io.StringReader;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.rover.domain.command.model.entity.plateau.PlateauDto;
import com.rover.domain.command.model.entity.rover.RoverInitializedBroadCastEventDto;
import com.rover.domain.command.model.exception.IllegalArgumentGameException;

public class SerializeUtils {

	static XPathFactory xpf = XPathFactory.newInstance();

	public static PlateauDto readFromEvent(String payload) {
		InputSource is = new InputSource(new StringReader(payload));
		try {
			NodeList nodes = (NodeList) xpf.newXPath().evaluate("/*/*", is, XPathConstants.NODESET);
			return new PlateauDto(nodes.item(0).getTextContent(), Integer.valueOf(nodes.item(1).getTextContent()),
					Integer.valueOf(nodes.item(2).getTextContent()));
		} catch (XPathExpressionException e) {
			throw new IllegalArgumentGameException("Unable to deserialize the plateau binary format");
		}

	}
	
	public static RoverInitializedBroadCastEventDto readFromBroadCast(String payload) {
		InputSource is = new InputSource(new StringReader(payload));
		try {
			NodeList nodes = (NodeList) xpf.newXPath().evaluate("/*/*", is, XPathConstants.NODESET);
			return new RoverInitializedBroadCastEventDto(nodes.item(0).getTextContent(), nodes.item(1).getTextContent(), Integer.valueOf(nodes.item(2).getTextContent()),
					Integer.valueOf(nodes.item(3).getTextContent()));
		} catch (XPathExpressionException e) {
			throw new IllegalArgumentGameException("Unable to deserialize the Rover broadcast event binary format");
		}

	}

}
