package com.example.flashcardapp.resources;

import com.example.flashcardapp.core.Flashcard;
import com.example.flashcardapp.db.FlashcardDAO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * RESTful API Resource for managing Flashcards.
 */
@Path("/flashcards")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Flashcard API", description = "Operations related to Flashcards")
public class FlashcardResource {

    private final FlashcardDAO flashcardDAO;

    /**
     * Constructor injecting the FlashcardDAO.
     *
     * @param flashcardDAO Data Access Object for Flashcards.
     */
    public FlashcardResource(FlashcardDAO flashcardDAO) {
        this.flashcardDAO = flashcardDAO;
    }

    /**
     * Retrieves a list of all flashcards.
     *
     * @return List of Flashcards.
     */
    @GET
    @Operation(summary = "Get all flashcards", description = "Retrieves a list of all flashcards.")
    public Response getAllFlashcards() {
        List<Flashcard> flashcards = flashcardDAO.findAll();
        return Response.ok(flashcards).build();
    }

    /**
     * Retrieves a specific flashcard by ID.
     *
     * @param id ID of the flashcard.
     * @return Flashcard if found.
     */
    @GET
    @Path("/{id}")
    @Operation(summary = "Get a flashcard by ID", description = "Retrieves a single flashcard by its unique ID.")
    public Response getFlashcardById(@PathParam("id") Long id) {
        Optional<Flashcard> flashcard = flashcardDAO.findById(id);
        if (flashcard.isPresent()) {
            return Response.ok(flashcard.get()).build();
        } else {
            throw new NotFoundException("Flashcard with ID " + id + " not found.");
        }
    }

    /**
     * Creates a new flashcard.
     *
     * @param flashcard Flashcard object to create.
     * @param uriInfo   URI context.
     * @return Response with location of created flashcard.
     */
    @POST
    @Operation(summary = "Create a new flashcard", description = "Creates a new flashcard with the provided details.")
    public Response createFlashcard(@Valid Flashcard flashcard, @Context UriInfo uriInfo) {
        Flashcard createdFlashcard = flashcardDAO.create(flashcard);
        URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(createdFlashcard.getId())).build();
        return Response.created(uri).entity(createdFlashcard).build();
    }

    /**
     * Updates an existing flashcard.
     *
     * @param id         ID of the flashcard to update.
     * @param flashcard  Flashcard object with updated details.
     * @return Updated Flashcard.
     */
    @PUT
    @Path("/{id}")
    @Operation(summary = "Update an existing flashcard", description = "Updates the flashcard identified by the given ID.")
    public Response updateFlashcard(@PathParam("id") Long id, @Valid Flashcard flashcard) {
        Optional<Flashcard> updatedFlashcard = flashcardDAO.update(id, flashcard);
        if (updatedFlashcard.isPresent()) {
            return Response.ok(updatedFlashcard.get()).build();
        } else {
            throw new NotFoundException("Flashcard with ID " + id + " not found.");
        }
    }

    /**
     * Deletes a flashcard by ID.
     *
     * @param id ID of the flashcard to delete.
     * @return Response indicating the outcome.
     */
    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a flashcard", description = "Deletes the flashcard identified by the given ID.")
    public Response deleteFlashcard(@PathParam("id") Long id) {
        boolean deleted = flashcardDAO.delete(id);
        if (deleted) {
            return Response.noContent().build();
        } else {
            throw new NotFoundException("Flashcard with ID " + id + " not found.");
        }
    }

    /**
     * Searches flashcards by category.
     *
     * @param category Category to search for.
     * @return List of Flashcards matching the category.
     */
    @GET
    @Path("/search")
    @Operation(summary = "Search flashcards by category", description = "Retrieves flashcards that belong to the specified category.")
    public Response searchFlashcardsByCategory(@QueryParam("category") String category) {
        if (category == null || category.isEmpty()) {
            throw new BadRequestException("Category query parameter is required.");
        }
        List<Flashcard> flashcards = flashcardDAO.findByCategory(category);
        return Response.ok(flashcards).build();
    }

}
