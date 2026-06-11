package org.project.server.mapper;

import org.project.server.dto.CharacterDTO;
import org.project.server.model.Character;

public class CharacterMapper {
    private CharacterMapper(){}

    public static CharacterDTO toDTO(Character character){
        return new CharacterDTO(character.getAttackPoints(), character.getDefencePoints(), character.getAgilityPoints(), character.getHealth());
    }
}
