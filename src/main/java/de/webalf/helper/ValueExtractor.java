package de.webalf.helper;

import de.webalf.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Helper to return Values from XML document
 *
 * @author Alf
 * @version 2.0
 * @since 14.03.2020
 */
public class ValueExtractor {
	/**
	 * Returns the values associated to the given tag. These Tags must be included in the xml file returned from the url
	 *
	 * @param url     to xml document
	 * @param tagName to which the values are to be returned
	 * @return List of values
	 */
	public static List<String> getValues(String url, String tagName) {
		return getString(tagName, getElement(url));
	}

	/**
	 * Returns the Element parsed from the url response
	 *
	 * @param url to send the get request to
	 * @return element from the url
	 */
	private static Element getElement(String url) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			final String errorMessage = "Error while creating Document Builder";
			Logger.logSevere(errorMessage, e);
			throw new RuntimeException(errorMessage);
		}
		Document document;
		try {
			document = builder.parse(new InputSource(new StringReader(getPathList(url))));
		} catch (SAXException | IOException e) {
			String errorMessage = "Exception while parsing response to document from url: " + url;
			Logger.logSevere(errorMessage, e);
			throw new RuntimeException(errorMessage);
		}
		return document.getDocumentElement();
	}

	/**
	 * Returns the response from given Url
	 *
	 * @param url to send get request
	 * @return string with response
	 * @throws RuntimeException when HTTP calls fails
	 */
	private static String getPathList(String url) {
		InputStream response;
		try {
			response = new URL(url).openStream();
		} catch (IOException e) {
			String errorMessage = "Error while getting PathList from url: " + url;
			Logger.logSevere(errorMessage, e);
			throw new RuntimeException(errorMessage);
		}

		try (Scanner scanner = new Scanner(response)) {
			return scanner.useDelimiter("\\A").next();
		}
	}

	/**
	 * Returns all values included in the given tag from the element
	 *
	 * @param tagName to search for
	 * @param element in which the tag is included
	 * @return List of values
	 */
	private static ArrayList<String> getString(String tagName, Element element) {
		NodeList list = element.getElementsByTagName(tagName);
		ArrayList<String> ret = new ArrayList<>();
		if (list != null && list.getLength() > 0) {
			for (int i = 0; i < list.getLength(); i++) {
				NodeList subList = list.item(i).getChildNodes();
				if (subList != null && subList.getLength() > 0) {
					for (int j = 0; j < subList.getLength(); j++) {
						ret.add(subList.item(j).getNodeValue());
					}
				}
			}
		}
		return ret;
	}
}
