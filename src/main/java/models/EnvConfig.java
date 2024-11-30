package models;

import lombok.Data;

@Data
public class EnvConfig {
    private String env;
    private Config config;
}
