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
package com.github.jferard.fastods.odselement;

import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.util.XMLUtil;
import com.github.jferard.fastods.util.ZipUTF8Writer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;

/**
 * 3.1.3.5 office:document-settings and 3.10 office:settings
 *
 * A typical {@code settings.xml} file has two {@code config-item-set}s:
 * <ul>
 *     <li>{@code ooo:view-settings}
 *     		<ul>
 *     		 	   <li>{@code config-item}s for the view settings</li>
 *     		 	   <li>{@code config-item-map-indexed} with a {@code config-item-map-entry} per view
 *     					<ul>
 *     		 	   			<li>{@code config-item}s of the wiew</li>
 *     		 	   			<li>a {@code config-item-map-named} with a {@code config-item-map-entry} per table
 * 		    					<ul>
 *     				 	   			<li>{@code config-item}s of the table in the wiew</li>
 * 		    					</ul>
 *     		 	   			</li>
 *     					</ul>
 *     		 	   </li>
 *     		</ul>
 *     	<li>{@code ooo:configuration-settings}
 *     		<ul>
 *     		    <li>{@code config-item}s for the configuration settings</li>
 *     		</ul>
 *     	</li>
 * </ul>
 * *
 *
 * @author Julien Férard
 * @author Martin Schulz
 */
@SuppressWarnings("PMD.CommentRequired")
public class SettingsElement implements OdsElement {
	private final ConfigItem allowPrintJobCancel;
	private final ConfigItem applyUserData;
	private final ConfigItem autoCalculate;
	private final ConfigItem characterCompressionType;

	private final ConfigItem gridColor;
	private final ConfigItem hasColumnRowHeaders;
	private final ConfigItem hasSheetTabs;
	private final ConfigItem isKernAsianPunctuation;
	private final ConfigItem isOutlineSymbolsSet;
	private final ConfigItem isRasterAxisSynchronized;
	private final ConfigItem isSnapToRaster;
	private final ConfigItem linkUpdateMode;
	private final ConfigItem loadReadonly;
	private final ConfigItem printerName;
	private final ConfigItem printerSetup;
	private final ConfigItem rasterIsVisible;
	private final ConfigItem rasterResolutionX;
	private final ConfigItem rasterResolutionY;
	private final ConfigItem rasterSubdivisionX;
	private final ConfigItem rasterSubdivisionY;
	private final ConfigItem saveVersionOnClose;
	private final ConfigItem showGrid;
	private final ConfigItem showNotes;
	private final ConfigItem showPageBreaks;
	// ConfigurationSettings
	private final ConfigItem showZeroValues;

	// private final List<String> tableConfigs;
	private List<Table> tables;
	private final ConfigItem updateFromTemplate;
	// ViewIdSettings
	private ConfigItem viewIdActiveTable;
	private final ConfigItem viewIdGridColor;
	private final ConfigItem viewIdHasColumnRowHeaders;
	private final ConfigItem viewIdHasSheetTabs;
	private final ConfigItem viewIdHorizontalScrollbarWidth;
	private final ConfigItem viewIdIsOutlineSymbolsSet;
	private final ConfigItem viewIdIsRasterAxisSynchronized;
	private final ConfigItem viewIdIsSnapToRaster;
	private final ConfigItem viewIdPageViewZoomValue;
	private final ConfigItem viewIdRasterIsVisible;
	private final ConfigItem viewIdRasterResolutionX;
	private final ConfigItem viewIdRasterResolutionY;
	private final ConfigItem viewIdRasterSubdivisionX;
	private final ConfigItem viewIdRasterSubdivisionY;
	private final ConfigItem viewIdShowGrid;
	private final ConfigItem viewIdShowNotes;
	private final ConfigItem viewIdShowPageBreakPreview;
	private final ConfigItem viewIdShowPageBreaks;
	private final ConfigItem viewIdShowZeroValues;
	private final ConfigItem viewIdZoomType;
	private final ConfigItem viewIdZoomValue;
	private final ConfigItem visibleAreaHeight;
	private final ConfigItem visibleAreaLeft;
	// ViewSettings
	private final ConfigItem visibleAreaTop;

	private final ConfigItem visibleAreaWidth;

	SettingsElement() {
		this.tables = Collections.emptyList();
		this.visibleAreaTop = new ConfigItem("VisibleAreaTop", "int", "0");
		this.visibleAreaLeft = new ConfigItem("VisibleAreaLeft", "int", "0");
		this.visibleAreaWidth = new ConfigItem("VisibleAreaWidth", "int",
				"680");
		this.visibleAreaHeight = new ConfigItem("VisibleAreaHeight", "int",
				"400");
		this.viewIdActiveTable = new ConfigItem("ActiveTable", "string",
				"Tab1");
		this.viewIdHorizontalScrollbarWidth = new ConfigItem(
				"ViewIdHorizontalScrollbarWidth", "int", "270");
		this.viewIdZoomType = new ConfigItem("ViewIdZoomType", "short", "0");
		this.viewIdZoomValue = new ConfigItem("ViewIdZoomValue", "int", "100");
		this.viewIdPageViewZoomValue = new ConfigItem("ViewIdPageViewZoomValue",
				"int", "60");
		this.viewIdShowPageBreakPreview = new ConfigItem(
				"ViewIdShowPageBreakPreview", "boolean", "false");
		this.viewIdShowZeroValues = new ConfigItem("ViewIdShowZeroValues",
				"boolean", "true");
		this.viewIdShowNotes = new ConfigItem("ViewIdShowNotes", "boolean",
				"true");
		this.viewIdShowGrid = new ConfigItem("ViewIdShowGrid", "boolean",
				"true");
		this.viewIdGridColor = new ConfigItem("ViewIdGridColor", "long",
				"12632256");
		this.viewIdShowPageBreaks = new ConfigItem("ViewIdShowPageBreaks",
				"boolean", "true");
		this.viewIdHasColumnRowHeaders = new ConfigItem(
				"ViewIdHasColumnRowHeaders", "boolean", "true");
		this.viewIdHasSheetTabs = new ConfigItem("ViewIdHasSheetTabs",
				"boolean", "true");
		this.viewIdIsOutlineSymbolsSet = new ConfigItem(
				"ViewIdIsOutlineSymbolsSet", "boolean", "true");
		this.viewIdIsSnapToRaster = new ConfigItem("ViewIdIsSnapToRaster",
				"boolean", "false");
		this.viewIdRasterIsVisible = new ConfigItem("ViewIdRasterIsVisible",
				"boolean", "false");
		this.viewIdRasterResolutionX = new ConfigItem("ViewIdRasterResolutionX",
				"int", "1000");
		this.viewIdRasterResolutionY = new ConfigItem("ViewIdRasterResolutionY",
				"int", "1000");
		this.viewIdRasterSubdivisionX = new ConfigItem(
				"ViewIdRasterSubdivisionX", "int", "1");
		this.viewIdRasterSubdivisionY = new ConfigItem(
				"ViewIdRasterSubdivisionY", "int", "1");
		this.viewIdIsRasterAxisSynchronized = new ConfigItem(
				"ViewIdIsRasterAxisSynchronized", "boolean", "true");
		this.showZeroValues = new ConfigItem("ShowZeroValues", "boolean",
				"true");
		this.showNotes = new ConfigItem("ShowNotes", "boolean", "true");
		this.showGrid = new ConfigItem("ShowGrid", "boolean", "true");
		this.gridColor = new ConfigItem("GridColor", "long", "12632256");
		this.showPageBreaks = new ConfigItem("ShowPageBreaks", "boolean",
				"true");
		this.linkUpdateMode = new ConfigItem("LinkUpdateMode", "short", "3");
		this.hasColumnRowHeaders = new ConfigItem("HasColumnRowHeaders",
				"boolean", "true");
		this.hasSheetTabs = new ConfigItem("HasSheetTabs", "boolean", "true");
		this.isOutlineSymbolsSet = new ConfigItem("IsOutlineSymbolsSet",
				"boolean", "true");
		this.isSnapToRaster = new ConfigItem("IsSnapToRaster", "boolean",
				"false");
		this.rasterIsVisible = new ConfigItem("RasterIsVisible", "boolean",
				"false");
		this.rasterResolutionX = new ConfigItem("RasterResolutionX", "int",
				"1000");
		this.rasterResolutionY = new ConfigItem("RasterResolutionY", "int",
				"1000");
		this.rasterSubdivisionX = new ConfigItem("RasterSubdivisionX", "int",
				"1");
		this.rasterSubdivisionY = new ConfigItem("RasterSubdivisionY", "int",
				"1");
		this.isRasterAxisSynchronized = new ConfigItem(
				"IsRasterAxisSynchronized", "boolean", "true");
		this.autoCalculate = new ConfigItem("AutoCalculate", "boolean", "true");
		this.printerName = new ConfigItem("PrinterName", "string", "");
		this.printerSetup = new ConfigItem("PrinterSetup", "base64Binary", "");
		this.applyUserData = new ConfigItem("ApplyUserData", "boolean", "true");
		this.characterCompressionType = new ConfigItem(
				"CharacterCompressionType", "short", "0");
		this.isKernAsianPunctuation = new ConfigItem("IsKernAsianPunctuation",
				"boolean", "false");
		this.saveVersionOnClose = new ConfigItem("SaveVersionOnClose",
				"boolean", "false");
		this.updateFromTemplate = new ConfigItem("UpdateFromTemplate",
				"boolean", "true");
		this.allowPrintJobCancel = new ConfigItem("AllowPrintJobCancel",
				"boolean", "true");
		this.loadReadonly = new ConfigItem("LoadReadonly", "boolean", "false");
		// this.tableConfigs = new LinkedList<String>();
	}

	/**
	 * Set the active table , this is the table that is shown if you open the
	 * file.
	 *
	 * @param table
	 *            The table to show
	 */
	public void setActiveTable(final Table table) {
		this.viewIdActiveTable = new ConfigItem("ActiveTable", "string",
				table.getName());
	}

	public void setTables(final List<Table> tables) {
		this.tables = tables;
	}

	@Override
	public void write(final XMLUtil util, final ZipUTF8Writer writer)
			throws IOException {
		writer.putNextEntry(new ZipEntry("settings.xml"));
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		writer.write(
				"<office:document-settings xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:settingsEntry=\"urn:oasis:names:tc:opendocument:xmlns:settingsEntry:1.0\" xmlns:ooo=\"http://openoffice.org/2004/office\" office:version=\"1.1\">");
		writer.write("<office:settings>");
		writer.write(
				"<config:config-item-set config:name=\"ooo:view-settings\">");
		this.visibleAreaTop.appendXML(util, writer);
		this.visibleAreaLeft.appendXML(util, writer);
		this.visibleAreaWidth.appendXML(util, writer);
		this.visibleAreaHeight.appendXML(util, writer);
		writer.write(
				"<config:config-item-map-indexed config:name=\"Views\">");
		writer.write("<config:config-item-map-entry>");
		writer.write(
				"<config:config-item config:name=\"ViewId\" settingsEntry:type=\"string\">View1</config:config-item>");
		writer.write(
				"<config:config-item-map-named config:name=\"Tables\">");

		for (final Table t : this.tables)
			t.appendXMLToSettingsElement(util, writer);

		writer.write("</config:config-item-map-named>");
		this.viewIdActiveTable.appendXML(util, writer);
		this.viewIdHorizontalScrollbarWidth.appendXML(util, writer);
		this.viewIdPageViewZoomValue.appendXML(util, writer);
		this.viewIdZoomType.appendXML(util, writer);
		this.viewIdZoomValue.appendXML(util, writer);
		this.viewIdShowPageBreakPreview.appendXML(util, writer);
		this.viewIdShowZeroValues.appendXML(util, writer);
		this.viewIdShowNotes.appendXML(util, writer);
		this.viewIdShowGrid.appendXML(util, writer);
		this.viewIdGridColor.appendXML(util, writer);
		this.viewIdShowPageBreaks.appendXML(util, writer);
		this.viewIdHasColumnRowHeaders.appendXML(util, writer);
		this.viewIdIsOutlineSymbolsSet.appendXML(util, writer);
		this.viewIdHasSheetTabs.appendXML(util, writer);
		this.viewIdIsSnapToRaster.appendXML(util, writer);
		this.viewIdRasterIsVisible.appendXML(util, writer);
		this.viewIdRasterResolutionX.appendXML(util, writer);
		this.viewIdRasterResolutionY.appendXML(util, writer);
		this.viewIdRasterSubdivisionX.appendXML(util, writer);

		this.viewIdRasterSubdivisionY.appendXML(util, writer);
		this.viewIdIsRasterAxisSynchronized.appendXML(util, writer);
		writer.write("</config:config-item-map-entry>");
		writer.write("</config:config-item-map-indexed>");
		writer.write("</config:config-item-set>");
		writer.write(
				"<config:config-item-set config:name=\"ooo:configuration-settings\">");
		this.showZeroValues.appendXML(util, writer);
		this.showNotes.appendXML(util, writer);
		this.showGrid.appendXML(util, writer);
		this.gridColor.appendXML(util, writer);
		this.showPageBreaks.appendXML(util, writer);
		this.linkUpdateMode.appendXML(util, writer);
		this.hasColumnRowHeaders.appendXML(util, writer);
		this.hasSheetTabs.appendXML(util, writer);
		this.isOutlineSymbolsSet.appendXML(util, writer);
		this.isSnapToRaster.appendXML(util, writer);
		this.rasterIsVisible.appendXML(util, writer);
		this.rasterResolutionX.appendXML(util, writer);
		this.rasterResolutionY.appendXML(util, writer);
		this.rasterSubdivisionX.appendXML(util, writer);
		this.rasterSubdivisionY.appendXML(util, writer);
		this.isRasterAxisSynchronized.appendXML(util, writer);
		this.autoCalculate.appendXML(util, writer);
		this.printerName.appendXML(util, writer);
		this.printerSetup.appendXML(util, writer);
		this.applyUserData.appendXML(util, writer);
		this.characterCompressionType.appendXML(util, writer);
		this.isKernAsianPunctuation.appendXML(util, writer);
		this.saveVersionOnClose.appendXML(util, writer);
		this.updateFromTemplate.appendXML(util, writer);
		this.allowPrintJobCancel.appendXML(util, writer);
		this.loadReadonly.appendXML(util, writer);
		writer.write("</config:config-item-set>");
		writer.write("</office:settings>");
		writer.write("</office:document-settings>");
		writer.flush();
		writer.closeEntry();
	}
}
