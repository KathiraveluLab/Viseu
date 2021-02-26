package edu.emory.viseu.overlay.gll;

public class DICOMEntry {
    private String AccessionNumber;
    private String AcquisitionDate;
    private String PatientID;

    public DICOMEntry(String patientID, String accessionNumber, String acquisitionDate) {
        AccessionNumber = accessionNumber;
        AcquisitionDate = acquisitionDate;
        PatientID = patientID;
    }

    public String getAccessionNumber() {
        return AccessionNumber;
    }

    public String getAcquisitionDate() {
        return AcquisitionDate;
    }

    public String getPatientID() {
        return PatientID;
    }

    public String toString()
    {
        return "PatientID : " + PatientID + ", AccessionNumber: " + AccessionNumber + ", AcquisitionDate : " + AcquisitionDate;
    }
}