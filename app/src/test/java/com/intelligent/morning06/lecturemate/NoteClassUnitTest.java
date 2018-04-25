package com.intelligent.morning06.lecturemate;

import com.intelligent.morning06.lecturemate.DataAccess.Note;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;


public class NoteClassUnitTest {
    @Test
    public void TestConstructorLarge() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        Note note = new Note(1, "testTitle", "testText", now, 23);

        assertEquals(1, note.getId());
        assertEquals("testTitle", note.getTitle());
        assertEquals("testText", note.getText());
        assertEquals(now, note.getCreationDate());
        assertEquals(23, note.getLectureId());
    }

    @Test
    public void TestConstructorSmall() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        Note note = new Note("testTitle", "testText", now ,24);

        assertEquals(-1, note.getId());
        assertEquals("testTitle", note.getTitle());
        assertEquals("testText", note.getText());
        assertEquals(now, note.getCreationDate());
        assertEquals(24, note.getLectureId());
    }
}