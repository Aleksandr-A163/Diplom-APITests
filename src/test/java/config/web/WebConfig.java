package config.web;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:config/${env}.properties",
        "file:~/${env}.properties",
        "file:./${env}.properties"
})
public interface WebConfig extends Config {

    @Key("browser")
    @DefaultValue("chrome")
    Browser browser();

    @Key("browserSize")
    @DefaultValue("1920x1080")
    String browserSize();

    @Key("browserVersion")
    @DefaultValue("120.0")
    String browserVersion();

    @Key("baseUrl")
    @DefaultValue("https://demoqa.com")
    String baseUrl();

    @Key("loadStrategy")
    @DefaultValue("normal")
    String loadStrategy();

    @Key("isRemote")
    @DefaultValue("false")
    Boolean isRemote();

    @Key("remoteUrl")
    @DefaultValue("http://localhost:4444")
    String remoteUrl();

    @Key("selenoidUser")
    @DefaultValue("null")
    String selenoidUser();

    @Key("selenoidPass")
    @DefaultValue("null")
    String selenoidPass();
}