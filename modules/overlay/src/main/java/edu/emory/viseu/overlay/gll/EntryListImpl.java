package edu.emory.viseu.overlay.gll;

import java.util.ArrayList;
import java.util.List;

public class EntryListImpl implements EntryList {
    public List<DICOMEntry> getEntryList()
    {
        return getEntList();
    }
    private static List<DICOMEntry> getEntList()
    {
        List<DICOMEntry> entList = new ArrayList<DICOMEntry>(5);

        entList.add(new DICOMEntry("12345", "QW2020SWWQE", "20210219"));
        entList.add(new DICOMEntry("31243", "WF2020SWWQE", "20210220"));
        entList.add(new DICOMEntry("21334", "RG2020SWWQE", "20210221"));
        entList.add(new DICOMEntry("32332", "DW2020SWWQE", "20210222"));
        entList.add(new DICOMEntry("32342", "DW2020SWWQE", "20210222"));

        return entList;
    }
}