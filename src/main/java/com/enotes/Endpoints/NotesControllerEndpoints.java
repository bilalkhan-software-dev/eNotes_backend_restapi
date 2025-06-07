package com.enotes.Endpoints;

import com.enotes.Exception.ResourceNotFoundException;
import com.enotes.Util.AppConstant;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/v1/note")
public interface NotesControllerEndpoints {

    @PostMapping("/add-notes")
    @PreAuthorize(AppConstant.AUTH_ROLE_USER)
    public ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam(required = false) MultipartFile file) throws Exception;

    @GetMapping("/all-notes")
    @PreAuthorize(AppConstant.AUTH_ROLE_ADMIN_OR_USER)
    public ResponseEntity<?> getAllNotes();

    @GetMapping("/active-notes")
    @PreAuthorize(AppConstant.AUTH_ROLE_USER)
    public ResponseEntity<?> getAllActiveNotes(
            @RequestParam(name = "number",defaultValue = AppConstant.DefaultPageNumber) Integer pageNo,
            @RequestParam(name = "size",defaultValue = AppConstant.DefaultPageSize) Integer pageSize,
            @RequestParam(name = "sort",defaultValue = "id") String sort,
            @RequestParam(name = "direction",defaultValue = "asc") String direction
    ) throws ResourceNotFoundException;


    @GetMapping("/search-notes")
    @PreAuthorize(AppConstant.AUTH_ROLE_USER)
    public ResponseEntity<?> searchNotes(@RequestParam String keyword,
                                         @RequestParam(name = "number",defaultValue = AppConstant.DefaultPageNumber) Integer pageNo,
                                         @RequestParam(name = "size",defaultValue = AppConstant.DefaultPageSize) Integer pageSize,
                                         @RequestParam(name = "sort",defaultValue = "id") String sort,
                                         @RequestParam(name = "direction",defaultValue = "asc") String direction
    ) throws ResourceNotFoundException;

    @GetMapping("/recycle-bin")
    @PreAuthorize(AppConstant.AUTH_ROLE_USER)
    public ResponseEntity<?> getUserRecycleBinNotes() throws ResourceNotFoundException;

    @GetMapping("/download/{id}")
    @PreAuthorize(AppConstant.AUTH_ROLE_ADMIN_OR_USER)
    public ResponseEntity<?> downloadNoteWithId(@PathVariable Integer id) throws Exception;

    @DeleteMapping("/soft-delete/{id}")
    @PreAuthorize(AppConstant.AUTH_ROLE_USER)
    public ResponseEntity<?> temporaryDeleteNote(@PathVariable Integer id) throws ResourceNotFoundException;

    @DeleteMapping("/hard-delete/{id}")
    @PreAuthorize(AppConstant.AUTH_ROLE_USER)
    public ResponseEntity<?> permanentlyDeleteNote(@PathVariable Integer id) throws ResourceNotFoundException;

    @DeleteMapping("/empty-bin")
    @PreAuthorize(AppConstant.AUTH_ROLE_USER)
    public ResponseEntity<?> emptyRecycleBin();

    @GetMapping("/add-favourite/{noteId}")
    @PreAuthorize(AppConstant.AUTH_ROLE_USER)
    public ResponseEntity<?> addNotesToFavourite(@PathVariable Integer noteId) throws ResourceNotFoundException;

    @DeleteMapping("/un-favourite-note/{favNoteId}")
    @PreAuthorize(AppConstant.AUTH_ROLE_USER)
    public ResponseEntity<?> unFavouriteNotes(@PathVariable Integer favNoteId) throws ResourceNotFoundException;

    @GetMapping("/favourite-notes")
    @PreAuthorize(AppConstant.AUTH_ROLE_USER)
    public ResponseEntity<?> getAllFavouriteNotesOfTheUser();

    @GetMapping("/copy/{noteId}")
    @PreAuthorize(AppConstant.AUTH_ROLE_USER)
    public ResponseEntity<?> copyNote(@PathVariable Integer noteId) throws ResourceNotFoundException;

    @PutMapping("/restore-notes/{id}")
    @PreAuthorize(AppConstant.AUTH_ROLE_USER)
    public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws ResourceNotFoundException;

    @PutMapping("/update-notes/{id}")
    @PreAuthorize(AppConstant.AUTH_ROLE_USER)
    public ResponseEntity<?> updateNotes(@PathVariable Integer id, @RequestParam String notes, @RequestParam(required = false) MultipartFile file) throws Exception;



}
