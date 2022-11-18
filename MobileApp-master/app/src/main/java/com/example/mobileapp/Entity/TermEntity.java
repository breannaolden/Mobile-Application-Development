package com.example.mobileapp.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "terms_table")
public class TermEntity {

    @PrimaryKey(autoGenerate = true)
    private int termId;
    private String termTitle;
    private String termStart;
    private String termEnd;

    public TermEntity(int termId, String termTitle, String termStart, String termEnd) {
        this.termId = termId;
        this.termTitle = termTitle;
        this.termStart = termStart;
        this.termEnd = termEnd;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public String getTermTitle() {
        return termTitle;
    }

    public void setTermTitle(String termTitle) {
        this.termTitle = termTitle;
    }

    public String getTermStart() {
        return termStart;
    }

    public void setTermStart(String termStart) {
        this.termStart = termStart;
    }

    public String getTermEnd() {
        return termEnd;
    }

    public void setTermEnd(String termEnd) {
        this.termEnd = termEnd;
    }

    @Override
    public String toString() {
        return "TermEntity{" +
                "termId=" + termId +
                ", termTitle='" + termTitle + '\'' +
                ", termStart='" + termStart + '\'' +
                ", termEnd='" + termEnd + '\'' +
                '}';
    }
}
