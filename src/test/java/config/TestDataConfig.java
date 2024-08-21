package config;

import helpers.FakerData;
import org.aeonbits.owner.Config;

@Config.Sources({"classpath:config/userdata.properties"})
public interface TestDataConfig extends Config {

    @Key("userLogin")
    default String userLogin() {
        String username = FakerData.generateUsername();
        System.out.println("Generated Username: " + username); // Логирование
        return username;
    }

    @Key("userPassword")
    default String userPassword() {
        String password = FakerData.generatePassword(9, 16);
        System.out.println("Generated Password: " + password); // Логирование
        return password;
    }

    @Key("isbn")
    String isbn();  // Значение будет считываться из файла userdata.properties
}