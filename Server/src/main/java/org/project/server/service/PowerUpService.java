package org.project.server.service;

import jakarta.transaction.Transactional;
import org.project.server.dto.PowerUpDTO;
import org.project.server.model.PowerUp;
import org.project.server.model.User;
import org.project.server.model.UserPower;
import org.project.server.repository.PowerUpRepository;
import org.project.server.repository.UserPowerRepository;
import org.project.server.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PowerUpService {
    private final PowerUpRepository powerUpRepository;
    private final UserRepository userRepository;
    private final UserPowerRepository userPowerRepository;

    public PowerUpService(PowerUpRepository powerUpRepository, UserRepository userRepository, UserPowerRepository userPowerRepository) {
        this.powerUpRepository = powerUpRepository;
        this.userRepository = userRepository;
        this.userPowerRepository = userPowerRepository;
    }

    public List<PowerUpDTO> getPowers(String username) {
        List<PowerUp> powers = powerUpRepository.findAll();

        List<UserPower> ownedPowers = userPowerRepository.findByUserUsername(username);

        return powers.stream().map(power -> {
                    int amount = ownedPowers.stream()
                            .filter(p -> p.getPowerUp().getId().equals(power.getId()))
                            .findFirst()
                            .map(UserPower::getAmount)
                            .orElse(0);

                    return new PowerUpDTO(
                            power.getId(),
                            power.getName(),
                            power.getDescription(),
                            power.getPrice(),
                            power.getType(),
                            power.getValue(),
                            amount
                    );
                })
                .toList();

    }

    @Transactional
    public void buyPower(Long powerId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow();

        PowerUp power = powerUpRepository.findById(powerId).orElseThrow();

        if (user.getPowerCoins() < power.getPrice()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough coins.");
        }

        user.setPowerCoins(user.getPowerCoins() - power.getPrice());

        UserPower userPower = userPowerRepository.findByUserUsernameAndPowerUpId(username, powerId).orElse(null);

        if (userPower == null) {
            userPower = new UserPower();
            userPower.setUser(user);
            userPower.setPowerUp(power);
            userPower.setAmount(1);
        } else {
            userPower.setAmount(userPower.getAmount() + 1);
        }

        userRepository.save(user);
        userPowerRepository.save(userPower);
    }
}
