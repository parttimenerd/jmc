/*
 * Copyright (c) 2026, Johannes Bechberger and/or its affiliates. All rights reserved.
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * The contents of this file are subject to the terms of either the Universal Permissive License
 * v 1.0 as shown at https://oss.oracle.com/licenses/upl
 */
package org.openjdk.jmc.flightrecorder.cjfr;

import org.eclipse.jface.dialogs.ProgressIndicator;
import org.openjdk.jmc.flightrecorder.ui.JfrEditor;
import org.openjdk.jmc.flightrecorder.ui.RecordingLoader;

/**
 * Eclipse editor for .cjfr (condensed JFR) files. Inflates the recording to standard JFR bytes in
 * memory and hands them to the standard JFR loading machinery.
 */
public class CjfrEditor extends JfrEditor {

	public static final String EDITOR_ID = "org.openjdk.jmc.flightrecorder.cjfr.CjfrEditor"; //$NON-NLS-1$

	@Override
	protected RecordingLoader createRecordingLoader(ProgressIndicator progressIndicator) {
		return new CjfrRecordingLoader(this, progressIndicator);
	}
}
