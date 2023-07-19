package Utilities;

import Schema.Customer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by chao zhang on 2023.7.19.
 */

public class Writer {
    public void write2csv(List<Record> lst, String FileName, String DataPath){
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter=null;
        String NEW_LINE_SEPARATOR = "\n";
        try {
            File directory=new File(DataPath);
            if(!directory.exists())
                directory.mkdirs();
            fileWriter = new FileWriter(DataPath+FileName,false);
            bufferedWriter = new BufferedWriter(fileWriter);
//            //Write the CSV file header
//            bufferedWriter.write(VendorFILE_HEADER.toString());
//            //Add a new line separator after the header
//            bufferedWriter.write(NEW_LINE_SEPARATOR);
            for (int i=0;i<lst.size();i++) {
                bufferedWriter.write(lst.get(i).toString());
                bufferedWriter.write(NEW_LINE_SEPARATOR);
            }
        } catch (Exception e) {
            System.out.println("Error in Writer!");
            e.printStackTrace();
        } finally {
            try {
                bufferedWriter.flush();
                bufferedWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !");
                e.printStackTrace();
            }
        }
    }
}
