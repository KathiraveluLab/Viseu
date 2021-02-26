package edu.emory.viseu.overlay.gll;

import java.util.List;

import edu.emory.viseu.overlay.csv.scanner_util.ScannerSingleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LazyLoader {
    private static Logger logger = LogManager.getLogger(ScannerSingleton.class.getName());

    public static void main(String[] args)
    {
        EntryList entryList = new EntryListProxyImpl();
        DICOMList dicomList = new DICOMList
                ("DICOOM Workflow", entryList);

        logger.info("DICOM Workflow: " + dicomList.getWorkflowDescription());

        logger.info("Requesting for the whole list of metadata attributes");

        entryList = dicomList.getEntryList();
        List<DICOMEntry> entList = entryList.getEntryList();
        for (DICOMEntry ent : entList) {
            logger.info(ent);
        }
    }
}
