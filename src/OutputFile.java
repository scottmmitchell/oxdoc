/**

oxdoc (c) Copyright 2005-2012 by Y. Zwols

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

 **/

package oxdoc;

import oxdoc.html.Header;

import java.io.*;
import java.text.MessageFormat;

public class OutputFile {
	private String fileName = null;
	private OxDoc oxdoc = null;
	private int iconType = 0;
	private String title = "";

	private StringBuffer content = new StringBuffer(); // main content
	private StringBuffer css = new StringBuffer(); // extra css style

	class ExtBufferedWriter extends BufferedWriter {
		ExtBufferedWriter(Writer out) {
			super(out);
		}

		void writeln(Object s) throws IOException {
			write(s.toString());
			newLine();
		}
	}

	// create an HTML file
	public OutputFile(String fileName, String title, int iconType, OxDoc oxdoc) throws IOException {
		this.oxdoc = oxdoc;
		this.title = title;
		this.iconType = iconType;
		this.fileName = fileName;
	}

	public void close() throws IOException {
		File aFile = new File(oxdoc.fileManager.outputFile(fileName).trim());
		aFile.getParentFile().mkdirs();
		ExtBufferedWriter output = new ExtBufferedWriter(new FileWriter(aFile));

		writeDocHeader(output);
		output.write(content.toString());
		writeDocFooter(output);
		output.close();
	}

	public void write(Object s) {
		content.append(s.toString());
	}

	public void writeln(Object s) {
		content.append(s.toString() + "\n");
	}

	public void append_css(Object s) {
		css.append(s.toString() + "\n");
	}

	private void writeDocHeader(ExtBufferedWriter output) throws IOException {
		output.writeln("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		// writeln("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
		output.writeln("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">");
		output.writeln("<head>");
		if (css.length() > 0) {
			output.writeln("<style type=\"text/css\">");
			output.writeln(css.toString());
			output.writeln("</style>");
		}

		if (oxdoc.config.EnableIcons)
			output.writeln("<link rel=\"stylesheet\" type=\"text/css\" href=\"oxdoc.css\">");
		else
			output.writeln("<link rel=\"stylesheet\" type=\"text/css\" href=\"oxdoc-noicons.css\">");
		output.writeln("<link rel=\"stylesheet\" type=\"text/css\" media=\"print\" href=\"print.css\">");
		output.writeln(oxdoc.config.MathProcessor.ExtraHeader());
		output.writeln("<title>" + title
				+ ((oxdoc.config.WindowTitle.length() > 0) ? " - " + oxdoc.config.WindowTitle : "") + "</title>");
		output.writeln("</head>");
		output.writeln("<body>");
		output.writeln("<div class=\"header\">");
		output.write("[ ");
		if (oxdoc.config.UpLevel)
			output.writeln(oxdoc.fileManager.smallIcon(FileManager.UPLEVEL)
					+ "<a href=\"..\\default.html\">Up Level</a> |");
		/** Added by CF **/

		output.writeln(oxdoc.fileManager.smallIcon(FileManager.PROJECT) + "<a href=\"default.html\">Project home</a>");
		output.writeln(" | " + oxdoc.fileManager.smallIcon(FileManager.INDEX) + "<a href=\"index.html\">Index</a>");
		output.writeln(" | " + oxdoc.fileManager.smallIcon(FileManager.HIERARCHY)
				+ "<a href=\"hierarchy.html\">Class hierarchy</a> ]</div>");

		Header h1 = new Header(oxdoc, 1, iconType, title);
		output.writeln(h1);
	}

	private void writeDocFooter(ExtBufferedWriter output) throws IOException {
		Object[] args = { OxDoc.ProductName, OxDoc.Version, OxDoc.Url };

		output.writeln("<div class=\"footer\">");
		output.writeln(MessageFormat.format(
				"Generated by <a href=\"{2}\">{0} {1}</a> &copy Copyright 2005-2012 by Y. Zwols<br>", args));
		output.writeln(oxdoc.config.MathProcessor.ExtraFooter());
		output.writeln("</div>");
	}
}
