/* *****************************************************************************
 * FastODS - a Martin Schulz's SimpleODS fork
 *    Copyright (C) 2016 J. Férard <https://github.com/jferard>
 * SimpleODS - A lightweight java library to create simple OpenOffice spreadsheets
 *    Copyright (C) 2008-2013 Martin Schulz <mtschulz at users.sourceforge.net>
 *
 * This file is part of FastODS.
 *
 * FastODS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * FastODS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 * ****************************************************************************/
package com.github.jferard.fastods;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.logging.Logger;

import com.github.jferard.fastods.datastyle.DataStyleBuilderFactory;
import com.github.jferard.fastods.datastyle.LocaleDataStyles;
import com.github.jferard.fastods.entry.OdsEntries;
import com.github.jferard.fastods.util.EqualityUtil;
import com.github.jferard.fastods.util.PositionUtil;
import com.github.jferard.fastods.util.WriteUtil;
import com.github.jferard.fastods.util.XMLUtil;

import static com.github.jferard.fastods.OdsFactory.FileState.FILE_EXISTS;
import static com.github.jferard.fastods.OdsFactory.FileState.IS_DIRECTORY;
import static com.github.jferard.fastods.OdsFactory.FileState.OK;

/**
 *
 * @author Julien Férard
 */
public class OdsFactory {
	public static enum FileState {
		IS_DIRECTORY,
		FILE_EXISTS,
		OK
	}

	private final Logger logger;
	private final XMLUtil xmlUtil;
	private LocaleDataStyles format;
	private WriteUtil writeUtil;
	private PositionUtil positionUtil;
	
	public OdsFactory() {
		this(Locale.getDefault());
	}
	
	public OdsFactory(final Locale locale) {
		this.positionUtil = new PositionUtil(new EqualityUtil());
		this.writeUtil = new WriteUtil();
		this.xmlUtil = XMLUtil.create();
		final DataStyleBuilderFactory builderFactory = new DataStyleBuilderFactory(
				this.xmlUtil, locale);
		this.format = new LocaleDataStyles(builderFactory);
		this.logger = Logger.getLogger(OdsDocument.class.getName());
	}
	
	/**
	 * Create a new, empty file, use addTable to add tables.
	 *
	 * @param filename
	 *            - The filename of the new spreadsheet file, if this file
	 *            exists it is overwritten
	 */
	public FileState checkFile(final String filename) {
		final File f = new File(filename);
		if (f.isDirectory())
			return IS_DIRECTORY;

		if (f.exists())
			return FILE_EXISTS;

		return OK;
	}
	
	/**
	 * Create a new, empty document. Use addTable to add tables.
	 */
	public OdsDocument createDocument() {
		OdsEntries entries = OdsEntries.create(this.positionUtil, this.xmlUtil,
				this.writeUtil, this.format);
		return new OdsDocument(this.logger,
				entries, this.xmlUtil);
	}

	/**
	 * Create a new ODS file writer from a document. Be careful: this method opens immediatly a stream.
	 */
	public OdsFileWriter createWriter(OdsDocument document, String filename) throws FileNotFoundException {
		return OdsFileWriter.builder(this.logger, document).filename(filename).build();
	}
}
