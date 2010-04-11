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

	public static String BindingTableEditor_Label;
	public static String BindingTableEditor_Description;
	public static String BindingTableEditor_Id;
	public static String BindingTableEditor_ContextId;
	public static String BindingTableEditor_Find;
	public static String BindingTableEditor_Keybindings;
	public static String BindingTableEditor_KeySequence;
	public static String BindingTableEditor_Command;
	public static String BindingTableEditor_Up;
	public static String BindingTableEditor_Down;
	public static String BindingTableEditor_Add;
	public static String BindingTableEditor_Remove; 
	
	public static String CommandEditor_Label;
	public static String CommandEditor_Description;
	public static String CommandEditor_Id;
	public static String CommandEditor_Name;
	public static String CommandEditor_LabelDescription;
	public static String CommandEditor_Parameters;
	public static String CommandEditor_ParameterName;
	public static String CommandEditor_ParameterTypeId;
	public static String CommandEditor_ParameterOptional;
	public static String CommandEditor_ParameterOptional_No;
	public static String CommandEditor_ParameterOptional_Yes;
	public static String CommandEditor_Up;
	public static String CommandEditor_Down;
	public static String CommandEditor_Add;
	public static String CommandEditor_Remove;
	
	public static String DirectMenuItemEditor_Label;
	public static String DirectMenuItemEditor_Description;
	public static String DirectMenuItemEditor_ClassURI;
	public static String DirectMenuItemEditor_Find;
	
	public static String ControlFactory_BindingContexts;
	public static String ControlFactory_Add;
	public static String ControlFactory_Up;
	public static String ControlFactory_Down;
	public static String ControlFactory_Remove;
	public static String ControlFactory_Tags;
	public static String ControlFactory_Key;
	public static String ControlFactory_Value;
	
	static {
		NLS.initializeMessages(Messages.class.getName(), Messages.class);
	}
}
