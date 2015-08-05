package adept.kbapi.sparql;

import com.google.common.collect.ImmutableMap;
import java.lang.StringBuffer;
import java.lang.Character;
import java.util.Map;

/**
 * 
 * escape Sparql special characters.
 */
public class SparqlUtils {
	
	/**
	 * See http://www.w3.org/TR/rdf-sparql-query/#grammarEscapes
	 * @param name
	 * @return
	 */
	private static final ImmutableMap<String, String> SPARQL_ESCAPE_SEARCH_REPLACEMENTS = ImmutableMap.<String,String>builder()
		.put("\t", "\\t")
		.put("\n", "\\n")
		.put("\r", "\\r")
		.put("\b", "\\b")
		.put("\f", "\\f")
		.put("\"", "\\\"")
		.put("'", "\\'")
		.put("\\", "\\\\")
		.build();

	
	public static String escape(String string) 
	{
		
		StringBuffer bufOutput = new StringBuffer(string);
		for (int i = 0; i < bufOutput.length(); i++) {
			String replacement = SPARQL_ESCAPE_SEARCH_REPLACEMENTS.get("" + bufOutput.charAt(i));
			if(replacement!=null) {
				bufOutput.deleteCharAt(i);
				bufOutput.insert(i, replacement);
				// advance past the replacement
				i += (replacement.length() - 1);
			}
		}
		return bufOutput.toString();
	}
	
}