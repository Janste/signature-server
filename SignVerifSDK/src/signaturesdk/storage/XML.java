package signaturesdk.storage;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import signaturesdk.beans.AcquisitionSignWord;

/**
 * Format:
 * <signature>
 * 	<segment points="2">
 * 		<point number="0"></point>
 * 		<point number="1"></point>
 * 	</segment>
 * 	<segment points="2">
 * 		<point number="0"></point>
 * 		<point number="1"></point>
 * 	</segment>
 * </signature>
 */
public class XML {
	
	private static boolean noXMLReadedExpt;


	/**
	 * Export a raw signature to XML
	 * 
	 * @param FileName
	 *            output filename
	 * @param asw
	 *            the AcquisitionSignWord to export
	 * @throws IOException
	 *             if there's some writing error
	 */
	public static void exportRawSignature(LinkedList<AcquisitionSignWord> listAsw, String FileName)
			throws IOException {

		// prepare di output buffer
		FileWriter fileWriter = new FileWriter(FileName, false);
		BufferedWriter buffWriter = new BufferedWriter(fileWriter);

		

		// write the xml
		buffWriter.append("<?xml version=\"1.0\"?>\n");
		buffWriter.append("<signature>\n");
		for(AcquisitionSignWord asw : listAsw) {
			
			// get points info
			LinkedList<Double> Lx = asw.getX();
			LinkedList<Double> Ly = asw.getY();
			LinkedList<Double> Lp = asw.getPressure();
			LinkedList<Long> Lt = asw.getTime();
			
			buffWriter.append("<segment points=\""+Lx.size()+"\">\n");
			for (int i = 0; i < asw.getPointsNum(); i++) {
				double x = Lx.get(i);
				double y = Ly.get(i);
				double p = Lp.get(i);
				long t = Lt.get(i);
	
				String point = String
						.format(Locale.US,
								"<point number=\"%d\"><x>%.3f</x><y>%.3f</y><p>%.3f</p><t>%d</t></point>\n",
								 i, x, y, p, t);
				buffWriter.append(point);
			}
			buffWriter.append("</segment>\n");
		}
		buffWriter.append("</signature>");

		buffWriter.close();// from javadoc: "Closes the stream, flushing it first."
		fileWriter.close();

		return;
	}

	/**
	 * Read an XML contains X,Y,Pressure and time for each point
	 * @param fileName
	 * @return The data readed as LinkedList<AcquisitionSignWord> or null on some error
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	static synchronized public LinkedList<AcquisitionSignWord> importRawSignature(String fileName) throws ParserConfigurationException, SAXException, IOException {

		//get 
		LinkedList<AcquisitionSignWord> listAsw = new LinkedList<AcquisitionSignWord>();

		
		// Create a JAXP SAXParserFactory and configure it
        SAXParserFactory spf = SAXParserFactory.newInstance();

        // Set namespaceAware to true to get a parser that corresponds to
        // the default SAX2 namespace feature setting.  This is necessary
        // because the default value from JAXP 1.0 was defined to be false.
        spf.setNamespaceAware(false);

        // Create a JAXP SAXParser
        SAXParser saxParser = spf.newSAXParser();
        
        XMLReader xmlReader = saxParser.getXMLReader();

        // Set the ContentHandler of the XMLReader
        xmlReader.setContentHandler(new XML.XMLSaxReader(listAsw));

        // Set an ErrorHandler before parsing
        noXMLReadedExpt = true;
        xmlReader.setErrorHandler(new MyErrorHandler());

        // Tell the XMLReader to parse the XML document
        xmlReader.parse(fileName);
        
        //no exception: all is gone ok!
        if(noXMLReadedExpt)
        	return listAsw;
        else
        	return null;
	}
	
	/**
	 * SAX reader class
	 */
	private static class XMLSaxReader extends DefaultHandler{
		
		private LinkedList<Double> tLx, tLy, tLp;
		private LinkedList<Long> tLt;
		private LinkedList<AcquisitionSignWord> listAsw;
		private AcquisitionSignWord asw;

		private NumberFormat nf = NumberFormat.getInstance(Locale.US);
		
		//Set the AcquisitionSignWord list where insert the readed data
		public XMLSaxReader(  LinkedList<AcquisitionSignWord> listAsw) {
			this.listAsw = listAsw;
		}
	
		private String activeTag;
	
		public void startElement(String namespaceURI, String localName,
				String qName, Attributes atts) throws SAXException {
	
			// get the active tag
			this.activeTag = new String(qName);
			
			//start a new Signature Word
			if (qName.compareTo("segment") == 0) {
				this.asw = new AcquisitionSignWord();
				this.tLx = this.asw.getX();
				this.tLy = this.asw.getY();
				this.tLp = this.asw.getPressure();
				this.tLt = this.asw.getTime();
			}
			
		}
		
		public void endElement(String uri, String localName, String qName)
			    throws SAXException {
			this.activeTag = ""; //say no tag is active
			
			//Signature Word ended
			if (qName.compareTo("segment") == 0) {
				this.listAsw.add(this.asw);
			}
		}
	
		public void characters(char ch[], int start, int length)
				throws SAXException {
			
			
			String tCh =  new String(ch, start, length);
			try {
				if (this.activeTag.compareTo("x") == 0) {
					tLx.add(nf.parse(tCh).doubleValue());
				} else if (this.activeTag.compareTo("y") == 0) {
					tLy.add(nf.parse(tCh).doubleValue());
				} else if (this.activeTag.compareTo("p") == 0) {
					tLp.add(nf.parse(tCh).doubleValue());
				} else if (this.activeTag.compareTo("t") == 0) {
					tLt.add(nf.parse(tCh).longValue());
				}
			} catch (ParseException e) {
					e.printStackTrace();
			}
	
		}
	}
	
	 // Error handler to report errors and warnings
    private static class MyErrorHandler implements ErrorHandler {
        /** Error handler output goes here */

        public MyErrorHandler() {

        }


        // The following methods are standard SAX ErrorHandler methods.
        // See SAX documentation for more info.
        public void warning(SAXParseException spe) throws SAXException {
        	noXMLReadedExpt = false;
        }
        
        public void error(SAXParseException spe) throws SAXException {
        	noXMLReadedExpt = false;
        }

        public void fatalError(SAXParseException spe) throws SAXException {
        	noXMLReadedExpt = false;
        }
    }

}
