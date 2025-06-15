--  Creator Roles
INSERT INTO creator_roles (creator_role_id, creator_role)
VALUES (1, 'Author'),
       (2, 'Co-Author'),
       (3, 'Editor'),
       (4, 'Director'),
       (5, 'Game Designer')
ON CONFLICT (creator_role_id) DO NOTHING;
SELECT SETVAL('creator_roles_creator_role_id_seq', (SELECT MAX(creator_role_id) FROM creator_roles));

-- Product types
INSERT INTO product_types (product_type_id, name, display_name, borrow_duration_days, main_creator_role_id, default_cover_url)
VALUES (1, 'book', 'Book', 28, 1, '/images/default-book.jpeg'),
       (2, 'ebook', 'E-Book', 28, 1, '/images/default-ebook.jpeg'),
       (3, 'dvd', 'DVD', 14, 4, '/images/default-dvd.webp'),
       (4, 'boardgame', 'Board Game', 7, 5, '/images/default-boardgame.jpeg')
ON CONFLICT (product_type_id) DO NOTHING;
SELECT SETVAL('product_types_product_type_id_seq', (SELECT MAX(product_type_id) FROM product_types));

-- Item status
INSERT INTO item_status (item_status_id, item_status_name)
VALUES (1, 'available'),
       (2, 'reserved'),
       (3, 'borrowed'),
       (4, 'damaged'),
       (5, 'lost')
ON CONFLICT (item_status_id) DO NOTHING;
SELECT SETVAL('item_status_item_status_id_seq', (SELECT MAX(item_status_id) FROM item_status));

-- Client Roles
INSERT INTO user_roles (role_id, role_name)
VALUES (1, 'ROLE_ADMIN'),
       (2, 'ROLE_CLIENT')
ON CONFLICT (role_id) DO NOTHING;
SELECT SETVAL('user_roles_role_id_seq', (SELECT MAX(role_id) FROM user_roles));;

-- Languages  ─────────────────────────────────────────────────
INSERT INTO languages (language_id, language_name)
VALUES (1, 'English'),
       (2, 'German'),
       (3, 'Hindi'),
       (4, 'Chinese'),
       (5, 'Japanese'),
       (6, 'French'),
       (7, 'Spanish'),
       (8, 'Russian'),
       (9, 'Arabic'),
       (10, 'Korean')
ON CONFLICT (language_id) DO NOTHING;
SELECT SETVAL('languages_language_id_seq', (SELECT MAX(language_id) FROM languages));

-- DUMMY DATA BELLOW
-- Products with photos
INSERT INTO products (product_id, product_type_id, product_link_to_emedia, title, release_year, photo, description)
VALUES (1, 1, NULL, 'Harry Potter and the Philosopher''s Stone', '1997', 'photo_url_1', 'Harry Potter discovers on his eleventh birthday that he is a wizard. He begins his magical education at Hogwarts School of Witchcraft and Wizardry and learns about his mysterious past. Along the way, he uncovers a secret about a powerful stone hidden in the school.'),
       (2, 2, 'https://ebooks.voebb.de/hp1', 'Harry Potter and the Philosopher''s Stone', '1997', 'photo_url_1', 'Harry Potter discovers on his eleventh birthday that he is a wizard. He begins his magical education at Hogwarts School of Witchcraft and Wizardry and learns about his mysterious past. Along the way, he uncovers a secret about a powerful stone hidden in the school.'),
       (3, 1, NULL, 'Harry Potter and the Chamber of Secrets', '1998', 'photo_url_2', 'During his second year at Hogwarts, Harry investigates a series of mysterious attacks on students. He discovers an ancient legend about the Chamber of Secrets and faces a terrifying monster. Secrets from the school’s dark past come to light.'),
       (4, 2, 'https://ebooks.voebb.de/hp2', 'Harry Potter and the Chamber of Secrets', '1998', 'photo_url_2', 'During his second year at Hogwarts, Harry investigates a series of mysterious attacks on students. He discovers an ancient legend about the Chamber of Secrets and faces a terrifying monster. Secrets from the school’s dark past come to light.'),
       (5, 1, NULL, 'Harry Potter and the Prisoner of Azkaban', '1999', 'photo_url_3', 'Harry learns that Sirius Black, a dangerous escaped convict, may be after him. As he unravels the truth, Harry discovers shocking secrets about his family and the night his parents died. The story deepens Harry’s connection to his past.'),
       (6, 2, 'https://ebooks.voebb.de/hp3', 'Harry Potter and the Goblet of Fire', '2000', 'photo_url_4', 'Harry is unexpectedly entered into the deadly Triwizard Tournament, where he faces three life-threatening challenges. As the tournament unfolds, dark forces re-emerge and Voldemort returns to power. The wizarding world is changed forever.'),
       (7, 1, NULL, 'Harry Potter and the Order of the Phoenix', '2003', 'photo_url_5', 'With the Ministry of Magic refusing to believe in Voldemort’s return, Harry creates a student group to prepare for the dangers ahead. He struggles with visions tied to the Dark Lord and feels increasingly isolated. The battle for truth and unity begins.'),
       (13, 4, NULL, 'Monopoly', '1935', 'photo_url_13', 'Monopoly is the iconic board game of buying, trading, and developing properties to build wealth. Players compete to bankrupt their opponents through strategy and chance. It''s one of the most played and recognizable games in the world.')
ON CONFLICT (product_id) DO NOTHING;

-- Products without photos
INSERT INTO products (product_id, product_type_id, product_link_to_emedia, title, release_year, description)
VALUES (8, 2, 'https://ebooks.voebb.de/hp4', 'Harry Potter and the Half-Blood Prince', '2005', 'In his sixth year, Harry explores Voldemort’s past to uncover the key to his destruction. He finds a mysterious potion book marked ''Property of the Half-Blood Prince.'' As dark events unfold, tragedy strikes at Hogwarts.'),
       (9, 1, NULL, 'Harry Potter and the Deathly Hallows', '2007', 'Harry, Ron, and Hermione leave Hogwarts to hunt down Voldemort’s Horcruxes. Their journey is filled with danger, sacrifice, and revelations. The final battle at Hogwarts decides the fate of the wizarding world.'),
       (10, 1, NULL, 'Harry Potter and the Cursed Child', '2016', 'Set nineteen years later, Harry is now a father and Ministry of Magic official. His son Albus struggles with living in his father’s shadow and changes the past with a time-turner. Together they face the unintended consequences of altering time.'),
       (11, 1, NULL, 'Fantastic Beasts and Where to Find Them', '2001', 'This magical guidebook by Newt Scamander details the magical creatures of the wizarding world. It includes descriptions, habitats, and classifications of beasts from dragons to nifflers. A fun and imaginative companion to the Harry Potter series.'),
       (12, 3, NULL, 'The Matrix', '1999', 'Neo, a hacker, learns that his world is a simulated reality created by machines to enslave humanity. He joins a group of rebels to free human minds and fight back. The film explores themes of illusion, freedom, and reality.')
ON CONFLICT (product_id) DO NOTHING;
SELECT SETVAL('products_product_id_seq', (SELECT MAX(product_id) FROM products));

-- Book Details (for both physical books and e-books)
INSERT INTO book_details (product_id, book_isbn, book_edition, book_pages)
VALUES (1, '9780747532699', '1st Edition', 223),
       (2, '9780747532699', 'Digital Edition', 223),
       (3, '9780747538493', '1st Edition', 251),
       (4, '9780747538493', 'Digital Edition', 251),
       (5, '9780747542155', '1st Edition', 317),
       (6, '9780747542155', 'Digital Edition', 317),
       (7, '9780747546245', '1st Edition', 607),
       (8, '9780747546245', 'Digital Edition', 607),
       (9, '9780747595830', '1st Edition', 607),
       (10, '9781338216660', '1st Edition', 320),
       (11, '9781408880715', '1st Edition', 128)
ON CONFLICT DO NOTHING;

-- Creators
INSERT INTO creators (creator_id, creator_first_name, creator_last_name)
VALUES (1, 'J. K.', 'Rowling'),
       (2, 'John', 'Tiffany'),
       (3, 'Jack', 'Thorne'),
       (4, 'Elizabeth', 'Magie')
ON CONFLICT (creator_id) DO NOTHING;
SELECT SETVAL('creators_creator_id_seq', (SELECT MAX(creator_id) FROM creators));

-- Join table (same Creator ->  multiple  Roles; same Product -> multiple Creators)
INSERT INTO creator_product_relation (creator_id, product_id, creator_role_id)
VALUES
    -- Rowling on both books, different roles
    (1, 10, 1), -- AUTHOR on Cursed Child   (already there)
    (1, 11, 2), -- CO_AUTHOR on Fantastic Beasts

    -- Cursed Child still has two co-authors
    (2, 10, 2), -- Tiffany  CO_AUTHOR
    (3, 10, 2), -- Thorne   CO_AUTHOR

    (4, 13, 5)
ON CONFLICT(creator_id, product_id, creator_role_id) DO NOTHING;

--  Clients ─────────────────────────────────────────────────
-- Test password is 12345678
INSERT INTO custom_users (custom_user_id, first_name, last_name, email, phone_number, password, is_enabled, borrowed_products_count)
VALUES (1, 'Admin 1', 'Admin 1', 'admin1@example.com', '+4917012345671', '$2a$12$Rcps34Enqr7WYhMH0/POmesuIR9CiEGn1wrtq/VKqrh2H6tWmIu9e', TRUE, 0),
       (2, 'Admin 2', 'Admin 2', 'admin2@example.com', '+4917012345672', '$2a$12$Rcps34Enqr7WYhMH0/POmesuIR9CiEGn1wrtq/VKqrh2H6tWmIu9e', TRUE, 0),
       (3, 'Client 1', 'Client 1', 'client1@example.com', '+4917012345673', '$2a$12$Rcps34Enqr7WYhMH0/POmesuIR9CiEGn1wrtq/VKqrh2H6tWmIu9e', TRUE, 0),
       (4, 'Client 2', 'Client 2', 'client2@example.com', '+4917012345674', '$2a$12$Rcps34Enqr7WYhMH0/POmesuIR9CiEGn1wrtq/VKqrh2H6tWmIu9e', TRUE, 0),
       (5, 'Helly ', 'R.', 'helly@example.com', '+4917012345675', '$2a$12$Rcps34Enqr7WYhMH0/POmesuIR9CiEGn1wrtq/VKqrh2H6tWmIu9e', TRUE, 0),
       (6, 'Mark', 'S', 'mark@example.com', '+4917012345676', '$2a$12$Rcps34Enqr7WYhMH0/POmesuIR9CiEGn1wrtq/VKqrh2H6tWmIu9e', TRUE, 0),
       (7, 'Ronald', 'B.', 'ronald@example.com', '+4917012345677', '$2a$12$Rcps34Enqr7WYhMH0/POmesuIR9CiEGn1wrtq/VKqrh2H6tWmIu9e', TRUE, 5),
       (8, 'Ellen', 'Ripley', 'ripley@weyland.com', '+4917012345678', '$2a$12$Rcps34Enqr7WYhMH0/POmesuIR9CiEGn1wrtq/VKqrh2H6tWmIu9e', TRUE, 1),
       (9, 'Neo', 'Anderson', 'neo@matrix.com', '+4917012345679', '$2a$12$Rcps34Enqr7WYhMH0/POmesuIR9CiEGn1wrtq/VKqrh2H6tWmIu9e', TRUE, 2),
       (10, 'Leia', 'Organa', 'leia@rebelalliance.org', '+4917012345680', '$2a$12$Rcps34Enqr7WYhMH0/POmesuIR9CiEGn1wrtq/VKqrh2H6tWmIu9e', TRUE, 0),
       (11, 'Forrest', 'Gump', 'forrest@gumpco.com', '+4917012345681', '$2a$12$Rcps34Enqr7WYhMH0/POmesuIR9CiEGn1wrtq/VKqrh2H6tWmIu9e', FALSE, 3),
       (12, 'Vito', 'Corleone', 'vito@corleonefamily.it', '+4917012345682', '$2a$12$Rcps34Enqr7WYhMH0/POmesuIR9CiEGn1wrtq/VKqrh2H6tWmIu9e', TRUE, 4),
       (13, 'Amélie', 'Poulain', 'amelie@montmartre.fr', '+4917012345683', '$2a$12$Rcps34Enqr7WYhMH0/POmesuIR9CiEGn1wrtq/VKqrh2H6tWmIu9e', TRUE, 0)
ON CONFLICT (custom_user_id) DO NOTHING;
SELECT SETVAL('custom_users_custom_user_id_seq', (SELECT MAX(custom_user_id) FROM custom_users));

-- Join table (same Client -> multiple Roles)
INSERT INTO users_roles_relation (custom_user_id, role_id)
VALUES (1, 1),
       (2, 1),
       (3, 2),
       (4, 2),
       (5, 2),
       (6, 2),
       (7, 2)
ON CONFLICT (role_id, custom_user_id) DO NOTHING;

-- Countries ─────────────────────────────────────────────────
INSERT INTO countries (country_id, country_name)
VALUES (1, 'United Kingdom'),
       (2, 'United States'),
       (3, 'Canada'),
       (4, 'Germany'),
       (5, 'France'),
       (6, 'Italy'),
       (7, 'Spain'),
       (8, 'Australia'),
       (9, 'Japan'),
       (10, 'China'),
       (11, 'India'),
       (12, 'Brazil'),
       (13, 'Mexico'),
       (14, 'South Korea'),
       (15, 'Russia'),
       (16, 'Netherlands'),
       (17, 'Sweden'),
       (18, 'Norway'),

       (19, 'Switzerland'),
       (20, 'South Africa'),
       (21, 'Argentina'),
       (22, 'New Zealand')
ON CONFLICT (country_id) DO NOTHING;
SELECT SETVAL('countries_country_id_seq', (SELECT MAX(country_id) FROM countries));

-- Join table (product linked to countries)
INSERT INTO country_relation (product_id, country_id)
VALUES (1, 1), -- Philosopher's Stone published in the UK
       (1, 2), -- ... and in the US
       (10, 1), -- Cursed Child in the UK
       (11, 2);
-- Fantastic Beasts in the US

-- Join table (product linked to languages)
INSERT INTO language_relation (product_id, language_id)
VALUES (1, 1),
       (1, 2);

-- Libraries ─────────────────────────────────────────────────
INSERT INTO libraries (
    library_id,
    address_district,
    library_name,
    library_description,
    address_city,
    address_postcode,
    address_street,
    address_house_nr,
    address_osm_link
) VALUES
-- Mitte
(1, 'Mitte', 'Zentral- und Landesbibliothek Berlin', NULL, 'Berlin', '10178', 'Breite Str.', '30-36', 'https://www.openstreetmap.org/?mlat=52.517&mlon=13.401'),
(2, 'Mitte', 'Philipp-Schaeffer-Bibliothek', NULL, 'Berlin', '10119', 'Brunnenstraße', '181', 'https://www.openstreetmap.org/?mlat=52.535&mlon=13.396'),

-- Friedrichshain-Kreuzberg
(3, 'Friedrichshain-Kreuzberg', 'ZLB – Amerika-Gedenkbibliothek', NULL, 'Berlin', '10961', 'Blücherplatz', '1', 'https://www.openstreetmap.org/?mlat=52.497&mlon=13.390'),
(4, 'Friedrichshain-Kreuzberg', 'Bezirkszentralbibliothek Frankfurter Allee', NULL, 'Berlin', '10247', 'Frankfurter Allee', '14A', 'https://www.openstreetmap.org/?mlat=52.512&mlon=13.463'),

-- Pankow
(5, 'Pankow', 'Stadtbibliothek Pankow', NULL, 'Berlin', '13187', 'Breite Str.', '36', 'https://www.openstreetmap.org/?mlat=52.566&mlon=13.411'),
(6, 'Pankow', 'Bibliothek am Wasserturm', NULL, 'Berlin', '10405', 'Prenzlauer Allee', '227/228', 'https://www.openstreetmap.org/?mlat=52.532&mlon=13.425'),

-- Charlottenburg-Wilmersdorf
(7, 'Charlottenburg-Wilmersdorf', 'Hauptbibliothek – Dietrich-Bonhoeffer-Bibliothek', NULL, 'Berlin', '10713', 'Brandenburgische Str.', '2', 'https://www.openstreetmap.org/?mlat=52.489&mlon=13.319'),
(8, 'Charlottenburg-Wilmersdorf', 'Johanna-Moosdorf-Bibliothek', NULL, 'Berlin', '10625', 'Pestalozzistraße', '5-8', 'https://www.openstreetmap.org/?mlat=52.510&mlon=13.305'),

-- Spandau
(9, 'Spandau', 'Stadtbibliothek Spandau', NULL, 'Berlin', '13597', 'Carl-Schurz-Str.', '13', 'https://www.openstreetmap.org/?mlat=52.536&mlon=13.199'),
(10, 'Spandau', 'Johannes-Bobrowski-Bibliothek', NULL, 'Berlin', '13581', 'Pausiner Str.', '20', 'https://www.openstreetmap.org/?mlat=52.541&mlon=13.181'),

-- Steglitz-Zehlendorf
(11, 'Steglitz-Zehlendorf', 'Ingeborg-Drewitz-Bibliothek', NULL, 'Berlin', '12165', 'Grunewaldstr.', '3', 'https://www.openstreetmap.org/?mlat=52.455&mlon=13.323'),
(12, 'Steglitz-Zehlendorf', 'Gottfried-Benn-Bibliothek', NULL, 'Berlin', '14193', 'Nentershäuser Platz', '1', 'https://www.openstreetmap.org/?mlat=52.483&mlon=13.263'),

-- Tempelhof-Schöneberg
(13, 'Tempelhof-Schöneberg', 'Thomas-Dehler-Bibliothek', NULL, 'Berlin', '10825', 'Martin-Luther-Straße', '77', 'https://www.openstreetmap.org/?mlat=52.486&mlon=13.351'),
(14, 'Tempelhof-Schöneberg', 'Bezirkszentralbibliothek Eva-Maria-Buch-Haus', NULL, 'Berlin', '12099', 'Götzstraße', '8–12', 'https://www.openstreetmap.org/?mlat=52.458&mlon=13.387'),

-- Neukölln
(15, 'Neukölln', 'Helene-Nathan-Bibliothek', NULL, 'Berlin', '12043', 'Karl-Marx-Str.', '66', 'https://www.openstreetmap.org/?mlat=52.482&mlon=13.439'),
(16, 'Neukölln', 'Stadtteilbibliothek Rudow', NULL, 'Berlin', '12353', 'Zwickauer Damm', '10', 'https://www.openstreetmap.org/?mlat=52.422&mlon=13.493'),

-- Treptow-Köpenick
(17, 'Treptow-Köpenick', 'Stadtbibliothek Köpenick', NULL, 'Berlin', '12555', 'Alter Markt', '2', 'https://www.openstreetmap.org/?mlat=52.445&mlon=13.578'),
(18, 'Treptow-Köpenick', 'Stadtteilbibliothek Altglienicke', NULL, 'Berlin', '12524', 'Ortolfstraße', '182–184', 'https://www.openstreetmap.org/?mlat=52.418&mlon=13.515'),

-- Marzahn-Hellersdorf
(19, 'Marzahn-Hellersdorf', 'Mark-Twain-Bibliothek', NULL, 'Berlin', '12679', 'Marzahner Promenade', '55', 'https://www.openstreetmap.org/?mlat=52.545&mlon=13.566'),
(20, 'Marzahn-Hellersdorf', 'Stadtteilbibliothek Kaulsdorf', NULL, 'Berlin', '12621', 'Heesestraße', '7', 'https://www.openstreetmap.org/?mlat=52.500&mlon=13.565'),

-- Lichtenberg
(21, 'Lichtenberg', 'Anna-Seghers-Bibliothek', NULL, 'Berlin', '13051', 'Prerower Platz', '2', 'https://www.openstreetmap.org/?mlat=52.564&mlon=13.510'),
(22, 'Lichtenberg', 'Bodo-Uhse-Bibliothek', NULL, 'Berlin', '10319', 'Erich-Kurz-Str.', '9', 'https://www.openstreetmap.org/?mlat=52.505&mlon=13.512'),

-- Reinickendorf
(23, 'Reinickendorf', 'Stadtbibliothek Reinickendorf', NULL, 'Berlin', '13437', 'Eichborndamm', '215', 'https://www.openstreetmap.org/?mlat=52.588&mlon=13.317'),
(24, 'Reinickendorf', 'Humboldt-Bibliothek', NULL, 'Berlin', '13507', 'Karolinenstraße', '19', 'https://www.openstreetmap.org/?mlat=52.573&mlon=13.293')
ON CONFLICT (library_id) DO NOTHING;

SELECT SETVAL('libraries_library_id_seq', (SELECT MAX(library_id) FROM libraries));

-- Items
INSERT INTO product_items (item_id, product_id, status_id)
VALUES (1, 1, 1),
       (2, 1, 2),
       (3, 1, 1),
       (4, 1, 1),
       (5, 2, 1),
       (6, 2, 1),
       (7, 2, 4),
       (8, 3, 1),
       (9, 3, 1),
       (10, 3, 4),
       (11, 3, 3),
       (12, 3, 2),
       (13, 4, 2),
       (14, 4, 2),
       (15, 4, 1),
       (16, 5, 4),
       (17, 5, 1),
       (18, 5, 1),
       (19, 5, 1),
       (20, 6, 2),
       (21, 6, 1),
       (22, 6, 1),
       (23, 7, 1),
       (24, 7, 4),
       (25, 7, 4),
       (26, 7, 1),
       (27, 8, 1),
       (28, 8, 1),
       (29, 8, 1),
       (30, 9, 1),
       (31, 9, 5),
       (32, 9, 1),
       (33, 9, 1),
       (34, 10, 1),
       (35, 10, 1),
       (36, 10, 1),
       (37, 10, 1),
       (38, 10, 1),
       (39, 11, 4),
       (40, 11, 1),
       (41, 11, 1),
       (42, 11, 4),
       (43, 11, 5),
       (44, 11, 2),
       (45, 13, 3),
       -- available items
       (46, 13, 1),
       (47, 13, 1),
       -- test scenarios in borrowings
       (48, 13, 3),
       (49, 12, 3),
       (50, 11, 3),
       (51, 8, 3)
ON CONFLICT (item_id) DO NOTHING;
SELECT SETVAL('product_items_item_id_seq', (SELECT MAX(item_id) FROM product_items));

-- ItemLocation
INSERT INTO item_location (item_id, library_id, location_in_library)
VALUES (1, 1, 'Shelf A-12'),
       (2, 3, 'Room 2-17'),
       (3, 5, 'Shelf B-23'),
       (4, 2, 'Top Rack-8'),
       (5, 4, 'Shelf C-4'),
       (6, 1, 'Room 1-19'),
       (7, 2, 'Lower Shelf-5'),
       (8, 5, 'Shelf A-3'),
       (9, 3, 'Room 1-22'),
       (10, 4, 'Shelf B-10'),
       (11, 1, 'Shelf C-14'),
       (12, 2, 'Top Rack-7'),
       (13, 5, 'Room 2-25'),
       (14, 3, 'Shelf A-9'),
       (15, 4, 'Shelf C-16'),
       (16, 1, 'Room 1-5'),
       (17, 2, 'Shelf B-20'),
       (18, 5, 'Lower Shelf-12'),
       (19, 3, 'Top Rack-2'),
       (20, 4, 'Room 2-28'),
       (21, 1, 'Shelf C-6'),
       (22, 2, 'Room 1-13'),
       (23, 5, 'Room 2-10'),
       (24, 3, 'Shelf A-4'),
       (25, 4, 'Shelf B-19'),
       (26, 1, 'Room 1-9'),
       (27, 2, 'Shelf C-11'),
       (28, 5, 'Top Rack-1'),
       (29, 3, 'Room 2-7'),
       (30, 4, 'Lower Shelf-20'),
       (31, 1, 'Shelf B-15'),
       (32, 2, 'Room 1-6'),
       (33, 5, 'Shelf A-22'),
       (34, 3, 'Room 2-19'),
       (35, 4, 'Shelf C-9'),
       (36, 1, 'Shelf A-2'),
       (37, 2, 'Room 2-14'),
       (38, 5, 'Shelf B-17'),
       (39, 3, 'Lower Shelf-1'),
       (40, 4, 'Room 1-18'),
       (41, 5, 'Top Rack-9'),
       (42, 3, 'Lower Shelf-1'),
       (43, 4, 'Room 1-18'),
       (44, 5, 'Top Rack-9'),
       (45, 2, 'Next to the Staff Desk'),
       (46, 1, 'Main Library at the Entrance'),
       (47, 3, 'Top Rack-6'),
       (48, 5, 'Top Rack-9'),
       (49, 2, 'Next to the Staff Desk'),
       (50, 1, 'Main Library at the Entrance'),
       (51, 3, 'Top Rack-6')
ON CONFLICT (item_id) DO NOTHING;

--  ─────────── mock: BORROWINGS  ───────────
-- CASE 1: Active borrow (due in 3 days)
INSERT INTO borrows (custom_user_id, item_id, borrow_start_date, borrow_due_date, return_date, extends_count)
VALUES (3, 50, CURRENT_DATE - INTERVAL '25 days', CURRENT_DATE + INTERVAL '3 days', NULL, 0);

-- CASE 2: Overdue borrow (due 5 days ago)
INSERT INTO borrows (custom_user_id, item_id, borrow_start_date, borrow_due_date, return_date, extends_count)
VALUES (3, 49, CURRENT_DATE - INTERVAL '19 days', CURRENT_DATE - INTERVAL '5 days', NULL, 1);

-- CASE 3: Returned borrow (returned yesterday)
INSERT INTO borrows (custom_user_id, item_id, borrow_start_date, borrow_due_date, return_date, extends_count)
VALUES (3, 48, CURRENT_DATE - INTERVAL '8 days', CURRENT_DATE - INTERVAL '1 day', CURRENT_DATE - INTERVAL '1 day', 0);

-- CASE 4: Active borrow, max extensions
INSERT INTO borrows (custom_user_id, item_id, borrow_start_date, borrow_due_date, return_date, extends_count)
VALUES (3, 44, CURRENT_DATE - INTERVAL '42 days', CURRENT_DATE + INTERVAL '14 days', NULL, 2);

-- CASE 5: Returned early
INSERT INTO borrows (custom_user_id, item_id, borrow_start_date, borrow_due_date, return_date, extends_count)
VALUES (1, 45, CURRENT_DATE - INTERVAL '3 days', CURRENT_DATE + INTERVAL '4 days', CURRENT_DATE, 0);

-- CASE 6: Overdue, maxed out extensions
INSERT INTO borrows (custom_user_id, item_id, borrow_start_date, borrow_due_date, return_date, extends_count)
VALUES (2, 51, CURRENT_DATE - INTERVAL '57 days', CURRENT_DATE - INTERVAL '1 day', NULL, 2);


SELECT SETVAL('borrows_borrow_id_seq', (SELECT MAX(borrow_id) FROM borrows));

--  ─────────── mock: RESERVATIONS ───────────
INSERT INTO reservations (reservation_id, custom_user_id, item_id, reservation_start, reservation_due)
VALUES
-- Starts today, ends in 3 days
(1, 3, 2, CURRENT_DATE, CURRENT_DATE + INTERVAL '3 days'),

-- Started yesterday, ends in 2 days
(2, 3, 12, CURRENT_DATE - INTERVAL '1 day', CURRENT_DATE + INTERVAL '2 days'),

-- Started 2 days ago, ends tomorrow
(3, 2, 13, CURRENT_DATE - INTERVAL '2 days', CURRENT_DATE + INTERVAL '1 day'),

-- Ends today
(4, 5, 14, CURRENT_DATE - INTERVAL '3 days', CURRENT_DATE),

-- Started 3 days ago, expired yesterday (simulate overdue for deletion job)
(5, 1, 44, CURRENT_DATE - INTERVAL '4 days', CURRENT_DATE - INTERVAL '1 day'),

-- Ends tomorrow
(6, 1, 20, CURRENT_DATE - INTERVAL '2 days', CURRENT_DATE + INTERVAL '1 day')
ON CONFLICT DO NOTHING;
SELECT SETVAL('reservations_reservation_id_seq', (SELECT MAX(reservation_id) FROM reservations));