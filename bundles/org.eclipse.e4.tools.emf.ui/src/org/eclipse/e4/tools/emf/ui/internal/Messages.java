/*******************************************************************************
 * Copyright (c) 2010 BestSolution.at and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 ******************************************************************************/
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
	
	public static String DirectToolItemEditor_ClassURI;
	public static String DirectToolItemEditor_Find;
	public static String DirectToolItemEditor_Label;
	public static String DirectToolItemEditor_Description;
	
	public static String HandledMenuItemEditor_Label;
	public static String HandledMenuItemEditor_Description;
	public static String HandledMenuItemEditor_Command;
	public static String HandledMenuItemEditor_Find;
	public static String HandledMenuItemEditor_Parameters;
	public static String HandledMenuItemEditor_Tag;
	public static String HandledMenuItemEditor_Value;
	public static String HandledMenuItemEditor_Up;
	public static String HandledMenuItemEditor_Down;
	public static String HandledMenuItemEditor_Add;
	public static String HandledMenuItemEditor_Remove;
	
	public static String HandledToolItemEditor_Command;
	public static String HandledToolItemEditor_Find;
	public static String HandledToolItemEditor_Parameters;
	public static String HandledToolItemEditor_ParametersName;
	public static String HandledToolItemEditor_ParametersValue;
	public static String HandledToolItemEditor_Up;
	public static String HandledToolItemEditor_Down;
	public static String HandledToolItemEditor_Add;
	public static String HandledToolItemEditor_Remove;
	public static String HandledToolItemEditor_Label;
	public static String HandledToolItemEditor_Description;
	
	public static String HandlerEditor_Label;
	public static String HandlerEditor_Description;
	public static String HandlerEditor_Id;
	public static String HandlerEditor_Command;
	public static String HandlerEditor_Find;
	public static String HandlerEditor_ClassURI;
	
	public static String InputPartEditor_Label;
	public static String InputPartEditor_InputURI;
	
	public static String KeyBindingEditor_Label;
	public static String KeyBindingEditor_Description;
	public static String KeyBindingEditor_Id;
	public static String KeyBindingEditor_Sequence;
	public static String KeyBindingEditor_Command;
	public static String KeyBindingEditor_Find;
	public static String KeyBindingEditor_Parameters;
	
	static {
		NLS.initializeMessages(Messages.class.getName(), Messages.class);
	}
}
