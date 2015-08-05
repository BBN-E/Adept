package adept.kbapi.sql.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class provides temporary instances used by the KBQueryProcessor
 * in retrieving ADEPT objects from the KB.
 */
public class SourceAlgorithmsResult {
    private String algorithmName;
    private String contributingSiteName;
    
    // parses the current row in the passed ResultSet and parses it. Does not move the cursor forward in the result set
    public SourceAlgorithmsResult(ResultSet resultSet) throws SQLException {
        if (resultSet.getRow() > 0) {
            algorithmName = resultSet.getString("algorithmName");
            contributingSiteName = resultSet.getString("contributingSiteName");
        }
    }
    
    public String getAlgorithmName() { return algorithmName; }
    public String getContributingSiteName() { return contributingSiteName; }
}
