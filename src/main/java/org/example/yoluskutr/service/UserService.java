package org.example.yoluskutr.service;

import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.yoluskutr.entity.User;
import org.example.yoluskutr.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    public void saveUser(User user){
        userRepo.save(user);
    }
    public List<User> getUsers(){
        return userRepo.findAll();
    }
    public void controlUser(String agent){
        User user = userRepo.findFirstById(1).orElseThrow();
        if(!user.getAgent().equals(agent)) throw new RuntimeException("Giremezsiniz");
    }
    public byte[] exportToExcel(List<User> users) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Users");

            // Başlık satırını oluşturun
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("IP Address");
            headerRow.createCell(2).setCellValue("User Agent");

            // Verileri satırlara ekleyin
            int rowNum = 1;
            for (User user : users) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(user.getId());
                row.createCell(1).setCellValue(user.getIpAddress());
                row.createCell(2).setCellValue(user.getAgent());
            }

            // Excel dosyasını kaydedin
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
