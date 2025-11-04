-- Create tb_user table
CREATE TABLE tb_user (
    id       BIGSERIAL    PRIMARY KEY,
    email    VARCHAR(200) NOT NULL UNIQUE,
    password VARCHAR(129) NOT NULL,
    name     VARCHAR(120) NULL
);

COMMENT ON TABLE tb_user IS 'All users';
COMMENT ON COLUMN tb_user.id IS 'unique identifier of the user';
COMMENT ON COLUMN tb_user.email IS 'email for user';
COMMENT ON COLUMN tb_user.password IS 'password';
COMMENT ON COLUMN tb_user.name IS 'name of the user';

-- Create index for email lookups
CREATE INDEX idx_tb_user_email ON tb_user(email);
