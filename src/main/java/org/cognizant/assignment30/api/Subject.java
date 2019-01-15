package org.cognizant.assignment28.api;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Subject {

    @EmbeddedId
    private SubBookIdentity subBookIdentity;

    @Column(name = "sub_title")
    private String subTitle;

    @Column(name = "duration_in_hours")
    private int durationInHours;

    @Transient
    private Set<Book> books = new HashSet<>(0);

    public Subject() {
    }

    public Subject(SubBookIdentity subBookIdentity, String subTitle, int durationInHours) {
        this.subBookIdentity = subBookIdentity;
        this.subTitle = subTitle;
        this.durationInHours = durationInHours;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "subjectId=" + subBookIdentity.getSubjectId() +
                ", subTitle=" + subTitle +
                ", durationInHours=" + durationInHours +
                (books.isEmpty() ? ", bookId=" + subBookIdentity.getBookId() : ", books=" + books) +
                '}';
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public int getDurationInHours() {
        return durationInHours;
    }

    public void setDurationInHours(int durationInHours) {
        this.durationInHours = durationInHours;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public SubBookIdentity getSubBookIdentity() {
        return subBookIdentity;
    }

    public void setSubBookIdentity(SubBookIdentity subBookIdentity) {
        this.subBookIdentity = subBookIdentity;
    }

}
