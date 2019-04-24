package com.redhat.developers.microsweeper.service;

import com.redhat.developers.microsweeper.model.Score;

import javax.transaction.Transactional;
import java.util.List;

public class QuarkusPanacheScoreboardService implements ScoreboardService {

    @Override
    public List<Score> getScoreboard() {
        return Score.listAll();
    }

    @Override
    @Transactional
    public void addScore(Score score) {
        Score.persist(score);
    }

    @Override
    public void clearScores() {
        Score.deleteAll();
    }

}
