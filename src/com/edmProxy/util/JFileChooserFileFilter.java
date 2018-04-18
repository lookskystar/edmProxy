package com.edmProxy.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class JFileChooserFileFilter extends FileFilter {

	transient private static final Class CLASS = JFileChooserFileFilter.class;
	private final String exts = ".txt";
	private String description = "*.txt";

	@Override
	public boolean accept(File file) {
		if (file.isDirectory())
			return true;
		String fileName = file.getName();
		if (fileName.endsWith(exts))
			return true;
		

		return false;
	}

	@Override
	public String getDescription() {
		return description;
	}

}
