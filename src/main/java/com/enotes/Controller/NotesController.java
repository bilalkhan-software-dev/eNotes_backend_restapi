package com.enotes.Controller;

import com.enotes.Dto.FavouriteNotesDto;
import com.enotes.Dto.NotesDto;
import com.enotes.Dto.NotesResponse;
import com.enotes.Endpoints.NotesControllerEndpoints;
import com.enotes.Entity.FileDetails;
import com.enotes.Exception.ResourceNotFoundException;
import com.enotes.Service.NotesService;
import com.enotes.Util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotesController implements NotesControllerEndpoints {

    private final NotesService notesService;


    @Override
    public ResponseEntity<?> saveNotes(String notes, MultipartFile file) throws Exception {

        boolean isAdded = notesService.addNote(notes, file);
        if (isAdded) {
            return CommonUtil.createBuildResponseMessage("Note Saved Successfully!", HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("Note Saved Failed", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    public ResponseEntity<?> getAllNotes() {
        List<NotesDto> allNotes = notesService.getAllNotes();

        if (CollectionUtils.isEmpty(allNotes)) {
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(allNotes, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllActiveNotes(Integer pageNo, Integer pageSize, String sort, String direction) throws ResourceNotFoundException {

        NotesResponse notesByUserWithPagination = notesService.getAllActiveNotesByUserWithPagination(pageNo, pageSize, sort, direction);

        List<NotesDto> notesDtoList = notesByUserWithPagination.getNotesDtoList();

        if (CollectionUtils.isEmpty(notesDtoList)){
            return CommonUtil.createErrorResponseMessage("No notes Added by the user",HttpStatus.NO_CONTENT);
        }

        return CommonUtil.createBuildResponse(notesByUserWithPagination, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchNotes(String keyword, Integer pageNo, Integer pageSize, String sort, String direction) throws ResourceNotFoundException {

     NotesResponse searchResult =   notesService.searchNotes(keyword,pageNo,pageSize,sort,direction);

        List<NotesDto> notesDtoList = searchResult.getNotesDtoList();

        if (CollectionUtils.isEmpty(notesDtoList)){
            return CommonUtil.createErrorResponseMessage("No result found",HttpStatus.NO_CONTENT);
        }

        return CommonUtil.createBuildResponse(searchResult,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getUserRecycleBinNotes() throws ResourceNotFoundException {

        List<NotesDto> userRecycleBinNotes = notesService.getUserRecycleBinNotes();

        if (CollectionUtils.isEmpty(userRecycleBinNotes)){
            return CommonUtil.createBuildResponseMessage("Notes not available is Recycle Bin ",HttpStatus.OK);
        }
        return CommonUtil.createBuildResponse(userRecycleBinNotes, HttpStatus.OK);
    }


   @Override
    public ResponseEntity<?> downloadNoteWithId( Integer id) throws Exception {

        FileDetails fileDetails = notesService.getFileDetailsById(id);
        byte[] data = notesService.downloadFile(fileDetails);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(CommonUtil.getContentType(fileDetails.getOriginalFileName())));
        headers.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());

        return ResponseEntity.ok().headers(headers).body(data);
    }



    @Override
    public ResponseEntity<?> temporaryDeleteNote( Integer id) throws ResourceNotFoundException {

        boolean isDeleted = notesService.temporaryDeleteNote(id);
        if (isDeleted) {
            return CommonUtil.createBuildResponseMessage("Note Successfully move to Recycle Bin", HttpStatus.OK);
        }
        return CommonUtil.createErrorResponseMessage("Note Deletion Failed", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> permanentlyDeleteNote(Integer id) throws ResourceNotFoundException {

        notesService.permanentlyDeleteNote(id);
            return CommonUtil.createBuildResponseMessage("Note deleted permanently Successfully!", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> emptyRecycleBin()  {

        notesService.clearRecycleBin();;
            return CommonUtil.createBuildResponseMessage("Recycle Bin empty Successfully!", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> addNotesToFavourite( Integer noteId) throws ResourceNotFoundException {

        notesService.addNotesToFavourite(noteId);
        return CommonUtil.createBuildResponseMessage("Note Successfully added to favourite !",HttpStatus.CREATED);

    }


    @Override
    public ResponseEntity<?> unFavouriteNotes( Integer favNoteId) throws ResourceNotFoundException {

        notesService.unFavouriteNote(favNoteId);
        return CommonUtil.createBuildResponseMessage("Notes Successfully removed from favourite list",HttpStatus.OK);

    }

    @Override
    public ResponseEntity<?> getAllFavouriteNotesOfTheUser(){

        List<FavouriteNotesDto> userFavouriteNotes = notesService.getUserFavouriteNotes();

        if (CollectionUtils.isEmpty(userFavouriteNotes)){
            return ResponseEntity.noContent().build();
        }

        return CommonUtil.createBuildResponse(userFavouriteNotes,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> copyNote( Integer noteId) throws ResourceNotFoundException {

        boolean isCopied = notesService.copyNote(noteId);
        if (isCopied){
            return CommonUtil.createBuildResponseMessage("Note Successfully Copied!",HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("Copying note failed! Try again later",HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    public ResponseEntity<?> restoreNotes( Integer id) throws ResourceNotFoundException {

        boolean isRestored = notesService.restoreNotes(id);
        if (isRestored){
            return CommonUtil.createBuildResponseMessage("Notes restored Successfully!",HttpStatus.OK);
        }

        return CommonUtil.createErrorResponseMessage("Error during restoring note",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<?> updateNotes( Integer id, String notes, MultipartFile file) throws Exception {

        boolean isUpdated = notesService.updateNote(id, notes, file);

        if (isUpdated) {
            return CommonUtil.createBuildResponseMessage("Note updated Successfully!", HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("Note updated Failed !", HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
