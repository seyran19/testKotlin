ALTER TABLE budget ADD COLUMN author_id INT NULL;
ALTER TABLE budget ADD CONSTRAINT fk_budget_author FOREIGN KEY (author_id) REFERENCES author(id);
