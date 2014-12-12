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
 * The Class Post, which represents the top-level forum post or email message within an SGML structured document.
 *
 * Post has a list called quotes, which is a list of all the top level PostQuotes in that Post.
 * PostQuote has a subQuotes list,  which is a list of all the top level PostQuotes inside that PostQuote.
 */
public class Post extends Chunk {

	/** The sequence id. */
	private final long sequenceId;

	/** The open tag. */
	private Tag openTag;

	/** The close tag. */
	private Tag closeTag;

	/** The quotes. */
	private List<PostQuote> quotes;

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
	public Post(long sequenceId, TokenOffset tokenOffset, TokenStream tokenStream, Tag oTag, Tag cTag) {
        // Arguments to Chunk constructor will be required.
		super(tokenOffset, tokenStream);
        checkArgument(oTag!=null);
        checkArgument(cTag!=null);
		this.sequenceId = sequenceId;
		this.quotes = new ArrayList<PostQuote>();
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
	 * Gets the quotes
	 * 
	 * @return the quotes
	 */
	public List<PostQuote> getQuotes() {
		return quotes;
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
	 * Gets all sub quotes
	 * 
	 * @return all sub quotes
	 */
	public List<PostQuote> getAllSubQuotes() {
		if (quotes == null || quotes.size() == 0) {
			return null;
		} else {
			List<PostQuote> allSubQuotes = new ArrayList<PostQuote>();
		
			for (PostQuote quote : quotes) {
				allSubQuotes.addAll(getAllSubQuotes(quote));
			}

			return allSubQuotes;
		}
	}

	private List<PostQuote> getAllSubQuotes(PostQuote quote) {
		List<PostQuote> allSubQuotes = new ArrayList<PostQuote>();
		if (quote.getSubQuotes() == null || quote.getSubQuotes().size() == 0) {
			allSubQuotes.add(quote);
		} else {
			for (PostQuote subQuote : quote.getSubQuotes()) {
				allSubQuotes.addAll(getAllSubQuotes(subQuote));
			}
		}
		
		return allSubQuotes;
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
    public void setQuotes(List<PostQuote> inQuotes) {
        checkArgument(inQuotes!=null);
        for( PostQuote arg : inQuotes) {
            checkArgument(arg!=null);
        }
        quotes = inQuotes;
    }

	/**
	 * Adds the quote
	 * 
	 * @param inQuote the quote
	 */
	public void addQuote(PostQuote inQuote) {
        checkArgument(inQuote!=null);
        quotes.add(inQuote);
	}

	/*
	 * (non-Javadoc)
	 * The Post value actually includes the entirety of the tags, including all the special characters like <,>, = and /
     * The Reader that tokenizes documents with Posts creates a tokenStream that does not include those characters - only the words inside the tags.
     * Thus if Post had used Chunk's setValue() functions, the values of Posts would be missing a few characters at the beginning and the end.
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

}
