CREATE TABLE books (
   id         BINARY(16)      NOT NULL,
   version    BIGINT          NOT NULL DEFAULT 0,
   title      VARCHAR(150)    NOT NULL,
   price      DECIMAL(10, 2)  CHECK (price > 0),
   cover_image LONGBLOB,
   deleted_at  DATETIME,
   created_at  DATETIME        NOT NULL,
   updated_at  DATETIME,

   PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;