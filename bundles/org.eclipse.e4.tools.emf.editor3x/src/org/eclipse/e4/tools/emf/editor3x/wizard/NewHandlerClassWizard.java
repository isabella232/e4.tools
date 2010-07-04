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
package org.eclipse.e4.tools.emf.editor3x.wizard;

import java.io.ByteArrayInputStream;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.tools.emf.editor3x.templates.HandlerTemplate;
import org.eclipse.e4.tools.emf.editor3x.wizard.AbstractNewClassPage.JavaClass;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.databinding.swt.IWidgetValueProperty;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class NewHandlerClassWizard extends Wizard implements INewWizard {
	private IPackageFragmentRoot root;
	private IFile file;

	@Override
	public void addPages() {
		addPage(new AbstractNewClassPage("Classinformation",
				ResourcesPlugin.getWorkspace(), "New Handler",
				"Create a new handler class", root) {

			@Override
			protected JavaClass createInstance() {
				return new HandlerClass(root);
			}

			@Override
			protected void createFields(Composite parent, DataBindingContext dbc) {
				IWidgetValueProperty textProp = WidgetProperties
						.text(SWT.Modify);

				{
					Label l = new Label(parent, SWT.NONE);
					l.setText("Execute Method");
					l.setLayoutData(new GridData(GridData.END, GridData.CENTER,
							false, false));

					Text t = new Text(parent, SWT.BORDER);
					t.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
					dbc.bindValue(
							textProp.observe(t),
							BeanProperties.value("executeMethodName").observe(
									getClazz()));

					l = new Label(parent, SWT.NONE);
				}

				{
					Label l = new Label(parent, SWT.NONE);
					l.setText("Can-Execute Method");
					l.setLayoutData(new GridData(GridData.END, GridData.CENTER,
							false, false));

					Text t = new Text(parent, SWT.BORDER);
					t.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
					dbc.bindValue(textProp.observe(t),
							BeanProperties.value("canExecuteMethodName")
									.observe(getClazz()));
					dbc.bindValue(
							WidgetProperties.enabled().observe(t),
							BeanProperties.value("useCanExecute").observe(
									getClazz()));

					Button b = new Button(parent, SWT.CHECK);
					dbc.bindValue(
							WidgetProperties.selection().observe(b),
							BeanProperties.value("useCanExecute").observe(
									getClazz()));

					l = new Label(parent, SWT.NONE);
				}
			}
		});
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		root = getFragmentRoot(getInitialJavaElement(selection));
	}

	protected IJavaElement getInitialJavaElement(IStructuredSelection selection) {
		IJavaElement jelem = null;
		if (selection != null && !selection.isEmpty()) {
			Object selectedElement = selection.getFirstElement();
			if (selectedElement instanceof IAdaptable) {
				IAdaptable adaptable = (IAdaptable) selectedElement;

				jelem = (IJavaElement) adaptable.getAdapter(IJavaElement.class);
				if (jelem == null || !jelem.exists()) {
					jelem = null;
					IResource resource = (IResource) adaptable
							.getAdapter(IResource.class);
					if (resource != null
							&& resource.getType() != IResource.ROOT) {
						while (jelem == null
								&& resource.getType() != IResource.PROJECT) {
							resource = resource.getParent();
							jelem = (IJavaElement) resource
									.getAdapter(IJavaElement.class);
						}
						if (jelem == null) {
							jelem = JavaCore.create(resource); // java project
						}
					}
				}
			}
		}

		return jelem;
	}

	protected IPackageFragmentRoot getFragmentRoot(IJavaElement elem) {
		IPackageFragmentRoot initRoot = null;
		if (elem != null) {
			initRoot = (IPackageFragmentRoot) elem
					.getAncestor(IJavaElement.PACKAGE_FRAGMENT_ROOT);
			try {
				if (initRoot == null
						|| initRoot.getKind() != IPackageFragmentRoot.K_SOURCE) {
					IJavaProject jproject = elem.getJavaProject();
					if (jproject != null) {
						initRoot = null;
						if (jproject.exists()) {
							IPackageFragmentRoot[] roots = jproject
									.getPackageFragmentRoots();
							for (int i = 0; i < roots.length; i++) {
								if (roots[i].getKind() == IPackageFragmentRoot.K_SOURCE) {
									initRoot = roots[i];
									break;
								}
							}
						}
						if (initRoot == null) {
							initRoot = jproject.getPackageFragmentRoot(jproject
									.getResource());
						}
					}
				}
			} catch (JavaModelException e) {
				// TODO
				e.printStackTrace();
			}
		}
		return initRoot;
	}

	@Override
	public boolean performFinish() {
		HandlerClass clazz = (HandlerClass) ((AbstractNewClassPage) getPages()[0])
				.getClazz();

		HandlerTemplate template = new HandlerTemplate();
		String content = template.generate(clazz);

		IPackageFragment fragment = clazz.getPackageFragment();
		if (fragment != null) {
			String cuName = clazz.getName() + ".java";
			IResource resource = fragment.getCompilationUnit(cuName)
					.getResource();
			file = (IFile) resource;
			try {
				if (!file.exists()) {
					file.create(new ByteArrayInputStream(content.getBytes()),
							true, null);
				} else {
					file.setContents(new ByteArrayInputStream(content.getBytes()),
							IFile.FORCE | IFile.KEEP_HISTORY, null);
				}
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return true;

	}
	
	public IFile getFile() {
		return file;
	}

	public static class HandlerClass extends JavaClass {
		private String executeMethodName = "execute";
		private String canExecuteMethodName = "canExecute";
		private boolean useCanExecute = false;

		public HandlerClass(IPackageFragmentRoot root) {
			super(root);
		}

		public String getExecuteMethodName() {
			return executeMethodName;
		}

		public void setExecuteMethodName(String executeMethodName) {
			support.firePropertyChange("executeMethodName",
					this.executeMethodName,
					this.executeMethodName = executeMethodName);
		}

		public String getCanExecuteMethodName() {
			return canExecuteMethodName;
		}

		public void setCanExecuteMethodName(String canExecuteMethodName) {
			support.firePropertyChange("canExecuteMethodName",
					this.canExecuteMethodName,
					this.canExecuteMethodName = canExecuteMethodName);
		}

		public boolean isUseCanExecute() {
			return useCanExecute;
		}

		public void setUseCanExecute(boolean useCanExecute) {
			support.firePropertyChange("useCanExecute", this.useCanExecute,
					this.useCanExecute = useCanExecute);
		}
	}
}
