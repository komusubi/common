/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.komusubi.common.protocol.smtp;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author jun.ozeki
 * @since 2008/10/02
 * @version $Id: MailMessage.java 1114 2009-04-05 17:17:07Z ozeki $
 */
public class MailMessage {

	private Destination fromUser;
	// ArrayListの初期値3もあれば十分でしょ。
	private ArrayList<Destination> toRecipients = new ArrayList<Destination>(3);
	private ArrayList<Destination> ccRecipients = new ArrayList<Destination>(3);
	private ArrayList<Destination> bccRecipients = new ArrayList<Destination>(3);
	private MailContent mailContent;
	
	public Destination getFrom() {
    	return fromUser;
    }
	public void setFrom(Destination user) {
    	fromUser = user;
    }
	public MailContent getContent() {
		return mailContent;
	}
	public void setContent(MailContent content) {
		mailContent = content;
	}
	public List<Destination> getToRecipients() {
    	return toRecipients;
    }
	public void addToRecipient(Destination dest) {
		toRecipients.add(dest);
	}
	public List<Destination> getCcRecipients() {
    	return ccRecipients;
    }
	public void addCcRecipient(Destination dest) {
		ccRecipients.add(dest);
	}
	public List<Destination> getBccRecipients() {
    	return bccRecipients;
    }
	public void addBccRecipient(Destination dest) {
    	bccRecipients.add(dest);
    }
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MailMessage [fromUser=").append(fromUser).append(", toRecipients=")
				.append(toRecipients).append(", ccRecipients=").append(ccRecipients)
				.append(", bccRecipients=").append(bccRecipients).append(", mailContent=")
				.append(mailContent).append("]");
		return builder.toString();
	}
	
}
