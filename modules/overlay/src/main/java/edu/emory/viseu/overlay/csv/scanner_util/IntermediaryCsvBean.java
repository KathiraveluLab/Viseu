package edu.emory.viseu.overlay.csv.scanner_util;

import com.opencsv.bean.CsvBindByName;
import edu.emory.viseu.overlay.csv.core.AbstractCsvBean;


public class IntermediaryCsvBean extends AbstractCsvBean {

    @CsvBindByName(column = "DeviceSerialNumber")
    private String scanner;

    @CsvBindByName(column = "StudyInstanceUID")
    private String studyID;

    @CsvBindByName(column = "PatientID")
    private String patientID;

    @CsvBindByName(column = "DurationInMinutes")
    private double iDuration;

    @CsvBindByName(column = "Number of Series in the Study")
    private int seriesInStudy;

    @CsvBindByName(column = "Exam Start Time")
    private int iStart;

    @CsvBindByName(column = "Exam End Time")
    private int iEnd;

    @CsvBindByName(column = "StudyDescription")
    private String studyDescription;

    @CsvBindByName(column = "Modality")
    private String modality;

    @Override
    public String getScanner() {
        return scanner;
    }

    @Override
    public String getDetails() {
        return studyDescription;
    }

    @Override
    public void produceFinal(int index, String date) {
        ScannerSingleton ss = ScannerSingleton.getInstance(date);
        ss.addToScannerHashmap(this.scanner, this.patientID, String.valueOf(this.iStart), String.valueOf(this.iEnd),
                this.iDuration, this.studyDescription, this.modality, this.seriesInStudy);
    }
}