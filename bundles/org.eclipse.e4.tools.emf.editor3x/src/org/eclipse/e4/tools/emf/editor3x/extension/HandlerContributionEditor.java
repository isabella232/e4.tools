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
package org.eclipse.e4.tools.emf.editor3x.extension;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.e4.tools.emf.editor3x.wizard.NewHandlerClassWizard;
import org.eclipse.e4.tools.emf.ui.common.IContributionClassCreator;
import org.eclipse.e4.ui.model.application.MContribution;
import org.eclipse.e4.ui.model.application.commands.impl.CommandsPackageImpl;
import org.eclipse.e4.ui.model.application.impl.ApplicationPackageImpl;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

public class HandlerContributionEditor implements IContributionClassCreator {

	public EClass getEditorClass() {
		return CommandsPackageImpl.Literals.HANDLER;
	}

	public void createOpen(MContribution contribution, EditingDomain domain, IProject project, Shell shell) {
		if( contribution.getContributionURI() == null || contribution.getContributionURI().trim().length() == 0 ) {
			NewHandlerClassWizard wizard = new NewHandlerClassWizard();
			wizard.init( null, new StructuredSelection(project));
			WizardDialog dialog = new WizardDialog(shell, wizard);
			if( dialog.open() == WizardDialog.OK ) {
				IFile f = wizard.getFile();
				ICompilationUnit el = JavaCore.createCompilationUnitFrom(f);
				try {
					String packageName = el.getPackageDeclarations()[0].getElementName();
					String className = el.getElementName();
					Command cmd = SetCommand.create(domain, contribution, ApplicationPackageImpl.Literals.CONTRIBUTION__CONTRIBUTION_URI, "platform:/plugin/" + f.getProject().getName() + "/" + packageName+"."+className);
					if( cmd.canExecute() ) {
						domain.getCommandStack().execute(cmd);
					}
					System.err.println("DONE");
				} catch (JavaModelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}