package com.enotes.Controller;

import com.enotes.Dto.CategoryDto;
import com.enotes.Dto.FavouriteNotesDto;
import com.enotes.Dto.NotesDto;
import com.enotes.Dto.NotesResponse;
import com.enotes.Entity.FileDetails;
import com.enotes.Exception.ResourceNotFoundException;
import com.enotes.Service.NotesService;
import com.enotes.Util.AppConstant;
import com.enotes.Util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/note")
@RequiredArgsConstructor
public class NotesController {

    private final NotesService notesService;

    @PostMapping("/add-notes")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam(required = false) MultipartFile file) throws Exception {

        boolean isAdded = notesService.addNote(notes, file);
        if (isAdded) {
            return CommonUtil.createBuildResponseMessage("Note Saved Successfully!", HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("Note Saved Failed", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/all-notes")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> getAllNotes() {
        List<NotesDto> allNotes = notesService.getAllNotes();

        if (CollectionUtils.isEmpty(allNotes)) {
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(allNotes, HttpStatus.OK);
    }

    @GetMapping("/active-notes")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllActiveNotes(
            @RequestParam(name = "number",defaultValue = AppConstant.DefaultPageNumber) Integer pageNo,
            @RequestParam(name = "size",defaultValue = AppConstant.DefaultPageSize) Integer pageSize,
            @RequestParam(name = "sort",defaultValue = "id") String sort,
            @RequestParam(name = "direction",defaultValue = "asc") String direction
    ) throws ResourceNotFoundException {

        NotesResponse notesByUserWithPagination = notesService.getAllActiveNotesByUserWithPagination(pageNo, pageSize, sort, direction);

        List<NotesDto> notesDtoList = notesByUserWithPagination.getNotesDtoList();

        if (CollectionUtils.isEmpty(notesDtoList)){
            return CommonUtil.createErrorResponseMessage("No notes Added by the user",HttpStatus.NO_CONTENT);
        }

        return CommonUtil.createBuildResponse(notesByUserWithPagination, HttpStatus.OK);
    }

    @GetMapping("/recycle-bin")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUserRecycleBinNotes() throws ResourceNotFoundException {

        List<NotesDto> userRecycleBinNotes = notesService.getUserRecycleBinNotes();

        if (CollectionUtils.isEmpty(userRecycleBinNotes)){
            return CommonUtil.createBuildResponseMessage("Notes not available is Recycle Bin ",HttpStatus.OK);
        }
        return CommonUtil.createBuildResponse(userRecycleBinNotes, HttpStatus.OK);
    }


    @GetMapping("/download/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> downloadNoteWithId(@PathVariable Integer id) throws Exception {

        FileDetails fileDetails = notesService.getFileDetailsById(id);
        byte[] data = notesService.downloadFile(fileDetails);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(CommonUtil.getContentType(fileDetails.getOriginalFileName())));
        headers.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());

        return ResponseEntity.ok().headers(headers).body(data);
    }



    @DeleteMapping("/soft-delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> temporaryDeleteNote(@PathVariable Integer id) throws ResourceNotFoundException {

        boolean isDeleted = notesService.temporaryDeleteNote(id);
        if (isDeleted) {
            return CommonUtil.createBuildResponseMessage("Note Successfully move to Recycle Bin", HttpStatus.OK);
        }
        return CommonUtil.createErrorResponseMessage("Note Deletion Failed", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/hard-delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> permanentlyDeleteNote(@PathVariable Integer id) throws ResourceNotFoundException {

        notesService.permanentlyDeleteNote(id);
            return CommonUtil.createBuildResponseMessage("Note deleted permanently Successfully!", HttpStatus.OK);
    }

    @DeleteMapping("/empty-bin")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> emptyRecycleBin()  {

        notesService.clearRecycleBin();;
            return CommonUtil.createBuildResponseMessage("Recycle Bin empty Successfully!", HttpStatus.OK);
    }


    @GetMapping("/add-favourite/{noteId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addNotesToFavourite(@PathVariable Integer noteId) throws ResourceNotFoundException {

        notesService.addNotesToFavourite(noteId);
        return CommonUtil.createBuildResponseMessage("Note Successfully added to favourite !",HttpStatus.CREATED);

    }


    @DeleteMapping("/un-favourite-note/{favNoteId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> unFavouriteNotes(@PathVariable Integer favNoteId) throws ResourceNotFoundException {

        notesService.unFavouriteNote(favNoteId);
        return CommonUtil.createBuildResponseMessage("Notes Successfully removed from favourite list",HttpStatus.OK);

    }

    @GetMapping("/favourite-notes")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllFavouriteNotesOfTheUser(){

        List<FavouriteNotesDto> userFavouriteNotes = notesService.getUserFavouriteNotes();

        if (CollectionUtils.isEmpty(userFavouriteNotes)){
            return ResponseEntity.noContent().build();
        }

        return CommonUtil.createBuildResponse(userFavouriteNotes,HttpStatus.OK);
    }

    @GetMapping("/copy/{noteId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> copyNote(@PathVariable Integer noteId) throws ResourceNotFoundException {

        boolean isCopied = notesService.copyNote(noteId);
        if (isCopied){
            return CommonUtil.createBuildResponseMessage("Note Successfully Copied!",HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("Copying note failed! Try again later",HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @PutMapping("/restore-notes/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws ResourceNotFoundException {

        boolean isRestored = notesService.restoreNotes(id);
        if (isRestored){
            return CommonUtil.createBuildResponseMessage("Notes restored Successfully!",HttpStatus.OK);
        }

        return CommonUtil.createErrorResponseMessage("Error during restoring note",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/update-notes/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateNotes(@PathVariable Integer id, @RequestParam String notes, @RequestParam(required = false) MultipartFile file) throws Exception {

        boolean isUpdated = notesService.updateNote(id, notes, file);

        if (isUpdated) {
            return CommonUtil.createBuildResponseMessage("Note updated Successfully!", HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("Note updated Failed !", HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
