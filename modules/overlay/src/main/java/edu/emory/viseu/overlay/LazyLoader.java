package edu.emory.viseu.overlay;

import java.util.List;

public class LazyLoader {
    public static void main(String[] args)
    {
        EntryList entryList = new EntryListProxyImpl();
        DICOMList dicomList = new DICOMList
                ("DICOOM Workflow", entryList);

        System.out.println("DICOM Workflow: " + dicomList.getWorkflowDescription());

        System.out.println("Requesting for the whole list of metadata attributes");

        entryList = dicomList.getEntryList();
        List<DICOMEntry> entList = entryList.getEntryList();
        for (DICOMEntry ent : entList) {
            System.out.println(ent);
        }
    }
}
