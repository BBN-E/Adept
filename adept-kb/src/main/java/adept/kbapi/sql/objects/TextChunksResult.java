package adept.kbapi.sql.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class provides temporary instances used by the KBQueryProcessor
 * in retrieving ADEPT objects from the KB.
 */
public class TextChunksResult {
    private String ID;
    private String sourceDocument;
    private String value;
    private String serializedTokenStream;
    private int beginOffset;
    private int endOffset;
    
    // parses the current row in the passed ResultSet and parses it. Does not move the cursor forward in the result set
    public TextChunksResult(ResultSet resultSet) throws SQLException {
        if (resultSet.getRow() > 0) {
            ID = resultSet.getString("ID");
            sourceDocument = resultSet.getString("sourceDocument");
            value = resultSet.getString("value");
            serializedTokenStream = resultSet.getString("serializedTokenStream");
            beginOffset = resultSet.getInt("beginOffset");
            endOffset = resultSet.getInt("endOffset");
        }
    }
    
    public String getID() { return ID; }
    public String getSourceDocument() { return sourceDocument; }
    public String getValue() { return value; }
    public String getSerializedTokenStream() { return serializedTokenStream; }
    public int getBeginOffset() { return beginOffset; }
    public int getEndOffset() { return endOffset; }
}
