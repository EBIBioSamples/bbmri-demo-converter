package uk.ac.ebi.fg.biosd.bbmri_demo;

import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.utils.io.IOUtils;

/**
 * TODO: comment me!
 *
 * @author brandizi
 * <dl><dt>Date:</dt><dd>16 Mar 2015</dd>
 *
 */
public class BBSampleTabGeneratorTest
{
	Logger log = LoggerFactory.getLogger ( this.getClass () );
	
	@Test
	public void basicTest () throws IOException
	{
		String tpl = IOUtils.readResource ( this.getClass (), "/template.sampletab.tsv" );

		BBSampleTabGenerator gen = new BBSampleTabGenerator ();
		
		gen.setParameter ( "biobankId", "biobank-123" );
		gen.setParameter ( "biosdSampleGroupId", "SampleGroup-456" );
		gen.setParameter ( "releaseDate", new DateTime ( 2015, 2, 23, 0, 0, 0 ).toDate () );
		gen.addSampleGroupColumn ( "Material", "DNA" );
		
		String stab = gen.generate ( tpl );
		
		log.info ( "--- Generated SampleTab: {}", stab );
		
		// TODO: assertions
	}
}
