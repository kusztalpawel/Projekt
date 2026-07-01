package org.project.server.mapper;

import org.project.server.dto.SkinDTO;
import org.project.server.model.Skin;

public class SkinMapper {
    private SkinMapper() {
    }

    public static SkinDTO toDTO(Skin skin, boolean unlocked) {
        return new SkinDTO(skin.getUrl(), unlocked);
    }
}
