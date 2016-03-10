package net.sanmuyao.core.converter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.converter.StringHttpMessageConverter;

/**
 * SimpleStringHttpMessageConverter类
 *
 * <pre>
 * ResponseBody默认会产生很大的响应头(Accept-Charset会达到4K+), 重写仅支持部分字符集
 * </pre>
 *
 * @date 2014年7月26日
 */
public class SimpleStringHttpMessageConverter extends StringHttpMessageConverter {

	private static final String[] SUPPORTED_CHARSETS = new String[] { "UTF-8", "Big5", "GBK", "GB2312", "GB18030", "ISO-8859-1" };

	private List<Charset> supportedCharsets = null;

	public SimpleStringHttpMessageConverter() {
		super();
		initCharsets();
	}

	private void initCharsets() {
		supportedCharsets = new ArrayList<Charset>();
		for (String charsetName : SUPPORTED_CHARSETS) {
			if (Charset.isSupported(charsetName)) {
				supportedCharsets.add(Charset.forName(charsetName));
			}
		}
	}

	@Override
	protected List<Charset> getAcceptedCharsets() {
		if (supportedCharsets != null && supportedCharsets.size() > 0) {
			return supportedCharsets;
		}
		return super.getAcceptedCharsets();
	}
}
