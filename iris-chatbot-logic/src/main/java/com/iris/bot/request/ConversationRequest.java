package com.iris.bot.request;

/**
 * This is the representation of the request object that frontend send to the
 * bot engine
 */
public class ConversationRequest {

	/**
	 * sender id as per facebook
	 */
	private String sender;

	/**
	 * actual text message
	 */
	private String message;

	/**
	 * timestamp sent by facebook
	 */
	private Long timestamp;

	/**
	 * Sequence number of the message
	 */
	private Long seq;

	/**
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * @param sender
	 *            the sender to set
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the timestamp
	 */
	public Long getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the seq
	 */
	public Long getSeq() {
		return seq;
	}

	/**
	 * @param seq
	 *            the seq to set
	 */
	public void setSeq(Long seq) {
		this.seq = seq;
	}

	/**
	 * Return the JSON format of the object.
	 */
	public String toString() {
		return String.format("{'sender':'%s','message':'%s','timestamp':%d,'seq':%d}", sender, message, timestamp, seq);
	}

}
