package ru.satird.pcs.services;

import ru.satird.pcs.domains.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {

    Optional<RefreshToken> findByToken(String token);
    RefreshToken verifyExpiration(RefreshToken token);
    int deleteByUserId(Long userId);
    RefreshToken createRefreshToken(Long userId);
}
