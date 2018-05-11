package com.emerchantpay.gateway.genesisandroid.api.util;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class RequestBuilderWithAttribute {

	private String parent;
	private String attribute;
	private List<Map.Entry<String, String>> topLevelElements;
	private List<Map.Entry<String, Object>> elements;

	public RequestBuilderWithAttribute(String parent, String attribute) {
		this.parent = parent;
		this.attribute = attribute;
		this.topLevelElements = new ArrayList<Map.Entry<String, String>>();
		this.elements = new ArrayList<Map.Entry<String, Object>>();
	}

	public RequestBuilderWithAttribute addTopLevelElement(String name, String value) {
		topLevelElements.add(new AbstractMap.SimpleEntry<String, String>(name, value));
		return this;
	}

	public RequestBuilderWithAttribute addElement(String name, Object value) {
		elements.add(new AbstractMap.SimpleEntry<String, Object>(name, value));
		return this;
	}

	public String toQueryString() {
		QueryString queryString = new QueryString();
		for (Map.Entry<String, String> entry : topLevelElements) {
			queryString.append(StringUtils.underscore(entry.getKey()), entry.getValue());
		}
		for (Map.Entry<String, Object> entry : elements) {
			queryString.append(
					parentBracketChildString(StringUtils.underscore(parent), StringUtils.underscore(entry.getKey())),
					entry.getValue());
		}
		return queryString.toString();
	}

	public String toXML() {
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("<%s>", parent + " name=" + "\"" + attribute + "\""));
		for (Map.Entry<String, Object> entry : elements) {
			builder.append(buildXMLElement(entry.getKey(), entry.getValue()));
		}
		builder.append(String.format("</%s>", parent));
		return builder.toString();
	}

	protected static String buildXMLElement(Object element) {
		return buildXMLElement("", element);
	}

	@SuppressWarnings("unchecked")
	protected static String buildXMLElement(String name, Object element) {
		if (element == null) {
			return "";
		} else if (element instanceof Request) {
			return ((Request) element).toXML();
		} else if (element instanceof Calendar) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			return String.format("<%s type=\"datetime\">%s</%s>", name,
					dateFormat.format(((Calendar) element).getTime()), name);
		} else if (element instanceof Map<?, ?>) {
			return formatAsXML(name, (Map<String, Object>) element);
		} else if (element instanceof List<?>) {
			StringBuilder xml = new StringBuilder();
			for (Object item : (List<Object>) element) {
				xml.append(buildXMLElement("item", item));
			}
			return wrapInXMLTag(name, xml.toString(), "array");
		} else {

			return String.format("<%s>%s</%s>", xmlEscape(name), element == null ? "" : xmlEscape(element.toString()),
					xmlEscape(name));
		}
	}

	protected static String formatAsXML(String name, Map<String, Object> map) {
		if (map == null)
			return "";
		StringBuilder xml = new StringBuilder();
		xml.append(String.format("<%s>", name));
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			xml.append(buildXMLElement(entry.getKey(), entry.getValue()));
		}
		xml.append(String.format("</%s>", name));
		return xml.toString();
	}

	protected static Object buildQueryStringElement(String name, String value) {
		if (value != null) {
			try {
				return String.format("%s=%s&", URLEncoder.encode(name, "UTF-8"), URLEncoder.encode(value, "UTF-8"));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			return "";
		}
	}

	protected static String parentBracketChildString(String parent, String child) {
		return String.format("%s[%s]", parent, child);
	}

	protected static String wrapInXMLTag(String tagName, String xml) {
		return String.format("<%s/>%s</%s>", tagName, xml, tagName);
	}

	protected static String wrapInXMLTag(String tagName, String xml, String type) {
		return String.format("<%s type=\"%s\">%s</%s>", tagName, type, xml, tagName);
	}

	protected static String replaceAllStrings(String[] findArr, String[] replaceArr, String input) {
		for (Integer i = 0; i < findArr.length; i++) {
			input = input.replace(findArr[i], replaceArr[i]);
		}

		return input;
	}

	protected static String xmlEscape(String input) {
		String[] findArray = new String[]{"&", "<", ">", "'", "\""};
		String[] replaceArray = new String[]{"&amp;", "&lt;", "&gt;", "&apos;", "&quot;"};

		return replaceAllStrings(findArray, replaceArray, input);
	}
}
