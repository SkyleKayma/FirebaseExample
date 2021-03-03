package fr.skyle.firebaseexample.model;

public class Article {

    private String key;
    private String title;
    private String content;
    private Long date;

    // --- Constructor

    public Article() {
        this.key = "";
        this.title = "";
        this.content = "";
        this.date = 0L;
    }

    public Article(String title, String content, Long date) {
        this.key = "";
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public Article(String key, String title, String content, Long date) {
        this.key = key;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    // --- Getters and Setters

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
