package pers.hsc.evats.modules.sys.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pers.hsc.evats.core.common.controller.BaseController;
import pers.hsc.evats.core.model.AjaxJson;
import pers.hsc.evats.core.utils.MessageUtils;
import pers.hsc.evats.core.utils.StringUtils;
import pers.hsc.evats.core.utils.email.Email;
import pers.hsc.evats.core.utils.email.EmailResult;
import pers.hsc.evats.core.utils.sms.data.SmsResult;
import pers.hsc.evats.core.utils.sms.data.SmsTemplate;
import pers.hsc.evats.core.utils.sms.sender.CCPSmsSender;
import pers.hsc.evats.core.utils.sms.sender.HuyiSmsSender;
import pers.hsc.evats.core.utils.sms.sender.SmsSender;
import pers.hsc.evats.modules.sys.data.CloudSmsSetting;
import pers.hsc.evats.modules.sys.data.EmailSetting;
import pers.hsc.evats.modules.sys.data.HySmsSetting;

@Controller
@RequestMapping("${admin.url.prefix}/sys/setting")
public class SettingController extends BaseController {

	@RequestMapping(value = "/email", method = RequestMethod.GET)
	public String email(HttpServletRequest request, HttpServletResponse response) {
		EmailSetting emailSetting = new EmailSetting();
		emailSetting.load();
		Map<String, String> trueOrFalse = new HashMap<String, String>();
		trueOrFalse.put("true", "是");
		trueOrFalse.put("false", "否");
		request.setAttribute("trueOrFalse", trueOrFalse);
		request.setAttribute("data", emailSetting);
		return display("email");
	}

	@RequestMapping(value = "/email", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson saveEmail(EmailSetting emailSetting, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.success("邮件配置保存成功");
		try {
			emailSetting.set();
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson.fail("邮件配置保存失败");
		}
		return ajaxJson;
	}

	@RequestMapping(value = "/testEmail", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson testEmail(EmailSetting emailSetting, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.success("邮件发送成功");
		try {
			// 先保存配置
			emailSetting.set();
			String testemail = request.getParameter("testemail");
			String testcontent = request.getParameter("testcontent");
			if (StringUtils.isEmpty(testemail)) {
				ajaxJson.fail("测试邮箱不能为空");
			}
			if (StringUtils.isEmpty(testcontent)) {
				testcontent = MessageUtils.getMessage("msg.email.testmsg");
			}
			EmailResult emailResult = Email.newEmail().send(testemail, "", testcontent);
			if (!emailResult.isSuccess()) {
				ajaxJson.fail(emailResult.getMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson.fail("邮件发送失败" + e.getMessage());
		}
		return ajaxJson;
	}

	@RequestMapping(value = "/sms", method = RequestMethod.GET)
	public String sms(HttpServletRequest request, HttpServletResponse response) {
		CloudSmsSetting cloudSmsSetting = new CloudSmsSetting();
		cloudSmsSetting.load();
		request.setAttribute("cloudSms", cloudSmsSetting);
		HySmsSetting hySmsSetting = new HySmsSetting();
		hySmsSetting.load();
		request.setAttribute("hySms", hySmsSetting);
		return display("sms");
	}

	@RequestMapping(value = "/cloudsms", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson saveCloudsmsSms(CloudSmsSetting cloudSmsSetting, HttpServletRequest request,
			HttpServletResponse response) {
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.success("短信配置保存成功");
		try {
			cloudSmsSetting.set();
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson.fail("短信配置保存失败");
		}
		return ajaxJson;
	}

	@RequestMapping(value = "/testCloudSms", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson testCloudSms(CloudSmsSetting cloudSmsSetting, HttpServletRequest request,
			HttpServletResponse response) {
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.success("短信发送成功");
		try {
			// 先保存配置
			cloudSmsSetting.set();
			String testphone = request.getParameter("testphone");
			String testtemplateId = request.getParameter("testtemplateId");
			String testdata = request.getParameter("testdata");
			if (StringUtils.isEmpty(testphone)) {
				ajaxJson.fail("测试号码不能为空");
			}
			if (StringUtils.isEmpty(testtemplateId)) {
				ajaxJson.fail("测试模版不能为空");
			}
			SmsSender smsSender = new CCPSmsSender();
			SmsTemplate smsTemplate = SmsTemplate.newTemplateById(testtemplateId);
			SmsResult smsResult = null;
			if (StringUtils.isEmpty(testdata)) {
				smsResult = smsSender.send(testphone, smsTemplate);
			} else {
				smsResult = smsSender.send(testphone, smsTemplate, testdata.split(","));
			}

			if (!smsResult.isSuccess()) {
				ajaxJson.fail(smsResult.getMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson.fail("短信发送失败" + e.getMessage());
		}
		return ajaxJson;
	}

	@RequestMapping(value = "/hysms", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson saveHySms(HySmsSetting hySmsSetting, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.success("短信配置保存成功");
		try {
			hySmsSetting.set();
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson.fail("短信配置保存失败");
		}
		return ajaxJson;
	}

	@RequestMapping(value = "/testHySms", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson testHySms(HySmsSetting hySmsSetting, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.success("短信发送成功");
		try {
			// 先保存配置
			hySmsSetting.set();
			String testphone = request.getParameter("testphone");
			String testcontent = request.getParameter("testcontent");
			if (StringUtils.isEmpty(testphone)) {
				ajaxJson.fail("测试电话不能为空");
			}
			if (StringUtils.isEmpty(testcontent)) {
				ajaxJson.fail("测试短信內容不能为空");
			}
			SmsSender smsSender = new HuyiSmsSender();
			SmsTemplate smsTemplate = SmsTemplate.newTemplateByContent(testcontent);
			SmsResult smsResult = smsSender.send(testphone, smsTemplate);
			if (!smsResult.isSuccess()) {
				ajaxJson.fail(smsResult.getMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson.fail("短信发送失败" + e.getMessage());
		}
		return ajaxJson;
	}

}
