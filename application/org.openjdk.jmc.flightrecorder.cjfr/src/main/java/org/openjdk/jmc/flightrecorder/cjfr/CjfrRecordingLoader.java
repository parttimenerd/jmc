/*
 * Copyright (c) 2026, Johannes Bechberger and/or its affiliates. All rights reserved.
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * The contents of this file are subject to the terms of either the Universal Permissive License
 * v 1.0 as shown at https://oss.oracle.com/licenses/upl
 */
package org.openjdk.jmc.flightrecorder.cjfr;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.eclipse.jface.dialogs.ProgressIndicator;
import org.openjdk.jmc.flightrecorder.CouldNotLoadRecordingException;
import org.openjdk.jmc.flightrecorder.JfrLoaderToolkit;
import org.openjdk.jmc.flightrecorder.internal.EventArrays;
import org.openjdk.jmc.flightrecorder.ui.FlightRecorderUI;
import org.openjdk.jmc.flightrecorder.ui.JfrEditor;
import org.openjdk.jmc.flightrecorder.ui.RecordingLoader;

import me.bechberger.condensed.CondensedInputStream;
import me.bechberger.jfr.BasicJFRReader;
import me.bechberger.jfr.WritingJFRReader;

/**
 * Loads a .cjfr file by inflating it to standard JFR bytes in memory, then delegating to
 * {@link JfrLoaderToolkit#loadStream}.
 */
public class CjfrRecordingLoader extends RecordingLoader {

	public CjfrRecordingLoader(JfrEditor editor, ProgressIndicator ui) {
		super(editor, ui);
	}

	@Override
	protected EventArrays doCreateRecording(File file, Runnable lm) throws CouldNotLoadRecordingException, IOException {
		if (!file.getName().endsWith(".cjfr")) { //$NON-NLS-1$
			return super.doCreateRecording(file, lm);
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (CondensedInputStream cin = new CondensedInputStream(new BufferedInputStream(new FileInputStream(file)))) {
			BasicJFRReader reader = new BasicJFRReader(cin);
			WritingJFRReader.toJFRStream(reader, baos);
		}
		boolean hideExperimentals = !FlightRecorderUI.getDefault().includeExperimentalEventsAndFields();
		boolean ignoreTruncated = FlightRecorderUI.getDefault().allowIncompleteRecordingFile();
		boolean showHiddenFrames = shouldShowHiddenFrames();
		return JfrLoaderToolkit.loadStream(new ByteArrayInputStream(baos.toByteArray()), hideExperimentals,
				ignoreTruncated, showHiddenFrames);
	}
}
