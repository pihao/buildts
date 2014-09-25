package com.kissyid.buildts;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class SwitcherAction implements IWorkbenchWindowActionDelegate
{
	public void run(IAction action)
	{
		if (ResourceListener.INS == null)
		{
			ResourceListener.INS = new ResourceListener();
			ResourcesPlugin.getWorkspace().addResourceChangeListener(ResourceListener.INS);
		}
		else
		{
			ResourcesPlugin.getWorkspace().removeResourceChangeListener(ResourceListener.INS);
			ResourceListener.INS = null;
		}
	}

	public void selectionChanged(IAction action, ISelection selection)
	{
	}

	public void dispose()
	{
	}

	public void init(IWorkbenchWindow window)
	{
	}

}
