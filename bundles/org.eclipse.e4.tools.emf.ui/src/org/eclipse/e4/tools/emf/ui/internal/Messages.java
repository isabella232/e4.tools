package org.eclipse.e4.tools.emf.ui.internal;

import org.eclipse.osgi.util.NLS;

public class Messages {

	public static String ApplicationEditor_Label;
	public static String ApplicationEditor_Description;
	public static String ApplicationEditor_Id;
	public static String ApplicationEditor_Handlers;
	public static String ApplicationEditor_PartDescriptors;
	public static String ApplicationEditor_BindingTables;
	public static String ApplicationEditor_Commands;
	public static String ApplicationEditor_Windows;

	static {
		NLS.initializeMessages(Messages.class.getName(), Messages.class);
	}
}
