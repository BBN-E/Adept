/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/*
 * 
 */
package adept.module;

import adept.common.Document;
import adept.common.HltContentContainer;

/**
 * The Class EntityMentionDetector - An instance can be created to
 * support EntityMention output.
 */
public abstract class EntityMentionDetector extends AbstractModule implements IDocumentProcessor {
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
