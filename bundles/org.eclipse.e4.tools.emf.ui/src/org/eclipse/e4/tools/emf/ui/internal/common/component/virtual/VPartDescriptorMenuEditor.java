package org.eclipse.e4.tools.emf.ui.internal.common.component.virtual;

import javax.inject.Inject;
import org.eclipse.e4.tools.emf.ui.internal.common.ModelEditor;
import org.eclipse.e4.tools.services.IResourcePool;
import org.eclipse.emf.edit.domain.EditingDomain;

public class VPartDescriptorMenuEditor extends VMenuEditor {
	@Inject
	public VPartDescriptorMenuEditor(EditingDomain editingDomain, ModelEditor editor, IResourcePool resourcePool) {
		super(editingDomain, editor, org.eclipse.e4.ui.model.application.descriptor.basic.impl.BasicPackageImpl.Literals.PART_DESCRIPTOR__MENUS, resourcePool);
	}

}
