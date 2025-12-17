package com.learnenglish.LearnEnglish.util;

import com.learnenglish.LearnEnglish.entity.Exercise_results;

public record SaveResultOutcome(
    Exercise_results result,
    boolean firstTime,
    boolean improved
) {}
