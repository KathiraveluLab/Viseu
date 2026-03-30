package edu.emory.viseu.overlay.csv.scanner_util;

import edu.emory.viseu.overlay.csv.core.AbstractCsvBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScannerUtil {
    private static Logger logger = LogManager.getLogger(ScannerUtil.class.getName());

    private static List<Scanner> scannerList = new ArrayList<>();

    public static void updateScannerList(Scanner scanner) {
        scannerList.add(scanner);
    }

    public static void getFinalCsvString(int index,
                                         List<AbstractCsvBean> beans, String filename, Map<String, String> scannersSubsetMap) {
        String date = "0";
        String wholeStr = "0";
        String parts[];
        for (AbstractCsvBean bean: beans) {
            wholeStr = filename.split("\\.")[0];
            parts = wholeStr.split("/");
            date = parts[parts.length-1];
            bean.produceFinal(index, date);
        }
        ScannerSingleton ss = ScannerSingleton.getInstance(date);
        ss.produceFinalCSV(index, date, scannersSubsetMap);
    }

    /**
     * Calculates the difference in minutes between two time strings (HHMMSS).
     * Correctly handles midnight rollover (e.g., 2359 to 0001).
     */
    public static double getDiffInMins(String start, String end) {
        try {
            // Standardizing strings to 6 digits (HHMMSS)
            String s = String.format("%06d", (int)Double.parseDouble(start));
            String e = String.format("%06d", (int)Double.parseDouble(end));

            double startH = Double.parseDouble(s.substring(0, 2));
            double startM = Double.parseDouble(s.substring(2, 4));
            double startS = Double.parseDouble(s.substring(4, 6));

            double endH = Double.parseDouble(e.substring(0, 2));
            double endM = Double.parseDouble(e.substring(2, 4));
            double endS = Double.parseDouble(e.substring(4, 6));

            double startTotalMins = (startH * 60.0) + startM + (startS / 60.0);
            double endTotalMins = (endH * 60.0) + endM + (endS / 60.0);

            double diff = endTotalMins - startTotalMins;

            // Handle midnight rollover: If end < start, assume it's the next day
            if (diff < 0) {
                diff += 1440; // Add 24 hours in minutes
            }

            return diff;
        } catch (Exception e) {
            logger.error("Calculating duration failed for start: " + start + ", end: " + end);
            return 99999;
        }
    }

}
