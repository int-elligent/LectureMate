package com.intelligent.morning06.lecturemate;

import com.intelligent.morning06.lecturemate.DataAccess.Image;

import org.junit.Test;

import java.time.LocalDateTime;

import static junit.framework.Assert.assertEquals;

public class ImageClassUnitTest {
    @Test
    public void TestConstructorLarge() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        Image image = new Image(1, "TestImage", "TestPath", now, 23);

        assertEquals(1, image.getId());
        assertEquals("TestImage", image.getTitle());
        assertEquals("TestPath", image.getFilePath());
        assertEquals(now, image.getCreationDate());
        assertEquals(23, image.getLectureId());
    }

    @Test
    public void TestConstructorSmall() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        Image image = new Image("TestImage", "TestPath", now, 12);

        assertEquals(-1, image.getId());
        assertEquals("TestImage", image.getTitle());
        assertEquals("TestPath", image.getFilePath());
        assertEquals(now, image.getCreationDate());
        assertEquals(12, image.getLectureId());
    }
}
