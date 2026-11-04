package com.devut.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String category;
    private String date;
    private String time;
    private String place;
    @Column(length = 2000)
    private String description;
    private String accent;
    private String imageUrl;

    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }
    public String getTitle() { 
        return title; 
    }
    public void setTitle(String title) { 
        this.title = title; 
    }
    public String getCategory() { 
        return category; 
    }
    public void setCategory(String category) { 
        this.category = category; 
    }
    public String getDate() { 
        return date; 
    }

    public void setDate(String date) { 
        this.date = date; 
    }
    public String getTime() { 
        return time; 
    }
    public void setTime(String time) { 
        this.time = time; 
    }
    public String getPlace() { 
        return place; 
    }
    public void setPlace(String place) { 
        this.place = place; 
    }
    public String getDescription() { 
        return description; 
    }

    public void setDescription(String description) { 
        this.description = description; 
    }

    public String getAccent() { 
        return accent; 
    }

    public void setAccent(String accent) { 
        this.accent = accent; 
    }

    public String getImageUrl() { 
        return imageUrl; 
    }
    
    public void setImageUrl(String imageUrl) { 
        this.imageUrl = imageUrl; 
    }
}
