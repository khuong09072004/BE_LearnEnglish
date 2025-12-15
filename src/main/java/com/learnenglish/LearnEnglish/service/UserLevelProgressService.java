package com.learnenglish.LearnEnglish.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnenglish.LearnEnglish.entity.Exercises;
import com.learnenglish.LearnEnglish.entity.Levels;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.entity.User_level_progress;
import com.learnenglish.LearnEnglish.repository.LevelsRepository;
import com.learnenglish.LearnEnglish.repository.UserLevelProgressRepository;
import com.learnenglish.LearnEnglish.repository.UserRepository;

import jakarta.transaction.Transactional;
@Service
@Transactional
public class UserLevelProgressService {

    @Autowired
    private UserLevelProgressRepository userLevelProgressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LevelsRepository levelsRepository;

    public void updateLevelProgress(User user, Exercises exercise) {

        Levels level = exercise.getTopic().getLevel();

        // Lấy hoặc tạo progress cho level
        User_level_progress progress =
            userLevelProgressRepository
                .findByUserIdAndLevelId(user.getId(), level.getId())
                .orElseGet(() -> {
                    User_level_progress p = new User_level_progress();
                    p.setUser(user);
                    p.setLevel(level);
                    p.setProgress(0);
                    p.set_completed(false);
                    return userLevelProgressRepository.save(p);
                });

        //  Tính % progress của level
        Integer percent =
            userLevelProgressRepository
                .calculateLevelProgress(user.getId(), level.getId());

        progress.setProgress(percent);

        //  Nếu hoàn thành level
        if (percent == 100 && !progress.is_completed()) {
            progress.set_completed(true);
            progress.setCompleted_at(LocalDateTime.now());
            openNextLevel(user, level);
        }

        userLevelProgressRepository.save(progress);
    }

    private void openNextLevel(User user, Levels currentLevel) {

        levelsRepository
            .findByLevelOrder(currentLevel.getLevelOrder() + 1)
            .ifPresent(nextLevel -> {

                // tạo progress cho level mới nếu chưa có
                if (!userLevelProgressRepository
                        .existsByUserIdAndLevelId(user.getId(), nextLevel.getId())) {

                    User_level_progress p = new User_level_progress();
                    p.setUser(user);
                    p.setLevel(nextLevel);
                    p.setProgress(0);
                    p.set_completed(false);
                    userLevelProgressRepository.save(p);
                }

                // update level hiện tại của user
                user.setLevel(nextLevel);
                userRepository.save(user);
            });
    }
}



