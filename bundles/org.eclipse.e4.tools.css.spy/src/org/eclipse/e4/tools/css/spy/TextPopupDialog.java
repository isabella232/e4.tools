package org.eclipse.e4.tools.css.spy;

import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class TextPopupDialog extends PopupDialog {

	private String content;
	private boolean contentEditable;

	public TextPopupDialog(Shell shell, String title, String content, boolean editable, 
			String infoText) {
		super(shell, SWT.RESIZE | SWT.ON_TOP, true, true, true, false, false, title, infoText);
		this.content = content;
		this.contentEditable = editable;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		int styleBits = SWT.MULTI | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL;
		if(!contentEditable) {
			styleBits |= SWT.READ_ONLY;
		}
		Text textWidget = new Text(container, styleBits);
		textWidget.setText(content);
		return container;
	}
	
	
}
