package config;


import org.aeonbits.owner.Config;

@Config.Sources({"classpath:config/userdata.properties"})
public interface TestDataConfig extends Config {


    @Key("isbn")
    String isbn();  // Значение будет считываться из файла userdata.properties
}