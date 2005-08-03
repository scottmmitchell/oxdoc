import java.io.*;
import java.util.*;
import java.text.*;
import java.util.regex.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

	public class LatexImageManager extends ArrayList {
		private static Hashtable _formulas = new Hashtable();
		private static Hashtable _filenames = new Hashtable();
		private static boolean _cacheLoaded = false;
		private static int _imageCounter = 0;

		private static class ImageEntry {
			public String _filename = "";
			public String _formula = "";
			public boolean needsGenerating = true;

			public ImageEntry(String formula, String filename) {
				_filename = filename;
				_formula = formula;
				_formulas.put(formula, this);
				_filenames.put(filename, this);
			}

			public String filename() { return _filename; }
			public String formula()  { return _formula; }

			public static ImageEntry register(String Formula) {
				return register(Formula, null);
			}

			public static ImageEntry register(String formula, String filename) {
				if (!_cacheLoaded) {
					_cacheLoaded = true;
					loadCache();
				}

				formula = formula.trim().replace('\n',' ');
				ImageEntry entry = ((ImageEntry) _formulas.get(formula));
				if (entry != null)
					return entry;

				if (filename == null) {
					do {
						filename = "img" + (new Integer(++_imageCounter)).toString() + ".png";
					} while (_filenames.get(filename) != null);
				}
			
				return new ImageEntry(formula, filename);
			}
		}

		public static String getFormulaFilename(String Formula) {
			return ImageEntry.register(Formula, null).filename();
		}

		private static void saveCache() {
			try {
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				Document doc = builder.newDocument();

				Element root = doc.createElement("cache");
				doc.appendChild(root);
				
				for (Enumeration enum = _formulas.elements(); enum.hasMoreElements() ;) {
					ImageEntry e = (ImageEntry) enum.nextElement();
					Element newElement = doc.createElement("image");
					newElement.setAttribute("formula", e.formula());
					newElement.setAttribute("filename", e.filename());
					root.appendChild(newElement);
				}

            	// Prepare the DOM document for writing
            	Source source = new DOMSource(doc);
    
            	// Prepare the output file
            	File file = new File(FileManager.imageCache());
            	Result result = new StreamResult(file);
    
            	// Write the DOM document to the file
            	Transformer xformer = TransformerFactory.newInstance().newTransformer();
            	xformer.transform(source, result);
        	}
			catch (Exception e) {
				oxdoc.warning("Error writing image cache. Don't worry.");
			}
		}

		private static void loadCache() {
    		try {
            	File file = new File(FileManager.imageCache());
				if (!file.exists())
					return;

				// Parse the file
         		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         		Document doc = builder.parse(FileManager.imageCache());

         		// Find the tags of interest
         		NodeList nodes = doc.getElementsByTagName("image");
				for (int i = 0; i < nodes.getLength(); i++) {
            		Element element = (Element) nodes.item(i);
					String formula  = element.getAttribute("formula");
					String filename = element.getAttribute("filename");
					if (FileManager.imageFileExists(filename)) {
						ImageEntry entry = ImageEntry.register(formula, filename);
						entry.needsGenerating = false;
					}
				}
			}
			catch (Exception e) {
				oxdoc.warning("Error reading image cache. Don't worry.");
			}
		}

		public static void makeLatexFiles() throws IOException {

			for (Enumeration enum = _formulas.elements(); enum.hasMoreElements() ;) {
				ImageEntry e = (ImageEntry) enum.nextElement();
				if (e.needsGenerating) 
					makeLatexFile(e);
			}
			saveCache();
		}

		private static void makeLatexFile(ImageEntry e) throws IOException {
			oxdoc.message("Generating image for formula \"" + e.formula() + "\"...");

			File aFile = new File(FileManager.tempTexFile());
     		Writer output = new BufferedWriter( new FileWriter(aFile) );
			output.write("\\documentclass{article}\n");
			output.write("\\usepackage{amsmath}\n");

			for (int i = 0; i < Config.LatexPackages.size(); i++)
				output.write("\\usepackage{"  + (String) Config.LatexPackages.get(i) + "}\n");
			output.write("\\begin{document}\n");
			output.write("\\pagestyle{empty}\n");
			output.write("\\begin{align*}\n");
			output.write(e.formula() + "\n");
			output.write("\\end{align*}\n");
			output.write("\\end{document}\n");
			output.close();


			String latexParams  = "{0} -aux-directory={1} -output-directory={1} -interaction=batchmode {2}";
			Object[] args = {Config.LatexArg, FileManager.tempDir(), FileManager.tempFile("__oxdoc.tex")};
			
			run(Config.Latex, MessageFormat.format(latexParams, args));
			
			String dvipngParams = "{0} -T tight --gamma 1.5 -bg Transparent -o {1} {2}";
			for (int i = 0; i < 2; i++) {
				Object[] _args = {Config.DvipngArg, FileManager.imageFile(e.filename()), FileManager.tempFile("__oxdoc.dvi")};
				run(Config.Dvipng, MessageFormat.format(dvipngParams, _args));
			}
		}

		private static void run(String filename, String parameters) throws IOException {
			oxdoc.message("   " + filename + " "+ parameters);
			Runtime run = Runtime.getRuntime();
			try {
         		Process pp = run.exec(filename + " " + parameters);
         		pp.waitFor();
			} catch (InterruptedException E) {
				System.out.println("Execution interrupted");
				System.exit(1);
			}
		 }
	}
