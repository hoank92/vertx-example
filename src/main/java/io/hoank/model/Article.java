package io.hoank.model;

import lombok.Data;

/**
 * Created by hoank92 on May, 2019
 */

@Data
public class Article {
    private String author;
    private String content;

    public Article(String author, String content) {
        this.author = author;
        this.content = content;
    }
}
