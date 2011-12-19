/**
 * 
 */
package org.komusubi.common.protocol.smtp;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple Mail Transfer Protocol Server Class.
 * Java Mail実装のSMTPサーバー
 * @author jun.ozeki
 * @since 2008/10/02
 * @version $Id: SmtpServer.java 1254 2009-07-06 14:35:03Z ozeki $
 */
public class SmtpServer implements Serializable {
    private static final long serialVersionUID = 5050401872058525467L;
    private static final Logger logger = LoggerFactory.getLogger(SmtpServer.class);
    
    // server name or ip addr
	private String host;
	private int port = -1; // -1 use system default port
	private String username;
	private String password;
	private boolean auth;
	private Properties props = new Properties();
	
	/**
	 * STMP サーバー名.
	 * @return host
	 */
	public String getHost() {
    	return host;
    }
	/**
	 * SMTP サーバー名.
	 * @param host
	 */
	public void setHost(String host) {
    	this.host = host;
    }
	/**
	 * SMTP Port番号.
	 * default 25番
	 * @return ポート番号
	 */
	public int getPort() {
    	return port;
    }
	/**
	 * SMTP Port番号.
	 * @param port
	 */
	public void setPort(int port) {
    	this.port = port;
    }
	/**
	 * SMTP 認証ユーザー名.
	 * @return username
	 */
	public String getUsername() {
    	return username;
    }
	/**
	 * SMTP　認証ユーザー名.
	 * @param username
	 */
	public void setUsername(String username) {
    	this.username = username;
    }
	/**
	 * SMTP 認証ユーザーパスワード.
	 * @return password
	 */
	public String getPassword() {
    	return password;
    }
	/**
	 * SMTP 認証ユーザーパスワード.
	 * @param password
	 */
	public void setPassword(String password) {
    	this.password = password;
    }
	public boolean isAuth() {
    	return auth;
    }
	public void setAuth(boolean auth) {
    	this.auth = auth;
    }
	/**
	 * 宛先作成。
	 * @param dest 宛先ユーザー
	 * @param charset 文字コード
	 * @return 宛先{@link InternetAddress}
	 */
	protected InternetAddress createInternetAddress(Destination dest, String charset) {
        try {
	        InternetAddress address = new InternetAddress(dest.getEmail());
//	        StringBuilder builder = new StringBuilder(dest.getSurName());
//	        builder.append(" ").append(dest.getGivenName());
	        address.setPersonal(dest.getName(), charset);
	        return address;
        } catch (javax.mail.internet.AddressException e) {
        	throw new AddressException(e);
        } catch (UnsupportedEncodingException e) {
        	throw new org.komusubi.common.UnsupportedEncodingException(e);
		}
	}
	
	public void send(MailMessage mail) {

		props.put("mail.smtp.host", getHost());
		props.put("mail.host", getHost());
		props.put("mail.smtp.auth", Boolean.toString(isAuth()));
		
		props.put("mail.smtp.starttls.enable","true");
		
		// HELO, EHLOコマンドで利用する値を設定
		// ex) EHLO amy.hi-ho.ne.jp
		props.put("mail.smtp.localhost", getHost());
		
		Session session = Session.getDefaultInstance(props, null);
		// debug 設定
		session.setDebug(logger.isDebugEnabled());
		MimeMessage message= new MimeMessage(session);
		Transport transport = null;
		try {
			// TODO charsetが指定されてない場合の対処 default charset は何？
			String charset = mail.getContent().getContentType().getParameter("charset");
			for (Destination dest: mail.getToRecipients())
				message.addRecipient(Message.RecipientType.TO, createInternetAddress(dest, charset));
			
			for (Destination dest: mail.getCcRecipients())
				message.addRecipient(Message.RecipientType.CC, createInternetAddress(dest, charset));
			
			for (Destination dest: mail.getBccRecipients())
				message.addRecipient(Message.RecipientType.BCC, createInternetAddress(dest, charset));

			message.setFrom(createInternetAddress(mail.getFrom(), charset));
		 
			// TODO HTMLメールも使うのでsetContentで統一し、ContentTypeで明示的に指定する。ダメ？
			// MimeBodyPart#setTextで最終的にsetContentをcallしている。
			message.setSubject(mail.getContent().getSubject(), charset);
//			message.setText(mail.getContent().getBody(), charset);
//			message.setHeader("Content-type", mail.getContent().getContentType().format());
			
			// TODO setContentの後に"Content-Transfer-Encoding"Headerを設定しないとjava mailに削除される
			// MimeBodyPart#invalidateContentHeaders テキストじゃないと見なされるみたい。
			message.setContent(mail.getContent().getBody(), mail.getContent().getContentType().toString());
			message.setHeader("Content-Transfer-Encoding", "7bit");
//			if (charset.equals)
			message.setSentDate(new Date());
			
			transport = session.getTransport("smtp");
			// SMTP認証不要であればユーザー名、パスワードはnullのままでOKのはず。
			transport.connect(getHost(), getPort(), getUsername(), getPassword());
	       transport.sendMessage(message, message.getAllRecipients());
        } catch (MessagingException e) {
        	throw new org.komusubi.common.MessagingException(e);
        } finally {
        	try {
//        		if (transport != null && transport.isConnected())
        		if (transport != null)
        			transport.close();
            } catch (MessagingException e) {
            	// ignored.
            }
        }
	}
}
