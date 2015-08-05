package adept.kbapi.sql.objects;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class provides temporary instances used by the KBQueryProcessor
 * in retrieving ADEPT objects from the KB.
 */
public class SourceDocumentsResult {
    private String ID;
    private String sourceLanguageId;
    private String corpus;
    private Date publicationDate;
    private String URI;
    
    // parses the current row in the passed ResultSet and parses it. Does not move the cursor forward in the result set
    public SourceDocumentsResult(ResultSet resultSet) throws SQLException {
        if (resultSet.getRow() > 0) {
            ID = resultSet.getString("ID");
            sourceLanguageId = resultSet.getString("sourceLanguage");
            corpus = resultSet.getString("corpus");
            publicationDate = resultSet.getDate("publicationDate");
            URI = resultSet.getString("URI");
        }
    }
    
    public String getID() { return ID; }
    public String getSourceLanguageId() { return sourceLanguageId; }
    public String getCorpus() { return corpus; }
    public Date getPublicationDate() { return publicationDate; }
    public String getUri() { return URI; }
}
