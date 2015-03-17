package uk.ac.ebi.fg.biosd.bbmri_demo;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.fg.biosd.bbmri_demo.ConverterCmd;

/**
 * Unit test for the example CLI {@link ConverterCmd}.
 */
public class ConverterCmdTest
{
	private static Logger log = LoggerFactory.getLogger ( ConverterCmdTest.class );

	@BeforeClass
	public static void setNoExitOption ()
	{
		// Prevents the CLI from invoking System.exit()
		System.setProperty ( ConverterCmd.NO_EXIT_PROP, "true" );
	}
	
	
	
	@Test
	public void testBasics()
	{
		// Capture the output into memory
		PrintStream outBkp = System.out;
		ByteArrayOutputStream outBuf = new ByteArrayOutputStream ();
		System.setOut ( new PrintStream ( outBuf ) );
		
		ConverterCmd.main (  "--template", "target/test-classes/template.sampletab.tsv", "biobank-123" );

		System.setOut ( outBkp ); // restore the original output
		
		log.debug ( "CLI output:\n{}", outBuf.toString () );
		assertTrue ( "Can't find CLI output!", outBuf.toString ().contains ( "biobank-123" ) );
		assertEquals ( "Bad exit code!", 0, ConverterCmd.getExitCode () );
	}

	@Test
	public void testHelpOption()
	{
		// Capture the output into memory
		PrintStream outBkp = System.out;
		ByteArrayOutputStream outBuf = new ByteArrayOutputStream ();
		System.setOut ( new PrintStream ( outBuf ) );

		ConverterCmd.main (  "--help" );
		
		System.setOut ( outBkp );  // restore the original output

		log.debug ( "CLI output:\n{}", outBuf.toString () );
		assertTrue ( "Can't find CLI output!", outBuf.toString ().contains ( "BBMRI Demo, BioBank -> SampleTab Converter" ) );
		assertEquals ( "Bad exit code!", 1, ConverterCmd.getExitCode () );
	}

}
