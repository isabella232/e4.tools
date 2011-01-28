package org.eclipse.e4.tools.emf.ui.internal.common.xml;

import java.io.IOException;
import java.io.StringWriter;
import org.eclipse.e4.tools.emf.ui.common.IModelResource;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.jface.text.Document;

public class EMFDocument {
	private IModelResource modelResource;
	private Document document;

	public EMFDocument(IModelResource modelResource) {
		this.modelResource = modelResource;
		this.document = new Document();
		updateFromEMF();
	}

	public void updateFromEMF() {
		this.document.set(toXMI((EObject) modelResource.getRoot().get(0)));
	}

	public Document getDocument() {
		return document;
	}

	private String toXMI(EObject root) {
		XMIResourceImpl resource = new XMIResourceImpl();
		resource.getContents().add(EcoreUtil.copy(root));
		StringWriter writer = new StringWriter();
		try {
			resource.save(writer, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return writer.toString();
	}
}
