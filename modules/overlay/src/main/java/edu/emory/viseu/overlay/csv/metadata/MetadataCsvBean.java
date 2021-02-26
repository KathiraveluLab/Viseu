package edu.emory.viseu.overlay.csv.metadata;

import com.opencsv.bean.CsvBindByName;
import edu.emory.viseu.overlay.csv.core.AbstractCsvBean;
import edu.emory.viseu.overlay.csv.scanner_util.ScannerSingleton;


public class MetadataCsvBean {

    @CsvBindByName(column = "DeviceSerialNumber")
    private String scanner;

    @CsvBindByName(column = "StudyInstanceUID")
    private String studyID;

    @CsvBindByName(column = "PatientID")
    private String patientID;

    @CsvBindByName(column = "StudyDescription")
    private String studyDescription;

    @CsvBindByName(column = "Modality")
    private String modality;

    public String getScanner() {
        return scanner;
    }

    public String getDetails() {
        return studyDescription;
    }

    public String getStudyID() {
        return studyID;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getStudyDescription() {
        return studyDescription;
    }

    public String getModality() {
        return modality;
    }
}