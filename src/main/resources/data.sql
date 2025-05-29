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
INSERT INTO product_types (product_type_id, name, display_name, borrow_duration_days, main_creator_role_id)
VALUES (1, 'book', 'Book', 28, 1),
       (2, 'ebook', 'E-Book', 28, 1),
       (3, 'dvd', 'DVD', 14, 4),
       (4, 'boardgame', 'Board Game', 7, 5)
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

-- Products
INSERT INTO products (product_id, product_type_id, product_link_to_emedia, title, release_year, photo, description)
VALUES (1, 1, NULL, 'Harry Potter and the Philosopher''s Stone', '1997', 'photo_url_1', 'First book of Harry Potter series'),
       (2, 2, 'https://ebooks.voebb.de/hp1', 'Harry Potter and the Philosopher''s Stone', '1997', 'photo_url_1', 'First book of Harry Potter series'),
       (3, 1, NULL, 'Harry Potter and the Chamber of Secrets', '1998', 'photo_url_2', 'Second book in the series'),
       (4, 2, 'https://ebooks.voebb.de/hp2', 'Harry Potter and the Chamber of Secrets', '1998', 'photo_url_2', 'Second book in the series'),
       (5, 1, NULL, 'Harry Potter and the Prisoner of Azkaban', '1999', 'photo_url_3', 'Third book in the series'),
       (6, 2, 'https://ebooks.voebb.de/hp3', 'Harry Potter and the Goblet of Fire', '2000', 'photo_url_4', 'Fourth book in the series'),
       (7, 1, NULL, 'Harry Potter and the Order of the Phoenix', '2003', 'photo_url_5', 'Fifth book in the series'),
       (8, 2, 'https://ebooks.voebb.de/hp4', 'Harry Potter and the Half-Blood Prince', '2005', 'photo_url_6', 'Sixth book in the series'),
       (9, 1, NULL, 'Harry Potter and the Deathly Hallows', '2007', 'photo_url_7', 'Final book in the series'),
       (10, 1, NULL, 'Harry Potter and the Cursed Child', '2016', 'photo_url_8', 'Play based on Harry Potter universe'),
       (11, 1, NULL, 'Fantastic Beasts and Where to Find Them', '2001', 'photo_url_9', 'A companion book to Harry Potter'),
       (12, 3, NULL, 'The Matrix', '1999', 'photo_url_9', 'DVD format'),
       (13, 4, NULL, 'Monopoly', '1935', 'photo_url_13', 'Holds the Guinness World Record for being played by the most people')
ON CONFLICT (product_id) DO NOTHING;
SELECT SETVAL('products_product_id_seq', (SELECT MAX(product_id) FROM products));

-- Book Details (for both physical books and e-books)
INSERT INTO book_details (product_id, book_isbn, book_edition, book_pages)
VALUES (1, '9780747532699', '1st Edition', 223),
       (2, '9780747532699-E', 'Digital Edition', 223),
       (3, '9780747538493', '1st Edition', 251),
       (4, '9780747538493-E', 'Digital Edition', 251),
       (5, '9780747542155', '1st Edition', 317),
       (6, '9780747542155-E', 'Digital Edition', 317),
       (7, '9780747546245', '1st Edition', 607),
       (8, '9780747546245-E', 'Digital Edition', 607),
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
VALUES (1, 'Admin 1', 'Admin 1', 'admin1@example.com', '+1234567890123451', '$2a$12$Rcps34Enqr7WYhMH0/POmesuIR9CiEGn1wrtq/VKqrh2H6tWmIu9e', TRUE, 0),
       (2, 'Admin 2', 'Admin 2', 'admin2@example.com', '+1234567890123452', '$2a$12$Rcps34Enqr7WYhMH0/POmesuIR9CiEGn1wrtq/VKqrh2H6tWmIu9e', TRUE, 0),
       (3, 'Client 1', 'Client 1', 'client1@example.com', '+1234567890123453', '$2a$12$Rcps34Enqr7WYhMH0/POmesuIR9CiEGn1wrtq/VKqrh2H6tWmIu9e', TRUE, 0),
       (4, 'Client 2', 'Client 2', 'client2@example.com', '+1234567890123454', '$2a$12$Rcps34Enqr7WYhMH0/POmesuIR9CiEGn1wrtq/VKqrh2H6tWmIu9e', TRUE, 0),
       (5, 'Helly ', 'R.', 'helly@example.com', '+1234567890123455', '$2a$12$Rcps34Enqr7WYhMH0/POmesuIR9CiEGn1wrtq/VKqrh2H6tWmIu9e', TRUE, 0),
       (6, 'Mark', 'S', 'mark@example.com', '+1234567890123456', '$2a$12$Rcps34Enqr7WYhMH0/POmesuIR9CiEGn1wrtq/VKqrh2H6tWmIu9e', TRUE, 0),
       (7, 'Ronald', 'B.', 'ronald@example.com', '+1234567890123457', '$2a$12$Rcps34Enqr7WYhMH0/POmesuIR9CiEGn1wrtq/VKqrh2H6tWmIu9e', TRUE, 5)
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
INSERT INTO libraries (library_id, library_name, library_description, address_city, address_district, address_postcode,
                       address_street, address_house_nr, address_osm_link)
VALUES (1, 'Central City Library', 'Main public library in the heart of Berlin.', 'Berlin', 'Mitte', '10115', 'Hauptstraße', '123', 'https://www.openstreetmap.org/?mlat=52.5300&mlon=13.3847#map=16/52.5300/13.3847'),
       (2, 'West End Library', 'A cozy library located in the Charlottenburg district.', 'Berlin', 'Charlottenburg-Wilmersdorf', '10623', 'Kaiser-Wilhelm-Straße', '45', 'https://www.openstreetmap.org/?mlat=52.5150&mlon=13.2900#map=16/52.5150/13.2900'),
       (3, 'East Side Library', 'Library near the East Side Gallery, offering a variety of books.', 'Berlin', 'Friedrichshain-Kreuzberg', '10243', 'Mühlenstraße', '10', 'https://www.openstreetmap.org/?mlat=52.5074&mlon=13.4396#map=16/52.5074/13.4396'),
       (4, 'North Gate Library', 'A library situated in the Reinickendorf district, known for its quiet reading rooms.', 'Berlin', 'Reinickendorf', '13407', 'Wilhelmsruher Damm', '23', 'https://www.openstreetmap.org/?mlat=52.5960&mlon=13.2905#map=16/52.5960/13.2905'),
       (5, 'South Park Library', 'Located near the Tempelhofer Feld, offering a peaceful environment for reading.', 'Berlin', 'Tempelhof-Schöneberg', '12103', 'Tempelhofer Damm', '89', 'https://www.openstreetmap.org/?mlat=52.4706&mlon=13.3989#map=16/52.4706/13.3989')
ON CONFLICT (library_id) DO NOTHING;
SELECT SETVAL('libraries_library_id_seq', (SELECT MAX(library_id) FROM libraries));

-- Items
INSERT INTO product_items (item_id, product_id, status_id)
VALUES (1, 1, 1),
       (2, 1, 2),
       (3, 1, 1),
       (4, 1, 1),
       (5, 2, 2),
       (6, 2, 1),
       (7, 2, 2),
       (8, 3, 1),
       (9, 3, 1),
       (10, 3, 4),
       (11, 3, 2),
       (12, 3, 5),
       (13, 4, 1),
       (14, 4, 2),
       (15, 4, 2),
       (16, 5, 4),
       (17, 5, 1),
       (18, 5, 2),
       (19, 5, 1),
       (20, 6, 2),
       (21, 6, 2),
       (22, 6, 1),
       (23, 7, 1),
       (24, 7, 4),
       (25, 7, 4),
       (26, 7, 2),
       (27, 8, 1),
       (28, 8, 1),
       (29, 8, 2),
       (30, 9, 2),
       (31, 9, 5),
       (32, 9, 1),
       (33, 9, 2),
       (34, 10, 1),
       (35, 10, 2),
       (36, 10, 1),
       (37, 10, 1),
       (38, 10, 2),
       (39, 11, 4),
       (40, 11, 2),
       (41, 11, 1),
       (42, 11, 4),
       (43, 11, 5),
       (44, 11, 3),
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
VALUES (1, 50, CURRENT_DATE - INTERVAL '25 days', CURRENT_DATE + INTERVAL '3 days', NULL, 0);

-- CASE 2: Overdue borrow (due 5 days ago)
INSERT INTO borrows (custom_user_id, item_id, borrow_start_date, borrow_due_date, return_date, extends_count)
VALUES (1, 49, CURRENT_DATE - INTERVAL '19 days', CURRENT_DATE - INTERVAL '5 days', NULL, 1);

-- CASE 3: Returned borrow (returned yesterday)
INSERT INTO borrows (custom_user_id, item_id, borrow_start_date, borrow_due_date, return_date, extends_count)
VALUES (1, 48, CURRENT_DATE - INTERVAL '8 days', CURRENT_DATE - INTERVAL '1 day', CURRENT_DATE - INTERVAL '1 day', 0);

-- CASE 4: Active borrow, max extensions
INSERT INTO borrows (custom_user_id, item_id, borrow_start_date, borrow_due_date, return_date, extends_count)
VALUES (1, 44, CURRENT_DATE - INTERVAL '42 days', CURRENT_DATE + INTERVAL '14 days', NULL, 2);

-- CASE 5: Returned early
INSERT INTO borrows (custom_user_id, item_id, borrow_start_date, borrow_due_date, return_date, extends_count)
VALUES (1, 45, CURRENT_DATE - INTERVAL '3 days', CURRENT_DATE + INTERVAL '4 days', CURRENT_DATE, 0);

-- CASE 6: Overdue, maxed out extensions
INSERT INTO borrows (custom_user_id, item_id, borrow_start_date, borrow_due_date, return_date, extends_count)
VALUES (2, 51, CURRENT_DATE - INTERVAL '57 days', CURRENT_DATE - INTERVAL '1 day', NULL, 2);


SELECT SETVAL('borrows_borrow_id_seq', (SELECT MAX(borrow_id) FROM borrows));

--  ─────────── mock: RESERVATIONS ───────────
INSERT INTO reservations (reservation_id, custom_user_id, item_id, reservation_start, reservation_due)
VALUES (1, 2, 1, DATE '2025-05-10', DATE '2025-05-13'),
       (2, 2, 2, DATE '2025-05-10', DATE '2025-05-13'),
       (3, 5, 3, DATE '2025-05-08', DATE '2025-05-11'),
       (4, 5, 3, DATE '2025-05-08', DATE '2025-05-11'),
       (5, 1, 44, DATE '2025-05-08', DATE '2025-05-11'),
       (6, 1, 20, DATE '2025-05-08', DATE '2025-05-11'),
       (7, 4, 12, DATE '2025-05-08', DATE '2025-05-11')
ON CONFLICT DO NOTHING;
SELECT SETVAL('reservations_reservation_id_seq', (SELECT MAX(reservation_id) FROM reservations));