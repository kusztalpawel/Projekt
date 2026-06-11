package org.project.server;

import org.junit.jupiter.api.Test;
import org.project.server.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CalculateAttackerTest {

    @Autowired
    private CharacterService characterService;

    @Test
    void shouldCalculateAttacker() {
        int firstAttacker = 1;
        int secondAttacker = 1;
        List<Boolean> results = new ArrayList<>();

        for(int i = 0; i < 7; i++){
            Boolean res = characterService.calculateAttacker(3,firstAttacker,2,secondAttacker);
            if(res){
                System.out.println("FIRST");
                firstAttacker++;
            } else {
                System.out.println("SECOND");
                secondAttacker++;
            }

            results.add(res);
        }

        assertEquals(new ArrayList<>(List.of(true, false, true, true, false, true, false)), results);
    }
}
