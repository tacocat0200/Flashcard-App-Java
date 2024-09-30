package com.example.flashcardapp;

import com.example.flashcardapp.core.Flashcard;
import com.example.flashcardapp.db.FlashcardDAO;
import com.example.flashcardapp.resources.FlashcardResource;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
public class FlashcardResourceTest {

    // Mocked FlashcardDAO to simulate database interactions
    private static final FlashcardDAO mockFlashcardDAO = mock(FlashcardDAO.class);

    // ResourceExtension to set up the testing environment for FlashcardResource
    private static final ResourceExtension RESOURCES = ResourceExtension.builder()
            .addResource(new FlashcardResource(mockFlashcardDAO))
            .build();

    // Sample flashcard instances for testing
    private Flashcard flashcard1;
    private Flashcard flashcard2;

    @BeforeEach
    void setup() {
        // Initialize sample flashcards before each test
        flashcard1 = new Flashcard();
        flashcard1.setId(1L);
        flashcard1.setQuestion("What is Java?");
        flashcard1.setAnswer("A high-level, class-based, object-oriented programming language.");
        flashcard1.setCategory("Programming");

        flashcard2 = new Flashcard();
        flashcard2.setId(2L);
        flashcard2.setQuestion("What is Dropwizard?");
        flashcard2.setAnswer("A Java framework for developing ops-friendly, high-performance RESTful web services.");
        flashcard2.setCategory("Framework");
    }

    @Test
    void testGetAllFlashcards() {
        // Arrange: Mock the DAO to return a list of flashcards
        List<Flashcard> flashcards = Arrays.asList(flashcard1, flashcard2);
        when(mockFlashcardDAO.findAll()).thenReturn(flashcards);

        // Act: Make a GET request to /flashcards
        List<?> response = RESOURCES.target("/flashcards")
                .request(MediaType.APPLICATION_JSON)
                .get(List.class);

        // Assert: Verify the response and interactions
        assertThat(response).hasSize(2);
        verify(mockFlashcardDAO).findAll();
    }

    @Test
    void testGetFlashcardById_Found() {
        // Arrange: Mock the DAO to return flashcard1 when searching by ID
        when(mockFlashcardDAO.findById(1L)).thenReturn(Optional.of(flashcard1));

        // Act: Make a GET request to /flashcards/1
        Response response = RESOURCES.target("/flashcards/1")
                .request(MediaType.APPLICATION_JSON)
                .get();

        // Assert: Verify the response status and content
        assertThat(response.getStatus()).isEqualTo(200);
        Flashcard returned = response.readEntity(Flashcard.class);
        assertThat(returned.getQuestion()).isEqualTo("What is Java?");
        verify(mockFlashcardDAO).findById(1L);
    }

    @Test
    void testGetFlashcardById_NotFound() {
        // Arrange: Mock the DAO to return empty when searching for a non-existent ID
        when(mockFlashcardDAO.findById(3L)).thenReturn(Optional.empty());

        // Act: Make a GET request to /flashcards/3
        Response response = RESOURCES.target("/flashcards/3")
                .request(MediaType.APPLICATION_JSON)
                .get();

        // Assert: Verify the response status is 404 Not Found
        assertThat(response.getStatus()).isEqualTo(404);
        verify(mockFlashcardDAO).findById(3L);
    }

    @Test
    void testCreateFlashcard() {
        // Arrange: Mock the DAO to return the flashcard with an assigned ID
        Flashcard newFlashcard = new Flashcard();
        newFlashcard.setQuestion("What is JUnit?");
        newFlashcard.setAnswer("A unit testing framework for Java.");
        newFlashcard.setCategory("Testing");

        Flashcard createdFlashcard = new Flashcard();
        createdFlashcard.setId(3L);
        createdFlashcard.setQuestion(newFlashcard.getQuestion());
        createdFlashcard.setAnswer(newFlashcard.getAnswer());
        createdFlashcard.setCategory(newFlashcard.getCategory());

        when(mockFlashcardDAO.create(newFlashcard)).thenReturn(createdFlashcard);

        // Act: Make a POST request to /flashcards with the new flashcard data
        Response response = RESOURCES.target("/flashcards")
                .request(MediaType.APPLICATION_JSON)
                .post(javax.ws.rs.client.Entity.entity(newFlashcard, MediaType.APPLICATION_JSON));

        // Assert: Verify the response status and content
        assertThat(response.getStatus()).isEqualTo(200);
        Flashcard returned = response.readEntity(Flashcard.class);
        assertThat(returned.getId()).isEqualTo(3L);
        assertThat(returned.getQuestion()).isEqualTo("What is JUnit?");
        verify(mockFlashcardDAO).create(newFlashcard);
    }

    @Test
    void testUpdateFlashcard_Found() {
        // Arrange: Mock the DAO to return an existing flashcard and to update it
        Flashcard updatedFlashcard = new Flashcard();
        updatedFlashcard.setQuestion("What is JUnit?");
        updatedFlashcard.setAnswer("A popular Java testing framework.");
        updatedFlashcard.setCategory("Testing");

        when(mockFlashcardDAO.findById(1L)).thenReturn(Optional.of(flashcard1));
        when(mockFlashcardDAO.update(flashcard1)).thenReturn(flashcard1);

        // Act: Make a PUT request to /flashcards/1 with updated data
        Response response = RESOURCES.target("/flashcards/1")
                .request(MediaType.APPLICATION_JSON)
                .put(javax.ws.rs.client.Entity.entity(updatedFlashcard, MediaType.APPLICATION_JSON));

        // Assert: Verify the response status and updated content
        assertThat(response.getStatus()).isEqualTo(200);
        Flashcard returned = response.readEntity(Flashcard.class);
        assertThat(returned.getQuestion()).isEqualTo("What is JUnit?");
        assertThat(returned.getAnswer()).isEqualTo("A popular Java testing framework.");
        verify(mockFlashcardDAO).findById(1L);
        verify(mockFlashcardDAO).update(flashcard1);
    }

    @Test
    void testUpdateFlashcard_NotFound() {
        // Arrange: Mock the DAO to return empty when searching for a non-existent ID
        Flashcard updatedFlashcard = new Flashcard();
        updatedFlashcard.setQuestion("What is JUnit?");
        updatedFlashcard.setAnswer("A popular Java testing framework.");
        updatedFlashcard.setCategory("Testing");

        when(mockFlashcardDAO.findById(4L)).thenReturn(Optional.empty());

        // Act: Make a PUT request to /flashcards/4 with updated data
        Response response = RESOURCES.target("/flashcards/4")
                .request(MediaType.APPLICATION_JSON)
                .put(javax.ws.rs.client.Entity.entity(updatedFlashcard, MediaType.APPLICATION_JSON));

        // Assert: Verify the response status is 404 Not Found
        assertThat(response.getStatus()).isEqualTo(404);
        verify(mockFlashcardDAO).findById(4L);
        verify(mockFlashcardDAO, never()).update(any(Flashcard.class));
    }

    @Test
    void testDeleteFlashcard_Found() {
        // Arrange: Mock the DAO to return an existing flashcard for deletion
        when(mockFlashcardDAO.findById(2L)).thenReturn(Optional.of(flashcard2));
        doNothing().when(mockFlashcardDAO).delete(flashcard2);

        // Act: Make a DELETE request to /flashcards/2
        Response response = RESOURCES.target("/flashcards/2")
                .request(MediaType.APPLICATION_JSON)
                .delete();

        // Assert: Verify the response status is 200 OK
        assertThat(response.getStatus()).isEqualTo(200);
        verify(mockFlashcardDAO).findById(2L);
        verify(mockFlashcardDAO).delete(flashcard2);
    }

    @Test
    void testDeleteFlashcard_NotFound() {
        // Arrange: Mock the DAO to return empty when searching for a non-existent ID
        when(mockFlashcardDAO.findById(5L)).thenReturn(Optional.empty());

        // Act: Make a DELETE request to /flashcards/5
        Response response = RESOURCES.target("/flashcards/5")
                .request(MediaType.APPLICATION_JSON)
                .delete();

        // Assert: Verify the response status is 404 Not Found
        assertThat(response.getStatus()).isEqualTo(404);
        verify(mockFlashcardDAO).findById(5L);
        verify(mockFlashcardDAO, never()).delete(any(Flashcard.class));
    }
}
