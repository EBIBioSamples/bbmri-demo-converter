package uk.ac.ebi.fg.biosd.bbmri_demo;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

/**
 * TODO: comment me!
 *
 * @author brandizi
 * <dl><dt>Date:</dt><dd>16 Mar 2015</dd>
 *
 */
public class BBSampleTabGenerator
{
	private Map<String, String> parameters = new HashMap<> ();
	private List<Pair<String, String>> sampleGroupColumns = null;
	
	public String generate ( String template )
	{
		try
		{
			String result1 = StrSubstitutor.replace ( template, parameters, "${", "}" );
			
			if ( sampleGroupColumns == null || sampleGroupColumns.size () == 0 ) return result1; 
			
			// Now find the group specification
			CSVReader csvr = new CSVReader ( new StringReader ( result1 ), '\t', '"' );
			
			StringWriter result2 = new StringWriter ();
			CSVWriter csvw = new CSVWriter ( result2, '\t', '"' );
			for ( String[] line ; (line = csvr.readNext ()) != null; )
			{
				if ( line.length > 0 && "Group Name".equalsIgnoreCase ( StringUtils.trimToNull ( line [ 0 ] ) ) )
				{
					// Assume this line contains the headers and the next one the values
					int oldlen = line.length;
					line = Arrays.copyOf ( line, oldlen + sampleGroupColumns.size () );

					String[] line1 = csvr.readNext ();
					line1 = Arrays.copyOf ( line1, line.length );
					
					int j = oldlen;
					for ( Pair<String, String> p: sampleGroupColumns )
					{
						line [ j ] = p.getKey ();
						line1 [ j++ ] = p.getValue ();
					}
					
					csvw.writeNext ( line );
					csvw.writeNext ( line1 );
				}
				else
					csvw.writeNext ( line );
			}
			csvw.close ();
			csvr.close ();
			
			return result2.toString ();
		}
		catch ( IOException ex )
		{
			throw new RuntimeException ( "Internal error while generating SampleTab: " + ex.getMessage (), ex );
		}
	}

	public Map<String, String> getParameters ()
	{
		return parameters;
	}

	public void setParameters ( Map<String, String> parameters )
	{
		this.parameters = parameters;
	}
	
	public Object setParameter ( String key, String value )
	{
		return this.parameters.put ( key, value );
	}

	public Object setParameter ( String key, Date date )
	{
		if ( date == null ) return this.setParameter ( key, (String) null );
		return this.setParameter ( key, new SimpleDateFormat ( "yyyy/MM/dd" ).format ( date ) );
	}

	
	public void setSampleGroupColumns ( List<Pair<String, String>> sgcols )
	{
		this.sampleGroupColumns = sgcols;
	}

	public List<Pair<String, String>> getSampleGroupColumns ( List<Pair<String, String>> sgcols )
	{
		return this.sampleGroupColumns;
	}

	public void addSampleGroupColumn ( String key, String value )
	{
		if ( this.sampleGroupColumns == null )
			this.sampleGroupColumns = new ArrayList<Pair<String,String>> ();
		this.sampleGroupColumns.add ( new MutablePair<String, String> ( key, value ) );
	}
}
