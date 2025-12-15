package com.learnenglish.LearnEnglish.util;

import org.springframework.stereotype.Component;
import com.learnenglish.LearnEnglish.entity.Levels;
import com.learnenglish.LearnEnglish.entity.User;

@Component
public class UserLevelHelper {

    public Levels getStudyingLevel(User user) {
        return user.getCurrentLevel() != null
                ? user.getCurrentLevel()
                : user.getLevel();
    }
}
