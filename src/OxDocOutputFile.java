import java.io.*;
import java.util.*;

	public class OxDocOutputFile {

		Writer output = null;
		String _fileName;
		boolean isHtml = false;

		// create a blank file
		public OxDocOutputFile(String fileName) throws IOException {
			File aFile = new File(OutputFileName(fileName));
     		output = new BufferedWriter( new FileWriter(aFile) );
			_fileName = fileName;
		}
		

		// create an HTML file
		public OxDocOutputFile(String fileName, String title) throws IOException {
			this(fileName);
			isHtml = true;
			writeDocHeader(title);
		}

		private static String OutputFileName(String fileName) {
			return OxDocConfig.OutputDir + fileName;
		}

		public static boolean fileExists(String fileName) {
			File aFile = new File(OutputFileName(fileName));
			return aFile.exists();			
		}

		public void close() throws IOException {
			if (isHtml)
				writeDocFooter();
			output.close();
		}

		public void writeChar(int ch) throws IOException {
			output.write(ch);
		}

		public void write(Object s) throws IOException {
			output.write(s.toString());
		}

		public void writeln(Object s) throws IOException {
			output.write(s.toString() + "\n");
		}
		
		private void writeDocHeader(String title) throws IOException {
			writeln("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			writeln("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
			writeln("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">");
			writeln("<head>");
            		writeln("<link rel=\"stylesheet\" type=\"text/css\" href=\"oxdoc.css\">");
			writeln("<title>" + title + "</title>");
			writeln("</head>");
			writeln("<body>");
			writeln("<div align=\"right\">[ <a href=\"default.html\">Home</a> | <a href=\"index.html\">Index</a> ]</div>");
			writeln("<h1>" + title + "</h1>");
		}

		private String dateStr() {
			return (new Date()).toString();
		}
		
		private void writeDocFooter() throws IOException {
			writeln("<div align=\"center\"><small>Generated by " + oxdoc.ProductName + " " + oxdoc.Version +
					" on <script>document.write(document.lastModified);</script>." +
					"<br>Original filename: <tt>" + _fileName + "</tt>" +
					"<br>oxdoc &copy; Copyright 2005 by Y. Zwols [<a href=\"mailto:yori@brown.edu\">yori@brown.edu</a>]" +
					"</small></div>");
		}
}