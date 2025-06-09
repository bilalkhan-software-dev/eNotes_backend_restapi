package com.enotes.Endpoints;

import com.enotes.Dto.NotesRequest;
import com.enotes.Exception.ResourceNotFoundException;
import static com.enotes.Util.AppConstant.AUTH_ROLE_ADMIN_OR_USER;
import static com.enotes.Util.AppConstant.AUTH_ROLE_ADMIN;
import static com.enotes.Util.AppConstant.AUTH_ROLE_USER;
import static com.enotes.Util.AppConstant.DefaultPageSize;
import static com.enotes.Util.AppConstant.DefaultPageNumber;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Notes", description = "All the notes operation apis")
@RequestMapping("/api/v1/note")
public interface NotesControllerEndpoints {


    @Operation(summary = "add notes", tags = {"Notes", "User"}, description = "Accessible only for user")
    @PostMapping(value = "/add-notes",consumes = "multipart/form-data")
    @PreAuthorize(AUTH_ROLE_USER)
    public ResponseEntity<?> saveNotes(@RequestParam
                                       @Parameter(description = "Json String notes",
                                       required = true,
                                       content = @Content
                                       (schema = @Schema(implementation = NotesRequest.class))
    ) String notes, @RequestParam(required = false) MultipartFile file) throws Exception;

    @Operation(summary = "All notes", tags = {"Notes", "User"}, description = "Accessible only for admin")
    @GetMapping("/all-notes")
    @PreAuthorize(AUTH_ROLE_ADMIN)
    public ResponseEntity<?> getAllNotes();

    @Operation(summary = "All active notes", tags = {"Notes", "User"}, description = "User can see added notes with pagination if it's not deleted! | Accessible only for user")
    @GetMapping("/active-notes")
    @PreAuthorize(AUTH_ROLE_USER)
    public ResponseEntity<?> getAllActiveNotes(
            @RequestParam(name = "number", defaultValue = DefaultPageNumber) Integer pageNo,
            @RequestParam(name = "size", defaultValue = DefaultPageSize) Integer pageSize,
            @RequestParam(name = "sort", defaultValue = "id") String sort,
            @RequestParam(name = "direction", defaultValue = "asc") String direction
    ) throws ResourceNotFoundException;

    @Operation(summary = "Search notes", tags = {"Notes", "User"}, description = "User can search notes with pagination | Accessible only for user")
    @GetMapping("/search-notes")
    @PreAuthorize(AUTH_ROLE_USER)
    public ResponseEntity<?> searchNotes(@RequestParam String keyword,
                                         @RequestParam(name = "number", defaultValue = DefaultPageNumber) Integer pageNo,
                                         @RequestParam(name = "size", defaultValue = DefaultPageSize) Integer pageSize,
                                         @RequestParam(name = "sort", defaultValue = "id") String sort,
                                         @RequestParam(name = "direction", defaultValue = "asc") String direction
    ) throws ResourceNotFoundException;

    @Operation(summary = "Recycle Bin", tags = {"Notes", "User"}, description = "User can see deleted notes! | Accessible only for user")
    @GetMapping("/recycle-bin")
    @PreAuthorize(AUTH_ROLE_USER)
    public ResponseEntity<?> getUserRecycleBinNotes() throws ResourceNotFoundException;

    @Operation(summary = "download notes", tags = {"Notes", "User"}, description = "User,Admin can download notes! | Accessible for both admin and user")
    @GetMapping("/download/{id}")
    @PreAuthorize(AUTH_ROLE_ADMIN_OR_USER)
    public ResponseEntity<?> downloadNoteWithId(@PathVariable Integer id) throws Exception;

    @Operation(summary = "Temporary delete notes", tags = {"Notes", "User"}, description = "Move notes to recycle bin! | Accessible only for user")
    @DeleteMapping("/soft-delete/{id}")
    @PreAuthorize(AUTH_ROLE_USER)
    public ResponseEntity<?> temporaryDeleteNote(@PathVariable Integer id) throws ResourceNotFoundException;


    @Operation(summary = "Permanently delete notes", tags = {"Notes", "User"}, description = "Accessible only for user")
    @DeleteMapping("/hard-delete/{id}")
    @PreAuthorize(AUTH_ROLE_USER)
    public ResponseEntity<?> permanentlyDeleteNote(@PathVariable Integer id) throws ResourceNotFoundException;

    @Operation(summary = "Emtpy recycle bin", tags = {"Notes", "User"}, description = "Accessible only for user")
    @DeleteMapping("/empty-bin")
    @PreAuthorize(AUTH_ROLE_USER)
    public ResponseEntity<?> emptyRecycleBin();

    @Operation(summary = "Add notes to favourite", tags = {"Notes", "User"}, description = "Accessible only for user")
    @GetMapping("/add-favourite/{noteId}")
    @PreAuthorize(AUTH_ROLE_USER)
    public ResponseEntity<?> addNotesToFavourite(@PathVariable Integer noteId) throws ResourceNotFoundException;

    @Operation(summary = "Add notes to un-favourite", tags = {"Notes", "User"}, description = "Accessible only for user")
    @DeleteMapping("/un-favourite-note/{favNoteId}")
    @PreAuthorize(AUTH_ROLE_USER)
    public ResponseEntity<?> unFavouriteNotes(@PathVariable Integer favNoteId) throws ResourceNotFoundException;

    @Operation(summary = "See all favourite notes", tags = {"Notes", "User"}, description = "Accessible only for user")
    @GetMapping("/favourite-notes")
    @PreAuthorize(AUTH_ROLE_USER)
    public ResponseEntity<?> getAllFavouriteNotesOfTheUser();

    @Operation(summary = "Copy notes", tags = {"Notes", "User"}, description = "Accessible only for user")
    @GetMapping("/copy/{noteId}")
    @PreAuthorize(AUTH_ROLE_USER)
    public ResponseEntity<?> copyNote(@PathVariable Integer noteId) throws ResourceNotFoundException;


    @Operation(summary = "Restore temporary deleted notes", tags = {"Notes", "User"}, description = "Accessible only for user")
    @PutMapping("/restore-notes/{id}")
    @PreAuthorize(AUTH_ROLE_USER)
    public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws ResourceNotFoundException;

    @Operation(summary = "Update notes", tags = {"Notes", "User"}, description = "Logged in user can update its notes | Accessible only for user")
    @PutMapping(value = "/update-notes/{id}",consumes = "multipart/form-data")
    @PreAuthorize(AUTH_ROLE_USER)
    public ResponseEntity<?> updateNotes(@PathVariable Integer id, @RequestParam
    @Parameter(description = "Json String notes",
            required = true,
            content = @Content
                    (schema = @Schema(implementation = NotesRequest.class)))
    String notes, @RequestParam(required = false) MultipartFile file) throws Exception;


}
