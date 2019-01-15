package org.cognizant.assignment28.api;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SubBookIdentity implements Serializable {
    @NotNull
    private long subjectId;

    @NotNull
    private long bookId;

    public SubBookIdentity() {
    }

    public SubBookIdentity(@NotNull long subjectId, @NotNull long bookId) {
        this.subjectId = subjectId;
        this.bookId = bookId;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubBookIdentity that = (SubBookIdentity) o;
        return subjectId == that.subjectId &&
                bookId == that.bookId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectId, bookId);
    }

}
