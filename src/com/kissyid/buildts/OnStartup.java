package com.kissyid.buildts;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.IStartup;

public class OnStartup implements IStartup
{
	public void earlyStartup()
	{
		if (ResourceListener.INS != null) return;
		ResourceListener.INS = new ResourceListener();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(ResourceListener.INS);
	}
}
