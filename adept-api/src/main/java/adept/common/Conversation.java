/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/**
 * 
 */
package adept.common;

import java.util.List;
import static com.google.common.base.Preconditions.checkArgument;


/**
 * Represents a thread where the messages are successive chunks of text,
 * including the authors who are participants in the thread.
 *  <p>This class is locally immutable.</p>
 *
 */
public class Conversation extends Document {
	
	private final List<ConversationElement> messages;
	private final List<Entity> participants;
	private final ConversationType conversationType;
	
	public Conversation(String conversationId,
                        Corpus corpus,
                        String uri,
			            String language,
			            ConversationType conversationType,
                        List<ConversationElement> messages,
                        List<Entity> participants
                        )
	{
        super(conversationId, corpus, "CONVERSATION", uri, language);

        checkArgument(messages!=null);
        for (final ConversationElement message : messages) {
            checkArgument(message!=null && message.getMessageChunk().getTokenStream()!=null);
        }
        this.messages = messages;

        checkArgument(participants!=null);
        for (final Entity person : participants) {
            checkArgument(person!=null);
        }
        this.participants = participants;
        
        this.conversationType = conversationType;
	}

    /**
     * The messages in the thread.
     * @return May be empty, but will not be or contain {@code null}. All must have a TokenStream.
     * TODO should all have same TokenStream?
     */
	public List<ConversationElement> getMessages()
	{
		return messages;
	}
	
    /**
     * The particpants in the thread, including silent participants. 
     * @return May be empty, but will not be or contain {@code null}.
     */
	public List<Entity> getParticipants()
	{
		return participants;
	}

}
