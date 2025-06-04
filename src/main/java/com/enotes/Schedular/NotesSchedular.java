package com.enotes.Schedular;

import com.enotes.Entity.Notes;
import com.enotes.Repository.NotesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotesSchedular {


    private final NotesRepository notesRepository;


    /**
     * ┌───────────── second (0)
     * │   ┌───────────── minute (0)
     * │   │ ┌───────────── hour (0 -> 12 AM)
     * │   │ │  ┌───────────── day of month (* = every day)
     * │   │ │  │  ┌───────────── month (* = every month)
     * │   │ │  │  │  ┌───────────── day of week (* = every day of the week)
     * │   │ │  │  │  │
     * 0  0  0  *  *  ?

     * 0 - Second (0 seconds)
     * 0 - Minute (0 minutes)
     * 0 - Hour (0 hour, which is 12 AM)
     * * - Day of month (every day)
     * * - Month (every month)
     * ? - Day of week (no specific value, typically used when day of month is specified)

     **/


    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteNoteScheduler() {
        log.info("Running scheduled note cleanup...");

        try {
            LocalDateTime cutOffDate = LocalDateTime.now().minusDays(7);
            List<Notes> deletedNotes = notesRepository.findByIsDeletedAndDeletedOnBefore(true, cutOffDate);

            if (!deletedNotes.isEmpty()) {
                notesRepository.deleteAll(deletedNotes);
                log.info("Permanently deleted {} notes older than {}", deletedNotes.size(), cutOffDate);
            } else {
                log.debug("No notes to delete.");
            }
        } catch (Exception e) {
            log.error("Failed to delete notes: {}", e.getMessage(), e);
        }
    }

}
