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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.databinding.swt.IWidgetValueProperty;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

public abstract class AbstractNewClassPage extends WizardPage {
	public static class JavaClass {
		protected PropertyChangeSupport support = new PropertyChangeSupport(this);

		private IPackageFragmentRoot fragmentRoot;
		private IPackageFragment packageFragment;
		private String name;

		public JavaClass(IPackageFragmentRoot fragmentRoot) {
			this.fragmentRoot = fragmentRoot;
		}

		public IPackageFragmentRoot getFragmentRoot() {
			return fragmentRoot;
		}

		public void setFragmentRoot(IPackageFragmentRoot fragmentRoot) {
			support.firePropertyChange("fragementRoot", this.fragmentRoot, this.fragmentRoot = fragmentRoot);
		}

		public IPackageFragment getPackageFragment() {
			return packageFragment;
		}

		public void setPackageFragment(IPackageFragment packageFragment) {
			support.firePropertyChange("packageFragment", this.packageFragment, this.packageFragment = packageFragment);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			support.firePropertyChange("name", this.name, this.name = name);
		}

		public void addPropertyChangeListener(PropertyChangeListener listener) {
			support.addPropertyChangeListener(listener);
		}

		public void removePropertyChangeListener(PropertyChangeListener listener) {
			support.removePropertyChangeListener(listener);
		}
	}

	private JavaClass clazz;
	private IWorkspace workspace;
	private IPackageFragmentRoot froot;
	
	protected AbstractNewClassPage(String pageName, IWorkspace workspace, String title, String description, IPackageFragmentRoot froot) {
		super(pageName);
		this.workspace = workspace;	
		this.froot = froot;
		
		setTitle(title);
		setDescription(description);
	}
	
	public void createControl(Composite parent) {
		final Image img = new Image(parent.getDisplay(), getClass().getClassLoader().getResourceAsStream("/icons/full/wizban/newclass_wiz.png"));
		setImageDescriptor(ImageDescriptor.createFromImage(img));
		
		parent.addDisposeListener(new DisposeListener() {
			
			public void widgetDisposed(DisposeEvent e) {
				img.dispose();
				setImageDescriptor(null);
			}
		});

		parent = new Composite(parent, SWT.NULL);		
		parent.setLayoutData(new GridData(GridData.FILL_BOTH));
		parent.setLayout(new GridLayout(3, false));
		
		clazz = createInstance();

		DataBindingContext dbc = new DataBindingContext();
		WizardPageSupport.create(this, dbc);
		
		{
			Label l = new Label(parent, SWT.NONE);
			l.setText("Source folder");
			l.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));

			Text t = new Text(parent, SWT.BORDER);
			t.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			t.setEditable(false);
			dbc.bindValue(
					WidgetProperties.text().observe(t), 
					BeanProperties.value("fragmentRoot").observe(clazz), 
					new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER), 
					new UpdateValueStrategy().setConverter(new PackageFragmentRootToStringConverter())
			);

			Button b = new Button(parent, SWT.PUSH);
			b.setText("Browse ...");
		}

		{
			Label l = new Label(parent, SWT.NONE);
			l.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
			l.setText("Package");

			Text t = new Text(parent, SWT.BORDER);
			t.setEditable(false);
			t.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			dbc.bindValue(
					WidgetProperties.text().observe(t), 
					BeanProperties.value("packageFragment").observe(clazz),
					new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER), 
					new UpdateValueStrategy().setConverter(new PackageFragmentToStringConverter())
			);

			Button b = new Button(parent, SWT.PUSH);
			b.setText("Browse ...");
			b.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					clazz.setPackageFragment(choosePackage());
				}
			});
		}

		{
			Label l = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
			l.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 3, 1));
		}

		{
			IWidgetValueProperty textProp = WidgetProperties.text(SWT.Modify);

			Label l = new Label(parent, SWT.NONE);
			l.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
			l.setText("Name");

			Text t = new Text(parent, SWT.BORDER);
			t.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			dbc.bindValue(textProp.observe(t), BeanProperties.value("name", String.class).observe(clazz));

			new Label(parent, SWT.NONE);
		}

		createFields(parent, dbc);
		setControl(parent);
	}
	
	protected IPackageFragment choosePackage() {
		IJavaElement[] packages= null;
		try {
			if (froot != null && froot.exists()) {
				packages= froot.getChildren();
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		if (packages == null) {
			packages= new IJavaElement[0];
		}

		ElementListSelectionDialog dialog= new ElementListSelectionDialog(getShell(), new JavaElementLabelProvider(JavaElementLabelProvider.SHOW_DEFAULT));
		dialog.setIgnoreCase(false);
		dialog.setTitle("Choose Package");
		dialog.setMessage("Choose a Package");
		dialog.setEmptyListMessage("You need to select a package");
		dialog.setElements(packages);
		dialog.setHelpAvailable(false);

		IPackageFragment pack= clazz.getPackageFragment();
		if (pack != null) {
			dialog.setInitialSelections(new Object[] { pack });
		}

		if (dialog.open() == Window.OK) {
			return (IPackageFragment) dialog.getFirstResult();
		}
		return null;
	}

	protected abstract void createFields(Composite parent, DataBindingContext dbc);

	protected abstract JavaClass createInstance();

	public JavaClass getClazz() {
		return clazz;
	}
	
	static class ClassnameValidator implements IValidator {

		public IStatus validate(Object value) {
			String name = value.toString();
			char[] ar = name.toCharArray();
			for (char c : ar) {
				if (!Character.isJavaIdentifierPart(c)) {
					return new Status(IStatus.ERROR, "", "'" + c + "' is not allowed in a Class-Name");
				}
			}

			if (!Character.isJavaIdentifierStart(ar[0])) {
				return new Status(IStatus.ERROR, "", "'" + ar[0] + "' is not allowed as the first character of a Class-Name");
			}

			return Status.OK_STATUS;
		}
	}

	static class PackageFragmentRootToStringConverter extends Converter {

		public PackageFragmentRootToStringConverter() {
			super(IPackageFragmentRoot.class, String.class);
		}

		public Object convert(Object fromObject) {
			IPackageFragmentRoot f = (IPackageFragmentRoot) fromObject;
			return f.getPath().makeRelative().toString();
		}
	}
	
	static class PackageFragmentToStringConverter extends Converter {

		public PackageFragmentToStringConverter() {
			super(IPackageFragment.class, String.class);
		}
		
		public Object convert(Object fromObject) {
			if( fromObject == null ) {
				return "";
			}
			IPackageFragment f = (IPackageFragment) fromObject;
			return f.getElementName();
		}
	}
}
