DELETE FROM daily_calories a USING (
    SELECT MAX(id) as max_id, user_id, date
    FROM daily_calories
    GROUP BY user_id, date
    HAVING COUNT(*) > 1
) b
WHERE a.user_id = b.user_id
  AND a.date = b.date
  AND a.id != b.max_id;

ALTER TABLE daily_calories
    ADD CONSTRAINT unique_user_date UNIQUE (user_id, date);

CREATE INDEX idx_daily_calories_user_date
    ON daily_calories(user_id, date DESC);