INSERT INTO app_user (email, name, password, role)
VALUES ('admin@email.se', 'Admin', '$2a$10$Q3iZyKrZa9yxR53YS3k9b.bZUjDc/KgqRt/FkbCGaQtCdfGcQ.oI.', 'ADMIN'),
       ('user@email.se', 'User', '$2a$10$Q3iZyKrZa9yxR53YS3k9b.bZUjDc/KgqRt/FkbCGaQtCdfGcQ.oI.', 'USER');

INSERT INTO categories (name, icon, global, parent_id, user_id)
VALUES ('Hudvård', 'skin', true, null, null),
       ('Makeup', 'makeup', true, null, null),
       ('Hår', 'hair', true, null, null),
       ('Naglar', 'nail', true, null, null);

INSERT INTO products (name, description, brand, purchase_url, status, category_id, user_id)
VALUES ('Vitamin C Serum', 'Ljusnar upp huden och jämnar ut hudtonen', 'The Ordinary', 'theordinary.com', 'HAS', 1, 2),
       ('Hyaluronic Acid', 'Fuktar och fyller ut huden', 'The Ordinary', 'theordinary.com', 'ALMOST_OUT', 1, 2),
       ('SPF 50 Moisturizer', 'Dagkräm med solskydd', 'La Roche-Posay', 'laroche-posay.se', 'HAS', 1, 2),
       ('Retinol Cream', 'Nattkräm med retinol för anti-aging', 'CeraVe', 'cereave.se', 'WANT', 1, 2),
       ('Foundation Serum', 'Lätt foundation med serum-konsistens', 'Charlotte Tilbury', 'charlottetilbury.com', 'HAS',
        2, 2),
       ('Mascara', 'Volymgivande mascara', 'NARS', 'narscosmetics.se', 'ALMOST_OUT', 2, 2),
       ('Lip Gloss', 'Glansig läppglans', 'Fenty Beauty', 'fentybeauty.com', 'WANT', 2, 2);