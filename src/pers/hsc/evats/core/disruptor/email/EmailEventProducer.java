package pers.hsc.evats.core.disruptor.email;

import com.lmax.disruptor.RingBuffer;

import pers.hsc.evats.core.disruptor.email.EmailEvent.EmailHandlerCallBack;
import pers.hsc.evats.core.utils.StringUtils;

/**
 * 内容生产者
 * 
 * @author hsc
 *
 *         Mar 29, 2018
 */
public class EmailEventProducer {
	private final RingBuffer<EmailEvent> ringBuffer;
	private EmailDao emailDao;

	public EmailEventProducer(RingBuffer<EmailEvent> ringBuffer, EmailDao emailDao) {
		this.ringBuffer = ringBuffer;
		this.emailDao = emailDao;
	}

	private void sendEmail(EmailData emailData, EmailHandlerCallBack callBack) {
		// 获取下一个序号
		long sequence = ringBuffer.next();
		String eventId = StringUtils.randomUUID();
		try {
			// 写入数据
			EmailEvent smsEvent = ringBuffer.get(sequence);
			smsEvent.setId(eventId);
			smsEvent.setEmailData(emailData);
			smsEvent.setHandlerCallBack(callBack);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 通知消费者该资源可以消费了
			ringBuffer.publish(sequence);
		}
		if (emailDao != null) {
			emailDao.doSend(eventId, emailData);
		}
	}

	public void sendEmail(String email, String content, String... datas) {
		sendEmail(email, "", content, null, datas);
	}

	public void sendEmail(String email, String content, EmailHandlerCallBack callBack, String... datas) {
		sendEmail(email, "", content, callBack, datas);
	}

	public void sendEmail(String email, String subject, String content, String... datas) {
		sendEmail(email, subject, content, null, datas);
	}

	public void sendEmail(String email, String subject, String content, EmailHandlerCallBack callBack,
			String... datas) {
		EmailData emailData = new EmailData();
		emailData.setEmail(email);
		emailData.setSubject(subject);
		emailData.setContent(content);
		emailData.setDatas(datas);
		sendEmail(emailData, callBack);
	}
}