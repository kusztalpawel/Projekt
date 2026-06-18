package org.project.server.service;

import org.project.server.model.*;
import org.project.server.model.Character;
import org.project.server.repository.CharacterRepository;
import org.project.server.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CharacterService {
    private final CharacterRepository characterRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ApplicationEventPublisher publisher;

    public CharacterService(CharacterRepository characterRepository, UserRepository userRepository, UserService userService, ApplicationEventPublisher publisher) {
        this.characterRepository = characterRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.publisher = publisher;
    }

    public Character getCharacter(Long id){
        return characterRepository.findById(id).orElseThrow();
    }

    public void createDefaultCharacter(User user){
        Character character = new Character();

        character.setUser(user);

        character.setAttackPoints(1);
        character.setDefencePoints(1);
        character.setAgilityPoints(1);
        character.setHealth(100f);

        characterRepository.save(character);
    }

    @Transactional
    public Character addStatPoint(String username, StatType stat) {
        User user = userRepository.findByUsername(username).orElseThrow();

        if (user.getPoints() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough points");
        }

        Character character = user.getCharacter();

        switch (stat) {
            case ATTACK -> character.setAttackPoints(character.getAttackPoints() + 1);
            case DEFENCE -> character.setDefencePoints(character.getDefencePoints() + 1);
            case AGILITY -> character.setAgilityPoints(character.getAgilityPoints() + 1);
            case HEALTH -> character.setHealth(character.getHealth() + 5);
        }

        user.setPoints(user.getPoints() - 1);

        characterRepository.save(character);

        userService.incrementStat(user, AchievementMetric.SKILLPOINTS_USED);
        publisher.publishEvent(new ProgressEvent(user.getId(), AchievementMetric.SKILLPOINTS_USED, user.getSkillpointsUsed()));

        return character;
    }

    public List<Attack> duel(String usernameId, String friendUsernameId){
        User player = userRepository.findByUsername(usernameId).orElseThrow();
        User enemy = userRepository.findByUsername(friendUsernameId).orElseThrow();
        Character playerCharacter = player.getCharacter();
        Character enemyCharacter = enemy.getCharacter();

        List<Attack> attacks = new ArrayList<>();
        float playerHealth = playerCharacter.getHealth();
        float enemyHealth = enemyCharacter.getHealth();
        int playerAttackCount = 1;
        int enemyAttackCount = 1;

        while(playerHealth > 0 && enemyHealth > 0){
            if(calculateAttacker(playerCharacter.getAgilityPoints(), playerAttackCount, enemyCharacter.getAgilityPoints(), enemyAttackCount)){
                enemyHealth  -= playerCharacter.getAttackPoints();
                attacks.add(new Attack(player.getUsername(), calculateDamage(playerCharacter.getAttackPoints(), enemyCharacter.getDefencePoints())));
                playerAttackCount++;
            } else {
                playerHealth -= enemyCharacter.getAttackPoints();
                attacks.add(new Attack(enemy.getUsername(), calculateDamage(enemyCharacter.getAttackPoints(), playerCharacter.getDefencePoints())));
                enemyAttackCount++;
            }
        }

        if(playerHealth > 0){
            attacks.add(new Attack(player.getUsername(), -1d));
        } else {
            attacks.add(new Attack(enemy.getUsername(), -1d));
        }

        return attacks;
    }

    public boolean calculateAttacker(int playerAgility, int playerAttackCount, int enemyAgility, int enemyAttackCount){
        return ((double) playerAgility / playerAttackCount) >= ((double) enemyAgility / enemyAttackCount);
    }

    private Double calculateDamage(int attack, int defence){
        return Math.ceil(Math.max(((double) attack / defence), 0d)*100d)/100d;
    }
}
