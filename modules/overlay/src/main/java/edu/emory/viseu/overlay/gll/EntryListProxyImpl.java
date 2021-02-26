package edu.emory.viseu.overlay.gll;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntryListProxyImpl implements EntryList {

    private EntryList entryList;
    public List<DICOMEntry> getEntryList()
    {
        if (entryList == null) {
            System.out.println("Printing the DICOM Metadata of the files");
            entryList = new EntryListImpl();
        }
        return entryList.getEntryList();
    }
}
