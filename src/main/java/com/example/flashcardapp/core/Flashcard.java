package com.example.flashcardapp.core;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Represents a Flashcard entity in the Flashcard App.
 */
@Entity
@Table(name = "flashcards")
public class Flashcard {

    // 1. Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 2. Flashcard Question
    @Column(name = "question", nullable = false, length = 255)
    @NotBlank(message = "Question cannot be blank")
    @Size(max = 255, message = "Question cannot exceed 255 characters")
    private String question;

    // 3. Flashcard Answer
    @Column(name = "answer", nullable = false, length = 255)
    @NotBlank(message = "Answer cannot be blank")
    @Size(max = 255, message = "Answer cannot exceed 255 characters")
    private String answer;

    // 4. Flashcard Category (Optional)
    @Column(name = "category", length = 100)
    @Size(max = 100, message = "Category cannot exceed 100 characters")
    private String category;

    // 5. Timestamp for Creation
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 6. Timestamp for Last Update
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // 7. Constructors

    /**
     * Default constructor required by Hibernate.
     */
    public Flashcard() {
        // Hibernate requires a default constructor
    }

    /**
     * Parameterized constructor for creating a new Flashcard.
     *
     * @param question  The question text.
     * @param answer    The answer text.
     * @param category  The category of the flashcard.
     */
    public Flashcard(String question, String answer, String category) {
        this.question = question;
        this.answer = answer;
        this.category = category;
    }

    // 8. Getters and Setters

    public Long getId() {
        return id;
    }

    // No setter for 'id' as it's auto-generated

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // No setter for 'createdAt' as it's managed automatically

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // No direct setter for 'updatedAt'; it's updated automatically

    // 9. Lifecycle Callback Methods

    /**
     * Sets the creation and update timestamps before persisting.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Updates the update timestamp before updating.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // 10. Overridden Methods

    @Override
    public String toString() {
        return "Flashcard{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", category='" + category + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    // Optionally, override equals() and hashCode() for entity comparison
}
