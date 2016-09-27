package mowitnow.parser;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import mowitnow.mower.MowerParserEventListener;

public class TestInputStreamParser {

	@Test
	public void testParseXebiaExample() throws IOException {
		// Instantiate the dependencies
		InputStreamParser dataParser = InputStreamParser.getInstance();
		InputStream input = new ByteArrayInputStream("5 5\n1 2 N\nGAGAGAGAA\n3 3 E\nAADAADADDA\n".getBytes());
		MowerParserEventListener eventListener = new MowerParserEventListener(); 
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		// start the test
		dataParser.parse(input, eventListener, output);
		assertEquals("1 3 N\n5 1 E\n", output.toString());
	}

	@Test
	public void testParseEmptyOrder() throws IOException {
		// Instantiate the dependencies
		InputStreamParser dataParser = InputStreamParser.getInstance();
		InputStream input = new ByteArrayInputStream("5 5\n1 2 N\n\n3 3 E\n\n".getBytes());
		MowerParserEventListener eventListener = new MowerParserEventListener(); 
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		// start the test
		dataParser.parse(input, eventListener, output);
		assertEquals("1 2 N\n3 3 E\n", output.toString());
	}

	@Test
	public void testParseBigPlayground() throws IOException {
		// Instantiate the dependencies
		InputStreamParser dataParser = InputStreamParser.getInstance();
		InputStream input = new ByteArrayInputStream("92233720368547758070 92233720368547758070\n9223372036854775807 9223372036854775807 N\nA\n9223372036854775807 9223372036854775807 E\nA\n".getBytes());
		MowerParserEventListener eventListener = new MowerParserEventListener(); 
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		// start the test for value bigger than Long.MAX_VALUE = 9223372036854775807L
		dataParser.parse(input, eventListener, output);
		assertEquals("9223372036854775807 9223372036854775808 N\n9223372036854775808 9223372036854775807 E\n", output.toString());
	}

	@Test
	public void testParseInputWithNoise() throws IOException {
		// Instantiate the dependencies
		InputStreamParser dataParser = InputStreamParser.getInstance();
		InputStream input = new ByteArrayInputStream("9^H0 \t 92+°\n\r\r\nazerty5&0 8AGD5 0N1E3W4AGDS\nANEWS0123456789GDA".getBytes());
		MowerParserEventListener eventListener = new MowerParserEventListener(); 
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		// start the test the clean input was "90 92\n50 85 N\nAGDA\n"
		dataParser.parse(input, eventListener, output);
		assertEquals("50 87 N\n", output.toString());
	}



}
