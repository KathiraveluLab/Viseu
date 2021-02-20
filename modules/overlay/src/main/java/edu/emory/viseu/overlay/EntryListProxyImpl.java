package edu.emory.viseu.overlay;

import java.util.List;

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
