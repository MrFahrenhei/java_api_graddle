package com.genesis;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Duration;

public class MainTest {
    
    @Test
    public void verifyNoExceptionThrown() throws Exception{
        assertTimeoutPreemptively(Duration.ofSeconds(2),
            () ->  Main.main(new String[]{}));
    }
}
