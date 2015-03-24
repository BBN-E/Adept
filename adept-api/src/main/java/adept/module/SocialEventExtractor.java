package adept.module;

import java.util.List;
import adept.common.*;


// TODO: Auto-generated Javadoc
/**
 * The Class SocialEventExtractor.
 */
public abstract class SocialEventExtractor extends AbstractModule implements
		IDocumentHltContentProcessor<Relation> {

	/* (non-Javadoc)
	 * @see adept.module.IDocumentHltContentProcessor#process(adept.common.Document, adept.common.HltContentContainer)
	 */
	@Override
	public List<Relation> process(Document document,
			HltContentContainer hltContentContainer)
			throws AdeptModuleException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see adept.module.IDocumentHltContentProcessor#processAsync(adept.common.Document, adept.common.HltContentContainer)
	 */
	@Override
	public long processAsync(Document document,
			HltContentContainer hltContentContainer)
			throws AdeptModuleException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see adept.module.IDocumentHltContentProcessor#tryGetResult(long, java.util.List)
	 */
	@Override
	public Boolean tryGetResult(long requestId, List<Relation> hltContents)
			throws AdeptModuleException {
		// TODO Auto-generated method stub
		return null;
	}


}
