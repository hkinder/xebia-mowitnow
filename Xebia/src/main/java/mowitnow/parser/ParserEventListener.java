package mowitnow.parser;

import java.io.IOException;
import java.io.OutputStream;

public interface ParserEventListener {

	public void stepChanged(ParserStep previousStep, ParserStep nextStep, OutputStream outputToWriteTo) throws IOException;

	public void handleChar(Character nextChar, ParserStep currentStep, OutputStream outputToWriteTo) throws IOException;

}
