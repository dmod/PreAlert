package dmod.prealert.tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.ghost4j.Ghostscript;
import org.ghost4j.GhostscriptException;
import org.junit.Test;

import dmod.prealert.Incident;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class IncidentParserTest {


	@Test
	public void defaultState() {

        //get Ghostscript instance
        Ghostscript gs = Ghostscript.getInstance();
 
        //prepare Ghostscript interpreter parameters
        //refer to Ghostscript documentation for parameter usage
        String[] gsArgs = new String[3];
        gsArgs[0] = "-sDEVICE=tiffpack";
        gsArgs[1] = "-osrc/test/resources/invest8115Baltimore.tiff";
        gsArgs[2] = "src/test/resources/invest8115Baltimore.bits";
 
        //execute and exit interpreter
        try {
 
            gs.initialize(gsArgs);
            gs.exit();
 
        } catch (GhostscriptException e) {
            e.printStackTrace();
        }
		
		ArrayList<String> command = new ArrayList<String>();
		command.add("C:/Program Files/gs/gs9.20/bin/gpcl6win32.exe");
		command.add("-sDEVICE=tiffpack");
		command.add("-osrc/test/resources/invest8115Baltimore.tiff");
		command.add("src/test/resources/invest8115Baltimore.bits");

		ProcessBuilder pb = new ProcessBuilder(command);

		Process p;
		try {
			p = pb.start();
			int exitCode = p.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.setProperty("jna.library.path", "C:/Program Files/gs/gs9.20/bin");
		
		File imageFile = new File("src/test/resources/invest8115Baltimore.tiff");
		ITesseract instance = new Tesseract();  // JNA Interface Mapping
        try {
            String result = instance.doOCR(imageFile);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }

        
		String id = "0";
		Incident incident = new Incident(id);
	}
	
}
