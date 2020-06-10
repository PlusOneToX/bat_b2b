package com.bat.flexible.api.util.excel;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class ExcelUtil {

    /**
     * suffix of excel 2003
     */
    public static final String OFFICE_EXCEL_V2003_SUFFIX = "xls";
    /**
     * suffix of excel 2007
     */
    public static final String OFFICE_EXCEL_V2007_SUFFIX = "xlsx";
    /**
     * suffix of excel 2010
     */
    public static final String OFFICE_EXCEL_V2010_SUFFIX = "xlsx";

    public static final String EMPTY = "";
    public static final String DOT = ".";
    public static final String LIB_PATH = "lib";
    public static final String STUDENT_INFO_XLS_PATH = LIB_PATH + "/student_info" + DOT + OFFICE_EXCEL_V2003_SUFFIX;
    public static final String STUDENT_INFO_XLSX_PATH = LIB_PATH + "/student_info" + DOT + OFFICE_EXCEL_V2007_SUFFIX;
    public static final String NOT_EXCEL_FILE = " is Not a Excel file!";
    public static final String PROCESSING = "Processing...";

    public static void main(String[] args) throws IOException {
        try {
            List<Student> list = readExcel("C:\\Users\\lava\\Desktop\\excel.xls");
            for (Student student : list) {
                System.out.println(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Check which version of The Excel file is. Throw exception if Excel file path is illegal.
     *
     * @param path the Excel file
     * @return a list that contains Students from Excel.
     * @throws IOException
     */
    public static List<Student> readExcel(String path) throws Exception {
        if (path ==null || path =="") {
            throw new IllegalArgumentException(path + " excel file path is either null or empty");
        } else {
            File file = new File(path);
            InputStream input = new FileInputStream(file);
            return readXlsx(input);
        }
    }

    /**
     * Read the Excel 2017 or 2010
     *
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     * @throws EncryptedDocumentException
     */
    public static List<Student> readXlsx(InputStream is) throws Exception {
        // System.out.println(PROCESSING + path);
        // InputStream is = new FileInputStream(path);
        Workbook workbook = WorkbookFactory.create(is);
        // Sheet hssfSheet = workbook.getSheetAt(0);
        Student student = null;
        List<Student> list = new ArrayList<Student>();
        // Read the Sheet
        for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
            Sheet hssfSheet = workbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // Read the Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                Row xssfRow = hssfSheet.getRow(rowNum);
                if (xssfRow != null) {
                    student = new Student();
                    Cell no = xssfRow.getCell(0);
                    Cell name = xssfRow.getCell(1);
                    Cell age = xssfRow.getCell(2);
                    Cell score = xssfRow.getCell(3);
                    student.setNo(getValue(no));
                    student.setName(getValue(name));
                    student.setAge(getValue(age));
                    student.setScore(Float.valueOf(getValue(score)));
                    list.add(student);
                }
            }
        }
        return list;
    }


    @SuppressWarnings("static-access")
    public static String getValue(Cell xssfRow) {
        if (xssfRow == null) {
            return "";
        }
        if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
            return String.valueOf(xssfRow.getBooleanCellValue());
        } else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
            if(DateUtil.isCellDateFormatted(xssfRow)){
                return String.valueOf(xssfRow.getDateCellValue().getTime());
            }else{
                DecimalFormat decimalFormat = new DecimalFormat("0");
                String value = decimalFormat.format(xssfRow.getNumericCellValue());
                return value;
            }
        } else {
            return String.valueOf(xssfRow.getStringCellValue()).trim();
        }
    }

    @SuppressWarnings("static-access")
    public static String getDoubleValue(Cell xssfRow) {
        if (xssfRow == null) {
            return "";
        }
        if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            String value = decimalFormat.format(xssfRow.getNumericCellValue());
            return value;
        } else {
            return String.valueOf(xssfRow.getStringCellValue()).trim();
        }
    }


    public static String getSuffiex(String path) {
        if (path ==null || path=="") {
            return EMPTY;
        }
        int index = path.lastIndexOf(DOT);
        if (index == -1) {
            return EMPTY;
        }
        return path.substring(index + 1, path.length());
    }

    static class Student {
        /**
         * id
         */
        private Integer id;
        /**
         * 学号
         */
        private String no;
        /**
         * 姓名
         */
        private String name;
        /**
         * 学院
         */
        private String age;
        /**
         * 成绩
         */
        private float score;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public float getScore() {
            return score;
        }

        public void setScore(float score) {
            this.score = score;
        }

        public String toString() {
            return "no=" + no + ",name=" + name + ",age=" + age + ",score=" + score;
        }

    }


}
