package com.enotes.Service.Impl;

import com.enotes.Dto.FavouriteNotesDto;
import com.enotes.Dto.NotesDto;
import com.enotes.Dto.NotesDto.FilesDto;
import com.enotes.Dto.NotesResponse;
import com.enotes.Entity.FavouriteNotes;
import com.enotes.Entity.FileDetails;
import com.enotes.Entity.Notes;
import com.enotes.Exception.ResourceNotFoundException;
import com.enotes.Repository.CategoryRepository;
import com.enotes.Repository.FavouriteNotesRepository;
import com.enotes.Repository.FileDetailRepository;
import com.enotes.Repository.NotesRepository;
import com.enotes.Service.NotesService;
import com.enotes.Util.CommonUtil;
import com.enotes.Util.Validation;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotesServiceImpl implements NotesService {

    private final NotesRepository notesRepository;
    private final ModelMapper modelMapper;
    private final Validation validation;
    private final CategoryRepository categoryRepository;
    private final FileDetailRepository fileDetailRepository;
    private final FavouriteNotesRepository favouriteNotesRepository;

    @Value("${upload.file.path}")
    private String uploadPath;
    private static final int DISPLAY_NAME_MAX_LENGTH = 8;
    private static final List<String> ALLOWED_EXTENSIONS = List.of("pdf", "xlsx", "jpg", "png", "docx");

    @Override
    public boolean addNote(String notes, MultipartFile file) throws Exception {
        // Parse notes JSON
        ObjectMapper mapper = new ObjectMapper();
        NotesDto notesDto = mapper.readValue(notes, NotesDto.class);

        // Set default values
        notesDto.setIsDeleted(false);
        notesDto.setDeletedOn(null);

        // Validate category exists
        checkCategoryIdExistOrNot(notesDto.getCategory().getId());

        // Validate note data
        validation.NotesValidation(notesDto);

        // Map to entity
        Notes note = modelMapper.map(notesDto, Notes.class);

        // Handle file upload if present
        if (file != null && !file.isEmpty()) {
            FileDetails fileDetails = saveFileDetails(file);
            note.setFileDetails(fileDetails);
        }

        // Save and return result
        Notes saved = notesRepository.save(note);
        return !ObjectUtils.isEmpty(saved);
    }

    @Override
    public boolean updateNote(Integer id, String notes, MultipartFile file) throws Exception {
        // Parse notes JSON
        ObjectMapper mapper = new ObjectMapper();
        NotesDto notesDto = mapper.readValue(notes, NotesDto.class);

        // Validate category exists
        checkCategoryIdExistOrNot(notesDto.getCategory().getId());

        // Validate note data
        validation.NotesValidation(notesDto);

        // Check if note exists
        Notes existingNote = notesRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("Note not found with id: " + id));

        modelMapper.getConfiguration().setSkipNullEnabled(true);
        // Update fields from DTO
        modelMapper.map(notesDto, existingNote);

        // Handle file upload if present
        if (file != null && !file.isEmpty()) {
            FileDetails fileDetails = saveFileDetails(file);
            existingNote.setFileDetails(fileDetails);
        }

        // Save and return result
        Notes saved = notesRepository.save(existingNote);
        return !ObjectUtils.isEmpty(saved);
    }


    // Save file
    private FileDetails saveFileDetails(MultipartFile file) throws IOException {

        if (!ObjectUtils.isEmpty(file) && !file.isEmpty()) {

            String originalFilename = file.getOriginalFilename();
            log.info("Original file name : {}", originalFilename);
            String extension = FilenameUtils.getExtension(originalFilename);
            log.info("Extension :{}", extension);


            if (!ALLOWED_EXTENSIONS.contains(extension)) {
                throw new IllegalArgumentException("Invalid file format ! Upload only .pdf , .xlsx, .jpg, .png, .docx format");
            }

            String rndString = UUID.randomUUID().toString();
            log.info("Random String : {}", rndString);
            String uploadFileName = rndString + "." + extension;
            log.info("Upload file name :{}", uploadFileName);

            // Creating a directory if it not exists
            File directory = new File(uploadPath);
            if (!directory.exists()) {
                directory.mkdir();
            }

            String storePath = uploadPath.concat(uploadFileName);
            log.info("Store path location :{}", storePath);

            // upload file
            long upload = Files.copy(file.getInputStream(), Paths.get(storePath));

            // if file upload successfully then set its details in a database
            if (upload != 0) {
                FileDetails fileDtls = new FileDetails();
                fileDtls.setOriginalFileName(originalFilename);
                fileDtls.setDisplayFileName(getDisplayName(originalFilename));
                fileDtls.setUploadFileName(uploadFileName);
                fileDtls.setFileSize(file.getSize());
                fileDtls.setPath(storePath);
                return fileDetailRepository.save(fileDtls);
            }
        }

        return null;
    }


    private String getDisplayName(String originalFilename) {
        // ****************.pdf -> original file name
        // *******.pdf -> display file name shorter than original file name
        String extension = FilenameUtils.getExtension(originalFilename);
        String fileName = FilenameUtils.removeExtension(originalFilename);

        if (fileName.length() > DISPLAY_NAME_MAX_LENGTH) {
            fileName = fileName.substring(0, 7);
        }
        fileName = fileName + "." + extension;
        return fileName;
    }


    private void checkCategoryIdExistOrNot(Integer id) throws ResourceNotFoundException {

        categoryRepository
                .findByIdAndIsDeletedFalse(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Category with id " + id + " does not exist or is marked as deleted.")
                );
    }

    @Override
    public byte[] downloadFile(FileDetails fileDetails) throws IOException {

        InputStream inputStream = new FileInputStream(fileDetails.getPath());

        return StreamUtils.copyToByteArray(inputStream);
    }

    @Override
    public FileDetails getFileDetailsById(Integer id) throws ResourceNotFoundException {

        return fileDetailRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("File not found with id :" + id + " or maybe file not available"));
    }

    @Override
    public List<NotesDto> getAllNotes() {

        return notesRepository.findAll()
                .stream().
                map(notes -> modelMapper.map(notes, NotesDto.class))
                .toList();
    }

    @Override
    public NotesResponse getAllActiveNotesByUserWithPagination(Integer pageNo, Integer pageSize, String sortBy, String direction) throws ResourceNotFoundException {
        Integer userId = CommonUtil.GetLoggedInUserDetails().getId();


        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Notes> notesWithPagination = notesRepository.findByCreatedByAndIsDeletedFalse(userId, pageable);

        List<NotesDto> notesDtoList = notesWithPagination.get().map(notes -> modelMapper.map(notes, NotesDto.class)).toList();


        return NotesResponse.builder()
                .notesDtoList(notesDtoList)
                .totalElements(notesWithPagination.getTotalElements())
                .totalPages(notesWithPagination.getTotalPages())
                .pageNo(notesWithPagination.getNumber())
                .pageSize(notesWithPagination.getSize())
                .isFirst(notesWithPagination.isFirst())
                .isLast(notesWithPagination.isLast())
                .sortBy(sortBy)
                .direction(direction)
                .build();
    }

    @Override
    public List<NotesDto> getUserRecycleBinNotes() throws ResourceNotFoundException {
        Integer userId = CommonUtil.GetLoggedInUserDetails().getId();
        return notesRepository.findByCreatedByAndIsDeletedTrue(userId)
                .stream()
                .map(notes -> modelMapper.map(notes, NotesDto.class))
                .toList();
    }


    @Override
    public boolean temporaryDeleteNote(Integer id) throws ResourceNotFoundException {

        Notes notes = notesRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("Note not available or notes also deleted"));

        notes.setIsDeleted(true);
        notes.setDeletedOn(LocalDateTime.now());

        return !ObjectUtils.isEmpty(notesRepository.save(notes));
    }

    @Override
    public void permanentlyDeleteNote(Integer id) throws ResourceNotFoundException {

        Notes notes = notesRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Notes not found!")
        );

        if (notes.getIsDeleted()){
            notesRepository.delete(notes);
        }else {
            throw new IllegalArgumentException("Sorry, you cannot delete this permanently directly");
        }
    }

    @Override
    public boolean restoreNotes(Integer id) throws ResourceNotFoundException {

        Notes notes = notesRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Notes id invalid ! Note found")
        );

        notes.setIsDeleted(false);
        notes.setDeletedOn(null);
        Notes save = notesRepository.save(notes);

        return !ObjectUtils.isEmpty(save);
    }

    @Override
    public void clearRecycleBin() {

        Integer userId = CommonUtil.GetLoggedInUserDetails().getId();
        List<Notes> isDeleted = notesRepository.findByCreatedByAndIsDeletedTrue(userId);

        if (!CollectionUtils.isEmpty(isDeleted)) {
            notesRepository.deleteAll(isDeleted);
        }
        if (ObjectUtils.isEmpty(isDeleted)){
            throw new IllegalArgumentException("Recycle Bin is already empty");
        }
    }


    @Override
    public void addNotesToFavourite(Integer noteId) throws ResourceNotFoundException {

        Integer userId = CommonUtil.GetLoggedInUserDetails().getId();

        Notes notes = notesRepository.findById(noteId).orElseThrow(
                () -> new ResourceNotFoundException("Invalid note id! Not Found")
        );

        FavouriteNotes build = FavouriteNotes.builder()
                .notes(notes)
                .userId(userId)
                .build();
        favouriteNotesRepository.save(build);

    }

    @Override
    public void unFavouriteNote(Integer favNotesId) throws ResourceNotFoundException {

        FavouriteNotes favouriteNotes = favouriteNotesRepository.findById(favNotesId).orElseThrow(
                () -> new ResourceNotFoundException("Favourite note id invalid! Not found")
        );
        favouriteNotesRepository.delete(favouriteNotes);

    }

    @Override
    public List<FavouriteNotesDto> getUserFavouriteNotes(){

        Integer userId = CommonUtil.GetLoggedInUserDetails().getId() ;
        List<FavouriteNotes> favouriteNotesByUserId = favouriteNotesRepository.findByUserId(userId);

        return favouriteNotesByUserId.stream()
                .map(favNotes ->modelMapper.map(favNotes, FavouriteNotesDto.class)).toList();
    }

    @Override
    public boolean copyNote(Integer noteId) throws ResourceNotFoundException {

        Notes copiedNote = notesRepository.findById(noteId).orElseThrow(
                () -> new ResourceNotFoundException("Invalid id! Note not found")
        );

        Notes pastedNote = Notes.builder()
                .title(copiedNote.getTitle())
                .description(copiedNote.getDescription())
                .category(copiedNote.getCategory())
                .isDeleted(copiedNote.getIsDeleted())
                .fileDetails(copiedNote.getFileDetails())
                .build();

        Notes saved = notesRepository.save(pastedNote);

        return !ObjectUtils.isEmpty(saved);
    }
}
