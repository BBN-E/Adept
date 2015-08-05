package adept.module;

import adept.common.*;


/**
 * The Class SocialEventExtractor - An instance can be created to
 * support Relation output.
 */
public abstract class SocialEventExtractor extends AbstractModule implements IDocumentProcessor {
    /* (non-Javadoc)
     * @see adept.module.IDocumentProcessor#process(adept.common.Document, adept.common.HltContentContainer)
     */
    @Override
    public HltContentContainer process(Document document, HltContentContainer hltContentContainer)
            throws AdeptModuleException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see adept.module.IDocumentProcessor#processAsync(adept.common.Document, adept.common.HltContentContainer)
     */
    @Override
    public long processAsync(Document document, HltContentContainer hltContentContainer)
            throws AdeptModuleException {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see adept.module.IDocumentProcessor#tryGetResult(long, adept.common.HltContentContainer)
     */
    @Override
    public Boolean tryGetResult(long requestId, HltContentContainer hltContentContainer)
            throws AdeptModuleException {
        // TODO Auto-generated method stub
        return null;
    }
}
