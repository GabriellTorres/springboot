package com.aprendendo.encurtadorurl.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "urls")
public class Url {
    
    @Id
    private String id;

    private String originalUrl;

    @Indexed(expireAfterSeconds = 0)
    private LocalDateTime expiredAt;

    public Url() {}

    public Url(String id, String originalUrl, LocalDateTime expiredAt) {
        this.id = id;
        this.originalUrl = originalUrl;
        this.expiredAt = expiredAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public LocalDateTime getExpireddAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    

    
}
