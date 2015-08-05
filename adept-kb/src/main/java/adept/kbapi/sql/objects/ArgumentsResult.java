package adept.kbapi.sql.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class provides temporary instances used by the KBQueryProcessor
 * in retrieving ADEPT objects from the KB.
 */
public class ArgumentsResult {
    private String ID;
    private String entityMention;
    private float confidence;
    private String relationMention;
    private String argType;
    
    // parses the current row in the passed ResultSet and parses it. Does not move the cursor forward in the result set
    public ArgumentsResult(ResultSet resultSet) throws SQLException {
        if (resultSet.getRow() > 0) {
            ID = resultSet.getString("ID");
            confidence = resultSet.getFloat("confidence");
            entityMention = resultSet.getString("entityMention");
            relationMention = resultSet.getString("relationMention");
            argType = resultSet.getString("argType");            
        }
    }
    
    public String getID() { return ID; }
    public float getConfidence() {return confidence; }
    public String getEntityMention() { return entityMention; }
    public String getRelationMention() { return relationMention; }
    public String getArgType() { return argType; }
}
