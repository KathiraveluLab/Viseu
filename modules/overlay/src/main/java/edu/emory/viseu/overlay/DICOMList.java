package edu.emory.viseu.overlay;

public class DICOMList {
    String workflowDescription;
    EntryList entryList;

    public String getWorkflowDescription() {
        return workflowDescription;
    }

    public EntryList getEntryList() {
        return entryList;
    }

    public DICOMList(String workflowDescription, EntryList entryList) {
        this.workflowDescription = workflowDescription;
        this.entryList = entryList;
    }
}