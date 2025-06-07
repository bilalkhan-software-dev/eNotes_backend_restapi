package com.enotes.Service;

import com.enotes.Dto.FavouriteNotesDto;
import com.enotes.Dto.NotesDto;
import com.enotes.Dto.NotesResponse;
import com.enotes.Entity.FileDetails;
import com.enotes.Exception.ResourceNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface NotesService {

    boolean addNote(String  notes,MultipartFile file) throws Exception;
    boolean updateNote(Integer id,String  notes,MultipartFile file) throws Exception;


    // show note according to the requirement
    List<NotesDto> getAllNotes();
    NotesResponse getAllActiveNotesByUserWithPagination(Integer pageNo,Integer pageSize,String sortBy,String direction) throws ResourceNotFoundException;
    NotesResponse searchNotes(String keyword, Integer pageNo, Integer pageSize, String sortBy, String direction);
    List<NotesDto> getUserRecycleBinNotes() throws ResourceNotFoundException;



    FileDetails getFileDetailsById(Integer id) throws ResourceNotFoundException;
    byte[] downloadFile(FileDetails fileDetails) throws IOException;


    // Delete and Restore note
    boolean temporaryDeleteNote(Integer id) throws ResourceNotFoundException;
    void permanentlyDeleteNote(Integer id) throws ResourceNotFoundException;
    void clearRecycleBin() ;
    boolean restoreNotes(Integer id) throws ResourceNotFoundException;


    // Favourite and Un-Favourite Note
    void addNotesToFavourite(Integer userId) throws ResourceNotFoundException;
    void unFavouriteNote(Integer favNotesId) throws ResourceNotFoundException;
    List<FavouriteNotesDto> getUserFavouriteNotes();

    // Copy note
    boolean copyNote(Integer noteId) throws ResourceNotFoundException;



}
