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
package jp.dip.komusubi.common.protocol.smtp;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import javax.mail.internet.ContentType;
import javax.mail.internet.ParseException;


/**
 * メールBodyクラス.
 * {@link ContentType} で表現されるコンテントタイプでメール本文を保持する。
 * @author jun.ozeki
 * @since 2008/10/20
 * @version $Id: MailContent.java 1114 2009-04-05 17:17:07Z ozeki $
 */
public class MailContent {
	public static void main(String[] args) throws IOException {
		System.out.println("path is " + Integer.class.getResource("/META-INF/mimetypes.default")); ///META-INF/mimetypes.default"));
		System.out.println("current " + MailContent.class.getResource("/META-INF/mime.types"));
		for (Enumeration<URL> e = MailContent.class.getClassLoader().getResources("META-INF/mimetypes.default"); e.hasMoreElements(); )
			System.out.println("url: " + e.nextElement());
		for (Enumeration<URL> e = MailContent.class.getClassLoader().getResources("/META-INF/mime.types"); e.hasMoreElements(); )
			System.out.println("url: " + e.nextElement());
		
		System.out.println("loader " + MailContent.class.getClassLoader());
		File f = new File(MailContent.class.getResource("/META-INF").getFile());
		if (f.isDirectory()) {
			for (File file: f.listFiles()) 
				System.out.println("file: " + file.getAbsolutePath());
		}
		System.out.println("path " + Thread.currentThread().getContextClassLoader().getResource("/META-INF/mimetypes.default"));
    }
	private ContentType contentType;
	private String body;

    private String subject;    

	/**
	 * コンストラクタ。
	 * @throws ParseException 
	 */
	public MailContent() throws ParseException {
		this(new ContentType("text/plain; charset=iso-2022-jp"), "");
	}

	/**
	 * コンストラクタ。
	 * @param aContentType
	 */
	public MailContent(ContentType aContentType) {
		this(aContentType, "");
	}
	
	/**
	 * コンストラクタ。
	 * @param aContentType 
	 * @param aBody
	 */
	public MailContent(ContentType aContentType, String aBody) {
		setContentType(aContentType);
		setBody(aBody);
	}
	public String getBody() {
    	return body;
    }

	public ContentType getContentType() {
    	return contentType;
    }
	public String getSubject() {
    	return subject;
    }
	public void setBody(String body) {
    	this.body = body;
    }
	public void setContentType(ContentType contentType) {
    	this.contentType = contentType;
    }
	public void setSubject(String subject) {
    	this.subject = subject;
    }
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MailContent [contentType=").append(contentType).append(", body=")
				.append(body).append(", subject=").append(subject).append("]");
		return builder.toString();
	}
}
