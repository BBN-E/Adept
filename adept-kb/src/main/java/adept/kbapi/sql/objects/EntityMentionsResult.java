package adept.kbapi.sql.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class provides temporary instances used by the KBQueryProcessor
 * in retrieving ADEPT objects from the KB.
 */
public class EntityMentionsResult {
    private String ID;
    private String mentionType;
    private String KBEntityId;
    private float confidence;
    private String chunk;
    private String sourceAlgorithm;
    
    // parses the current row in the passed ResultSet and parses it. Does not move the cursor forward in the result set
    public EntityMentionsResult(ResultSet resultSet) throws SQLException {
        if (resultSet.getRow() > 0) {
            ID = resultSet.getString("ID");
            mentionType = resultSet.getString("mentionType");
            KBEntityId = resultSet.getString("KBEntityId");
            confidence = resultSet.getFloat("confidence");
            chunk = resultSet.getString("chunk");
            sourceAlgorithm = resultSet.getString("sourceAlgorithm");
        }
    }    
    
    public String getID() { return ID; }
    public String getMentionType() { return mentionType; }
    public String getKBEntityId() { return KBEntityId; }
    public float getConfidence() { return confidence; }
    public String getChunk() { return chunk; }
    public String getSourceAlgorithm() { return sourceAlgorithm; }    
}
