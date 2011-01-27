package org.eclipse.e4.tools.emf.ui.internal.common.component.virtual;

import javax.inject.Inject;
import org.eclipse.e4.tools.emf.ui.internal.common.ModelEditor;
import org.eclipse.e4.tools.services.IResourcePool;
import org.eclipse.e4.ui.model.application.ui.basic.impl.BasicPackageImpl;
import org.eclipse.emf.edit.domain.EditingDomain;

public class VPartMenuEditor extends VMenuEditor {
	@Inject
	public VPartMenuEditor(EditingDomain editingDomain, ModelEditor editor, IResourcePool resourcePool) {
		super(editingDomain, editor, BasicPackageImpl.Literals.PART__MENUS, resourcePool);
	}

}
