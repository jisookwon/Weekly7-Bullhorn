package com.example.demo;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Size(min=3)
    private String title;

    @NotNull
    @Size(min=3)
    private String content;

    @NotNull
    @Size(min=3)
    private String postedDate;

    @NotNull
    @Size(min=3)
    private String postedBy;

    private String headshot;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Message() {
    }

    public Message(@NotNull @Size(min = 3) String title, @NotNull @Size(min = 3) String content, @NotNull @Size(min = 3) String postedDate, @NotNull @Size(min = 3) String postedBy) {
        this.title = title;
        this.content = content;
        this.postedDate = postedDate;
        this.postedBy = postedBy;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public String getHeadshot() {
        return headshot;
    }

    public void setHeadshot(String headshot) {
        this.headshot = headshot;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
