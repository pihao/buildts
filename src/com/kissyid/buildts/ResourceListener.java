package com.kissyid.buildts;

import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ResourceListener implements IResourceChangeListener {
	protected static boolean canUpdate = false;
	public static ResourceListener INS = null;

	public void resourceChanged(IResourceChangeEvent event)
	{
		IResourceDelta rd = event.getDelta();
		IResourceDeltaVisitor vis = new IResourceDeltaVisitor()
		{
			public boolean visit(IResourceDelta delta)
			{
				IResource r = delta.getResource();
				if ((r != null) && (r.getType() == 1))
				{
					String ext = r.getLocation().getFileExtension();
					if ((ext.equals("as")) || (ext.equals("mxml")))
					{
						ResourceListener.canUpdate = true;
					}
					if ((ext.equals("swf")) || (ext.equals("swc")))
					{
						System.out.println(ResourceListener.canUpdate);
						if (ResourceListener.canUpdate)
						{
							VersionUtil.updateVersion(delta);
							ResourceListener.canUpdate = false;
						}
						else
						{
							ResourceListener.this.checkReleaseUpdate(delta);
						}
					}
				}
				return true;
			}
		};

		try
		{
			rd.accept(vis);
		}
		catch (Exception localException) {}
	}

	public void checkReleaseUpdate(IResourceDelta delta)
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
			String outputPath = root.getAttributes().getNamedItem("ofp").getNodeValue().toLowerCase();
			if (delta.getResource().getLocation().toOSString().indexOf(outputPath) == -1) return;
			VersionUtil.updateVersion(delta);
		}
		catch (Exception localException) {}
	}
}
