package adept.kbapi.sql.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class provides temporary instances used by the KBQueryProcessor
 * in retrieving ADEPT objects from the KB.
 */
public class CorpusResult {
    private String ID;
    private String URI;    
    private String type;
    private String name;
    
    // parses the current row in the passed ResultSet and parses it. Does not move the cursor forward in the result set
    public CorpusResult(ResultSet resultSet) throws SQLException {
        if (resultSet.getRow() > 0) {
            ID = resultSet.getString("ID");
            URI = resultSet.getString("URI");
            type = resultSet.getString("type");
            name = resultSet.getString("name");
        }
    }
    
    public String getID() { return ID; }
    public String getURI() { return URI; }
    public String getType() { return type; }
    public String getName() { return name; }
}
