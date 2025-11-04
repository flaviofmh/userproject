-- Create tb_user_external_project table
CREATE TABLE tb_user_external_project (
    id      BIGSERIAL    NOT NULL,
    user_id BIGINT       NOT NULL,
    name    VARCHAR(120) NOT NULL,
    PRIMARY KEY (id, user_id),
    CONSTRAINT fk_user_external_project_user
        FOREIGN KEY (user_id)
        REFERENCES tb_user(id)
        ON DELETE CASCADE
);

COMMENT ON TABLE tb_user_external_project IS 'External Project identifier for users';
COMMENT ON COLUMN tb_user_external_project.id IS 'identifier of external project';
COMMENT ON COLUMN tb_user_external_project.user_id IS 'unique identifier of the user';
COMMENT ON COLUMN tb_user_external_project.name IS 'Name of external project';

-- Create indexes for better query performance
CREATE INDEX idx_tb_user_external_project_user_id ON tb_user_external_project(user_id);
CREATE INDEX idx_tb_user_external_project_name ON tb_user_external_project(name);
