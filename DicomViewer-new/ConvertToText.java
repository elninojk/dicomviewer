package view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.io.DicomInputStream;

public class ConvertToText {

//	static 
//	static File f = new File("C:\\Users\\1026807\\Downloads\\FW__\\93679240.dcm");

	public static File convertToText(File f) throws Exception {
		DicomInputStream din = null;
		Writer w = null;
		try {
					
			din = new DicomInputStream(f);
			Attributes tagDataSet = din.readDataset(-1, -1);
			File Text = new File("TextFile.txt");
			FileOutputStream is = new FileOutputStream(Text);
			OutputStreamWriter osw = new OutputStreamWriter(is);
			w = new BufferedWriter(osw);
			w.write((tagDataSet.getString(Tag.PatientName)).toString() + "\n");
			w.write(tagDataSet.toString());
			
			return Text;
		} catch (NullPointerException e) {
			throw e;
		} catch (Exception ex) {
			throw ex;
		}
		finally {
			din.close();
			w.close();
		}

	}
}
