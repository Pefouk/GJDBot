package online.playerfrais.gjdbot.ihm;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLReader {
	private final DocumentBuilderFactory factory;
	private final DocumentBuilder builder;
	private final Document document;
	private final Element racine;
	private final NodeList tok;
	private final NodeList pref;
	private final Element token;
	private final Element prefix;

	public XMLReader() throws Exception {
		this.factory = DocumentBuilderFactory.newInstance();
		this.builder = this.factory.newDocumentBuilder();
		this.document = this.builder.parse(new File("config.xml"));
		this.racine = this.document.getDocumentElement();
		this.tok = this.racine.getElementsByTagName("token");
		this.pref = this.racine.getElementsByTagName("prefix");
		this.token = (Element) this.tok.item(0);
		this.prefix = (Element) this.pref.item(0);
	}

	public Element getToken() {
		return token;
	}

	public Element getPrefix() {
		return prefix;
	}
}
