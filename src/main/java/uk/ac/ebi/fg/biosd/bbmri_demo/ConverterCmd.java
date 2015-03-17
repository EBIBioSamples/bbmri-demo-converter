package uk.ac.ebi.fg.biosd.bbmri_demo;

import static java.lang.System.out;

import uk.ac.ebi.utils.io.IOUtils;

import java.io.FileReader;
import java.io.PrintWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A skeleton for a typical Command Line entry point.
 * 
 */
public class ConverterCmd
{
	/**
	 * If you set this to true, main() will not invoke {@link System#exit(int)}. This is useful in unit tests.
	 */
	public static final String NO_EXIT_PROP = "uk.ac.ebi.debug.no_jvm_exit"; 
			
	private static int exitCode = 0;
	
	private static Logger log = LoggerFactory.getLogger ( ConverterCmd.class );

	
	
	public static void main ( String... args )
	{
		try
		{
			exitCode = 0;
			CommandLineParser clparser = new GnuParser ();
			CommandLine cli = clparser.parse ( getOptions(), args );
			
			args = cli.getArgs ();
			
			if ( cli.hasOption ( "help" ) || args.length == 0 ) 
			{
				printUsage ();
				return;
			}
			
			String templatePath = cli.getOptionValue ( "template",  "template.sampletab.tsv" );
			String bbId = args [ 0 ];
			
			// TODO: Get the data from the BBMRI instance 
			
			BBSampleTabGenerator stabGen = new BBSampleTabGenerator ();

			stabGen.setParameter ( "biobankId", bbId );
			// TODO Fill the generator with the rest of the data 
			
			String result = stabGen.generate ( IOUtils.readInputFully ( new FileReader ( templatePath ) ) );
			
			System.out.println ( result );
			
			log.info ( "The End!" );
		}
		catch ( Throwable ex ) 
		{
			log.error ( "Execution failed with the error: " + ex.getMessage (), ex  );
			exitCode = 1; // TODO: proper exit codes
		}
		finally 
		{
			if ( !"true".equals ( System.getProperty ( NO_EXIT_PROP ) ) )
				System.exit ( exitCode );
		}
	}
	
	@SuppressWarnings ( "static-access" )
	private static Options getOptions ()
	{
		Options opts = new Options ();

		opts.addOption ( OptionBuilder
			.withDescription ( "Prints out this message" )
			.withLongOpt ( "help" )
			.create ( 'h' )
		)
		.addOption ( OptionBuilder
			.withLongOpt ( "template" )
			.withArgName ( "path" )
			.withDescription ( "The path to the SampleTab template file (default: 'template.sampletab.tsv'" )
			.hasArg ( true )
			.create ( "t" )
		);
		
		return opts;		
	}
	
	private static void printUsage ()
	{
		out.println ();

		out.println ( "\n\n *** BBMRI Demo, BioBank -> SampleTab Converter ***" );
		out.println ( "\nConverts from BBMRI Demo Biobanks to SampleTab submissions" );
		
		out.println ( "\nSyntax:" );
		out.println ( "\n\tconvert.sh [options] <biobank-ID>" );		
		
		out.println ( "\nOptions:" );
		HelpFormatter helpFormatter = new HelpFormatter ();
		PrintWriter pw = new PrintWriter ( out, true );
		helpFormatter.printOptions ( pw, 100, getOptions (), 2, 4 );
		
		out.println ( "\n\n" );
		
		exitCode = 1;
	}

	/**
	 * This can be used when {@link #NO_EXIT_PROP} is "true" and you're invoking {@link #main(String...)} from 
	 * a JUnit test. It tells you the OS exit code that the JVM would return upon exit.
	 */
	public static int getExitCode ()
	{
		return exitCode;
	}

}
