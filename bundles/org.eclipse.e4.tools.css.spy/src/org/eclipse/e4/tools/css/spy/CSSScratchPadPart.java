package org.eclipse.e4.tools.css.spy;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.css.core.dom.ExtendedDocumentCSS;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.swt.internal.theme.ThemeEngine;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.w3c.css.sac.CSSParseException;
import org.w3c.dom.stylesheets.StyleSheet;
import org.w3c.dom.stylesheets.StyleSheetList;

@SuppressWarnings("restriction")
public class CSSScratchPadPart {
	@Inject
	@Optional
	private IThemeEngine themeEngine;

	/*
	 * public CSSScratchPadPart(Shell parentShell, IThemeEngine themeEngine) {
	 * super(parentShell); this.themeEngine = themeEngine;
	 * setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE/* | SWT.PRIMARY_MODAL
	 */
	// );
	// setShellStyle(SWT.DIALOG_TRIM | SWT.MAX | SWT.RESIZE
	// | getDefaultOrientation());
	// }

	/*
	 * @Override protected void configureShell(Shell newShell) {
	 * super.configureShell(newShell); newShell.setText("CSS Scratchpad"); }
	 */

	private static final int APPLY_ID = IDialogConstants.OK_ID + 100;
	/**
	 * Collection of buttons created by the <code>createButton</code> method.
	 */
	private HashMap<Integer, Button> buttons = new HashMap<>();

	private Text cssText;
	private Text exceptions;

	@PostConstruct
	protected Control createDialogArea(Composite parent) {

		Composite outer = parent;
		outer.setLayout(new GridLayout());
		outer.setLayoutData(new GridData(GridData.FILL_BOTH));

		SashForm sashForm = new SashForm(outer, SWT.VERTICAL);

		cssText = new Text(sashForm, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);

		exceptions = new Text(sashForm, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY);

		GridDataFactory.fillDefaults().grab(true, true).applyTo(sashForm);
		sashForm.setWeights(new int[] { 80, 20 });

		createButtonsForButtonBar(parent);
		return outer;
	}

	private void createButtonsForButtonBar(Composite parent) {
		createButton(parent, APPLY_ID, "Apply", true);
		createButton(parent, IDialogConstants.OK_ID, "Close", false);
		// createButton(parent, IDialogConstants.CANCEL_ID,
		// IDialogConstants.CANCEL_LABEL, false);
	}

	protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
		// increment the number of columns in the button bar
		((GridLayout) parent.getLayout()).numColumns++;
		Button button = new Button(parent, SWT.PUSH);
		button.setText(label);
		button.setFont(JFaceResources.getDialogFont());
		button.setData(Integer.valueOf(id));
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				buttonPressed(((Integer) event.widget.getData()).intValue());
			}
		});
		if (defaultButton) {
			Shell shell = parent.getShell();
			if (shell != null) {
				shell.setDefaultButton(button);
			}
		}
		buttons.put(Integer.valueOf(id), button);
		// setButtonLayoutData(button);
		return button;
	}

	protected void buttonPressed(int buttonId) {
		switch (buttonId) {
		case APPLY_ID:
			applyCSS();
			break;
		default:
			break;
		}
	}

	private void applyCSS() {
		if (themeEngine == null) {
			exceptions.setText("No theme engine available!");
			return;
		}
		long start = System.nanoTime();
		exceptions.setText("");

		StringBuilder sb = new StringBuilder();

		// FIXME: expose these new protocols: resetCurrentTheme() and
		// getCSSEngines()
		((ThemeEngine) themeEngine).resetCurrentTheme();

		int count = 0;
		for (CSSEngine engine : ((ThemeEngine) themeEngine).getCSSEngines()) {
			if (count++ > 0) {
				sb.append("\n\n");
			}
			sb.append("Engine[").append(engine.getClass().getSimpleName()).append("]");
			ExtendedDocumentCSS doc = (ExtendedDocumentCSS) engine.getDocumentCSS();
			List<StyleSheet> sheets = new ArrayList<>();
			StyleSheetList list = doc.getStyleSheets();
			for (int i = 0; i < list.getLength(); i++) {
				sheets.add(list.item(i));
			}

			try {
				Reader reader = new StringReader(cssText.getText());
				sheets.add(0, engine.parseStyleSheet(reader));
				doc.removeAllStyleSheets();
				for (StyleSheet sheet : sheets) {
					doc.addStyleSheet(sheet);
				}
				engine.reapply();

				long nanoDiff = System.nanoTime() - start;
				sb.append("\nTime: ").append(nanoDiff / 1000000).append("ms");
			} catch (CSSParseException e) {
				sb.append("\nError: line ").append(e.getLineNumber()).append(" col ").append(e.getColumnNumber())
						.append(": ").append(e.getLocalizedMessage());
			} catch (IOException e) {
				sb.append("\nError: ").append(e.getLocalizedMessage());
			}
		}
		exceptions.setText(sb.toString());
	}

}
