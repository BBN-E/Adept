/*
* ------
* Adept
* -----
* Copyright (C) 2014 Raytheon BBN Technologies Corp.
* -----
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
* -------
*/

package adept.common;

import java.util.List;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class Passage.
 */
public class Message extends Passage {

    /** The message sender. */
    private String sender;

    /** The message recipients. */
    private List<EmailAddress> recipients;
    
    /** The message CC recipients. */
    private List<EmailAddress> ccRecipients;

    /** The message BCC recipients. */
    private List<EmailAddress> bccRecipients;
    
    /** The date the message was sent. */
    private String sentDate;
    
    /** The message importance. */
    private String priority;

    /** The message subject. */
    private String subject;

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
	public Message(long sequenceId, TokenOffset tokenOffset,
			TokenStream tokenStream) {
		super(sequenceId, tokenOffset, tokenStream);
	}

	/**
	 * Gets the sender.
	 * 
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * Sets the sender.
	 * 
	 * @param sender
	 *            the new sender
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * Gets the sentDate.
	 * 
	 * @return the sentDate
	 */
	public String getSentDate() {
		return sentDate;
	}

	/**
	 * Sets the sentDate.
	 * 
	 * @param sentDate
	 *            the new sentDate
	 */
	public void setSentDate(String sentDate) {
		this.sentDate = sentDate;
	}

	/**
	 * Gets the priority.
	 * 
	 * @return the priority
	 */
	public String getPriority() {
		return priority;
	}

	/**
	 * Sets the priority.
	 * 
	 * @param priority
	 *            the new priority
	 */
	public void setPriority(String priority) {
		this.priority = priority;
	}

	/**
	 * Gets the subject.
	 * 
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Sets the subject.
	 * 
	 * @param subject
	 *            the new subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * Gets the recipients.
	 * 
	 * @return the recipients
	 */
	public List<EmailAddress> getRecipients() {
		return recipients;
	}

	/**
	 * Sets the recipients.
	 * 
	 * @param recipients
	 *            the new recipients
	 */
	public void setRecipients(List<EmailAddress> recipients) {
		this.recipients = recipients;
	}

    /**
     * Add new recipient to list of recipients.
     *
     *  @param recipient
     *             the new recipient
     */
    public void addRecipient(EmailAddress recipient) {
        if(recipients == null)
            recipients = new ArrayList<EmailAddress>();
        recipients.add(recipient);
    }

	/**
	 * Gets the cc recipients.
	 * 
	 * @return the cc recipients
	 */
	public List<EmailAddress> getCcRecipients() {
		return ccRecipients;
	}

	/**
	 * Sets the cc recipients.
	 * 
	 * @param ccRecipients
	 *            the new cc recipients
	 */
	public void setCcRecipients(List<EmailAddress> ccRecipients) {
		this.ccRecipients = ccRecipients;
	}

    /**
     * Add new cc recipient to list of cc recipients.
     *
     *  @param ccRecipient
     *             the new cc recipient
     */
    public void addCcRecipient(EmailAddress ccRecipient) {
        if(ccRecipients == null)
            ccRecipients = new ArrayList<EmailAddress>();
        ccRecipients.add(ccRecipient);
    }

	/**
	 * Gets the bcc recipients.
	 * 
	 * @return the bcc recipients
	 */
	public List<EmailAddress> getBccRecipients() {
		return bccRecipients;
	}

	/**
	 * Sets the bcc recipients.
	 * 
	 * @param bccRecipients
	 *            the new bcc recipients
	 */
	public void setBccRecipients(List<EmailAddress> bccRecipients) {
		this.bccRecipients = bccRecipients;
	}

    /**
     * Add new bcc recipient to list of bcc recipients.
     *
     *  @param bccRecipient
     *             the new bcc recipient
     */
    public void addBccRecipient(EmailAddress bccRecipient) {
        if(bccRecipients == null)
            bccRecipients = new ArrayList<EmailAddress>();
        bccRecipients.add(bccRecipient);
    }

}