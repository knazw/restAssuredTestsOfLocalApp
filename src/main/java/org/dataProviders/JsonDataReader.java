package org.dataProviders;

import com.google.gson.Gson;
import org.cypress.example.model.User;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JsonDataReader {
    private final String userFilePath = PropertiesStorage.getInstance().getJsonsPath() + "Users.json";
    private List<User> userList;

    public JsonDataReader(){
        userList = getCustomerData();
    }

    private List<User> getCustomerData() {
        Gson gson = new Gson();
        BufferedReader bufferReader = null;
        try {

            bufferReader = new BufferedReader(new FileReader(userFilePath));
            User[] users = gson.fromJson(bufferReader, User[].class);
            return Arrays.asList(users);
        }catch(FileNotFoundException e) {
            throw new RuntimeException("Json file not found at path : " + userFilePath);
        }finally {
            try { if(bufferReader != null) bufferReader.close();}
            catch (IOException ignore) {}
        }
    }

    public final User getUserByUsername(String username){
        return userList.stream().filter(x -> x.getUsername().equalsIgnoreCase(username)).findAny().get();
    }

    public static final String getJsonFile(String fileName) {
        String path = PropertiesStorage.getInstance().getJsonsPath();


        return readJsonFile(path + fileName);
    }

    private static String readJsonFile(String path) {
        BufferedReader bufferReader = null;
        try {

            bufferReader = new BufferedReader(new FileReader(path));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = bufferReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }catch(IOException e) {
            throw new RuntimeException("Json file not found at path : " + path);
        }finally {
            try { if(bufferReader != null) bufferReader.close();}
            catch (IOException ignore) {}
        }
    }

}
