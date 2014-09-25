package com.kissyid.buildts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.resources.IResourceDelta;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class VersionUtil {
	public static void createXml(IResourceDelta delta)
	{
		String projectName = delta.getResource().getProject().getName();

		String path = delta.getResource().getProject().getLocation().toOSString() + "/";
		try
		{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d = new Date();
			String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
			xml += "<version app=\"" + projectName + "\" ofp=\"bin-release\">\n";
			xml += "<build count=\"1\" time=\"" + format.format(d) + "\" />\n";
			xml += "<release count=\"1\" time=\"" + format.format(d) + "\" />\n";
			xml += "</version>\n";
			FileOutputStream fos = new FileOutputStream(path + ".buildtimestamp");
			fos.write(xml.getBytes());
			fos.close();
		}
		catch (Exception localException) {}
	}

	public static void updateVersion(IResourceDelta delta)
	{
		String path = delta.getResource().getProject().getLocation().toOSString() + "/";
		DocumentBuilderFactory docf = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder docb = docf.newDocumentBuilder();
			InputStream is = new FileInputStream(path + ".buildtimestamp");
			Document doc = docb.parse(is);
			doc.normalize();
			Element root = doc.getDocumentElement();
			NodeList lis = root.getChildNodes();

			String outputPath = root.getAttributes().getNamedItem("ofp").getNodeValue().toLowerCase();
			if (delta.getResource().getLocation().toOSString().indexOf(outputPath) == -1)
			{
				for (int i = 0; i < lis.getLength(); ++i)
				{
					if (lis.item(i).getNodeName() != "build") continue;
					int count = Integer.parseInt(lis.item(i).getAttributes().getNamedItem("count").getNodeValue());
					++count;
					lis.item(i).getAttributes().getNamedItem("count").setNodeValue(String.valueOf(count));
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					lis.item(i).getAttributes().getNamedItem("time").setNodeValue(sdf.format(new Date()));
				}
			}
			else
			{
				for (int i = 0; i < lis.getLength(); ++i)
				{
					if (lis.item(i).getNodeName() != "release") continue;
					int count = Integer.parseInt(lis.item(i).getAttributes().getNamedItem("count").getNodeValue());
					++count;
					lis.item(i).getAttributes().getNamedItem("count").setNodeValue(String.valueOf(count));
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					lis.item(i).getAttributes().getNamedItem("time").setNodeValue(sdf.format(new Date()));
				}
			}

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer tfer = tf.newTransformer();
			DOMSource s = new DOMSource(doc);
			StreamResult r = new StreamResult(new File(path + ".buildtimestamp"));
			tfer.transform(s, r);
		}
		catch (Exception localException)
		{
			createXml(delta);
		}
	}
}
