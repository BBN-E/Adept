package adept.data;

import java.util.List;

import adept.common.*;

// TODO: Auto-generated Javadoc
/**
 * The Interface IIngest.
 */
public interface IIngest {

    /**
     * Create index.
     *
     * @param indexName name for new index
     */
    void createIndex(String indexName);

	/**
	 * Upload.
	 *
	 * @param aHltContentContainer the a hlt content container
	 * @return the string
	 */
	String upload(HltContentContainer aHltContentContainer);

	/**
	 * Upload.
	 *
	 * @param aHltContentContainer the a hlt content container
     * @param index the index
     * @param type the type
	 * @return the string
	 */
	String upload(HltContentContainer aHltContentContainer, String index, String type);

	/**
	 * Upload.
	 *
	 * @param json the json
     * @param index the index
     * @param type the type

	 * @return the string
	 */
	String upload_JSON(String json, String index, String type);
	
	/**
	 * Bulk upload.
	 *
	 * @param aHltContentContainers the a hlt content containers
	 * @return the list
	 */
	List<String> bulkUpload(List<HltContentContainer> aHltContentContainers);

	/**
	 * Bulk upload.
	 *
	 * @param aHltContentContainers the a hlt content containers
	 * @param index the index
	 * @param type the type
	 * @return the string
	 */
	List<String> bulkUpload(List<HltContentContainer> aHltContentContainers, String index, String type);

	/**
	 * Bulk upload.
	 *
	 * @param jsons the jsons
	 * @param index the index
	 * @param type the type
	 * @return the string
	 */
	List<String> bulkUpload_JSON(List<String> jsons, String index, String type);

}
