package com.GraminSchool.utility;

import com.GraminSchool.entity.Role;
import com.GraminSchool.entity.User;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.List;

public class ExcelCsvHelperUserRegistration {


    //parse excel file(.xlsx)
    public static List<User> parseExcel(InputStream inputStream) throws IOException {
        List<User> userlist=new ArrayList<>();
        Workbook workbook=new XSSFWorkbook(inputStream);
        Sheet sheet=workbook.getSheetAt(0);

        for(int i=1;i<=sheet.getLastRowNum();i++){//skip header
            Row row=sheet.getRow(i);
            if (row==null) continue;

            User user=new User();
            user.setName(row.getCell(0).getStringCellValue());
            user.setEmail(row.getCell(1).getStringCellValue());
            user.setPhone(row.getCell(2).getStringCellValue());
            user.setPassword(row.getCell(3).getStringCellValue());
            String role=row.getCell(4).getStringCellValue();
            if (role.equalsIgnoreCase("ADMIN")){
                throw new IllegalArgumentException("Admin user registration is restricted");
            }else{
                user.setRole(Role.valueOf(row.getCell(4).getStringCellValue()));
            }
            user.setAddress(row.getCell(5).getStringCellValue());
            user.setStatus(Boolean.parseBoolean(row.getCell(6).getStringCellValue()));
            userlist.add(user);
        }
        workbook.close();
        return userlist;
    }

    //parse csv file(.csv)
    public static List<User> parseCSV(InputStream inputStream) throws IOException {
        List<User> userList=new ArrayList<>();
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
        String line;
        boolean fistLine=true;

        while ((line=br.readLine())!=null){
            if (fistLine){
                fistLine=false;
                continue;
            }
            String[] data=line.split(",");

            User user=new User();
            user.setName(data[0]);
            user.setEmail(data[1]);
            user.setPhone(data[2]);
            user.setPassword(data[3]);
            String role=data[4];
            if (role.equalsIgnoreCase("ADMIN")){
                throw new IllegalArgumentException("Admin user registration is restricted");
            }else{
                user.setRole(Role.valueOf(data[4]));
            }
            user.setAddress(data[5]);
            user.setStatus(Boolean.parseBoolean(data[6]));
            userList.add(user);
        }
        return userList;
    }




}
