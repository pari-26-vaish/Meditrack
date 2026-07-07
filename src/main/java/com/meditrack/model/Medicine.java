package com.meditrack.model;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String dosage;
    private LocalTime reminderTime;
    private String frequency;   // e.g. "Once daily", "Twice daily"
    private String notes;
    private String icon;        // emoji: 💊 🧴 💉
    private Integer pillsRemaining;

    public Medicine() {}

    public Medicine(String name, String dosage, LocalTime reminderTime) {
        this.name = name;
        this.dosage = dosage;
        this.reminderTime = reminderTime;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public LocalTime getReminderTime() { return reminderTime; }
    public void setReminderTime(LocalTime reminderTime) { this.reminderTime = reminderTime; }

    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public Integer getPillsRemaining() { return pillsRemaining; }
    public void setPillsRemaining(Integer pillsRemaining) { this.pillsRemaining = pillsRemaining; }
}