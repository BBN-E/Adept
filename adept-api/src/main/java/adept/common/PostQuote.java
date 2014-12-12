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
package adept.common;

import java.util.List;
import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkArgument;


/**
 * The Class PostQuote, which represents a single forum post or email message within an SGML structured document.
 *
 * Post has a list called quotes, which is a list of all the top level PostQuotes in that Post.
 * PostQuote has a subQuotes list,  which is a list of all the top level PostQuotes inside that PostQuote.
 */
public class PostQuote extends Chunk {

	/** The sequence id. */
	private final long sequenceId;

	/** The open tag. */
	private Tag openTag;

	/** The close tag. */
	private Tag closeTag;

	/** The quotes. */
	private List<PostQuote> subQuotes;

	/** The post. */
	private Post post;

	/**
	 * Instantiates a new passage.
	 * 
	 * @param sequenceId
	 *            the sequence id
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 */
	public PostQuote(long sequenceId, TokenOffset tokenOffset,
			TokenStream tokenStream, Tag oTag, Tag cTag) {
        // Arguments to Chunk constructor will be required.
		super(tokenOffset, tokenStream);
        checkArgument(oTag!=null);
        checkArgument(cTag!=null);
		this.sequenceId = sequenceId;
		this.subQuotes = new ArrayList<PostQuote>();
		this.openTag = oTag;
		this.closeTag = cTag;
		setValue();
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return the sequence id
	 */
	public long getSequenceId() {
		return sequenceId;
	}

	/**
	 * Gets the sub quotes
	 * 
	 * @return the sub quotes
	 */
	public List<PostQuote> getSubQuotes() {
		return subQuotes;
	}

	/**
	 * Gets the open tag
	 * 
	 * @return the open tag
	 */
	public Tag getOpenTag() {
		return openTag;
	}

	/**
	 * Gets the cose tag
	 * 
	 * @return the close tag
	 */
	public Tag getCloseTag() {
		return closeTag;
	}

	/**
	 * Gets the post
	 * 
	 * @return the post
	 */
	public Post getPost() {
		return post;
	}

    /**
     * Sets the open tag
     *
     * @param tag the open tag
     */
    public void setOpenTag(Tag tag) {
        checkArgument(tag!=null);
        openTag = tag;
    }

    /**
     * Sets the close tag
     *
     * @param tag the close tag
     */
    public void setCloseTag(Tag tag) {
        checkArgument(tag!=null);
        closeTag = tag;
    }

    /**
     * Sets the quotes
     *
     * @param inQuotes the quotes
     */
    public void setSubQuotes(List<PostQuote> inQuotes) {
        checkArgument(inQuotes!=null);
        for( PostQuote arg : inQuotes) {
            checkArgument(arg!=null);
        }
        subQuotes = inQuotes;
    }

    /**
     * Adds the quote
     *
     * @param inQuote the quotes
     */
    public void addSubQuote(PostQuote inQuote) {
        checkArgument(inQuote!=null);
        subQuotes.add(inQuote);
    }

	/**
	 * Sets the post
	 * 
	 * @param post the post
	 */
	public void setPost(Post post) {
		this.post = post;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.common.Chunk#setValue()
	 */
	private void setValue() {
		Document doc = tokenStream.getDocument();
		String docValue = doc.getValue();
		int charBegin = openTag.getCharOffset().getBegin();
		int charEnd = closeTag.getCharOffset().getEnd();
		this.value = docValue.substring(charBegin, charEnd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof PostQuote))
			return false;
		PostQuote quote = (PostQuote) obj;
		return (quote.tokenStream == this.tokenStream
                && quote.openTag.getCharOffset().getBegin() == this.openTag.getCharOffset().getBegin()
                && quote.openTag.getCharOffset().getEnd() == this.openTag.getCharOffset().getEnd());
	}

}
