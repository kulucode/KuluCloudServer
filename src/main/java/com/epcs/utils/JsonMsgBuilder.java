package com.epcs.utils;
/**
 * 创建微信客服消息
 * 
 */
public class JsonMsgBuilder {

	private final StringBuffer msgBuf = new StringBuffer("{");
	/**
	 * 创建
	 */
	public static JsonMsgBuilder create() {
		return new JsonMsgBuilder();
	}
	/**
	 * 模板消息
	 * 
	 * @param openId
	 *            接收者
	 * @param templateId
	 *            模板ID
	 * @param topColor
	 *            顶部颜色
	 * @param url
	 *            链接地址
	 * @param templates
	 *            模板数据
	 */
	public JsonMsgBuilder template(String openId, String templateId,
			String topColor, String url, Template... templates) {
		msgBuf.append("\"touser\":\"").append(openId).append("\",");
		msgBuf.append("\"template_id\":\"").append(templateId).append("\",");
		msgBuf.append("\"url\":\"").append(url).append("\",");
		msgBuf.append("\"topcolor\":\"").append(topColor).append("\",");
		msgBuf.append("\"data\":{");
		StringBuffer data = new StringBuffer("");
		for (Template t : templates) {
			data.append(t.templateData()).append(",");
		}
		msgBuf.append(data.substring(0, data.lastIndexOf(",")));
		msgBuf.append("}");
		return this;
	}
	public String build() {
		msgBuf.append("}");
		return msgBuf.toString();
	}
}
