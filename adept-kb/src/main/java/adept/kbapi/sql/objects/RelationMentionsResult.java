package adept.kbapi.sql.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class provides temporary instances used by the KBQueryProcessor
 * in retrieving ADEPT objects from the KB.
 */
public class RelationMentionsResult {
    private String ID;
    private String relationType;
    private String KBRelationId;
    private float confidence;
    private String chunk;
    private String sourceAlgorithm;
    
    // parses the current row in the passed ResultSet and parses it. Does not move the cursor forward in the result set
    public RelationMentionsResult(ResultSet resultSet) throws SQLException {
        if (resultSet.getRow() > 0) {
            ID = resultSet.getString("ID");
            relationType = resultSet.getString("relationType");
            KBRelationId = resultSet.getString("KBRelationId");
            confidence = resultSet.getFloat("confidence");
            chunk = resultSet.getString("chunk");
            sourceAlgorithm = resultSet.getString("sourceAlgorithm");
        }
    }    
    
    public String getID() { return ID; }
    public String getRelationType() { return relationType; }
    public String getKBRelationId() { return KBRelationId; }
    public float getConfidence() { return confidence; }
    public String getChunk() { return chunk; }
    public String getSourceAlgorithm() { return sourceAlgorithm; }    
}
