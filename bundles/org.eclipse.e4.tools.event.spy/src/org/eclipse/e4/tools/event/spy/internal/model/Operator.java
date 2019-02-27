/*******************************************************************************
 * Copyright (c) 2013 IBM Corporation and others.
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
 *******************************************************************************/
package org.eclipse.e4.tools.event.spy.internal.model;

public enum Operator {
	NotSelected("-- operator --", false), Equals("equals to", true), NotEquals("not equals to",
			false), Contains("contains", true), NotContains("not contains", false), StartsWith("starts with",
					true), NotStartsWith("not starts with", false);

	private String text;

	private boolean positive;

	private Operator(String text, boolean positive) {
		this.text = text;
		this.positive = positive;
	}

	@Override
	public String toString() {
		return text;
	}

	public boolean isPositive() {
		return positive;
	}

	public static Operator toOperator(String text) {
		for (Operator operator : values()) {
			if (operator.text.equals(text)) {
				return operator;
			}
		}
		throw new IllegalArgumentException(String.format("%s not found for: %s", Operator.class.getSimpleName(), text));
	}
}
