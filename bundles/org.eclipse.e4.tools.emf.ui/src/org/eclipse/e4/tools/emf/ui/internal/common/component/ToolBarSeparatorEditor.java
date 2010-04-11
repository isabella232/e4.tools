package org.eclipse.e4.tools.emf.ui.internal.common.component;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.e4.tools.emf.ui.common.component.AbstractComponentEditor;
import org.eclipse.e4.ui.model.application.impl.ApplicationPackageImpl;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.databinding.swt.IWidgetValueProperty;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ToolBarSeparatorEditor extends AbstractComponentEditor {
	private Image separatorImage;
	private Composite composite;
	private EMFDataBindingContext context;
	
	public ToolBarSeparatorEditor(EditingDomain editingDomain) {
		super(editingDomain);
	}

	@Override
	public Image getImage(Object element, Display display) {
		if (separatorImage == null) {
			try {
				separatorImage = loadSharedImage(display, new URL("platform:/plugin/org.eclipse.e4.ui.model.workbench.edit/icons/full/obj16/MenuItem_separator.gif"));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return separatorImage;
	}

	@Override
	public String getLabel(Object element) {
		return "Separator";
	}

	@Override
	public String getDetailLabel(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Composite getEditor(Composite parent, Object object) {
		if (composite == null) {
			context = new EMFDataBindingContext();
			composite = createForm(parent, context, getMaster());
		}
		getMaster().setValue(object);
		return composite;
	}
	
	private Composite createForm(Composite parent, EMFDataBindingContext context, WritableValue master) {
		parent = new Composite(parent, SWT.NONE);
		parent.setLayout(new GridLayout(3, false));

		IWidgetValueProperty textProp = WidgetProperties.text(SWT.Modify);

		// ------------------------------------------------------------
		{
			Label l = new Label(parent, SWT.NONE);
			l.setText("Id");

			Text t = new Text(parent, SWT.BORDER);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			t.setLayoutData(gd);
			context.bindValue(textProp.observeDelayed(200, t), EMFEditProperties.value(getEditingDomain(), ApplicationPackageImpl.Literals.APPLICATION_ELEMENT__ELEMENT_ID).observeDetail(getMaster()));
		}

		return parent;
	}

	@Override
	public IObservableList getChildList(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

}
