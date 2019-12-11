/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Anton Leherbauer (Wind River Systems) - [misc] Allow custom token for WhitespaceRule - https://bugs.eclipse.org/bugs/show_bug.cgi?id=251224
 *******************************************************************************/
package org.eclipse.e4.tools.emf.ui.script.js.text.scanners;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.e4.tools.emf.ui.script.js.ResourceProvider;
import org.eclipse.e4.tools.emf.ui.script.js.text.scanners.CombinedWordRule.CharacterBuffer;
import org.eclipse.e4.tools.emf.ui.script.js.text.scanners.CombinedWordRule.WordMatcher;
import org.eclipse.e4.tools.services.IResourcePool;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;

/**
 * A rule based JavaDoc scanner.
 */
public final class JavaScriptDocScanner extends JavaScriptCommentScanner {


	/**
	 * Detector for HTML comment delimiters.
	 */
	static class HTMLCommentDetector implements IWordDetector {

		/**
		 * @see IWordDetector#isWordStart(char)
		 */
		public boolean isWordStart(char c) {
			return (c == '<' || c == '-');
		}

		/**
		 * @see IWordDetector#isWordPart(char)
		 */
		public boolean isWordPart(char c) {
			return (c == '-' || c == '!' || c == '>');
		}
	}

	class TagRule extends SingleLineRule {

		/*
		 * @see SingleLineRule
		 */
		public TagRule(IToken token) {
			super("<", ">", token, (char) 0); //$NON-NLS-2$ //$NON-NLS-1$
		}

		/*
		 * @see SingleLineRule
		 */
		public TagRule(IToken token, char escapeCharacter) {
			super("<", ">", token, escapeCharacter); //$NON-NLS-2$ //$NON-NLS-1$
		}

		private IToken evaluateToken() {
			try {
				final String token= getDocument().get(getTokenOffset(), getTokenLength()) + "."; //$NON-NLS-1$

				int offset= 0;
				char character= token.charAt(++offset);

				if (character == '/')
					character= token.charAt(++offset);

				while (Character.isWhitespace(character))
					character= token.charAt(++offset);

				while (Character.isLetterOrDigit(character))
					character= token.charAt(++offset);

				while (Character.isWhitespace(character))
					character= token.charAt(++offset);

				if (offset >= 2 && token.charAt(offset) == fEndSequence[0])
					return fToken;

			} catch (BadLocationException exception) {
				// Do nothing
			}
			return getToken(ResourceProvider.JAVADOC_DEFAULT);
		}

		/*
		 * @see PatternRule#evaluate(ICharacterScanner)
		 */
		public IToken evaluate(ICharacterScanner scanner) {
			IToken result= super.evaluate(scanner);
			if (result == fToken)
				return evaluateToken();
			return result;
		}
	}

	private static String[] fgTokenProperties= {
		ResourceProvider.JAVADOC_KEYWORD,
		ResourceProvider.JAVADOC_TAG,
		ResourceProvider.JAVADOC_LINK,
		ResourceProvider.JAVADOC_DEFAULT/*,
		TASK_TAG*/
	};


	public JavaScriptDocScanner(IResourcePool manager, IPreferenceStore store) {
		super(manager, store, ResourceProvider.JAVADOC_DEFAULT, fgTokenProperties);
	}


	public IDocument getDocument() {
		return fDocument;
	}

	/*
	 * @see AbstractJavaScanner#createRules()
	 */
	protected List<IRule> createRules() {

		List<IRule> list= new ArrayList<IRule>();

		// Add rule for tags.
		Token token= getToken(ResourceProvider.JAVADOC_TAG);
		list.add(new TagRule(token));


		// Add rule for HTML comments
		WordRule wordRule= new WordRule(new HTMLCommentDetector(), token);
		wordRule.addWord("<!--", token); //$NON-NLS-1$
		wordRule.addWord("--!>", token); //$NON-NLS-1$
		list.add(wordRule);


		// Add rule for links.
		token= getToken(ResourceProvider.JAVADOC_LINK);
		list.add(new SingleLineRule("{@link", "}", token)); //$NON-NLS-2$ //$NON-NLS-1$
		list.add(new SingleLineRule("{@value", "}", token)); //$NON-NLS-2$ //$NON-NLS-1$
		list.add(new SingleLineRule("{@inheritDoc", "}", token)); //$NON-NLS-2$ //$NON-NLS-1$


		// Add generic whitespace rule.
		token= getToken(ResourceProvider.JAVADOC_DEFAULT);
		list.add(new WhitespaceRule(new JavaScriptWhitespaceDetector(), token));


		list.addAll(super.createRules());
		return list;
	}

	/*
	 * @see org.eclipse.jdt.internal.ui.text.JavaCommentScanner#createMatchers()
	 */
	protected List<WordMatcher> createMatchers() {
		List<WordMatcher> list= super.createMatchers();

		// Add word rule for keywords.
		final IToken token= getToken(ResourceProvider.JAVADOC_KEYWORD);
		WordMatcher matcher= new WordMatcher() {
			public IToken evaluate(ICharacterScanner scanner, CharacterBuffer word) {
				int length= word.length();
				if (length > 1 && word.charAt(0) == '@') {
					int i= 0;
					try {
						for (; i <= length; i++)
							scanner.unread();
						int c= scanner.read();
						i--;
						if (c == '*' || Character.isWhitespace((char)c)) {
							scanner.unread();
							return token;
						}
					} finally {
						for (; i >= 0; i--)
							scanner.read();
					}
				}
				return Token.UNDEFINED;
			}
		};
		list.add(matcher);

		return list;
	}
}